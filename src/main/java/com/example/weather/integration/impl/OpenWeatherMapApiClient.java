package com.example.weather.integration.impl;

import com.example.weather.dto.responseDto.WeatherApiResponseDto;
import com.example.weather.integration.WeatherApiClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class OpenWeatherMapApiClient implements WeatherApiClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenWeatherMapApiClient.class);

    private final String apiKey;
    private final String baseUrl;
    private final RestTemplate restTemplate;

    public OpenWeatherMapApiClient(
            @Value("${weather.api.key}") String apiKey,
            @Value("${weather.api.baseurl}") String baseUrl,
            RestTemplate restTemplate) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherApiResponseDto fetchWeatherData(String city) throws Exception {
        String url = String.format("%s?q=%s&appid=%s&cnt=24", baseUrl, city, apiKey);

        logger.info("Fetching weather data for city: {}", city);
        logger.debug("Constructed URL: {}", url);

        try {
            WeatherApiResponseDto response = restTemplate.getForObject(url, WeatherApiResponseDto.class);
            logger.debug("Received JSON response: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Failed to fetch or parse weather data for city: {}", city, e);
            throw e;
        }
    }
}
