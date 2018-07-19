package com.ktsiounis.example.nearme.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

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
    @BindView(R.id.ratingBar) public RatingBar ratingBar;
    @BindView(R.id.placeIcon) public ImageView placeIcon;
    @BindView(R.id.placePhoto) public ImageView placePhoto;
    @BindView(R.id.placeAddress) public TextView placeAddress;
    @BindView(R.id.placeName) public TextView placeName;
    @BindView(R.id.ratingNum) public TextView ratingNum;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_place_detail, container, false);

        ButterKnife.bind(this, rootView);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);

        ratingBar.setRating(Float.valueOf(mItem.getRating()));
        ratingNum.setText(mItem.getRating());
        placeAddress.setText(mItem.getVicinity());
        placeName.setText(mItem.getName());
        Picasso.with(getActivity())
                .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" +
                        mItem.getPlacePhotos().get(0).getPhoto_reference() + "&key=" + getResources().getString(R.string.API_KEY))
                .into(placePhoto);
        Picasso.with(getActivity())
                .load(mItem.getIcon())
                .into(placeIcon);

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
