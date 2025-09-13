package com.example.weather.integration;

import java.util.List;
import com.example.weather.dto.DailyForecast;

public interface WeatherCache {
    List<DailyForecast> get(String city);
    void put(String city, List<DailyForecast> forecast);
}

