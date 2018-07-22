package com.ktsiounis.example.nearme;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class NearMeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
