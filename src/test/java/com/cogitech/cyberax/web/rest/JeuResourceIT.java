package com.cogitech.cyberax.web.rest;

import com.cogitech.cyberax.ServeurCyberaxApp;
import com.cogitech.cyberax.domain.Jeu;
import com.cogitech.cyberax.repository.JeuRepository;
import com.cogitech.cyberax.repository.search.JeuSearchRepository;
import com.cogitech.cyberax.service.JeuService;
import com.cogitech.cyberax.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static com.cogitech.cyberax.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link JeuResource} REST controller.
 */
@SpringBootTest(classes = ServeurCyberaxApp.class)
public class JeuResourceIT {

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_LANCEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_LANCEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_CLOTURE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CLOTURE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ENCOUR = false;
    private static final Boolean UPDATED_ENCOUR = true;

    private static final Double DEFAULT_POURCENTAGE_MISE = 1D;
    private static final Double UPDATED_POURCENTAGE_MISE = 2D;

    private static final Double DEFAULT_POURCENTAGE_REBOURT = 1D;
    private static final Double UPDATED_POURCENTAGE_REBOURT = 2D;

    @Autowired
    private JeuRepository jeuRepository;

    @Autowired
    private JeuService jeuService;

    /**
     * This repository is mocked in the com.cogitech.cyberax.repository.search test package.
     *
     * @see com.cogitech.cyberax.repository.search.JeuSearchRepositoryMockConfiguration
     */
    @Autowired
    private JeuSearchRepository mockJeuSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restJeuMockMvc;

    private Jeu jeu;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JeuResource jeuResource = new JeuResource(jeuService);
        this.restJeuMockMvc = MockMvcBuilders.standaloneSetup(jeuResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jeu createEntity(EntityManager em) {
        Jeu jeu = new Jeu()
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateLancement(DEFAULT_DATE_LANCEMENT)
            .dateCloture(DEFAULT_DATE_CLOTURE)
            .encour(DEFAULT_ENCOUR)
            .pourcentageMise(DEFAULT_POURCENTAGE_MISE)
            .pourcentageRebourt(DEFAULT_POURCENTAGE_REBOURT);
        return jeu;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Jeu createUpdatedEntity(EntityManager em) {
        Jeu jeu = new Jeu()
            .dateCreation(UPDATED_DATE_CREATION)
            .dateLancement(UPDATED_DATE_LANCEMENT)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .encour(UPDATED_ENCOUR)
            .pourcentageMise(UPDATED_POURCENTAGE_MISE)
            .pourcentageRebourt(UPDATED_POURCENTAGE_REBOURT);
        return jeu;
    }

    @BeforeEach
    public void initTest() {
        jeu = createEntity(em);
    }

    @Test
    @Transactional
    public void createJeu() throws Exception {
        int databaseSizeBeforeCreate = jeuRepository.findAll().size();

        // Create the Jeu
        restJeuMockMvc.perform(post("/api/jeus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeu)))
            .andExpect(status().isCreated());

        // Validate the Jeu in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeCreate + 1);
        Jeu testJeu = jeuList.get(jeuList.size() - 1);
        assertThat(testJeu.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testJeu.getDateLancement()).isEqualTo(DEFAULT_DATE_LANCEMENT);
        assertThat(testJeu.getDateCloture()).isEqualTo(DEFAULT_DATE_CLOTURE);
        assertThat(testJeu.isEncour()).isEqualTo(DEFAULT_ENCOUR);
        assertThat(testJeu.getPourcentageMise()).isEqualTo(DEFAULT_POURCENTAGE_MISE);
        assertThat(testJeu.getPourcentageRebourt()).isEqualTo(DEFAULT_POURCENTAGE_REBOURT);

        // Validate the Jeu in Elasticsearch
        verify(mockJeuSearchRepository, times(1)).save(testJeu);
    }

    @Test
    @Transactional
    public void createJeuWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jeuRepository.findAll().size();

        // Create the Jeu with an existing ID
        jeu.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJeuMockMvc.perform(post("/api/jeus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeu)))
            .andExpect(status().isBadRequest());

        // Validate the Jeu in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeCreate);

        // Validate the Jeu in Elasticsearch
        verify(mockJeuSearchRepository, times(0)).save(jeu);
    }


    @Test
    @Transactional
    public void getAllJeus() throws Exception {
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);

        // Get all the jeuList
        restJeuMockMvc.perform(get("/api/jeus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jeu.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateLancement").value(hasItem(DEFAULT_DATE_LANCEMENT.toString())))
            .andExpect(jsonPath("$.[*].dateCloture").value(hasItem(DEFAULT_DATE_CLOTURE.toString())))
            .andExpect(jsonPath("$.[*].encour").value(hasItem(DEFAULT_ENCOUR.booleanValue())))
            .andExpect(jsonPath("$.[*].pourcentageMise").value(hasItem(DEFAULT_POURCENTAGE_MISE.doubleValue())))
            .andExpect(jsonPath("$.[*].pourcentageRebourt").value(hasItem(DEFAULT_POURCENTAGE_REBOURT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getJeu() throws Exception {
        // Initialize the database
        jeuRepository.saveAndFlush(jeu);

        // Get the jeu
        restJeuMockMvc.perform(get("/api/jeus/{id}", jeu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jeu.getId().intValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateLancement").value(DEFAULT_DATE_LANCEMENT.toString()))
            .andExpect(jsonPath("$.dateCloture").value(DEFAULT_DATE_CLOTURE.toString()))
            .andExpect(jsonPath("$.encour").value(DEFAULT_ENCOUR.booleanValue()))
            .andExpect(jsonPath("$.pourcentageMise").value(DEFAULT_POURCENTAGE_MISE.doubleValue()))
            .andExpect(jsonPath("$.pourcentageRebourt").value(DEFAULT_POURCENTAGE_REBOURT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingJeu() throws Exception {
        // Get the jeu
        restJeuMockMvc.perform(get("/api/jeus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJeu() throws Exception {
        // Initialize the database
        jeuService.save(jeu);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockJeuSearchRepository);

        int databaseSizeBeforeUpdate = jeuRepository.findAll().size();

        // Update the jeu
        Jeu updatedJeu = jeuRepository.findById(jeu.getId()).get();
        // Disconnect from session so that the updates on updatedJeu are not directly saved in db
        em.detach(updatedJeu);
        updatedJeu
            .dateCreation(UPDATED_DATE_CREATION)
            .dateLancement(UPDATED_DATE_LANCEMENT)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .encour(UPDATED_ENCOUR)
            .pourcentageMise(UPDATED_POURCENTAGE_MISE)
            .pourcentageRebourt(UPDATED_POURCENTAGE_REBOURT);

        restJeuMockMvc.perform(put("/api/jeus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJeu)))
            .andExpect(status().isOk());

        // Validate the Jeu in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeUpdate);
        Jeu testJeu = jeuList.get(jeuList.size() - 1);
        assertThat(testJeu.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testJeu.getDateLancement()).isEqualTo(UPDATED_DATE_LANCEMENT);
        assertThat(testJeu.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testJeu.isEncour()).isEqualTo(UPDATED_ENCOUR);
        assertThat(testJeu.getPourcentageMise()).isEqualTo(UPDATED_POURCENTAGE_MISE);
        assertThat(testJeu.getPourcentageRebourt()).isEqualTo(UPDATED_POURCENTAGE_REBOURT);

        // Validate the Jeu in Elasticsearch
        verify(mockJeuSearchRepository, times(1)).save(testJeu);
    }

    @Test
    @Transactional
    public void updateNonExistingJeu() throws Exception {
        int databaseSizeBeforeUpdate = jeuRepository.findAll().size();

        // Create the Jeu

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJeuMockMvc.perform(put("/api/jeus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jeu)))
            .andExpect(status().isBadRequest());

        // Validate the Jeu in the database
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Jeu in Elasticsearch
        verify(mockJeuSearchRepository, times(0)).save(jeu);
    }

    @Test
    @Transactional
    public void deleteJeu() throws Exception {
        // Initialize the database
        jeuService.save(jeu);

        int databaseSizeBeforeDelete = jeuRepository.findAll().size();

        // Delete the jeu
        restJeuMockMvc.perform(delete("/api/jeus/{id}", jeu.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Jeu> jeuList = jeuRepository.findAll();
        assertThat(jeuList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Jeu in Elasticsearch
        verify(mockJeuSearchRepository, times(1)).deleteById(jeu.getId());
    }

    @Test
    @Transactional
    public void searchJeu() throws Exception {
        // Initialize the database
        jeuService.save(jeu);
        when(mockJeuSearchRepository.search(queryStringQuery("id:" + jeu.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(jeu), PageRequest.of(0, 1), 1));
        // Search the jeu
        restJeuMockMvc.perform(get("/api/_search/jeus?query=id:" + jeu.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jeu.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateLancement").value(hasItem(DEFAULT_DATE_LANCEMENT.toString())))
            .andExpect(jsonPath("$.[*].dateCloture").value(hasItem(DEFAULT_DATE_CLOTURE.toString())))
            .andExpect(jsonPath("$.[*].encour").value(hasItem(DEFAULT_ENCOUR.booleanValue())))
            .andExpect(jsonPath("$.[*].pourcentageMise").value(hasItem(DEFAULT_POURCENTAGE_MISE.doubleValue())))
            .andExpect(jsonPath("$.[*].pourcentageRebourt").value(hasItem(DEFAULT_POURCENTAGE_REBOURT.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Jeu.class);
        Jeu jeu1 = new Jeu();
        jeu1.setId(1L);
        Jeu jeu2 = new Jeu();
        jeu2.setId(jeu1.getId());
        assertThat(jeu1).isEqualTo(jeu2);
        jeu2.setId(2L);
        assertThat(jeu1).isNotEqualTo(jeu2);
        jeu1.setId(null);
        assertThat(jeu1).isNotEqualTo(jeu2);
    }
}
