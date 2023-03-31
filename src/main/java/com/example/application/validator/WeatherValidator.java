package com.example.application.validator;

import com.example.application.domain.Location;
import com.example.application.domain.WeatherForecastDataListDto;
import com.example.application.exception.WeatherMapNotFoundException;
import com.example.application.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WeatherValidator {
    private static final double MINIMUM_WIND_SPEED = 5;
    private static final double MAXIMUM_WIND_SPEED = 18;
    private static final double MINIMUM_TEMPERATURE = 5;
    private static final double MAXIMUM_TEMPERATURE = 35;

    private final WeatherService weatherService;

    @Autowired
    public WeatherValidator(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public Map<Location, WeatherForecastDataListDto> calculateWindAndTemperatureCondition(Map<Location, WeatherForecastDataListDto> weatherMap) {


        return weatherMap.entrySet().stream()
                .filter(val -> val.getValue().getWind_spd() > MINIMUM_WIND_SPEED && val.getValue().getWind_spd() < MAXIMUM_WIND_SPEED)
                .filter(val -> val.getValue().getLow_temp() > MINIMUM_TEMPERATURE && val.getValue().getHigh_temp() < MAXIMUM_TEMPERATURE)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    public Map<Location, Double> calculateCoefficientCondition(Map<Location, WeatherForecastDataListDto> map) {

        Map<Location, Double> finalMap = new HashMap<>();
        for (Map.Entry<Location, WeatherForecastDataListDto> calculateCoefficientConditionMap : map.entrySet()) {

            double windSpeed = calculateCoefficientConditionMap.getValue().getWind_spd();
            double maxTemp = calculateCoefficientConditionMap.getValue().getHigh_temp();
            double minTemp = calculateCoefficientConditionMap.getValue().getLow_temp();
            double averageTemp = (maxTemp + minTemp) / 2;
            double result = windSpeed * 3 + averageTemp;
            finalMap.put(calculateCoefficientConditionMap.getKey(), result);
        }

        return finalMap;
    }

    public Map<Location, WeatherForecastDataListDto> calculateTheBestPlace(Map<Location, WeatherForecastDataListDto> weatherMap) throws WeatherMapNotFoundException {

        Map<Location, WeatherForecastDataListDto> calculateWindAndTemperatureConditionMap = calculateWindAndTemperatureCondition(weatherMap);

        Map<Location, Double> calculateCoefficientConditionMap = calculateCoefficientCondition(calculateWindAndTemperatureConditionMap);

        Map.Entry<Location, Double> maxValue =
                calculateCoefficientConditionMap.entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue()).orElseThrow(() -> (new WeatherMapNotFoundException("")));

        Map<Location, WeatherForecastDataListDto> mapOfBestLocation = calculateWindAndTemperatureConditionMap.entrySet()
                .stream()
                .filter(o -> o.getKey()
                        .equals(maxValue.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return calculateCoefficientConditionMap.size() > 0 ? mapOfBestLocation : new HashMap<>();
    }
}







