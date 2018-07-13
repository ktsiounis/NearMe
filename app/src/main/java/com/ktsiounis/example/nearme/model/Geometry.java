package com.ktsiounis.example.nearme.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ProgressBar;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantinos Tsiounis on 12-Jul-18.
 */
public class Geometry implements Parcelable {

    @SerializedName("location")
    private Location location;

    protected Geometry(Parcel in) {
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Geometry> CREATOR = new Creator<Geometry>() {
        @Override
        public Geometry createFromParcel(Parcel in) {
            return new Geometry(in);
        }

        @Override
        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Geometry(Location location) {

        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(location, flags);
    }
}
