package com.cogitech.cyberax.web.rest;

import com.cogitech.cyberax.domain.Terminal;
import com.cogitech.cyberax.repository.TerminalRepository;
import com.cogitech.cyberax.repository.search.TerminalSearchRepository;
import com.cogitech.cyberax.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.cogitech.cyberax.domain.Terminal}.
 */
@RestController
@RequestMapping("/api")
public class TerminalResource {

    private final Logger log = LoggerFactory.getLogger(TerminalResource.class);

    private static final String ENTITY_NAME = "terminal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TerminalRepository terminalRepository;

    private final TerminalSearchRepository terminalSearchRepository;

    public TerminalResource(TerminalRepository terminalRepository, TerminalSearchRepository terminalSearchRepository) {
        this.terminalRepository = terminalRepository;
        this.terminalSearchRepository = terminalSearchRepository;
    }

    /**
     * {@code POST  /terminals} : Create a new terminal.
     *
     * @param terminal the terminal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new terminal, or with status {@code 400 (Bad Request)} if the terminal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/terminals")
    public ResponseEntity<Terminal> createTerminal(@RequestBody Terminal terminal) throws URISyntaxException {
        log.debug("REST request to save Terminal : {}", terminal);
        if (terminal.getId() != null) {
            throw new BadRequestAlertException("A new terminal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Terminal result = terminalRepository.save(terminal);
        terminalSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/terminals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /terminals} : Updates an existing terminal.
     *
     * @param terminal the terminal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated terminal,
     * or with status {@code 400 (Bad Request)} if the terminal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the terminal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/terminals")
    public ResponseEntity<Terminal> updateTerminal(@RequestBody Terminal terminal) throws URISyntaxException {
        log.debug("REST request to update Terminal : {}", terminal);
        if (terminal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Terminal result = terminalRepository.save(terminal);
        terminalSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, terminal.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /terminals} : get all the terminals.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of terminals in body.
     */
    @GetMapping("/terminals")
    public ResponseEntity<List<Terminal>> getAllTerminals(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Terminals");
        Page<Terminal> page = terminalRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /terminals/:id} : get the "id" terminal.
     *
     * @param id the id of the terminal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the terminal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/terminals/{id}")
    public ResponseEntity<Terminal> getTerminal(@PathVariable Long id) {
        log.debug("REST request to get Terminal : {}", id);
        Optional<Terminal> terminal = terminalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(terminal);
    }

    /**
     * {@code DELETE  /terminals/:id} : delete the "id" terminal.
     *
     * @param id the id of the terminal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/terminals/{id}")
    public ResponseEntity<Void> deleteTerminal(@PathVariable Long id) {
        log.debug("REST request to delete Terminal : {}", id);
        terminalRepository.deleteById(id);
        terminalSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/terminals?query=:query} : search for the terminal corresponding
     * to the query.
     *
     * @param query the query of the terminal search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/terminals")
    public ResponseEntity<List<Terminal>> searchTerminals(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Terminals for query {}", query);
        Page<Terminal> page = terminalSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
