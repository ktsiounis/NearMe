package com.ktsiounis.example.nearme.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantinos Tsiounis on 12-Jul-18.
 */
public class Place {

    @SerializedName("geometry")
    private Geometry geometry;
    @SerializedName("icon")
    private String icon;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("place_id")
    private String place_id;
    @SerializedName("rating")
    private String rating;
    @SerializedName("opening_hours")
    private PlaceOpeningHours placeOpeningHours;

    public Place(Geometry geometry, String icon, String id, String name, String place_id, String rating, PlaceOpeningHours placeOpeningHours) {
        this.geometry = geometry;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.place_id = place_id;
        this.rating = rating;
        this.placeOpeningHours = placeOpeningHours;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public PlaceOpeningHours getPlaceOpeningHours() {
        return placeOpeningHours;
    }

    public void setPlaceOpeningHours(PlaceOpeningHours placeOpeningHours) {
        this.placeOpeningHours = placeOpeningHours;
    }
}
