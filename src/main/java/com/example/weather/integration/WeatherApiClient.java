package com.example.weather.integration;

import com.example.weather.dto.responseDto.WeatherApiResponseDto;

public interface WeatherApiClient {
    WeatherApiResponseDto fetchWeatherData(String city) throws Exception;
}
