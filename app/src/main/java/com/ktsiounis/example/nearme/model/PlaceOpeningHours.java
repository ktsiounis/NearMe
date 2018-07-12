package com.ktsiounis.example.nearme.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantinos Tsiounis on 12-Jul-18.
 */
public class PlaceOpeningHours {

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
}
