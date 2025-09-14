package com.example.weather.config;

import com.example.weather.integration.WeatherApiClient;
import com.example.weather.integration.WeatherCache;
import com.example.weather.integration.impl.OpenWeatherMapApiClient;
import com.example.weather.integration.impl.LFUCacheWithLRUTieBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class WeatherConfig {

    private static final Logger logger = LoggerFactory.getLogger(WeatherConfig.class);

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.baseurl}")
    private String weatherApiBaseUrl;

    @Bean
    public WeatherApiClient weatherApiClient(RestTemplate restTemplate) {
        logger.info("Creating WeatherApiClient with base URL: {}", weatherApiBaseUrl);
        return new OpenWeatherMapApiClient(apiKey, weatherApiBaseUrl, restTemplate);
    }

    @Bean
    public RestTemplate restTemplate() {
        logger.debug("Creating RestTemplate bean");
        return new RestTemplate();
    }

    @Bean
    public WeatherCache weatherCache() {
        logger.debug("Creating WeatherCache (LFU with LRU tie breaker)");
        return new LFUCacheWithLRUTieBreaker(5);
    }
}
