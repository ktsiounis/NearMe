package com.ktsiounis.example.nearme.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.activities.PlaceListActivity;
import com.ktsiounis.example.nearme.adapters.CategoriesAdapter;
import com.ktsiounis.example.nearme.model.Category;
import com.ktsiounis.example.nearme.model.Place;
import com.ktsiounis.example.nearme.model.PlacesResults;
import com.ktsiounis.example.nearme.rest.APIClientPlaces;
import com.ktsiounis.example.nearme.rest.RequestInterfacePlaces;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements CategoriesAdapter.ItemClickListener {

    public static final String TAG = "HomeFragment";

    @BindView(R.id.categories_recyclerview)
    RecyclerView categories;
    @BindView(R.id.search_input)
    EditText search_input;

    public ArrayList<Category> categoryArrayList;
    public CategoriesAdapter categoriesAdapter;
    private FusedLocationProviderClient mFusedLocationClient;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        categoriesAdapter = new CategoriesAdapter(getActivity(), this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        categories.setLayoutManager(mLayoutManager);
        categories.setItemAnimator(new DefaultItemAnimator());
        categories.setHasFixedSize(true);
        categories.setAdapter(categoriesAdapter);

        if (getArguments() != null && getArguments().containsKey("data")) {
            categoryArrayList = getArguments().getParcelableArrayList("data");
            categoriesAdapter.swapList(categoryArrayList);
        } else {
            categoryArrayList = new ArrayList<>();
        }

        for (int i = 0; i < categoryArrayList.size(); i++) {
            Log.d("Fragment", "onBindViewHolder: " + categoryArrayList.get(i).getThumbnail());
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemClickListener(final int position) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d(TAG, "onSuccess: " + location.getLatitude() + "," + location.getLongitude());
                RequestInterfacePlaces requestInterfacePlaces = APIClientPlaces.getClient().create(RequestInterfacePlaces.class);
                Call<PlacesResults> call =
                        requestInterfacePlaces.getNearByPlaces(location.getLatitude() + "," + location.getLongitude(),
                                "1500",
                                categoryArrayList.get(position).getTitle(),
                                getResources().getString(R.string.API_KEY));

                call.enqueue(new Callback<PlacesResults>() {
                    @Override
                    public void onResponse(Call<PlacesResults> call, Response<PlacesResults> response) {
                        ArrayList<Place> places = response.body().getPlaces();
                        Log.d(TAG, "onResponse: " + places.get(1).getName());
                        Intent i = new Intent(getActivity(), PlaceListActivity.class);
                        i.putParcelableArrayListExtra("places", categoryArrayList);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<PlacesResults> call, Throwable t) {
                        Log.e(TAG, "onFailure: ", t);
                    }
                });
            }
        });
    }

}
