package com.ktsiounis.example.nearme.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.activities.PlaceListActivity;
import com.ktsiounis.example.nearme.adapters.CategoriesAdapter;
import com.ktsiounis.example.nearme.model.Category;
import com.ktsiounis.example.nearme.model.Place;
import com.ktsiounis.example.nearme.model.PlacesResults;
import com.ktsiounis.example.nearme.rest.APIClientPlaces;
import com.ktsiounis.example.nearme.rest.RequestInterfacePlaces;
import com.ktsiounis.example.nearme.rest.RequestInterfaceTextSearch;

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
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;
    @BindView(R.id.searchRtn)
    ImageButton searchBtn;

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
        progressBar.setVisibility(View.INVISIBLE);

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

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: fix problem with location

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        fetchTextSearchResults(location);
                    }
                });

            }
        });

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
                fetchPlaces(location, position);
            }
        });
    }

    public void fetchPlaces(Location location, final int position) {

        progressBar.setVisibility(View.VISIBLE);

        String type = "";
        String keyword = "";
        RequestInterfacePlaces requestInterfacePlaces = APIClientPlaces.getClient().create(RequestInterfacePlaces.class);

        switch (categoryArrayList.get(position).getTitle()) {
            case "Hotels":
                type = "lodging";
                keyword = "hotel";
                break;
            case "Bars":
                type = "bar";
                keyword = "bar";
                break;
            case "Restaurants":
                type = "restaurant";
                keyword = "food";
                break;
            case "Coffee Shops":
                type = "cafe";
                keyword = "coffee";
                break;
            case "Gas Stations":
                type = "gas_station";
                keyword = "gas";
                break;
            case "Museums":
                type = "museum";
                keyword = "museum";
                break;
        }

        Call<PlacesResults> call =
                requestInterfacePlaces.getNearByPlaces(location.getLatitude() + "," + location.getLongitude(),
                        "1500",
                        type,
                        keyword,
                        getResources().getString(R.string.API_KEY));

        call.enqueue(new Callback<PlacesResults>() {
            @Override
            public void onResponse(Call<PlacesResults> call, Response<PlacesResults> response) {
                progressBar.setVisibility(View.INVISIBLE);
                ArrayList<Place> places = response.body().getPlaces();
                Intent i = new Intent(getActivity(), PlaceListActivity.class);
                i.putParcelableArrayListExtra("places", places);
                i.putExtra("category", categoryArrayList.get(position).getTitle());
                startActivity(i);
            }

            @Override
            public void onFailure(Call<PlacesResults> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    public void fetchTextSearchResults(Location location) {
        RequestInterfaceTextSearch requestInterfaceTextSearch = APIClientPlaces.getClient().create(RequestInterfaceTextSearch.class);

        Call<PlacesResults> call =
                requestInterfaceTextSearch.getTextSearchResults(
                        search_input.getText().toString().replace(" ", "+"),
                        "42.3675294,-71.186966",
                        "1500",
                        getResources().getString(R.string.API_KEY));

        call.enqueue(new Callback<PlacesResults>() {
            @Override
            public void onResponse(@NonNull Call<PlacesResults> call, @NonNull Response<PlacesResults> response) {
                ArrayList<Place> places = response.body().getPlaces();
//                Intent i = new Intent(getActivity(), PlaceListActivity.class);
//                i.putParcelableArrayListExtra("places", places);
//                startActivity(i);
            }

            @Override
            public void onFailure(Call<PlacesResults> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

}
