package com.example.travaleva.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking implements Serializable {
    private String userId;
    private String userName;
    private String placeId;
    private String placeName;
    private String startDate;  // Store as String in DD/MM/YYYY format
    private String endDate;    // Store as String in DD/MM/YYYY format
    private int adults;
    private int children;
    private double totalAmount;
    private String paymentMethod;
    private PaymentDetails paymentDetails;
    private String ticketNumber;

    // Constructor
    public Booking(String userId, String placeId, String startDate, String endDate,
                   int adults, int children, double totalAmount, String paymentMethod,
                   PaymentDetails paymentDetails, String userName, String placeName) {
        this.userId = userId;
        this.placeId = placeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.adults = adults;
        this.children = children;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
        this.userName = userName;
        this.placeName = placeName;
    }

    // Getter and Setter for ticketNumber
    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    // Getters and Setters for other fields
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPlaceId() { return placeId; }
    public void setPlaceId(String placeId) { this.placeId = placeId; }

    public String getPlaceName() { return placeName; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }

    public String getStartDate() { return startDate; }  // Return formatted string
    public void setStartDate(String startDate) { this.startDate = startDate; }  // Accept formatted string

    public String getEndDate() { return endDate; }    // Return formatted string
    public void setEndDate(String endDate) { this.endDate = endDate; }    // Accept formatted string

    public int getAdults() { return adults; }
    public void setAdults(int adults) { this.adults = adults; }

    public int getChildren() { return children; }
    public void setChildren(int children) { this.children = children; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentDetails getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(PaymentDetails paymentDetails) { this.paymentDetails = paymentDetails; }

    // Nested class for PaymentDetails
    public static class PaymentDetails implements Serializable {
        private String cardNumber;
        private String expiryDate;
        private String cvv;

        public PaymentDetails(String cardNumber, String expiryDate, String cvv) {
            this.cardNumber = cardNumber;
            this.expiryDate = expiryDate;
            this.cvv = cvv;
        }

        // Getters and Setters
        public String getCardNumber() { return cardNumber; }
        public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

        public String getExpiryDate() { return expiryDate; }
        public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

        public String getCvv() { return cvv; }
        public void setCvv(String cvv) { this.cvv = cvv; }
    }

    // Helper method to parse string date to Date object
    public static Date parseDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to format Date to string (DD/MM/YYYY)
    public static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }
}
