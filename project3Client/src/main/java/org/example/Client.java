package org.example;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        final String sensorName = "Client Sensor";

        registerSensor(sensorName);

        Random random = new Random();

        double startTemperature = 10.0;
        for (int i = 0; i < 30; i++) {
            if (startTemperature > 25) {
                startTemperature -= 5.0;
            } else {
                startTemperature += 1.5;
            }
            System.out.println(i);
            sendMeasurement(startTemperature,
                    random.nextBoolean(), sensorName);
        }
    }

    private static void registerSensor(String sensorName) {
        final String url = "http://localhost:8080/sensors/registration";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);

        makePostRequestWithJSONData(url, jsonData);
    }

    private static void sendMeasurement(double value, boolean raining, String sensorName) {
        final String url = "http://localhost:8080/measurements/add";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("value", value);
        jsonData.put("raining", raining);
        jsonData.put("sensor", Map.of("name", sensorName));

        makePostRequestWithJSONData(url, jsonData);
    }

    private static void makePostRequestWithJSONData(String url, Map<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

        try {
            restTemplate.postForObject(url, request, String.class);

            System.out.println("Измерение успешно отправлено на сервер!");
        } catch (HttpClientErrorException e) {
            System.out.println("ОШИБКА!");
            System.out.println(e.getMessage());
        }
    }
}