package com.example.weather.integration.impl;

import com.example.weather.dto.DailyForecast;
import com.example.weather.dto.responseDto.WeatherApiResponseDto;
import com.example.weather.integration.WeatherService;
import com.example.weather.integration.WeatherApiClient;
import com.example.weather.integration.WeatherCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeatherServiceImpl implements WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    private final WeatherApiClient apiClient;
    private final WeatherCache cache;

    @Autowired
    public WeatherServiceImpl(WeatherApiClient apiClient, WeatherCache cache) {
        this.apiClient = apiClient;
        this.cache = cache;
    }

    @Override
    public List<DailyForecast> fetchForecast(String city, Boolean isOffline) {
        logger.info("Fetching weather forecast for city: {}, isOffline={}", city, isOffline);

        try {
            // Always check cache first
            List<DailyForecast> cached = cache.get(city);
            if (cached != null) {
                logger.info("Cache hit for city: {}. Returning cached forecast ({} entries).", city, cached.size());
                return cached;
            }

            // If offline mode, return mock when no cache is found
            if (Boolean.TRUE.equals(isOffline)) {
                logger.warn("Offline mode enabled and no cache available for city: {}. Returning mock data.", city);
                return List.of(DailyForecast.mock());
            }

            // If not offline, call external API
            logger.info("Cache miss for city: {}. Calling external weather API.", city);
            WeatherApiResponseDto apiResponse = apiClient.fetchWeatherData(city);

            List<DailyForecast> forecasts = toDailyForecasts(apiResponse);
            cache.put(city, forecasts);

            logger.info("Fetched and cached forecast for city: {} ({} entries).", city, forecasts.size());
            return forecasts;

        } catch (Exception e) {
            logger.error("Failed to fetch forecast for city: {}. Error: {}", city, e.getMessage(), e);

            // If offline, try cache first before mock
            if (Boolean.TRUE.equals(isOffline)) {
                List<DailyForecast> cached = cache.get(city);
                if (cached != null) {
                    logger.info("Returning cached forecast for city: {} after failure ({} entries).", city, cached.size());
                    return cached;
                }
            }

            logger.warn("No cache available. Returning mock forecast for city: {}", city);
            return List.of(DailyForecast.mock());
        }
    }


    private List<DailyForecast> toDailyForecasts(WeatherApiResponseDto apiResponse) {
        List<DailyForecast> forecastList = new ArrayList<>();

        for (WeatherApiResponseDto.WeatherEntry entry : apiResponse.getList()) {
            String datetime = entry.getDt_txt();
            double tempCelsius = entry.getMain().getTemp() - 273.15;
            double windMph = entry.getWind().getSpeed() * 2.237;
            boolean rain = entry.getRain() != null;
            boolean thunderstorm = entry.getWeather().stream()
                    .anyMatch(w -> "Thunderstorm".equalsIgnoreCase(w.getMain()));

            DailyForecast df = new DailyForecast(datetime);
            df.update(tempCelsius, windMph, rain, thunderstorm);
            df.setPrediction();

            forecastList.add(df);
        }

        logger.debug("Converted API response to {} daily forecasts.", forecastList.size());
        return forecastList;
    }
}