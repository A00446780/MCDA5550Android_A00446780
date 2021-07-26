package com.example.HotelAndroidApp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public interface ApiInterface {

    @GET("gethotelslist")
    Call<List<HotelListData>> getHotelsList();

    @POST("reservation")
    Call<Confirmation> getConfirmation(@Body ReservationDetails reservationDetails);
    
}
