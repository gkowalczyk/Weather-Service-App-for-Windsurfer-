package com.example.application.service;

import ch.qos.logback.classic.Logger;
import com.example.application.client.WeatherClient;
import com.example.application.database.CityInterface;
import com.example.application.database.DataBase;
import com.example.application.domain.Location;
import com.example.application.domain.WeatherForecastDataListDto;
import com.example.application.exception.WeatherMapNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class WeatherService {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(WeatherService.class);
    private final WeatherClient weatherClient;
    private CityInterface cityInterface = new DataBase();


    @Autowired
    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public Map<Location, WeatherForecastDataListDto> addWeatherForecastsToMap(LocalDate date) throws WeatherMapNotFoundException {


        List<String> locations = new ArrayList<>(cityInterface.getLocation());
        Map<Location, List<WeatherForecastDataListDto>> weatherMap = new HashMap<>();

        for (int i = 0; i < locations.size(); i++) {
            weatherMap.put(new Location(weatherClient.getWeatherForecastDto(locations.get(i)).getCity_name(),
                    weatherClient.getWeatherForecastDto(locations.get(i)).getLat(),
                    weatherClient.getWeatherForecastDto(locations.get(i)).getLon()),
                    weatherClient.getWeatherForecastDto(locations.get(i)).getData());
        }

        try {
            Map<Location, WeatherForecastDataListDto> finalMap = new HashMap<>();
            for (Map.Entry<Location, List<WeatherForecastDataListDto>> map : weatherMap.entrySet()) {
                List<WeatherForecastDataListDto> findWeathersForOneDay = map.getValue().stream()
                        .filter(y -> y.getDatetime().isEqual(date))
                        .collect(Collectors.toList());

                finalMap.put(map.getKey(), findWeathersForOneDay.get(0));
            }

            return finalMap;

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new WeatherMapNotFoundException("");
        }
    }
}

