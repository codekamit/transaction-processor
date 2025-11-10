package com.orm.learn_orm.my_tests.workbook_test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient localApiClient(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8080")
                .build();
    }
}
