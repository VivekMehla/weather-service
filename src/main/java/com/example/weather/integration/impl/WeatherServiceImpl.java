package com.example.weather.integration.impl;

import com.example.weather.dto.DailyForecast;
import com.example.weather.dto.responseDto.WeatherApiResponseDto;
import com.example.weather.integration.WeatherService;
import com.example.weather.integration.WeatherApiClient;
import com.example.weather.integration.WeatherCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final WeatherApiClient apiClient;
    private final WeatherCache cache;

    @Autowired
    public WeatherServiceImpl(WeatherApiClient apiClient, WeatherCache cache) {
        this.apiClient = apiClient;
        this.cache = cache;
    }

    @Override
    public List<DailyForecast> fetchForecast(String city) {
        try {
            List<DailyForecast> cached = cache.get(city);
            if (cached != null) return cached;

            WeatherApiResponseDto apiResponse = apiClient.fetchWeatherData(city);
            List<DailyForecast> forecasts = toDailyForecasts(apiResponse);
            cache.put(city, forecasts);

            return forecasts;

        } catch (Exception e) {
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
        return forecastList;
    }
}
