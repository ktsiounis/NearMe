package com.ktsiounis.example.nearme.rest;

import com.ktsiounis.example.nearme.model.PlacesResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestInterfaceTextSearch {

    @GET("json")
    Call<PlacesResults> getTextSearchResults(@Query("query") String query,
                                             @Query("location") String location,
                                             @Query("radius") String radius,
                                             @Query("key") String key);

}
