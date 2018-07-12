package com.ktsiounis.example.nearme.rest;

import com.ktsiounis.example.nearme.model.PlacesResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Konstantinos Tsiounis on 12-Jul-18.
 */
public interface RequestInterfacePlaces {

    @GET("json")
    Call<PlacesResults> getNearByPlaces(@Query("location") String location, @Query("radius") String radius, @Query("type") String type, @Query("key") String key);

}
