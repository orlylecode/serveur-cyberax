package com.cogitech.cyberax.repository.search;

import com.cogitech.cyberax.domain.Terminal;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Terminal} entity.
 */
public interface TerminalSearchRepository extends ElasticsearchRepository<Terminal, Long> {
}
