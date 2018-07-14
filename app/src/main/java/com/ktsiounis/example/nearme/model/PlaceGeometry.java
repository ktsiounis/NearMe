package com.ktsiounis.example.nearme.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantinos Tsiounis on 12-Jul-18.
 */
public class PlaceGeometry implements Parcelable {

    @SerializedName("placeLocation")
    private PlaceLocation placeLocation;

    protected PlaceGeometry(Parcel in) {
        placeLocation = in.readParcelable(PlaceLocation.class.getClassLoader());
    }

    public static final Creator<PlaceGeometry> CREATOR = new Creator<PlaceGeometry>() {
        @Override
        public PlaceGeometry createFromParcel(Parcel in) {
            return new PlaceGeometry(in);
        }

        @Override
        public PlaceGeometry[] newArray(int size) {
            return new PlaceGeometry[size];
        }
    };

    public PlaceLocation getPlaceLocation() {
        return placeLocation;
    }

    public void setPlaceLocation(PlaceLocation placeLocation) {
        this.placeLocation = placeLocation;
    }

    public PlaceGeometry(PlaceLocation placeLocation) {

        this.placeLocation = placeLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(placeLocation, flags);
    }
}
