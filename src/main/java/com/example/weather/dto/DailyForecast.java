package com.example.weather.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyForecast {
    private String date;
    private double minTemp = Double.MAX_VALUE;
    private double maxTemp = Double.MIN_VALUE;
    private double maxWind = 0;
    private boolean rain = false;
    private boolean thunderstorm = false;
    private String prediction = "";

    public DailyForecast(String date) {
        this.date = date;
    }

    // Getters and setters (optional, depending on usage)

    public void update(double temp, double wind, boolean rainFlag, boolean thunderFlag) {
        minTemp = Math.min(minTemp, temp);
        maxTemp = Math.max(maxTemp, temp);
        maxWind = Math.max(maxWind, wind);
        rain = rain || rainFlag;
        thunderstorm = thunderstorm || thunderFlag;
    }

    public void setPrediction() {
        if (thunderstorm) {
            prediction = "Don’t step out! A Storm is brewing!";
        } else if (maxWind > 10) {
            prediction = "It’s too windy, watch out!";
        } else if (rain) {
            prediction = "Carry umbrella";
        } else if (maxTemp > 40) {
            prediction = "Use sunscreen lotion";
        } else {
            prediction = "Good weather";
        }
    }

    public static DailyForecast mock() {
        DailyForecast mock = new DailyForecast("2025-09-10");
        mock.minTemp = 25;
        mock.maxTemp = 35;
        mock.maxWind = 5;
        mock.rain = false;
        mock.thunderstorm = false;
        mock.prediction = "Offline mode: demo only";
        return mock;
    }

    // Optionally, override toString() for easier debugging
}

