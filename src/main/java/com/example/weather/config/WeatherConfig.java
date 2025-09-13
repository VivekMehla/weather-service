package com.example.weather.config;

import com.example.weather.integration.WeatherApiClient;
import com.example.weather.integration.WeatherCache;
import com.example.weather.integration.impl.OpenWeatherMapApiClient;
import com.example.weather.integration.impl.LFUCacheWithLRUTieBreaker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeatherConfig {
    @Bean
    public WeatherApiClient weatherApiClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        return new OpenWeatherMapApiClient(
                "d2929e9483efc82c82c32ee7e02d563e",
                "https://api.openweathermap.org/data/2.5/forecast",
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
