package com.Isabela01vSilva.bank_isabela.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${schedule.url}")
    private String scheduleUrl;

    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return builder.baseUrl(scheduleUrl).build();
    }
}
