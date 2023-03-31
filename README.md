#                              "Find The Best Place Surf Spot App"  [- WEBSITE LINK](https://go-surf-app.onrender.com/weathergui)

![alt text for image](https://www.stuartsurf.com.au/wp-content/uploads/X-F-Rap-slider-Jul2020.jpg)

**Technology Backend:**

- Spring Boot
- REST API
- JUnit5
- Mockito
- Lombok
- Vaadin

**The best location selection criteria are:**

- wind speed between 5 - 18 [m/s]

- temperature between 5 - 25 [oC]

- If there are in these ranges, then the best location is determinated by the highest value calculated from the following formula:

```bash
wind speed  x 3 + temperature 
```



###  Run project - Docker

After download repositories:


```bash
docker-compose up
```
open browser:
```bash
localhost:8082/weathergui or directly https://go-surf-app.onrender.com/weathergui
```

### Frontend:

![alt text for image](https://github.com/gkowalczyk/Weather-Service-App-for-Windsurfer/blob/main/src/main/resources/ScreenRecorderProject2.gif)

![alt text for image](https://github.com/gkowalczyk/Weather-Service-App-for-Windsurfer/blob/main/src/main/resources/frontend.png)

### Postman Endpoints tests:

- GET /v1/get  : Query Params  date format is "yyyy-MM-dd"
  ![alt text for image](https://github.com/gkowalczyk/Weather-Service-App-for-Windsurfer/blob/main/src/main/resources/postman_get.png)
  ![alt text for image](https://github.com/gkowalczyk/Weather-Service-App-for-Windsurfer/blob/main/src/main/resources/postman_exception.png)
 