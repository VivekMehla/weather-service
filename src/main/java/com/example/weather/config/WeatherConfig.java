package com.example.weather.config;

import com.example.weather.integration.WeatherApiClient;
import com.example.weather.integration.WeatherCache;
import com.example.weather.integration.impl.OpenWeatherMapApiClient;
import com.example.weather.integration.impl.LFUCacheWithLRUTieBreaker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WeatherConfig {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.baseurl}")
    private String weatherApiBaseUrl;

    @Bean
    public WeatherApiClient weatherApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        return new OpenWeatherMapApiClient(
                apiKey,
                weatherApiBaseUrl,
                restTemplate,
                objectMapper
        );
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WeatherCache weatherCache() {
        return new LFUCacheWithLRUTieBreaker(5);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
