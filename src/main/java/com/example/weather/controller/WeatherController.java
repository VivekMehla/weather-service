package com.example.weather.controller;

import com.example.weather.dto.DailyForecast;
import com.example.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${spring.application.name}")
@CrossOrigin(origins = "http://localhost:4200")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/forecast")
    public ResponseEntity<List<DailyForecast>> getForecast(@RequestParam String city){
        List<DailyForecast> forecast = weatherService.fetchForecast(city);
        return ResponseEntity.ok(forecast);
    }
}
