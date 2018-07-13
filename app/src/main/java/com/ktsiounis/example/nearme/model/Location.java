package com.ktsiounis.example.nearme.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantinos Tsiounis on 12-Jul-18.
 */
public class Location implements Parcelable {

    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;

    public Location(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    protected Location(Parcel in) {
        lat = in.readString();
        lng = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getLat() {

        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lat);
        dest.writeString(lng);
    }
}
