package com.example.travaleva.model;

public class Flight {
    private int airlineLogo;
    private String airlineName;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private String price;

    public Flight(int airlineLogo, String airlineName, String departureTime, String arrivalTime, String duration, String price) {
        this.airlineLogo = airlineLogo;
        this.airlineName = airlineName;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.price = price;
    }

    public int getAirlineLogo() {
        return airlineLogo;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getPrice() {
        return price;
    }
}