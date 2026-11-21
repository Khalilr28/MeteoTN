package com.devsecops.meteotn;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WeatherService {

    // Whitelist of supported cities: user input is never sent to the external API
    private static final Map<String, City> CITIES = new LinkedHashMap<>();

    static {
        add("tunis", "Tunis", 36.8065, 10.1815);
        add("sousse", "Sousse", 35.8256, 10.6369);
        add("sfax", "Sfax", 34.7406, 10.7603);
        add("monastir", "Monastir", 35.7643, 10.8113);
        add("nabeul", "Nabeul", 36.4513, 10.7357);
        add("bizerte", "Bizerte", 37.2744, 9.8739);
        add("kairouan", "Kairouan", 35.6781, 10.0963);
        add("gabes", "Gabès", 33.8815, 10.0982);
        add("tozeur", "Tozeur", 33.9197, 8.1335);
        add("djerba", "Djerba", 33.8076, 10.8451);
        add("tabarka", "Tabarka", 36.9544, 8.7580);
        add("tataouine", "Tataouine", 32.9297, 10.4518);
    }

    private final RestClient client = RestClient.builder()
            .baseUrl("https://api.open-meteo.com")
            .build();

    private static void add(String key, String name, double lat, double lon) {
        CITIES.put(key, new City(key, name, lat, lon));
    }

    public List<City> cities() {
        return List.copyOf(CITIES.values());
    }

    public Optional<City> find(String key) {
        if (key == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(CITIES.get(key.toLowerCase(Locale.ROOT)));
    }

    public Map<String, Object> forecast(City city) {
        return client.get()
                .uri(b -> b.path("/v1/forecast")
                        .queryParam("latitude", city.lat())
                        .queryParam("longitude", city.lon())
                        .queryParam("current",
                                "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,weather_code,wind_speed_10m")
                        .queryParam("daily", "weather_code,temperature_2m_max,temperature_2m_min")
                        .queryParam("timezone", "Africa/Tunis")
                        .queryParam("forecast_days", 5)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }
}