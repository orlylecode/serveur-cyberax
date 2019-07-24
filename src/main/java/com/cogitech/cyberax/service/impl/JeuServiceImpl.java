package com.cogitech.cyberax.service.impl;

import com.cogitech.cyberax.service.JeuService;
import com.cogitech.cyberax.domain.Jeu;
import com.cogitech.cyberax.repository.JeuRepository;
import com.cogitech.cyberax.repository.search.JeuSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Jeu}.
 */
@Service
@Transactional
public class JeuServiceImpl implements JeuService {

    private final Logger log = LoggerFactory.getLogger(JeuServiceImpl.class);

    private final JeuRepository jeuRepository;

    private final JeuSearchRepository jeuSearchRepository;

    public JeuServiceImpl(JeuRepository jeuRepository, JeuSearchRepository jeuSearchRepository) {
        this.jeuRepository = jeuRepository;
        this.jeuSearchRepository = jeuSearchRepository;
    }

    /**
     * Save a jeu.
     *
     * @param jeu the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Jeu save(Jeu jeu) {
        log.debug("Request to save Jeu : {}", jeu);
        Jeu result = jeuRepository.save(jeu);
        jeuSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the jeus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Jeu> findAll(Pageable pageable) {
        log.debug("Request to get all Jeus");
        return jeuRepository.findAll(pageable);
    }


    /**
     * Get one jeu by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Jeu> findOne(Long id) {
        log.debug("Request to get Jeu : {}", id);
        return jeuRepository.findById(id);
    }

    /**
     * Delete the jeu by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Jeu : {}", id);
        jeuRepository.deleteById(id);
        jeuSearchRepository.deleteById(id);
    }

    /**
     * Search for the jeu corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Jeu> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Jeus for query {}", query);
        return jeuSearchRepository.search(queryStringQuery(query), pageable);    }
}
