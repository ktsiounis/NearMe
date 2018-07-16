package com.ktsiounis.example.nearme.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.model.Place;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoritesFragment extends Fragment {

    @BindView(R.id.tvPlace)
    TextView tvPlace;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        ButterKnife.bind(this, view);

        //TODO: Retrieve favorites

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Place favPlaces = dataSnapshot.getValue(Place.class);
                    tvPlace.setText(favPlaces.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favorites").addListenerForSingleValueEvent(postListener);

        // Inflate the layout for this fragment
        return view;
    }

}
