package com.cogitech.cyberax.service;

import com.cogitech.cyberax.domain.Mise;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Mise}.
 */
public interface MiseService {

    /**
     * Save a mise.
     *
     * @param mise the entity to save.
     * @return the persisted entity.
     */
    Mise save(Mise mise);

    /**
     * Get all the mises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Mise> findAll(Pageable pageable);


    /**
     * Get the "id" mise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Mise> findOne(Long id);

    /**
     * Delete the "id" mise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the mise corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Mise> search(String query, Pageable pageable);
}
