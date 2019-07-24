package com.cogitech.cyberax.service;

import com.cogitech.cyberax.domain.Jeu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Jeu}.
 */
public interface JeuService {

    /**
     * Save a jeu.
     *
     * @param jeu the entity to save.
     * @return the persisted entity.
     */
    Jeu save(Jeu jeu);

    /**
     * Get all the jeus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Jeu> findAll(Pageable pageable);


    /**
     * Get the "id" jeu.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Jeu> findOne(Long id);

    /**
     * Delete the "id" jeu.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the jeu corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Jeu> search(String query, Pageable pageable);
}
