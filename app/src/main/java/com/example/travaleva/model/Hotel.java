package com.example.travaleva.model;
public class Hotel {
    private String hotelId;
    private String hotelName;
    private String address;
    private String city;
    private boolean breakfast;
    private boolean dailyHousekeeping;
    private boolean roomService;
    // Add other amenities fields

    public Hotel() {
        // Required empty constructor for Firebase
    }

    // Getters and setters
    public String getHotelId() { return hotelId; }
    public void setHotelId(String hotelId) { this.hotelId = hotelId; }
    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public boolean isBreakfast() { return breakfast; }
    public void setBreakfast(boolean breakfast) { this.breakfast = breakfast; }
    public boolean isDailyHousekeeping() { return dailyHousekeeping; }
    public void setDailyHousekeeping(boolean dailyHousekeeping) { this.dailyHousekeeping = dailyHousekeeping; }
    public boolean isRoomService() { return roomService; }
    public void setRoomService(boolean roomService) { this.roomService = roomService; }
}