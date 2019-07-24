package com.cogitech.cyberax.web.rest;

import com.cogitech.cyberax.domain.Gagnant;
import com.cogitech.cyberax.service.GagnantService;
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
 * REST controller for managing {@link com.cogitech.cyberax.domain.Gagnant}.
 */
@RestController
@RequestMapping("/api")
public class GagnantResource {

    private final Logger log = LoggerFactory.getLogger(GagnantResource.class);

    private static final String ENTITY_NAME = "gagnant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GagnantService gagnantService;

    public GagnantResource(GagnantService gagnantService) {
        this.gagnantService = gagnantService;
    }

    /**
     * {@code POST  /gagnants} : Create a new gagnant.
     *
     * @param gagnant the gagnant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gagnant, or with status {@code 400 (Bad Request)} if the gagnant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gagnants")
    public ResponseEntity<Gagnant> createGagnant(@RequestBody Gagnant gagnant) throws URISyntaxException {
        log.debug("REST request to save Gagnant : {}", gagnant);
        if (gagnant.getId() != null) {
            throw new BadRequestAlertException("A new gagnant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gagnant result = gagnantService.save(gagnant);
        return ResponseEntity.created(new URI("/api/gagnants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gagnants} : Updates an existing gagnant.
     *
     * @param gagnant the gagnant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gagnant,
     * or with status {@code 400 (Bad Request)} if the gagnant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gagnant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gagnants")
    public ResponseEntity<Gagnant> updateGagnant(@RequestBody Gagnant gagnant) throws URISyntaxException {
        log.debug("REST request to update Gagnant : {}", gagnant);
        if (gagnant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Gagnant result = gagnantService.save(gagnant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gagnant.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /gagnants} : get all the gagnants.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gagnants in body.
     */
    @GetMapping("/gagnants")
    public ResponseEntity<List<Gagnant>> getAllGagnants(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of Gagnants");
        Page<Gagnant> page = gagnantService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gagnants/:id} : get the "id" gagnant.
     *
     * @param id the id of the gagnant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gagnant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gagnants/{id}")
    public ResponseEntity<Gagnant> getGagnant(@PathVariable Long id) {
        log.debug("REST request to get Gagnant : {}", id);
        Optional<Gagnant> gagnant = gagnantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gagnant);
    }

    /**
     * {@code DELETE  /gagnants/:id} : delete the "id" gagnant.
     *
     * @param id the id of the gagnant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gagnants/{id}")
    public ResponseEntity<Void> deleteGagnant(@PathVariable Long id) {
        log.debug("REST request to delete Gagnant : {}", id);
        gagnantService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/gagnants?query=:query} : search for the gagnant corresponding
     * to the query.
     *
     * @param query the query of the gagnant search.
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the result of the search.
     */
    @GetMapping("/_search/gagnants")
    public ResponseEntity<List<Gagnant>> searchGagnants(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Gagnants for query {}", query);
        Page<Gagnant> page = gagnantService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
