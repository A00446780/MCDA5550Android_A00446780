package com.example.HotelAndroidApp;

import java.util.List;

// POJO for sending POST request
public class ReservationDetails {
    private String hotel_name;
    private String checkin;
    private String checkout;
    private List<Guests> guests_list;

    public ReservationDetails(String hotel_name, String checkin, String checkout, List<Guests> guests_list) {
        this.hotel_name = hotel_name;
        this.checkin = checkin;
        this.checkout = checkout;
        this.guests_list = guests_list;
    }
}
