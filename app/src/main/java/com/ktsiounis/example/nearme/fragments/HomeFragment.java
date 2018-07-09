package com.ktsiounis.example.nearme.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.ktsiounis.example.nearme.adapters.CategoriesAdapter;
import com.ktsiounis.example.nearme.model.Category;
import com.ktsiounis.example.nearme.rest.APIClient;
import com.ktsiounis.example.nearme.rest.RequestInterface;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;


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

        StorageReference load = FirebaseStorage.getInstance().getReference();

        categoryArrayList = new ArrayList<>();

        try {
            fetchCategories();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        categories.setLayoutManager(mLayoutManager);
        categories.setItemAnimator(new DefaultItemAnimator());
        categories.setHasFixedSize(true);
        categoriesAdapter = new CategoriesAdapter(this.getActivity(), this, load);
        categories.setAdapter(categoriesAdapter);


        // Inflate the layout for this fragment
        return view;
    }

    public void fetchCategories() throws IOException {
        RequestInterface requestInterface = APIClient.getClient().create(RequestInterface.class);
        Call<Category> call;

        //for(int i=0; i<7; i++) {

            call = requestInterface.getCategory(0);

            call.enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {
                    Category category = response.body();
                    categoryArrayList.add(category);
                    categoriesAdapter.swapList(categoryArrayList);
                }

                @Override
                public void onFailure(Call<Category> call, Throwable t) {
                    Log.e("ErrorOnResponse", "onFailure: " + t);
                }
            });

            //categoryArrayList.add(call.execute().body());
        //}

    }



    @Override
    public void onItemClickListener(int position) {
        Intent i = new Intent(getActivity(), DetailsActivity.class);
        startActivity(i);
    }
}
