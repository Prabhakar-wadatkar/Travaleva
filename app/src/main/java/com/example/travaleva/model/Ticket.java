package com.example.travaleva.model;

public class Ticket {
    private String ticketNumber;
    private String placeName;
    private String city;
    private String date;
    private double totalAmount;
    private String status; // New field: "active" or "canceled"

    // Default constructor (required for Firebase)
    public Ticket() {}

    // Getters and setters
    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }

    public String getPlaceName() { return placeName; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}