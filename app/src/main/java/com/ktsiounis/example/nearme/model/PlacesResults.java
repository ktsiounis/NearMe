package com.ktsiounis.example.nearme.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Konstantinos Tsiounis on 12-Jul-18.
 */
public class PlacesResults {

    @SerializedName("results")
    private ArrayList<Place> places;
    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Place> getPlaces() {

        return places;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }
}
