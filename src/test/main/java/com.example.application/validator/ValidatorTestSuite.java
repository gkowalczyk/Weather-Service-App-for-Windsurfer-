package com.example.application.validator;


import com.example.application.domain.Location;
import com.example.application.domain.WeatherForecastDataListDto;
import com.example.application.exception.WeatherMapNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ValidatorTestSuite {

    @Autowired
    private WeatherValidator weatherValidator;

    @Test
    void findTheBestPlaceToBeTestSuite() throws WeatherMapNotFoundException {
        //Given
        Map<Location, WeatherForecastDataListDto> mapOfLocations = new HashMap<>();
        mapOfLocations.put(new Location("Jastarnia", "1", "1"), new WeatherForecastDataListDto(30, 20, 10, LocalDate.of(2022, 10, 10)));
        mapOfLocations.put(new Location("Kołobrzeg", "1", "1"), new WeatherForecastDataListDto(20, 20, 20, LocalDate.of(2022, 10, 10)));
        mapOfLocations.put(new Location("Hel", "1", "1"), new WeatherForecastDataListDto(10, 20, 20, LocalDate.of(2022, 10, 10)));

        //When
        Map<Location, WeatherForecastDataListDto> map = weatherValidator.calculateTheBestPlace(mapOfLocations);

        //Then
        assertEquals(Map.of(new Location("Jastarnia", "1", "1"), new WeatherForecastDataListDto(30, 20, 10, LocalDate.of(2022, 10, 10))), map);
    }

    @Test
    void calculateCoefficientConditionTestSuite() {
        //Given
        Map<Location, WeatherForecastDataListDto> mapOfLocations = new HashMap<>();
        mapOfLocations.put(new Location("Jastarnia", "1", "1"), new WeatherForecastDataListDto(30, 20, 10, LocalDate.of(2022, 10, 10)));

        //When
        Map<Location, Double> map = weatherValidator.calculateCoefficientCondition(mapOfLocations);
        double result = map.entrySet().stream().map(x -> x.getValue()).findAny().get();

        //Then
        assertEquals(result, 55.0);

    }

    @Test
    void calculateWindAndTemperatureConditionTestSuite() {
        //Given
        Map<Location, WeatherForecastDataListDto> mapOfLocations = new HashMap<>();
        mapOfLocations.put(new Location("Jastarnia", "1", "1"), new WeatherForecastDataListDto(38, 20, 10, LocalDate.of(2022, 10, 10)));

        //When
        Map<Location, WeatherForecastDataListDto> map = weatherValidator.calculateWindAndTemperatureCondition(mapOfLocations);

        //Then
        assertEquals(map.size(), 0);
    }
}