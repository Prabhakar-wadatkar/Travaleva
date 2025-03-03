package com.example.travaleva.model;

public class RecentsData {
    private String addedBy;
    private String address;
    private String categories;
    private String charges;
    private String city;
    private String description;
    private String features;
    private String imageUrl;
    private String parkingAvailable;
    private String specialRules;
    private String timestamp;
    private String title;

    // No-argument constructor for Firebase
    public RecentsData() {
    }

    public RecentsData(String addedBy, String address, String categories, String charges, String city,
                       String description, String features, String imageUrl, String parkingAvailable,
                       String specialRules, String timestamp, String title) {
        this.addedBy = addedBy;
        this.address = address;
        this.categories = categories;
        this.charges = charges;
        this.city = city;
        this.description = description;
        this.features = features;
        this.imageUrl = imageUrl;
        this.parkingAvailable = parkingAvailable;
        this.specialRules = specialRules;
        this.timestamp = timestamp;
        this.title = title;
    }

    // Getters and Setters
    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getCharges() {
        return charges;
    }

    public void setCharges(String charges) {
        this.charges = charges;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getParkingAvailable() {
        return parkingAvailable;
    }

    public void setParkingAvailable(String parkingAvailable) {
        this.parkingAvailable = parkingAvailable;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
