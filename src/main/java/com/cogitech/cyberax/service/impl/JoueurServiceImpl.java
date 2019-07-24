package com.cogitech.cyberax.service.impl;

import com.cogitech.cyberax.service.JoueurService;
import com.cogitech.cyberax.domain.Joueur;
import com.cogitech.cyberax.repository.JoueurRepository;
import com.cogitech.cyberax.repository.search.JoueurSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Joueur}.
 */
@Service
@Transactional
public class JoueurServiceImpl implements JoueurService {

    private final Logger log = LoggerFactory.getLogger(JoueurServiceImpl.class);

    private final JoueurRepository joueurRepository;

    private final JoueurSearchRepository joueurSearchRepository;

    public JoueurServiceImpl(JoueurRepository joueurRepository, JoueurSearchRepository joueurSearchRepository) {
        this.joueurRepository = joueurRepository;
        this.joueurSearchRepository = joueurSearchRepository;
    }

    /**
     * Save a joueur.
     *
     * @param joueur the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Joueur save(Joueur joueur) {
        log.debug("Request to save Joueur : {}", joueur);
        Joueur result = joueurRepository.save(joueur);
        joueurSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the joueurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Joueur> findAll(Pageable pageable) {
        log.debug("Request to get all Joueurs");
        return joueurRepository.findAll(pageable);
    }


    /**
     * Get one joueur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Joueur> findOne(Long id) {
        log.debug("Request to get Joueur : {}", id);
        return joueurRepository.findById(id);
    }

    /**
     * Delete the joueur by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Joueur : {}", id);
        joueurRepository.deleteById(id);
        joueurSearchRepository.deleteById(id);
    }

    /**
     * Search for the joueur corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Joueur> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Joueurs for query {}", query);
        return joueurSearchRepository.search(queryStringQuery(query), pageable);    }
}
