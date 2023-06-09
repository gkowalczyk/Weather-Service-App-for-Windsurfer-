package com.example.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastDto {

    @JsonProperty("city_name")
    private String city_name;
    @JsonProperty("data")
    private List<WeatherForecastDataListDto> data;
    @JsonProperty("lat")
    private String lat;
    @JsonProperty("lon")
    private String lon;
  }
