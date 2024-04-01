package com.foodie.webservice.foodiewebservice;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class AppController {
    private static final String GEOCOD_API_KEY = "66aa333365a4a0a034603fa35a6a163a33a05aa";
    private static final String YELP_API_KEY = "qR98ybypU55qXV8cqt2jSgpEvDRx5Tncl9Flpttfo4eR_GKDzPM2hPUAe5H2DfzwWHTAzstzO799IGpufXYoZz1uH1z-fS5TIoiz3lH5H4RDR1Xt93D5aHGWbJVqY3Yx";

    private static final String GEOCOD_URL = "https://api.geocod.io/v1.7/geocode?q=%s&api_key=" + GEOCOD_API_KEY;
    private static final String YELP_URL = "https://api.yelp.com/v3/businesses/search?term=food&latitude=%s&longitude=%s";

    public static List<Restaurant> fetchNearbyRestaurants(String address) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Get the address's lat and lng
            JsonNode geocodeJson = objectMapper.readTree(fetchLocationAddress(address));
            String lat = geocodeJson.get("results").get(0).get("location").get("lat").toPrettyString();
            String lng = geocodeJson.get("results").get(0).get("location").get("lng").toPrettyString();

            // Get the list of restaurants
            JsonNode yelpJson = objectMapper.readTree(fetchFoodBusinessFromAddress(lat, lng));

            for (JsonNode node : yelpJson.get("businesses")) {
                String restaurantName = node.get("name").toPrettyString();
                String restaurantAddress = node.get("location").get("address1")
                        .toPrettyString();
                String restaurantRating = Float.toString(node.get("rating").floatValue());

                restaurants.add(new Restaurant(restaurantName, restaurantAddress, restaurantRating));
            }
        } catch (Exception ex) {
            throw new ExternalWebServiceErrorException("Error fetching the businesses");
        }

        return restaurants;
    }

    public static String fetchLocationAddress(String address) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(String.format(GEOCOD_URL, address), String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ExternalWebServiceErrorException("Unable to connect to GEOCOD API");
        }

        return response.getBody();
    }

    public static String fetchFoodBusinessFromAddress(String latitude, String longitude) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + YELP_API_KEY);

        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(String.format(YELP_URL, latitude, longitude),
                HttpMethod.GET,
                request, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new ExternalWebServiceErrorException("Unable to connect to Yelp API");
        }

        return response.getBody();
    }
}