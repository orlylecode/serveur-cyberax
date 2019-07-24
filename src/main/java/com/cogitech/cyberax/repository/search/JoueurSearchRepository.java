package com.cogitech.cyberax.repository.search;

import com.cogitech.cyberax.domain.Joueur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Joueur} entity.
 */
public interface JoueurSearchRepository extends ElasticsearchRepository<Joueur, Long> {
}
