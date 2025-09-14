package com.example.weather.integration;

import com.example.weather.dto.DailyForecast;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeatherServiceIntegrationTest {

    @Autowired
    private WeatherService weatherService;

    @Test
    void shouldFetchAndCacheForecastSuccessfully() {
        String city = "London";
        Boolean isOffline = true;
        List<DailyForecast> result1 = weatherService.fetchForecast(city,isOffline);
        List<DailyForecast> result2 = weatherService.fetchForecast(city,isOffline);

        assertNotNull(result1);
        assertFalse(result1.isEmpty());
        assertEquals(result1, result2); // Cached response should match
    }
}
