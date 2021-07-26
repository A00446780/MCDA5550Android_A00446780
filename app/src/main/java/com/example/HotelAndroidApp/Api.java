package com.example.HotelAndroidApp;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    public static ApiInterface getClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hotelresassignment-env.eba-k89exirh.us-east-1.elasticbeanstalk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface api = retrofit.create(ApiInterface.class);
        return api;
    }
}
