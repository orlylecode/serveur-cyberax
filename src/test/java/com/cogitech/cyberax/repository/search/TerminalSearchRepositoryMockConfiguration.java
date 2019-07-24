package com.cogitech.cyberax.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link TerminalSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TerminalSearchRepositoryMockConfiguration {

    @MockBean
    private TerminalSearchRepository mockTerminalSearchRepository;

}
