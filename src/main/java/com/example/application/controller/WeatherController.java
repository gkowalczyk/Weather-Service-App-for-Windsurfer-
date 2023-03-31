package com.example.application.controller;


import com.example.application.client.WeatherClient;
import com.example.application.domain.Location;
import com.example.application.domain.WeatherForecastDataListDto;
import com.example.application.exception.WeatherMapNotFoundException;
import com.example.application.service.WeatherService;
import com.example.application.validator.WeatherValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@CrossOrigin("*")
public class WeatherController {
    private final WeatherClient weatherClient;
    private final WeatherService weatherService;
    private final WeatherValidator weatherValidator;

    @Autowired
    public WeatherController(WeatherClient weatherClient, WeatherService weatherService, WeatherValidator weatherValidator) {
        this.weatherClient = weatherClient;
        this.weatherService = weatherService;
        this.weatherValidator = weatherValidator;
    }

    @GetMapping("get")
    public ResponseEntity<Map<Location, WeatherForecastDataListDto>> findTheBestPlaceToBe(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws WeatherMapNotFoundException, WeatherMapNotFoundException, WeatherMapNotFoundException, WeatherMapNotFoundException {
        return ResponseEntity.ok(weatherValidator.calculateTheBestPlace(weatherService.addWeatherForecastsToMap(date)));
    }
}