package com.cogitech.cyberax.service.impl;

import com.cogitech.cyberax.service.ListAttenteService;
import com.cogitech.cyberax.domain.ListAttente;
import com.cogitech.cyberax.repository.ListAttenteRepository;
import com.cogitech.cyberax.repository.search.ListAttenteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ListAttente}.
 */
@Service
@Transactional
public class ListAttenteServiceImpl implements ListAttenteService {

    private final Logger log = LoggerFactory.getLogger(ListAttenteServiceImpl.class);

    private final ListAttenteRepository listAttenteRepository;

    private final ListAttenteSearchRepository listAttenteSearchRepository;

    public ListAttenteServiceImpl(ListAttenteRepository listAttenteRepository, ListAttenteSearchRepository listAttenteSearchRepository) {
        this.listAttenteRepository = listAttenteRepository;
        this.listAttenteSearchRepository = listAttenteSearchRepository;
    }

    /**
     * Save a listAttente.
     *
     * @param listAttente the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ListAttente save(ListAttente listAttente) {
        log.debug("Request to save ListAttente : {}", listAttente);
        ListAttente result = listAttenteRepository.save(listAttente);
        listAttenteSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the listAttentes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ListAttente> findAll(Pageable pageable) {
        log.debug("Request to get all ListAttentes");
        return listAttenteRepository.findAll(pageable);
    }


    /**
     * Get one listAttente by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ListAttente> findOne(Long id) {
        log.debug("Request to get ListAttente : {}", id);
        return listAttenteRepository.findById(id);
    }

    /**
     * Delete the listAttente by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ListAttente : {}", id);
        listAttenteRepository.deleteById(id);
        listAttenteSearchRepository.deleteById(id);
    }

    /**
     * Search for the listAttente corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ListAttente> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ListAttentes for query {}", query);
        return listAttenteSearchRepository.search(queryStringQuery(query), pageable);    }
}
