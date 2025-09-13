package com.example.weather.integration;

import com.example.weather.dto.DailyForecast;

import java.util.List;

public interface WeatherService {
    public List<DailyForecast> fetchForecast(String city);
}
