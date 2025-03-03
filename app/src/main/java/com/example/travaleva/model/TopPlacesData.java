package com.example.travaleva.model;

public class TopPlacesData {
    private String title;
    private String description;
    private String city;
    private String charges;
    private String imageUrl;

    // Default constructor (required for Firebase)
    public TopPlacesData() {
    }

    public TopPlacesData(String title, String description, String city, String charges, String imageUrl) {
        this.title = title;
        this.description = description;
        this.city = city;
        this.charges = charges;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}