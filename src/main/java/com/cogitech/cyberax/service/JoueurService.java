package com.cogitech.cyberax.service;

import com.cogitech.cyberax.domain.Joueur;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Joueur}.
 */
public interface JoueurService {

    /**
     * Save a joueur.
     *
     * @param joueur the entity to save.
     * @return the persisted entity.
     */
    Joueur save(Joueur joueur);

    /**
     * Get all the joueurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Joueur> findAll(Pageable pageable);


    /**
     * Get the "id" joueur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Joueur> findOne(Long id);

    /**
     * Delete the "id" joueur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the joueur corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Joueur> search(String query, Pageable pageable);
}
