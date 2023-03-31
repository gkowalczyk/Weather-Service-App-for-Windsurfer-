package com.example.application.controller;


import com.example.application.domain.Location;
import com.example.application.domain.WeatherForecastDataListDto;
import com.example.application.exception.WeatherMapNotFoundException;
import com.example.application.service.WeatherService;
import com.example.application.validator.WeatherValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestClientException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

    @MockBean
    private WeatherValidator weatherValidator;
    @MockBean
    private WeatherService weatherService;
    @InjectMocks
    private WeatherController weatherController;
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void findTheBestPlaceToBe() throws Exception {
        //Given
        Map<Location, WeatherForecastDataListDto> resultMap = Map.of
                (new Location("Jastarnia", "1", "1"), new WeatherForecastDataListDto(30, 20, 10, LocalDate.now()));
        LocalDate date = LocalDate.now();

        //When
        when(weatherValidator.calculateTheBestPlace(weatherService.addWeatherForecastsToMap(LocalDate.now()))).thenReturn(resultMap);

        //Then
        MvcResult result = mockMvc.perform(get("/v1/get")
                        .queryParam("date", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                // .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasKey("Jastarnia")))
                .andReturn();

        HashMap finalMaps = objectMapper.readValue(result.getResponse().getContentAsString(), HashMap.class);
        //Then
        assertThat(finalMaps.toString()).isEqualTo(resultMap.toString());
        System.out.println(finalMaps.toString());

    }

    @Test
    void findTheBestPlaceToBe16DaysRangeShouldThrowException() throws WeatherMapNotFoundException {

        //When and Then
        when(weatherValidator.calculateTheBestPlace(weatherService.addWeatherForecastsToMap(LocalDate.now().minusDays(1)))).thenThrow((new IndexOutOfBoundsException()));

        LocalDate date = LocalDate.now().minusDays(1);
        assertThatThrownBy(() -> mockMvc.perform(MockMvcRequestBuilders
                        .get("/v1/get")
                        .queryParam("date", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(status().isInternalServerError())).hasCause(new IndexOutOfBoundsException());
    }

    @Test
    void findTheBestPlaceToBeShouldThrowRestClientException() throws Exception {

        //When and Then
        when(weatherValidator.calculateTheBestPlace(weatherService.addWeatherForecastsToMap(LocalDate.now()))).thenThrow((new RestClientException("")));

        LocalDate date = LocalDate.now();
        mockMvc.perform(get("/v1/get")
                        .queryParam("query???", date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}

