package com.ktsiounis.example.nearme.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantinos Tsiounis on 09-Jul-18.
 */
public class Category implements Parcelable {
    @SerializedName("title")
    private String title;
    @SerializedName("thumbnail")
    private String thumbnail;

    public Category(String title, String thumbnail) {
        this.title = title;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumbnail);
        dest.writeString(title);
    }

    public Category (Parcel parcel) {
        this.thumbnail = parcel.readString();
        this.title = parcel.readString();
    }

    // Method to recreate a Question from a Parcel
    public static Creator<Category> CREATOR = new Creator<Category>() {

        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }

    };
}
