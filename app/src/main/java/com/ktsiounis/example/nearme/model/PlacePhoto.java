package com.ktsiounis.example.nearme.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PlacePhoto implements Parcelable{

    @SerializedName("photo_reference") private String photo_reference;
    @SerializedName("height") private int height;
    @SerializedName("width") private int width;

    protected PlacePhoto(Parcel in) {
        photo_reference = in.readString();
        height = in.readInt();
        width = in.readInt();
    }

    public static final Creator<PlacePhoto> CREATOR = new Creator<PlacePhoto>() {
        @Override
        public PlacePhoto createFromParcel(Parcel in) {
            return new PlacePhoto(in);
        }

        @Override
        public PlacePhoto[] newArray(int size) {
            return new PlacePhoto[size];
        }
    };

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public PlacePhoto(String photo_reference, int height, int width) {
        this.photo_reference = photo_reference;
        this.height = height;
        this.width = width;
    }

    public PlacePhoto() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(photo_reference);
        parcel.writeInt(height);
        parcel.writeInt(width);
    }
}
