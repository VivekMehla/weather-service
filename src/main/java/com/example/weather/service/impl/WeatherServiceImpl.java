package com.example.weather.service.impl;

import com.example.weather.dto.DailyForecast;
import com.example.weather.dto.responseDto.WeatherApiResponseDto;
import com.example.weather.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {

    private final String API_KEY = "d2929e9483efc82c82c32ee7e02d563e";
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<DailyForecast> fetchForecast(String city) {
        try {
            String url = String.format("%s?q=%s&appid=%s&cnt=24", BASE_URL, city, API_KEY);
            RestTemplate restTemplate = new RestTemplate();
            String jsonResponse = restTemplate.getForObject(url, String.class);

            WeatherApiResponseDto apiResponse = objectMapper.readValue(jsonResponse, WeatherApiResponseDto.class);

            List<DailyForecast> forecastList = new ArrayList<>();

            for (WeatherApiResponseDto.WeatherEntry weatherEntry : apiResponse.getList()) {
                String datetime = weatherEntry.getDt_txt();

                double tempCelsius = weatherEntry.getMain().getTemp() - 273.15;
                double windMph = weatherEntry.getWind().getSpeed() * 2.237;
                boolean rain = weatherEntry.getRain() != null;
                boolean thunderstorm = weatherEntry.getWeather().stream()
                        .anyMatch(w -> "Thunderstorm".equalsIgnoreCase(w.getMain()));

                DailyForecast df = new DailyForecast(datetime); // Store full datetime string
                df.update(tempCelsius, windMph, rain, thunderstorm);
                df.setPrediction();

                forecastList.add(df);
            }

            return forecastList; // Return all date-time forecasts as list

        } catch (Exception e) {
            return Arrays.asList(DailyForecast.mock());
        }
    }

}
