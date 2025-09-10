package com.example.weather;

import org.springframework.boot.SpringApplication;

public class TestWeatherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(WeatherServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
