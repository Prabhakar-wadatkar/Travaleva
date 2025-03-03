package com.example.travaleva.model;

public class Category {

    private String name;
    private int imageResourceId; // Add this field

    public Category(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId; // Initialize the field
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Add the getter method
    public int getImageResourceId() {
        return imageResourceId;
    }
}