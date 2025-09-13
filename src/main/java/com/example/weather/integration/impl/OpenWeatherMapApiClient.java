package com.example.weather.integration.impl;

import com.example.weather.dto.responseDto.WeatherApiResponseDto;
import com.example.weather.integration.WeatherApiClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;


public class OpenWeatherMapApiClient implements WeatherApiClient {
    private final String apiKey;
    private final String baseUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OpenWeatherMapApiClient(
            @Value("${weather.api.key}") String apiKey,
            @Value("${weather.api.baseurl}") String baseUrl,
            RestTemplate restTemplate,
            ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public WeatherApiResponseDto fetchWeatherData(String city) throws Exception {
        String url = String.format("%s?q=%s&appid=%s&cnt=24", baseUrl, city, apiKey);
        String jsonResponse = restTemplate.getForObject(url, String.class);
        return objectMapper.readValue(jsonResponse, WeatherApiResponseDto.class);
    }
}
