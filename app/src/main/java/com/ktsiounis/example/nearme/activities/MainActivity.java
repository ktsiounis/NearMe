package com.ktsiounis.example.nearme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.adapters.CategoriesAdapter;
import com.ktsiounis.example.nearme.fragments.FavoritesFragment;
import com.ktsiounis.example.nearme.fragments.HomeFragment;
import com.ktsiounis.example.nearme.model.Category;
import com.ktsiounis.example.nearme.rest.APIClient;
import com.ktsiounis.example.nearme.rest.RequestInterface;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.navigation) BottomNavigationView navigation;
    private ActionBar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private SharedPreferences sp;
    public ArrayList<Category> categoryArrayList;
    private Bundle args;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle(R.string.title_home);
                    fragment = new HomeFragment();
                    args.putParcelableArrayList("data", categoryArrayList);
                    fragment.setArguments(args);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_dashboard:
                    toolbar.setTitle(R.string.title_favorites);
                    fragment = new FavoritesFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        toolbar = getSupportActionBar();
        categoryArrayList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences("logged", MODE_PRIVATE);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle(R.string.title_home);
        new CategoriesAsyncTask().execute();
    }

    public void fetchCategories() throws IOException {
        RequestInterface requestInterface = APIClient.getClient().create(RequestInterface.class);
        Call<Category> call;
        StorageReference reference = FirebaseStorage.getInstance().getReference();

        for(int i=0; i<6; i++) {
            call = requestInterface.getCategory(i);
            final Category category = call.execute().body();
            categoryArrayList.add(category);
        }
    }

    public class CategoriesAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            progressBar.setVisibility(View.VISIBLE);

            try {
                fetchCategories();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            progressBar.setVisibility(View.INVISIBLE);
            Fragment fragment = new HomeFragment();
            args = new Bundle();
            args.putParcelableArrayList("data", categoryArrayList);
            fragment.setArguments(args);
            loadFragment(fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void logoutUser() {
        mAuth.signOut();
        sp.edit().putBoolean("logged", false).apply();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
