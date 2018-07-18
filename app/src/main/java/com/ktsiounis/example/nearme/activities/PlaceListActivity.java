package com.ktsiounis.example.nearme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.adapters.PlaceListRecyclerViewAdapter;
import com.ktsiounis.example.nearme.fragments.PlaceDetailFragment;
import com.ktsiounis.example.nearme.model.Category;
import com.ktsiounis.example.nearme.model.Place;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Places. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PlaceDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PlaceListActivity extends AppCompatActivity implements PlaceListRecyclerViewAdapter.ItemClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ArrayList<Place> places;
    private String category;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.noPlacesTextView)
    public TextView noPlacesTV;
    @BindView(R.id.place_list)
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        ButterKnife.bind(this);

        places = getIntent().getParcelableArrayListExtra("places");
        category = getIntent().getStringExtra("category");

        if(places.isEmpty()) {
            noPlacesTV.setVisibility(View.VISIBLE);
        } else {
            noPlacesTV.setVisibility(View.INVISIBLE);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(category);

        if (findViewById(R.id.place_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        assert recyclerView != null;
        setupRecyclerView(recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new PlaceListRecyclerViewAdapter(this, places));
    }

    @Override
    public void onItemClickListener(int position) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable("place", places.get(position));
            PlaceDetailFragment fragment = new PlaceDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.place_detail_container, fragment)
                    .commit();
        } else {
            Bundle arguments = new Bundle();
            arguments.putParcelable("place", places.get(position));
            Intent intent = new Intent(this, PlaceDetailActivity.class);
            intent.putExtra("args", arguments);
            startActivity(intent);
        }
    }
}
