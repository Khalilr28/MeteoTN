package com.devsecops.meteotn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WeatherServiceTest {

    private final WeatherService service = new WeatherService();

    @Test
    void listsTunisianCities() {
        assertEquals(12, service.cities().size());
        assertTrue(service.find("tunis").isPresent());
    }

    @Test
    void unknownCityIsRejected() {
        assertTrue(service.find("paris").isEmpty());
        assertTrue(service.find("../etc/passwd").isEmpty());
    }
}