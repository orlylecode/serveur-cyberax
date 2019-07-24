package com.cogitech.cyberax.web.rest;

import com.cogitech.cyberax.ServeurCyberaxApp;
import com.cogitech.cyberax.domain.Joueur;
import com.cogitech.cyberax.repository.JoueurRepository;
import com.cogitech.cyberax.repository.search.JoueurSearchRepository;
import com.cogitech.cyberax.service.JoueurService;
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
 * Integration tests for the {@Link JoueurResource} REST controller.
 */
@SpringBootTest(classes = ServeurCyberaxApp.class)
public class JoueurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    @Autowired
    private JoueurRepository joueurRepository;

    @Autowired
    private JoueurService joueurService;

    /**
     * This repository is mocked in the com.cogitech.cyberax.repository.search test package.
     *
     * @see com.cogitech.cyberax.repository.search.JoueurSearchRepositoryMockConfiguration
     */
    @Autowired
    private JoueurSearchRepository mockJoueurSearchRepository;

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

    private MockMvc restJoueurMockMvc;

    private Joueur joueur;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JoueurResource joueurResource = new JoueurResource(joueurService);
        this.restJoueurMockMvc = MockMvcBuilders.standaloneSetup(joueurResource)
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
    public static Joueur createEntity(EntityManager em) {
        Joueur joueur = new Joueur()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .telephone(DEFAULT_TELEPHONE);
        return joueur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Joueur createUpdatedEntity(EntityManager em) {
        Joueur joueur = new Joueur()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE);
        return joueur;
    }

    @BeforeEach
    public void initTest() {
        joueur = createEntity(em);
    }

    @Test
    @Transactional
    public void createJoueur() throws Exception {
        int databaseSizeBeforeCreate = joueurRepository.findAll().size();

        // Create the Joueur
        restJoueurMockMvc.perform(post("/api/joueurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joueur)))
            .andExpect(status().isCreated());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeCreate + 1);
        Joueur testJoueur = joueurList.get(joueurList.size() - 1);
        assertThat(testJoueur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testJoueur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testJoueur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);

        // Validate the Joueur in Elasticsearch
        verify(mockJoueurSearchRepository, times(1)).save(testJoueur);
    }

    @Test
    @Transactional
    public void createJoueurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = joueurRepository.findAll().size();

        // Create the Joueur with an existing ID
        joueur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJoueurMockMvc.perform(post("/api/joueurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joueur)))
            .andExpect(status().isBadRequest());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeCreate);

        // Validate the Joueur in Elasticsearch
        verify(mockJoueurSearchRepository, times(0)).save(joueur);
    }


    @Test
    @Transactional
    public void getAllJoueurs() throws Exception {
        // Initialize the database
        joueurRepository.saveAndFlush(joueur);

        // Get all the joueurList
        restJoueurMockMvc.perform(get("/api/joueurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(joueur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())));
    }
    
    @Test
    @Transactional
    public void getJoueur() throws Exception {
        // Initialize the database
        joueurRepository.saveAndFlush(joueur);

        // Get the joueur
        restJoueurMockMvc.perform(get("/api/joueurs/{id}", joueur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(joueur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJoueur() throws Exception {
        // Get the joueur
        restJoueurMockMvc.perform(get("/api/joueurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJoueur() throws Exception {
        // Initialize the database
        joueurService.save(joueur);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockJoueurSearchRepository);

        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();

        // Update the joueur
        Joueur updatedJoueur = joueurRepository.findById(joueur.getId()).get();
        // Disconnect from session so that the updates on updatedJoueur are not directly saved in db
        em.detach(updatedJoueur);
        updatedJoueur
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .telephone(UPDATED_TELEPHONE);

        restJoueurMockMvc.perform(put("/api/joueurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJoueur)))
            .andExpect(status().isOk());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
        Joueur testJoueur = joueurList.get(joueurList.size() - 1);
        assertThat(testJoueur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testJoueur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testJoueur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);

        // Validate the Joueur in Elasticsearch
        verify(mockJoueurSearchRepository, times(1)).save(testJoueur);
    }

    @Test
    @Transactional
    public void updateNonExistingJoueur() throws Exception {
        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();

        // Create the Joueur

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJoueurMockMvc.perform(put("/api/joueurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joueur)))
            .andExpect(status().isBadRequest());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Joueur in Elasticsearch
        verify(mockJoueurSearchRepository, times(0)).save(joueur);
    }

    @Test
    @Transactional
    public void deleteJoueur() throws Exception {
        // Initialize the database
        joueurService.save(joueur);

        int databaseSizeBeforeDelete = joueurRepository.findAll().size();

        // Delete the joueur
        restJoueurMockMvc.perform(delete("/api/joueurs/{id}", joueur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Joueur in Elasticsearch
        verify(mockJoueurSearchRepository, times(1)).deleteById(joueur.getId());
    }

    @Test
    @Transactional
    public void searchJoueur() throws Exception {
        // Initialize the database
        joueurService.save(joueur);
        when(mockJoueurSearchRepository.search(queryStringQuery("id:" + joueur.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(joueur), PageRequest.of(0, 1), 1));
        // Search the joueur
        restJoueurMockMvc.perform(get("/api/_search/joueurs?query=id:" + joueur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(joueur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Joueur.class);
        Joueur joueur1 = new Joueur();
        joueur1.setId(1L);
        Joueur joueur2 = new Joueur();
        joueur2.setId(joueur1.getId());
        assertThat(joueur1).isEqualTo(joueur2);
        joueur2.setId(2L);
        assertThat(joueur1).isNotEqualTo(joueur2);
        joueur1.setId(null);
        assertThat(joueur1).isNotEqualTo(joueur2);
    }
}
