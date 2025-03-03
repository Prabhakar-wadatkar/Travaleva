package com.example.travaleva.model;

import java.io.Serializable;

public class Place implements Serializable {
    private String id;  // Add this field for the place ID
    private String title;
    private String description;
    private String charges;
    private String parkingAvailable;
    private String addedBy;
    private String categories;
    private String imageUrl;
    private String address;
    private String city;
    private String features;
    private String specialRules;
    private String timestamp;
    private double latitude;
    private double longitude;

    // Default constructor for Firebase
    public Place() {
    }

    // Constructor
    public Place(String id, String title, String description, String charges, String parkingAvailable, String addedBy,
                 String categories, String imageUrl, String address, String city,
                 String features, String specialRules, String timestamp) {
        this.id = id;  // Set the ID
        this.title = title;
        this.description = description;
        this.charges = charges;
        this.parkingAvailable = parkingAvailable;
        this.addedBy = addedBy;
        this.categories = categories;
        this.imageUrl = imageUrl;
        this.address = address;
        this.city = city;
        this.features = features;
        this.specialRules = specialRules;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getId() {
        return id;  // Return the ID
    }

    public void setId(String id) {
        this.id = id;  // Set the ID
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

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getParkingAvailable() {
        return parkingAvailable;
    }

    public void setParkingAvailable(String parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getSpecialRules() {
        return specialRules;
    }

    public void setSpecialRules(String specialRules) {
        this.specialRules = specialRules;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
