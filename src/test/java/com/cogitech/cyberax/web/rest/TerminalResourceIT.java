package com.cogitech.cyberax.web.rest;

import com.cogitech.cyberax.ServeurCyberaxApp;
import com.cogitech.cyberax.domain.Terminal;
import com.cogitech.cyberax.repository.TerminalRepository;
import com.cogitech.cyberax.repository.search.TerminalSearchRepository;
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
 * Integration tests for the {@Link TerminalResource} REST controller.
 */
@SpringBootTest(classes = ServeurCyberaxApp.class)
public class TerminalResourceIT {

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private TerminalRepository terminalRepository;

    /**
     * This repository is mocked in the com.cogitech.cyberax.repository.search test package.
     *
     * @see com.cogitech.cyberax.repository.search.TerminalSearchRepositoryMockConfiguration
     */
    @Autowired
    private TerminalSearchRepository mockTerminalSearchRepository;

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

    private MockMvc restTerminalMockMvc;

    private Terminal terminal;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TerminalResource terminalResource = new TerminalResource(terminalRepository, mockTerminalSearchRepository);
        this.restTerminalMockMvc = MockMvcBuilders.standaloneSetup(terminalResource)
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
    public static Terminal createEntity(EntityManager em) {
        Terminal terminal = new Terminal()
            .message(DEFAULT_MESSAGE);
        return terminal;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Terminal createUpdatedEntity(EntityManager em) {
        Terminal terminal = new Terminal()
            .message(UPDATED_MESSAGE);
        return terminal;
    }

    @BeforeEach
    public void initTest() {
        terminal = createEntity(em);
    }

    @Test
    @Transactional
    public void createTerminal() throws Exception {
        int databaseSizeBeforeCreate = terminalRepository.findAll().size();

        // Create the Terminal
        restTerminalMockMvc.perform(post("/api/terminals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(terminal)))
            .andExpect(status().isCreated());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeCreate + 1);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getMessage()).isEqualTo(DEFAULT_MESSAGE);

        // Validate the Terminal in Elasticsearch
        verify(mockTerminalSearchRepository, times(1)).save(testTerminal);
    }

    @Test
    @Transactional
    public void createTerminalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = terminalRepository.findAll().size();

        // Create the Terminal with an existing ID
        terminal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTerminalMockMvc.perform(post("/api/terminals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(terminal)))
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeCreate);

        // Validate the Terminal in Elasticsearch
        verify(mockTerminalSearchRepository, times(0)).save(terminal);
    }


    @Test
    @Transactional
    public void getAllTerminals() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList
        restTerminalMockMvc.perform(get("/api/terminals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminal.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }
    
    @Test
    @Transactional
    public void getTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get the terminal
        restTerminalMockMvc.perform(get("/api/terminals/{id}", terminal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(terminal.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTerminal() throws Exception {
        // Get the terminal
        restTerminalMockMvc.perform(get("/api/terminals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Update the terminal
        Terminal updatedTerminal = terminalRepository.findById(terminal.getId()).get();
        // Disconnect from session so that the updates on updatedTerminal are not directly saved in db
        em.detach(updatedTerminal);
        updatedTerminal
            .message(UPDATED_MESSAGE);

        restTerminalMockMvc.perform(put("/api/terminals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTerminal)))
            .andExpect(status().isOk());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getMessage()).isEqualTo(UPDATED_MESSAGE);

        // Validate the Terminal in Elasticsearch
        verify(mockTerminalSearchRepository, times(1)).save(testTerminal);
    }

    @Test
    @Transactional
    public void updateNonExistingTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Create the Terminal

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTerminalMockMvc.perform(put("/api/terminals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(terminal)))
            .andExpect(status().isBadRequest());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Terminal in Elasticsearch
        verify(mockTerminalSearchRepository, times(0)).save(terminal);
    }

    @Test
    @Transactional
    public void deleteTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        int databaseSizeBeforeDelete = terminalRepository.findAll().size();

        // Delete the terminal
        restTerminalMockMvc.perform(delete("/api/terminals/{id}", terminal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Terminal in Elasticsearch
        verify(mockTerminalSearchRepository, times(1)).deleteById(terminal.getId());
    }

    @Test
    @Transactional
    public void searchTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);
        when(mockTerminalSearchRepository.search(queryStringQuery("id:" + terminal.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(terminal), PageRequest.of(0, 1), 1));
        // Search the terminal
        restTerminalMockMvc.perform(get("/api/_search/terminals?query=id:" + terminal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminal.getId().intValue())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Terminal.class);
        Terminal terminal1 = new Terminal();
        terminal1.setId(1L);
        Terminal terminal2 = new Terminal();
        terminal2.setId(terminal1.getId());
        assertThat(terminal1).isEqualTo(terminal2);
        terminal2.setId(2L);
        assertThat(terminal1).isNotEqualTo(terminal2);
        terminal1.setId(null);
        assertThat(terminal1).isNotEqualTo(terminal2);
    }
}
