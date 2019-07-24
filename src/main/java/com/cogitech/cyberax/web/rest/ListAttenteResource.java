package com.cogitech.cyberax.web.rest;

import com.cogitech.cyberax.domain.ListAttente;
import com.cogitech.cyberax.service.ListAttenteService;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.cogitech.cyberax.domain.ListAttente}.
 */
@RestController
@RequestMapping("/api")
public class ListAttenteResource {

    private final Logger log = LoggerFactory.getLogger(ListAttenteResource.class);

    private static final String ENTITY_NAME = "listAttente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ListAttenteService listAttenteService;

    public ListAttenteResource(ListAttenteService listAttenteService) {
        this.listAttenteService = listAttenteService;
    }

    /**
     * {@code POST  /list-attentes} : Create a new listAttente.
     *
     * @param listAttente the listAttente to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new listAttente, or with status {@code 400 (Bad Request)} if the listAttente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/list-attentes")
    public ResponseEntity<ListAttente> createListAttente(@RequestBody ListAttente listAttente) throws URISyntaxException {
        log.debug("REST request to save ListAttente : {}", listAttente);
        if (listAttente.getId() != null) {
            throw new BadRequestAlertException("A new listAttente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ListAttente result = listAttenteService.save(listAttente);
        return ResponseEntity.created(new URI("/api/list-attentes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /list-attentes} : Updates an existing listAttente.
     *
     * @param listAttente the listAttente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated listAttente,
     * or with status {@code 400 (Bad Request)} if the listAttente is not valid,
     * or with status {@code 500 (Internal Server Error)} if the listAttente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/list-attentes")
    public ResponseEntity<ListAttente> updateListAttente(@RequestBody ListAttente listAttente) throws URISyntaxException {
        log.debug("REST request to update ListAttente : {}", listAttente);
        if (listAttente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ListAttente result = listAttenteService.save(listAttente);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, listAttente.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /list-attentes} : get all the listAttentes.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of listAttentes in body.
     */
    @GetMapping("/list-attentes")
    public ResponseEntity<List<ListAttente>> getAllListAttentes(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of ListAttentes");
        Page<ListAttente> page = listAttenteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /list-attentes/:id} : get the "id" listAttente.
     *
     * @param id the id of the listAttente to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the listAttente, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/list-attentes/{id}")
    public ResponseEntity<ListAttente> getListAttente(@PathVariable Long id) {
        log.debug("REST request to get ListAttente : {}", id);
        Optional<ListAttente> listAttente = listAttenteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(listAttente);
    }

    /**
     * {@code DELETE  /list-attentes/:id} : delete the "id" listAttente.
     *
     * @param id the id of the listAttente to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/list-attentes/{id}")
    public ResponseEntity<Void> deleteListAttente(@PathVariable Long id) {
        log.debug("REST request to delete ListAttente : {}", id);
        listAttenteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/list-attentes?query=:query} : search for the listAttente corresponding
     * to the query.
     *
     * @param query the query of the listAttente search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/list-attentes")
    public ResponseEntity<List<ListAttente>> searchListAttentes(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of ListAttentes for query {}", query);
        Page<ListAttente> page = listAttenteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
