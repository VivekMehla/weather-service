package com.example.weather.unit;

import com.example.weather.dto.DailyForecast;
import com.example.weather.dto.responseDto.WeatherApiResponseDto;
import com.example.weather.dto.responseDto.WeatherApiResponseDto.Main;
import com.example.weather.dto.responseDto.WeatherApiResponseDto.Wind;
import com.example.weather.dto.responseDto.WeatherApiResponseDto.Weather;
import com.example.weather.dto.responseDto.WeatherApiResponseDto.WeatherEntry;
import com.example.weather.integration.WeatherApiClient;
import com.example.weather.integration.WeatherCache;
import com.example.weather.integration.impl.WeatherServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherServiceImplTest {

    private WeatherApiClient apiClient;
    private WeatherCache cache;
    private WeatherServiceImpl weatherService;

    @BeforeEach
    void setUp() {
        apiClient = mock(WeatherApiClient.class);
        cache = mock(WeatherCache.class);
        weatherService = new WeatherServiceImpl(apiClient, cache);
    }

    @Test
    void shouldReturnCachedForecastIfPresent() throws Exception {
        String city = "London";
        Boolean isOffline = false;
        List<DailyForecast> mockForecast = createMockForecastList();
        when(cache.get(city)).thenReturn(mockForecast);

        List<DailyForecast> result = weatherService.fetchForecast(city,isOffline);

        assertEquals(mockForecast, result);
        verify(cache, never()).put(anyString(), any());
        verify(apiClient, never()).fetchWeatherData(anyString());
    }

    @Test
    void shouldFetchFromApiAndCacheIfNotInCache() throws Exception {
        String city = "Paris";
        Boolean isOffline = false;
        when(cache.get(city)).thenReturn(null);

        WeatherApiResponseDto response = createMockWeatherApiResponse();
        when(apiClient.fetchWeatherData(city)).thenReturn(response);

        List<DailyForecast> result = weatherService.fetchForecast(city,isOffline);

        assertEquals(1, result.size());
        assertEquals("2025-09-12 12:00:00", result.get(0).getDate());
        verify(cache).put(eq(city), any());
    }

    @Test
    void shouldReturnMockOnException() throws Exception {
        String city = "Tokyo";
        Boolean isOffline = true;
        when(cache.get(city)).thenReturn(null);
        when(apiClient.fetchWeatherData(city)).thenThrow(new RuntimeException("API down"));

        List<DailyForecast> result = weatherService.fetchForecast(city, isOffline);

        assertEquals(1, result.size());
        assertTrue(result.get(0).isMock());
        assertEquals("Offline mode: demo only", result.get(0).getPrediction());
    }

    // ---------- MOCK HELPERS (Inline) ----------

    private List<DailyForecast> createMockForecastList() {
        return List.of(
                DailyForecast.builder()
                        .date("2025-09-12 06:00:00")
                        .minTemp(22)
                        .maxTemp(30)
                        .maxWind(5)
                        .rain(false)
                        .thunderstorm(false)
                        .prediction("Good weather")
                        .build()
        );
    }

    private WeatherApiResponseDto createMockWeatherApiResponse() {
        WeatherEntry entry = new WeatherEntry();
        entry.setDt_txt("2025-09-12 12:00:00");

        Main main = new Main();
        main.setTemp(298.15); // 25°C
        entry.setMain(main);

        Wind wind = new Wind();
        wind.setSpeed(4.0);
        entry.setWind(wind);

        Weather weather = new Weather();
        weather.setMain("Clouds");
        entry.setWeather(List.of(weather));

        WeatherApiResponseDto dto = new WeatherApiResponseDto();
        dto.setList(List.of(entry));
        return dto;
    }
}
