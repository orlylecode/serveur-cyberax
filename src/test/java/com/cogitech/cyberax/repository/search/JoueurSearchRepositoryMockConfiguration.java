package com.cogitech.cyberax.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link JoueurSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class JoueurSearchRepositoryMockConfiguration {

    @MockBean
    private JoueurSearchRepository mockJoueurSearchRepository;

}
