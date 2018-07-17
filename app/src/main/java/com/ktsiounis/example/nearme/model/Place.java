package com.ktsiounis.example.nearme.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Konstantinos Tsiounis on 12-Jul-18.
 */
public class Place implements Parcelable {

    @SerializedName("geometry")
    private PlaceGeometry placeGeometry;
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
    @SerializedName(value = "vicinity", alternate = {"formatted_address"})
    private String vicinity;
    @SerializedName("photos")
    private ArrayList<PlacePhoto> placePhotos;

    public Place() {}

    public Place(PlaceGeometry placeGeometry,
                 String icon,
                 String id,
                 String name,
                 String place_id,
                 String rating,
                 String vicinity,
                 ArrayList<PlacePhoto> placePhotos) {
        this.placeGeometry = placeGeometry;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.place_id = place_id;
        this.rating = rating;
        this.vicinity = vicinity;
        this.placePhotos = placePhotos;
    }

    protected Place(Parcel in) {
        placeGeometry = in.readParcelable(PlaceGeometry.class.getClassLoader());
        icon = in.readString();
        id = in.readString();
        name = in.readString();
        place_id = in.readString();
        rating = in.readString();
        vicinity = in.readString();
        placePhotos = new ArrayList<>();
        in.readTypedList(placePhotos, PlacePhoto.CREATOR);
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public PlaceGeometry getPlaceGeometry() {
        return placeGeometry;
    }

    public void setPlaceGeometry(PlaceGeometry placeGeometry) {
        this.placeGeometry = placeGeometry;
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

    @Override
    public int describeContents() {
        return 0;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public ArrayList<PlacePhoto> getPlacePhotos() {
        return placePhotos;
    }

    public void setPlacePhotos(ArrayList<PlacePhoto> placePhotos) {
        this.placePhotos = placePhotos;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(placeGeometry, flags);
        dest.writeString(icon);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(place_id);
        dest.writeString(rating);
        dest.writeString(vicinity);
        dest.writeTypedList(placePhotos);
    }
}
