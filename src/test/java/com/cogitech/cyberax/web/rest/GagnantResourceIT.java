package com.cogitech.cyberax.web.rest;

import com.cogitech.cyberax.ServeurCyberaxApp;
import com.cogitech.cyberax.domain.Gagnant;
import com.cogitech.cyberax.repository.GagnantRepository;
import com.cogitech.cyberax.repository.search.GagnantSearchRepository;
import com.cogitech.cyberax.service.GagnantService;
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
 * Integration tests for the {@Link GagnantResource} REST controller.
 */
@SpringBootTest(classes = ServeurCyberaxApp.class)
public class GagnantResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;

    private static final Instant DEFAULT_DATE_GAIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_GAIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_PAYMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_PAYMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private GagnantRepository gagnantRepository;

    @Autowired
    private GagnantService gagnantService;

    /**
     * This repository is mocked in the com.cogitech.cyberax.repository.search test package.
     *
     * @see com.cogitech.cyberax.repository.search.GagnantSearchRepositoryMockConfiguration
     */
    @Autowired
    private GagnantSearchRepository mockGagnantSearchRepository;

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

    private MockMvc restGagnantMockMvc;

    private Gagnant gagnant;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GagnantResource gagnantResource = new GagnantResource(gagnantService);
        this.restGagnantMockMvc = MockMvcBuilders.standaloneSetup(gagnantResource)
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
    public static Gagnant createEntity(EntityManager em) {
        Gagnant gagnant = new Gagnant()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .telephone(DEFAULT_TELEPHONE)
            .position(DEFAULT_POSITION)
            .dateGain(DEFAULT_DATE_GAIN)
            .datePayment(DEFAULT_DATE_PAYMENT);
        return gagnant;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gagnant createUpdatedEntity(EntityManager em) {
        Gagnant gagnant = new Gagnant()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE)
            .position(UPDATED_POSITION)
            .dateGain(UPDATED_DATE_GAIN)
            .datePayment(UPDATED_DATE_PAYMENT);
        return gagnant;
    }

    @BeforeEach
    public void initTest() {
        gagnant = createEntity(em);
    }

    @Test
    @Transactional
    public void createGagnant() throws Exception {
        int databaseSizeBeforeCreate = gagnantRepository.findAll().size();

        // Create the Gagnant
        restGagnantMockMvc.perform(post("/api/gagnants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gagnant)))
            .andExpect(status().isCreated());

        // Validate the Gagnant in the database
        List<Gagnant> gagnantList = gagnantRepository.findAll();
        assertThat(gagnantList).hasSize(databaseSizeBeforeCreate + 1);
        Gagnant testGagnant = gagnantList.get(gagnantList.size() - 1);
        assertThat(testGagnant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testGagnant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testGagnant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testGagnant.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testGagnant.getDateGain()).isEqualTo(DEFAULT_DATE_GAIN);
        assertThat(testGagnant.getDatePayment()).isEqualTo(DEFAULT_DATE_PAYMENT);

        // Validate the Gagnant in Elasticsearch
        verify(mockGagnantSearchRepository, times(1)).save(testGagnant);
    }

    @Test
    @Transactional
    public void createGagnantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = gagnantRepository.findAll().size();

        // Create the Gagnant with an existing ID
        gagnant.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGagnantMockMvc.perform(post("/api/gagnants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gagnant)))
            .andExpect(status().isBadRequest());

        // Validate the Gagnant in the database
        List<Gagnant> gagnantList = gagnantRepository.findAll();
        assertThat(gagnantList).hasSize(databaseSizeBeforeCreate);

        // Validate the Gagnant in Elasticsearch
        verify(mockGagnantSearchRepository, times(0)).save(gagnant);
    }


    @Test
    @Transactional
    public void getAllGagnants() throws Exception {
        // Initialize the database
        gagnantRepository.saveAndFlush(gagnant);

        // Get all the gagnantList
        restGagnantMockMvc.perform(get("/api/gagnants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gagnant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].dateGain").value(hasItem(DEFAULT_DATE_GAIN.toString())))
            .andExpect(jsonPath("$.[*].datePayment").value(hasItem(DEFAULT_DATE_PAYMENT.toString())));
    }
    
    @Test
    @Transactional
    public void getGagnant() throws Exception {
        // Initialize the database
        gagnantRepository.saveAndFlush(gagnant);

        // Get the gagnant
        restGagnantMockMvc.perform(get("/api/gagnants/{id}", gagnant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(gagnant.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.dateGain").value(DEFAULT_DATE_GAIN.toString()))
            .andExpect(jsonPath("$.datePayment").value(DEFAULT_DATE_PAYMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGagnant() throws Exception {
        // Get the gagnant
        restGagnantMockMvc.perform(get("/api/gagnants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGagnant() throws Exception {
        // Initialize the database
        gagnantService.save(gagnant);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockGagnantSearchRepository);

        int databaseSizeBeforeUpdate = gagnantRepository.findAll().size();

        // Update the gagnant
        Gagnant updatedGagnant = gagnantRepository.findById(gagnant.getId()).get();
        // Disconnect from session so that the updates on updatedGagnant are not directly saved in db
        em.detach(updatedGagnant);
        updatedGagnant
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE)
            .position(UPDATED_POSITION)
            .dateGain(UPDATED_DATE_GAIN)
            .datePayment(UPDATED_DATE_PAYMENT);

        restGagnantMockMvc.perform(put("/api/gagnants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGagnant)))
            .andExpect(status().isOk());

        // Validate the Gagnant in the database
        List<Gagnant> gagnantList = gagnantRepository.findAll();
        assertThat(gagnantList).hasSize(databaseSizeBeforeUpdate);
        Gagnant testGagnant = gagnantList.get(gagnantList.size() - 1);
        assertThat(testGagnant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testGagnant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testGagnant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testGagnant.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testGagnant.getDateGain()).isEqualTo(UPDATED_DATE_GAIN);
        assertThat(testGagnant.getDatePayment()).isEqualTo(UPDATED_DATE_PAYMENT);

        // Validate the Gagnant in Elasticsearch
        verify(mockGagnantSearchRepository, times(1)).save(testGagnant);
    }

    @Test
    @Transactional
    public void updateNonExistingGagnant() throws Exception {
        int databaseSizeBeforeUpdate = gagnantRepository.findAll().size();

        // Create the Gagnant

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGagnantMockMvc.perform(put("/api/gagnants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(gagnant)))
            .andExpect(status().isBadRequest());

        // Validate the Gagnant in the database
        List<Gagnant> gagnantList = gagnantRepository.findAll();
        assertThat(gagnantList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Gagnant in Elasticsearch
        verify(mockGagnantSearchRepository, times(0)).save(gagnant);
    }

    @Test
    @Transactional
    public void deleteGagnant() throws Exception {
        // Initialize the database
        gagnantService.save(gagnant);

        int databaseSizeBeforeDelete = gagnantRepository.findAll().size();

        // Delete the gagnant
        restGagnantMockMvc.perform(delete("/api/gagnants/{id}", gagnant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gagnant> gagnantList = gagnantRepository.findAll();
        assertThat(gagnantList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Gagnant in Elasticsearch
        verify(mockGagnantSearchRepository, times(1)).deleteById(gagnant.getId());
    }

    @Test
    @Transactional
    public void searchGagnant() throws Exception {
        // Initialize the database
        gagnantService.save(gagnant);
        when(mockGagnantSearchRepository.search(queryStringQuery("id:" + gagnant.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(gagnant), PageRequest.of(0, 1), 1));
        // Search the gagnant
        restGagnantMockMvc.perform(get("/api/_search/gagnants?query=id:" + gagnant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gagnant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].dateGain").value(hasItem(DEFAULT_DATE_GAIN.toString())))
            .andExpect(jsonPath("$.[*].datePayment").value(hasItem(DEFAULT_DATE_PAYMENT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gagnant.class);
        Gagnant gagnant1 = new Gagnant();
        gagnant1.setId(1L);
        Gagnant gagnant2 = new Gagnant();
        gagnant2.setId(gagnant1.getId());
        assertThat(gagnant1).isEqualTo(gagnant2);
        gagnant2.setId(2L);
        assertThat(gagnant1).isNotEqualTo(gagnant2);
        gagnant1.setId(null);
        assertThat(gagnant1).isNotEqualTo(gagnant2);
    }
}
