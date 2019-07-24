package com.cogitech.cyberax.web.rest;

import com.cogitech.cyberax.ServeurCyberaxApp;
import com.cogitech.cyberax.domain.Mise;
import com.cogitech.cyberax.repository.MiseRepository;
import com.cogitech.cyberax.repository.search.MiseSearchRepository;
import com.cogitech.cyberax.service.MiseService;
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
 * Integration tests for the {@Link MiseResource} REST controller.
 */
@SpringBootTest(classes = ServeurCyberaxApp.class)
public class MiseResourceIT {

    private static final Double DEFAULT_MONTANT = 1D;
    private static final Double UPDATED_MONTANT = 2D;

    private static final Instant DEFAULT_DATE_MISE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MISE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_VALIDATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_VALIDATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_ETAT = 1;
    private static final Integer UPDATED_ETAT = 2;

    private static final Integer DEFAULT_POSITION_CLIC = 1;
    private static final Integer UPDATED_POSITION_CLIC = 2;

    @Autowired
    private MiseRepository miseRepository;

    @Autowired
    private MiseService miseService;

    /**
     * This repository is mocked in the com.cogitech.cyberax.repository.search test package.
     *
     * @see com.cogitech.cyberax.repository.search.MiseSearchRepositoryMockConfiguration
     */
    @Autowired
    private MiseSearchRepository mockMiseSearchRepository;

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

    private MockMvc restMiseMockMvc;

    private Mise mise;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MiseResource miseResource = new MiseResource(miseService);
        this.restMiseMockMvc = MockMvcBuilders.standaloneSetup(miseResource)
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
    public static Mise createEntity(EntityManager em) {
        Mise mise = new Mise()
            .montant(DEFAULT_MONTANT)
            .dateMise(DEFAULT_DATE_MISE)
            .dateValidation(DEFAULT_DATE_VALIDATION)
            .etat(DEFAULT_ETAT)
            .positionClic(DEFAULT_POSITION_CLIC);
        return mise;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mise createUpdatedEntity(EntityManager em) {
        Mise mise = new Mise()
            .montant(UPDATED_MONTANT)
            .dateMise(UPDATED_DATE_MISE)
            .dateValidation(UPDATED_DATE_VALIDATION)
            .etat(UPDATED_ETAT)
            .positionClic(UPDATED_POSITION_CLIC);
        return mise;
    }

    @BeforeEach
    public void initTest() {
        mise = createEntity(em);
    }

    @Test
    @Transactional
    public void createMise() throws Exception {
        int databaseSizeBeforeCreate = miseRepository.findAll().size();

        // Create the Mise
        restMiseMockMvc.perform(post("/api/mises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mise)))
            .andExpect(status().isCreated());

        // Validate the Mise in the database
        List<Mise> miseList = miseRepository.findAll();
        assertThat(miseList).hasSize(databaseSizeBeforeCreate + 1);
        Mise testMise = miseList.get(miseList.size() - 1);
        assertThat(testMise.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testMise.getDateMise()).isEqualTo(DEFAULT_DATE_MISE);
        assertThat(testMise.getDateValidation()).isEqualTo(DEFAULT_DATE_VALIDATION);
        assertThat(testMise.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testMise.getPositionClic()).isEqualTo(DEFAULT_POSITION_CLIC);

        // Validate the Mise in Elasticsearch
        verify(mockMiseSearchRepository, times(1)).save(testMise);
    }

    @Test
    @Transactional
    public void createMiseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = miseRepository.findAll().size();

        // Create the Mise with an existing ID
        mise.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMiseMockMvc.perform(post("/api/mises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mise)))
            .andExpect(status().isBadRequest());

        // Validate the Mise in the database
        List<Mise> miseList = miseRepository.findAll();
        assertThat(miseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Mise in Elasticsearch
        verify(mockMiseSearchRepository, times(0)).save(mise);
    }


    @Test
    @Transactional
    public void getAllMises() throws Exception {
        // Initialize the database
        miseRepository.saveAndFlush(mise);

        // Get all the miseList
        restMiseMockMvc.perform(get("/api/mises?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mise.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateMise").value(hasItem(DEFAULT_DATE_MISE.toString())))
            .andExpect(jsonPath("$.[*].dateValidation").value(hasItem(DEFAULT_DATE_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].positionClic").value(hasItem(DEFAULT_POSITION_CLIC)));
    }
    
    @Test
    @Transactional
    public void getMise() throws Exception {
        // Initialize the database
        miseRepository.saveAndFlush(mise);

        // Get the mise
        restMiseMockMvc.perform(get("/api/mises/{id}", mise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(mise.getId().intValue()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()))
            .andExpect(jsonPath("$.dateMise").value(DEFAULT_DATE_MISE.toString()))
            .andExpect(jsonPath("$.dateValidation").value(DEFAULT_DATE_VALIDATION.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT))
            .andExpect(jsonPath("$.positionClic").value(DEFAULT_POSITION_CLIC));
    }

    @Test
    @Transactional
    public void getNonExistingMise() throws Exception {
        // Get the mise
        restMiseMockMvc.perform(get("/api/mises/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMise() throws Exception {
        // Initialize the database
        miseService.save(mise);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockMiseSearchRepository);

        int databaseSizeBeforeUpdate = miseRepository.findAll().size();

        // Update the mise
        Mise updatedMise = miseRepository.findById(mise.getId()).get();
        // Disconnect from session so that the updates on updatedMise are not directly saved in db
        em.detach(updatedMise);
        updatedMise
            .montant(UPDATED_MONTANT)
            .dateMise(UPDATED_DATE_MISE)
            .dateValidation(UPDATED_DATE_VALIDATION)
            .etat(UPDATED_ETAT)
            .positionClic(UPDATED_POSITION_CLIC);

        restMiseMockMvc.perform(put("/api/mises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMise)))
            .andExpect(status().isOk());

        // Validate the Mise in the database
        List<Mise> miseList = miseRepository.findAll();
        assertThat(miseList).hasSize(databaseSizeBeforeUpdate);
        Mise testMise = miseList.get(miseList.size() - 1);
        assertThat(testMise.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testMise.getDateMise()).isEqualTo(UPDATED_DATE_MISE);
        assertThat(testMise.getDateValidation()).isEqualTo(UPDATED_DATE_VALIDATION);
        assertThat(testMise.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testMise.getPositionClic()).isEqualTo(UPDATED_POSITION_CLIC);

        // Validate the Mise in Elasticsearch
        verify(mockMiseSearchRepository, times(1)).save(testMise);
    }

    @Test
    @Transactional
    public void updateNonExistingMise() throws Exception {
        int databaseSizeBeforeUpdate = miseRepository.findAll().size();

        // Create the Mise

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMiseMockMvc.perform(put("/api/mises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(mise)))
            .andExpect(status().isBadRequest());

        // Validate the Mise in the database
        List<Mise> miseList = miseRepository.findAll();
        assertThat(miseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Mise in Elasticsearch
        verify(mockMiseSearchRepository, times(0)).save(mise);
    }

    @Test
    @Transactional
    public void deleteMise() throws Exception {
        // Initialize the database
        miseService.save(mise);

        int databaseSizeBeforeDelete = miseRepository.findAll().size();

        // Delete the mise
        restMiseMockMvc.perform(delete("/api/mises/{id}", mise.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Mise> miseList = miseRepository.findAll();
        assertThat(miseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Mise in Elasticsearch
        verify(mockMiseSearchRepository, times(1)).deleteById(mise.getId());
    }

    @Test
    @Transactional
    public void searchMise() throws Exception {
        // Initialize the database
        miseService.save(mise);
        when(mockMiseSearchRepository.search(queryStringQuery("id:" + mise.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(mise), PageRequest.of(0, 1), 1));
        // Search the mise
        restMiseMockMvc.perform(get("/api/_search/mises?query=id:" + mise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mise.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())))
            .andExpect(jsonPath("$.[*].dateMise").value(hasItem(DEFAULT_DATE_MISE.toString())))
            .andExpect(jsonPath("$.[*].dateValidation").value(hasItem(DEFAULT_DATE_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].positionClic").value(hasItem(DEFAULT_POSITION_CLIC)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mise.class);
        Mise mise1 = new Mise();
        mise1.setId(1L);
        Mise mise2 = new Mise();
        mise2.setId(mise1.getId());
        assertThat(mise1).isEqualTo(mise2);
        mise2.setId(2L);
        assertThat(mise1).isNotEqualTo(mise2);
        mise1.setId(null);
        assertThat(mise1).isNotEqualTo(mise2);
    }
}
