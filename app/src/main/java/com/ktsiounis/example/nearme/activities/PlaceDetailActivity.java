package com.ktsiounis.example.nearme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.model.Place;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Place detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PlaceListActivity}.
 */
public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.fab) public FloatingActionButton fab;
    @BindView(R.id.details_toolbar) public Toolbar toolbar;
    @BindView(R.id.ratingBar) public RatingBar ratingBar;
    @BindView(R.id.placeIcon) public ImageView placeIcon;
    @BindView(R.id.placePhoto) public ImageView placePhoto;
    @BindView(R.id.placeAddress) public TextView placeAddress;
    @BindView(R.id.placeName) public TextView placeName;
    @BindView(R.id.ratingNum) public TextView ratingNum;

    private Place place;
    private GoogleMap mMap;
    private Boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        ButterKnife.bind(this);

        if(savedInstanceState != null){
            place = savedInstanceState.getParcelable("placeState");
        } else {
            place = getIntent().getExtras().getBundle("args").getParcelable("place");
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFavorite){
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("favorites")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot favorite : dataSnapshot.getChildren()){
                                        if(favorite.getValue(Place.class).getName().equals(place.getName())){
                                            favorite.getRef().removeValue();
                                            fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                            isFavorite = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                    Snackbar.make(view, "The place saved removed from favorites!", Snackbar.LENGTH_LONG)
                            .setAction("OK", null).show();

                } else {
                    String key = FirebaseDatabase.getInstance().getReference().push().getKey();
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favorites").child(key).setValue(place);
                    Snackbar.make(view, "The place saved in your favorites!", Snackbar.LENGTH_LONG)
                            .setAction("OK", null).show();
                    fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                    isFavorite = true;
                }
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(place.getName());

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ratingBar.setRating(Float.valueOf(place.getRating()));
        ratingNum.setText(place.getRating());
        placeAddress.setText(place.getVicinity());
        placeName.setText(place.getName());
        if(!place.getPlacePhotos().isEmpty()){
            Picasso.with(this)
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" +
                            place.getPlacePhotos().get(0).getPhoto_reference() + "&key=" + getResources().getString(R.string.API_KEY))
                    .into(placePhoto);
        }
        Picasso.with(this)
                .load(place.getIcon())
                .into(placeIcon);

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("favorites")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot favorite : dataSnapshot.getChildren()){
                            Log.d("DetailsActivity", "onDataChange: It's a favorite " + favorite.getValue(Place.class).getName());
                            if(favorite.getValue(Place.class).getName().equals(place.getName())){
                                Log.d("DetailsActivity", "onDataChange: It's a favorite " + favorite.getValue(Place.class).getName());
                                fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                                isFavorite = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, PlaceListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng placeLocation = new LatLng(Double.valueOf(place.getPlaceGeometry().getPlaceLocation().getLat()), Double.valueOf(place.getPlaceGeometry().getPlaceLocation().getLng()));

        mMap.addMarker(new MarkerOptions()
                .position(placeLocation)
                .title(place.getName()));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("place", place);
    }
}
