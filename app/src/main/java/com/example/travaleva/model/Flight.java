package com.example.travaleva.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Flight implements Parcelable {
    private String airline;
    private String arrivalTime;
    private String departureTime;
    private String destination;
    private String flightNumber;
    private String id;
    private String origin;
    private String price;

    // Default constructor required for calls to DataSnapshot.getValue(Flight.class)
    public Flight() {
    }

    public Flight(String airline, String arrivalTime, String departureTime, String destination, String flightNumber, String id, String origin, String price) {
        this.airline = airline;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.destination = destination;
        this.flightNumber = flightNumber;
        this.id = id;
        this.origin = origin;
        this.price = price;
    }

    // Getters and setters
    public String getAirline() { return airline; }
    public void setAirline(String airline) { this.airline = airline; }

    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    // Parcelable implementation
    protected Flight(Parcel in) {
        airline = in.readString();
        arrivalTime = in.readString();
        departureTime = in.readString();
        destination = in.readString();
        flightNumber = in.readString();
        id = in.readString();
        origin = in.readString();
        price = in.readString();
    }

    public static final Creator<Flight> CREATOR = new Creator<Flight>() {
        @Override
        public Flight createFromParcel(Parcel in) {
            return new Flight(in);
        }

        @Override
        public Flight[] newArray(int size) {
            return new Flight[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(airline);
        dest.writeString(arrivalTime);
        dest.writeString(departureTime);
        dest.writeString(destination);
        dest.writeString(flightNumber);
        dest.writeString(id);
        dest.writeString(origin);
        dest.writeString(price);
    }
}