package com.example.weather.controller;

import com.example.weather.dto.DailyForecast;
import com.example.weather.integration.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${spring.application.name}")
@Tag(name = "Weather API", description = "Fetch weather forecast by city")
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/forecast")
    @Operation(summary = "Get forecast", description = "Returns weather forecast for a given city.")
    public ResponseEntity<List<DailyForecast>> getForecast(@RequestParam String city, @RequestParam Boolean isOffline) {
        logger.info("Received request for weather forecast for city: {}", city);

        List<DailyForecast> forecast = weatherService.fetchForecast(city,isOffline);

        logger.info("Returning forecast with {} entries for city: {}", forecast.size(), city);
        return ResponseEntity.ok(forecast);
    }
}
