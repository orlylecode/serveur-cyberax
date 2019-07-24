package com.cogitech.cyberax.web.rest;

import com.cogitech.cyberax.domain.Mise;
import com.cogitech.cyberax.service.MiseService;
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
 * REST controller for managing {@link com.cogitech.cyberax.domain.Mise}.
 */
@RestController
@RequestMapping("/api")
public class MiseResource {

    private final Logger log = LoggerFactory.getLogger(MiseResource.class);

    private static final String ENTITY_NAME = "mise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MiseService miseService;

    public MiseResource(MiseService miseService) {
        this.miseService = miseService;
    }

    /**
     * {@code POST  /mises} : Create a new mise.
     *
     * @param mise the mise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mise, or with status {@code 400 (Bad Request)} if the mise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mises")
    public ResponseEntity<Mise> createMise(@RequestBody Mise mise) throws URISyntaxException {
        log.debug("REST request to save Mise : {}", mise);
        if (mise.getId() != null) {
            throw new BadRequestAlertException("A new mise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Mise result = miseService.save(mise);
        return ResponseEntity.created(new URI("/api/mises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mises} : Updates an existing mise.
     *
     * @param mise the mise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mise,
     * or with status {@code 400 (Bad Request)} if the mise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mises")
    public ResponseEntity<Mise> updateMise(@RequestBody Mise mise) throws URISyntaxException {
        log.debug("REST request to update Mise : {}", mise);
        if (mise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Mise result = miseService.save(mise);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, mise.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /mises} : get all the mises.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mises in body.
     */
    @GetMapping("/mises")
    public ResponseEntity<List<Mise>> getAllMises(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Mises");
        Page<Mise> page = miseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mises/:id} : get the "id" mise.
     *
     * @param id the id of the mise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mises/{id}")
    public ResponseEntity<Mise> getMise(@PathVariable Long id) {
        log.debug("REST request to get Mise : {}", id);
        Optional<Mise> mise = miseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mise);
    }

    /**
     * {@code DELETE  /mises/:id} : delete the "id" mise.
     *
     * @param id the id of the mise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mises/{id}")
    public ResponseEntity<Void> deleteMise(@PathVariable Long id) {
        log.debug("REST request to delete Mise : {}", id);
        miseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/mises?query=:query} : search for the mise corresponding
     * to the query.
     *
     * @param query the query of the mise search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/mises")
    public ResponseEntity<List<Mise>> searchMises(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Mises for query {}", query);
        Page<Mise> page = miseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
