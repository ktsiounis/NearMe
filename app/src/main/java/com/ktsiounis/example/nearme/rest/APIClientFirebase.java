package com.ktsiounis.example.nearme.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Konstantinos Tsiounis on 09-Jul-18.
 */
public class APIClientFirebase {

    public static final String BASE_URL = "https://nearme-1530782701082.firebaseio.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
