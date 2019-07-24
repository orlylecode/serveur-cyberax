package com.cogitech.cyberax.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link GagnantSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class GagnantSearchRepositoryMockConfiguration {

    @MockBean
    private GagnantSearchRepository mockGagnantSearchRepository;

}
