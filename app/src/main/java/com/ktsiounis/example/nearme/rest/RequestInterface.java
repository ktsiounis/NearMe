package com.ktsiounis.example.nearme.rest;

import com.ktsiounis.example.nearme.model.Category;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Konstantinos Tsiounis on 09-Jul-18.
 */
public interface RequestInterface {

    @GET("categories/{id}.json")
    Call<Category> getCategory(@Path("id") int groupID);

}
