package com.cogitech.cyberax.repository.search;

import com.cogitech.cyberax.domain.Gagnant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Gagnant} entity.
 */
public interface GagnantSearchRepository extends ElasticsearchRepository<Gagnant, Long> {
}
