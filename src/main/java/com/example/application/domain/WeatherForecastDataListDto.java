package com.example.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherForecastDataListDto {

    @JsonProperty("high_temp")
    private double high_temp;
    @JsonProperty("low_temp")
    private double low_temp;
    @JsonProperty("wind_spd")
    private double wind_spd;
    @JsonProperty("datetime")
    private LocalDate datetime;


    @Override
    public String toString() {
        return
                "{high_temp="+high_temp +
                        ", low_temp="+low_temp +
                        ", wind_spd="+wind_spd +
                        ", datetime="+datetime +
                        '}';
    }
}
