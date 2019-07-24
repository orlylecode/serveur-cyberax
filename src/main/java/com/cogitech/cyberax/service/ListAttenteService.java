package com.cogitech.cyberax.service;

import com.cogitech.cyberax.domain.ListAttente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ListAttente}.
 */
public interface ListAttenteService {

    /**
     * Save a listAttente.
     *
     * @param listAttente the entity to save.
     * @return the persisted entity.
     */
    ListAttente save(ListAttente listAttente);

    /**
     * Get all the listAttentes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ListAttente> findAll(Pageable pageable);


    /**
     * Get the "id" listAttente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ListAttente> findOne(Long id);

    /**
     * Delete the "id" listAttente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the listAttente corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ListAttente> search(String query, Pageable pageable);
}
