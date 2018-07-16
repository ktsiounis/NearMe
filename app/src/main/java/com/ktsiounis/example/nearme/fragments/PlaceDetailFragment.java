package com.ktsiounis.example.nearme.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.activities.PlaceDetailActivity;
import com.ktsiounis.example.nearme.activities.PlaceListActivity;
import com.ktsiounis.example.nearme.model.Place;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Place detail screen.
 * This fragment is either contained in a {@link PlaceListActivity}
 * in two-pane mode (on tablets) or a {@link PlaceDetailActivity}
 * on handsets.
 */
public class PlaceDetailFragment extends Fragment implements OnMapReadyCallback {

    private Place mItem;
    private GoogleMap mMap;

    @BindView(R.id.fragment_fab) public FloatingActionButton fab;

    public PlaceDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("place")) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = getArguments().getParcelable("place");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_place_detail, container, false);

        ButterKnife.bind(this, rootView);

        //TODO: Implement the other elements for details

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng placeLocation = new LatLng(Double.valueOf(mItem.getPlaceGeometry().getPlaceLocation().getLat()), Double.valueOf(mItem.getPlaceGeometry().getPlaceLocation().getLng()));

        mMap.addMarker(new MarkerOptions()
                .position(placeLocation)
                .title(mItem.getName()));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}
