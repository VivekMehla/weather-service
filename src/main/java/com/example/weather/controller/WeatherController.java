package com.example.weather.controller;

import com.example.weather.dto.DailyForecast;
import com.example.weather.integration.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${spring.application.name}")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Weather API", description = "Fetch weather forecast by city")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/forecast")
    @Operation(summary = "Get forecast", description = "Returns weather forecast for a given city.")
    public ResponseEntity<List<DailyForecast>> getForecast(@RequestParam String city){
        List<DailyForecast> forecast = weatherService.fetchForecast(city);
        return ResponseEntity.ok(forecast);
    }
}
