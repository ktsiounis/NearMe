package com.ktsiounis.example.nearme.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import retrofit2.Call;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.activities.DetailsActivity;
import com.ktsiounis.example.nearme.activities.PlaceListActivity;
import com.ktsiounis.example.nearme.adapters.CategoriesAdapter;
import com.ktsiounis.example.nearme.model.Category;
import com.ktsiounis.example.nearme.rest.APIClient;
import com.ktsiounis.example.nearme.rest.RequestInterface;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment implements CategoriesAdapter.ItemClickListener {

    @BindView(R.id.categories_recyclerview)
    RecyclerView categories;
    @BindView(R.id.search_input)
    EditText search_input;

    public ArrayList<Category> categoryArrayList;
    public CategoriesAdapter categoriesAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        if(getArguments() != null && getArguments().containsKey("data")){
            categoryArrayList = getArguments().getParcelableArrayList("data");
            categoriesAdapter.swapList(categoryArrayList);
        } else {
            categoryArrayList = new ArrayList<>();
        }

        for (int i=0; i < categoryArrayList.size(); i++){
            Log.d("Fragment", "onBindViewHolder: " + categoryArrayList.get(i).getThumbnail());
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemClickListener(int position) {
        Intent i = new Intent(getActivity(), PlaceListActivity.class);
        i.putParcelableArrayListExtra("places", categoryArrayList);
        startActivity(i);
    }

}
