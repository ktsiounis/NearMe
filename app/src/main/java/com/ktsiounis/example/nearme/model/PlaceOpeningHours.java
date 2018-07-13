package com.ktsiounis.example.nearme.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantinos Tsiounis on 12-Jul-18.
 */
public class PlaceOpeningHours implements Parcelable {

    @SerializedName("open_now")
    private Boolean open_now;

    public Boolean getOpen_now() {
        return open_now;
    }

    public void setOpen_now(Boolean open_now) {
        this.open_now = open_now;
    }

    public PlaceOpeningHours(Boolean open_now) {

        this.open_now = open_now;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
