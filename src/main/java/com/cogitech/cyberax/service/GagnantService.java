package com.cogitech.cyberax.service;

import com.cogitech.cyberax.domain.Gagnant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Gagnant}.
 */
public interface GagnantService {

    /**
     * Save a gagnant.
     *
     * @param gagnant the entity to save.
     * @return the persisted entity.
     */
    Gagnant save(Gagnant gagnant);

    /**
     * Get all the gagnants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Gagnant> findAll(Pageable pageable);


    /**
     * Get the "id" gagnant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Gagnant> findOne(Long id);

    /**
     * Delete the "id" gagnant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the gagnant corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Gagnant> search(String query, Pageable pageable);
}
