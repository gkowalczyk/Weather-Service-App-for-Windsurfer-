package com.example.application.views.map;


import com.example.application.client.WeatherClient;
import com.example.application.domain.Location;
import com.example.application.domain.WeatherForecastDataListDto;
import com.example.application.exception.WeatherMapNotFoundException;
import com.example.application.service.WeatherService;
import com.example.application.validator.WeatherValidator;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.map.configuration.Coordinate;
import com.vaadin.flow.component.map.configuration.feature.MarkerFeature;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.map.Map;

@PageTitle("App")
@Route(value = "weathergui", layout = MainLayout.class)
@CssImport("./styles/styles.css")
public class WeatherGui extends HorizontalLayout {

    private TextArea spot;
    private TextArea lat;
    private TextArea lot;
    private TextArea maxTemp;
    private TextArea minTemp;
    private TextArea windSpeed;
    private Button button;
    private final WeatherClient weatherClient;
    private final WeatherService weatherService;
    private final WeatherValidator weatherValidator;
    private final DatePicker datePicker;
    private Map maps;


    public WeatherGui(WeatherClient weatherClient, WeatherService weatherService, WeatherValidator weatherValidator) {
        this.weatherService = weatherService;
        this.weatherValidator = weatherValidator;
        this.datePicker = new DatePicker("Choose date of surfing");
        this.spot = new TextArea("Spot");
        spot.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
        this.lat = new TextArea("Latitude [°]");
        lat.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
        this.lot = new TextArea("Longitude [°]");
        lot.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
        this.maxTemp = new TextArea("Max temperature [°C]");
        maxTemp.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
        this.minTemp = new TextArea("Min temperature [°C]");
        minTemp.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
        this.windSpeed = new TextArea("Wind speed [m/s]");
        windSpeed.addThemeVariants(TextAreaVariant.LUMO_ALIGN_CENTER);
        this.weatherClient = weatherClient;
        this.button = new Button(" S E A R C H");
        this.maps = new Map();

        /**
         *  left layout.
         */
        VerticalLayout sideBar = new VerticalLayout();
        sideBar.setWidth("200px");
        sideBar.add(datePicker, button, spot, maxTemp, minTemp, windSpeed);
        sideBar.setAlignItems(Alignment.CENTER);
        add(sideBar);

        /**
         *  map layout.
         */
        VerticalLayout verticalLayoutMap = new VerticalLayout();
        verticalLayoutMap.add(maps);
        maps.setSizeFull();
        verticalLayoutMap.setAlignItems(Alignment.CENTER);
        verticalLayoutMap.setWidthFull();
        add(verticalLayoutMap);

        /**
         *  button logic
         */
        button.addThemeVariants(ButtonVariant.LUMO_LARGE);
        button.addClickListener(x -> {

            java.util.Map<Location, WeatherForecastDataListDto> map = null;
            try {
                map = this.weatherValidator.calculateTheBestPlace(this.weatherService.addWeatherForecastsToMap(datePicker.getValue()));
            } catch (WeatherMapNotFoundException e) {
                e.printStackTrace();
            }
            spot.setValue(map.entrySet().stream().
                    map(spot -> spot.getKey().getName())
                    .findFirst().get());

            maxTemp.setValue(map.entrySet()
                    .stream().map(spot -> spot.getValue()
                            .getHigh_temp())
                    .findFirst().get()
                    .toString());

            minTemp.setValue(map.entrySet().
                    stream().map(spot -> spot.getValue()
                            .getLow_temp())
                    .findFirst().get()
                    .toString());

            windSpeed.setValue(map.entrySet().stream()
                    .map(spot -> spot.getValue()
                            .getWind_spd())
                    .findFirst().get()
                    .toString());

            double latitude = Double.parseDouble(map.entrySet().stream()
                    .map(lat -> lat.getKey().getLat())
                    .findFirst()
                    .get());

            double longtitude = Double.parseDouble(map.entrySet().stream()
                    .map(lat -> lat.getKey().getLon())
                    .findFirst()
                    .get());

            Coordinate coordinate = new Coordinate(longtitude, latitude);

            maps.setCenter(coordinate);
            maps.setZoom(5);
            MarkerFeature weatherSpot = new MarkerFeature(coordinate);
            maps.getFeatureLayer().addFeature(weatherSpot);
        });
    }
}
