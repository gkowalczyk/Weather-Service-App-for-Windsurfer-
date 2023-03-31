package com.example.application.client;

import ch.qos.logback.classic.Logger;

import com.example.application.config.WeatherBitConfig;
import com.example.application.domain.WeatherForecastDto;
import com.example.application.exception.WeatherMapNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor

public class WeatherClient {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(WeatherClient.class);
    private final RestTemplate restTemplate;
    private final WeatherBitConfig weatherBitConfig;

    public WeatherForecastDto getWeatherForecastDto(String city)  {

        URI url = UriComponentsBuilder.
                fromHttpUrl(weatherBitConfig.getWeatherApiEndpoint())
                .queryParam("key", weatherBitConfig.getWeatherAppKey())
                .queryParam("city", city)
                .build()
                .encode()
                .toUri();
        try {

            WeatherForecastDto forecast = restTemplate.getForObject(url, WeatherForecastDto.class);
            return ofNullable(forecast)
                    .orElseThrow(() -> new WeatherMapNotFoundException(""));

        } catch (RestClientException | WeatherMapNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return new WeatherForecastDto();
        }
    }
}



