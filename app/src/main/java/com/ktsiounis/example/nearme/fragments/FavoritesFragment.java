package com.ktsiounis.example.nearme.fragments;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.activities.PlaceDetailActivity;
import com.ktsiounis.example.nearme.adapters.PlaceListRecyclerViewAdapter;
import com.ktsiounis.example.nearme.model.Place;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoritesFragment extends Fragment implements PlaceListRecyclerViewAdapter.ItemClickListener  {

    @BindView(R.id.place_list)
    public RecyclerView recyclerView;

    private static ArrayList<Place> places = new ArrayList<>();
    private static PlaceListRecyclerViewAdapter recyclerViewAdapter;
    private boolean mTwoPane;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        ButterKnife.bind(this, view);

        new FavoritesTask().execute();

        recyclerViewAdapter = new PlaceListRecyclerViewAdapter(this, places);
        recyclerView.setAdapter(recyclerViewAdapter);

        if (getActivity().findViewById(R.id.place_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemClickListener(int position) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("place", places.get(position));
            PlaceDetailFragment fragment = new PlaceDetailFragment();
            fragment.setArguments(arguments);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.place_detail_container, fragment)
                    .commit();
        } else {
            Bundle arguments = new Bundle();
            arguments.putParcelable("place", places.get(position));
            Intent intent = new Intent(getActivity(), PlaceDetailActivity.class);
            intent.putExtra("args", arguments);
            startActivity(intent);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public static class FavoritesTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    places.clear();
                    for (DataSnapshot favoriteSnapshot: dataSnapshot.getChildren()){
                        Place place = favoriteSnapshot.getValue(Place.class);
                        places.add(place);
                        Log.d("FavoritesTask", "onDataChange: " + place.getName());
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favorites")
                    .addValueEventListener(postListener);

            return null;
        }

    }

}
