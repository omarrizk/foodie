package com.foodie.webservice.foodiewebservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantController {
    @GetMapping("/restaurants")
    public List<Restaurant> findNearbyRestaurants(@RequestParam(value = "address", defaultValue = "") String address) {
        if (address.isEmpty()) {
            throw new InvalidParameterException("Invalid parameter: address parameter not set");
        }

        return AppController.fetchNearbyRestaurants(address);
    }
}
