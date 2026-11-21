package com.devsecops.meteotn;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class WeatherController {

    private final WeatherService service;

    public WeatherController(WeatherService service) {
        this.service = service;
    }

    @GetMapping("/cities")
    public List<City> cities() {
        return service.cities();
    }

    @GetMapping("/weather/{key}")
    public Map<String, Object> weather(@PathVariable String key) {
        City city = service.find(key)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown city"));
        try {
            return service.forecast(city);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Weather provider unavailable");
        }
    }
}