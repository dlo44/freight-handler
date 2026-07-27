package org.example.freighthandler;

import org.example.MainApplication;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
@Import(MainApplication.class)

public class TestConfig {
    @Bean
    RestTemplateBuilder restTemplateBuilder() {
        return Mockito.mock(RestTemplateBuilder.class);
    }
}
