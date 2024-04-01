package com.foodie.webservice.foodiewebservice;

public class Restaurant {
    private final String name;
    private final String address;
    private final String rating;

    public Restaurant(String name, String address, String rating){
        this.name = name;
        this.address = address;
        this.rating = rating;
    }

    public String getName() { return this.name; }
    public String getAddress() { return this.address; }
    public String getRating() { return this.rating; }
}
