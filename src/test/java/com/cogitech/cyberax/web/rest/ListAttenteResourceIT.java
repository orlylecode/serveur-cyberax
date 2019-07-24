package com.cogitech.cyberax.web.rest;

import com.cogitech.cyberax.ServeurCyberaxApp;
import com.cogitech.cyberax.domain.ListAttente;
import com.cogitech.cyberax.repository.ListAttenteRepository;
import com.cogitech.cyberax.repository.search.ListAttenteSearchRepository;
import com.cogitech.cyberax.service.ListAttenteService;
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
 * Integration tests for the {@Link ListAttenteResource} REST controller.
 */
@SpringBootTest(classes = ServeurCyberaxApp.class)
public class ListAttenteResourceIT {

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final Integer DEFAULT_DATE_CREATION = 1;
    private static final Integer UPDATED_DATE_CREATION = 2;

    @Autowired
    private ListAttenteRepository listAttenteRepository;

    @Autowired
    private ListAttenteService listAttenteService;

    /**
     * This repository is mocked in the com.cogitech.cyberax.repository.search test package.
     *
     * @see com.cogitech.cyberax.repository.search.ListAttenteSearchRepositoryMockConfiguration
     */
    @Autowired
    private ListAttenteSearchRepository mockListAttenteSearchRepository;

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

    private MockMvc restListAttenteMockMvc;

    private ListAttente listAttente;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ListAttenteResource listAttenteResource = new ListAttenteResource(listAttenteService);
        this.restListAttenteMockMvc = MockMvcBuilders.standaloneSetup(listAttenteResource)
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
    public static ListAttente createEntity(EntityManager em) {
        ListAttente listAttente = new ListAttente()
            .version(DEFAULT_VERSION)
            .dateCreation(DEFAULT_DATE_CREATION);
        return listAttente;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ListAttente createUpdatedEntity(EntityManager em) {
        ListAttente listAttente = new ListAttente()
            .version(UPDATED_VERSION)
            .dateCreation(UPDATED_DATE_CREATION);
        return listAttente;
    }

    @BeforeEach
    public void initTest() {
        listAttente = createEntity(em);
    }

    @Test
    @Transactional
    public void createListAttente() throws Exception {
        int databaseSizeBeforeCreate = listAttenteRepository.findAll().size();

        // Create the ListAttente
        restListAttenteMockMvc.perform(post("/api/list-attentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listAttente)))
            .andExpect(status().isCreated());

        // Validate the ListAttente in the database
        List<ListAttente> listAttenteList = listAttenteRepository.findAll();
        assertThat(listAttenteList).hasSize(databaseSizeBeforeCreate + 1);
        ListAttente testListAttente = listAttenteList.get(listAttenteList.size() - 1);
        assertThat(testListAttente.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testListAttente.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);

        // Validate the ListAttente in Elasticsearch
        verify(mockListAttenteSearchRepository, times(1)).save(testListAttente);
    }

    @Test
    @Transactional
    public void createListAttenteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = listAttenteRepository.findAll().size();

        // Create the ListAttente with an existing ID
        listAttente.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restListAttenteMockMvc.perform(post("/api/list-attentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listAttente)))
            .andExpect(status().isBadRequest());

        // Validate the ListAttente in the database
        List<ListAttente> listAttenteList = listAttenteRepository.findAll();
        assertThat(listAttenteList).hasSize(databaseSizeBeforeCreate);

        // Validate the ListAttente in Elasticsearch
        verify(mockListAttenteSearchRepository, times(0)).save(listAttente);
    }


    @Test
    @Transactional
    public void getAllListAttentes() throws Exception {
        // Initialize the database
        listAttenteRepository.saveAndFlush(listAttente);

        // Get all the listAttenteList
        restListAttenteMockMvc.perform(get("/api/list-attentes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(listAttente.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION)));
    }
    
    @Test
    @Transactional
    public void getListAttente() throws Exception {
        // Initialize the database
        listAttenteRepository.saveAndFlush(listAttente);

        // Get the listAttente
        restListAttenteMockMvc.perform(get("/api/list-attentes/{id}", listAttente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(listAttente.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION));
    }

    @Test
    @Transactional
    public void getNonExistingListAttente() throws Exception {
        // Get the listAttente
        restListAttenteMockMvc.perform(get("/api/list-attentes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateListAttente() throws Exception {
        // Initialize the database
        listAttenteService.save(listAttente);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockListAttenteSearchRepository);

        int databaseSizeBeforeUpdate = listAttenteRepository.findAll().size();

        // Update the listAttente
        ListAttente updatedListAttente = listAttenteRepository.findById(listAttente.getId()).get();
        // Disconnect from session so that the updates on updatedListAttente are not directly saved in db
        em.detach(updatedListAttente);
        updatedListAttente
            .version(UPDATED_VERSION)
            .dateCreation(UPDATED_DATE_CREATION);

        restListAttenteMockMvc.perform(put("/api/list-attentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedListAttente)))
            .andExpect(status().isOk());

        // Validate the ListAttente in the database
        List<ListAttente> listAttenteList = listAttenteRepository.findAll();
        assertThat(listAttenteList).hasSize(databaseSizeBeforeUpdate);
        ListAttente testListAttente = listAttenteList.get(listAttenteList.size() - 1);
        assertThat(testListAttente.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testListAttente.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);

        // Validate the ListAttente in Elasticsearch
        verify(mockListAttenteSearchRepository, times(1)).save(testListAttente);
    }

    @Test
    @Transactional
    public void updateNonExistingListAttente() throws Exception {
        int databaseSizeBeforeUpdate = listAttenteRepository.findAll().size();

        // Create the ListAttente

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restListAttenteMockMvc.perform(put("/api/list-attentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(listAttente)))
            .andExpect(status().isBadRequest());

        // Validate the ListAttente in the database
        List<ListAttente> listAttenteList = listAttenteRepository.findAll();
        assertThat(listAttenteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ListAttente in Elasticsearch
        verify(mockListAttenteSearchRepository, times(0)).save(listAttente);
    }

    @Test
    @Transactional
    public void deleteListAttente() throws Exception {
        // Initialize the database
        listAttenteService.save(listAttente);

        int databaseSizeBeforeDelete = listAttenteRepository.findAll().size();

        // Delete the listAttente
        restListAttenteMockMvc.perform(delete("/api/list-attentes/{id}", listAttente.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ListAttente> listAttenteList = listAttenteRepository.findAll();
        assertThat(listAttenteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ListAttente in Elasticsearch
        verify(mockListAttenteSearchRepository, times(1)).deleteById(listAttente.getId());
    }

    @Test
    @Transactional
    public void searchListAttente() throws Exception {
        // Initialize the database
        listAttenteService.save(listAttente);
        when(mockListAttenteSearchRepository.search(queryStringQuery("id:" + listAttente.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(listAttente), PageRequest.of(0, 1), 1));
        // Search the listAttente
        restListAttenteMockMvc.perform(get("/api/_search/list-attentes?query=id:" + listAttente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(listAttente.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ListAttente.class);
        ListAttente listAttente1 = new ListAttente();
        listAttente1.setId(1L);
        ListAttente listAttente2 = new ListAttente();
        listAttente2.setId(listAttente1.getId());
        assertThat(listAttente1).isEqualTo(listAttente2);
        listAttente2.setId(2L);
        assertThat(listAttente1).isNotEqualTo(listAttente2);
        listAttente1.setId(null);
        assertThat(listAttente1).isNotEqualTo(listAttente2);
    }
}
