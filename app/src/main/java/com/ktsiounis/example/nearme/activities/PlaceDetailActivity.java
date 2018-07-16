package com.ktsiounis.example.nearme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.fragments.PlaceDetailFragment;
import com.ktsiounis.example.nearme.model.Category;
import com.ktsiounis.example.nearme.model.Place;

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
    @BindView(R.id.details_toolbar) Toolbar toolbar;

    private Place place;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        ButterKnife.bind(this);

        place = getIntent().getExtras().getBundle("args").getParcelable("place");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(place.getName());

//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putParcelable("place", place);
//            PlaceDetailFragment fragment = new PlaceDetailFragment();
//            fragment.setArguments(arguments);
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.place_detail_container, fragment)
//                    .commit();
//        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

}
