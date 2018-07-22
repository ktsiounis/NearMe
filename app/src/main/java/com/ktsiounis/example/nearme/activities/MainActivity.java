package com.ktsiounis.example.nearme.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.fragments.FavoritesFragment;
import com.ktsiounis.example.nearme.fragments.HomeFragment;
import com.ktsiounis.example.nearme.model.Category;
import com.ktsiounis.example.nearme.rest.APIClientFirebase;
import com.ktsiounis.example.nearme.rest.RequestInterfaceFirebase;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_LOCATION = 0;

    @BindView(R.id.navigation) BottomNavigationView navigation;
    public ActionBar toolbar;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.adView)
    public AdView adView;
    @BindView(R.id.offlineMessage)
    public TextView offlineMessage;
    @BindView(R.id.tryAgainBtn)
    public Button tryAgainBtn;

    private FirebaseAuth mAuth;
    private SharedPreferences sp;
    public ArrayList<Category> categoryArrayList = new ArrayList<>();
    private Bundle args = new Bundle();

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
        if(mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "You need to log in first", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        sp = getSharedPreferences("logged", MODE_PRIVATE);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        offlineMessage.setVisibility(View.INVISIBLE);
        tryAgainBtn.setVisibility(View.INVISIBLE);

        if(savedInstanceState != null){
            progressBar.setVisibility(View.INVISIBLE);
            categoryArrayList.clear();
            categoryArrayList = savedInstanceState.getParcelableArrayList("categories");
            Log.d("MainActivity", "onCreate: " + categoryArrayList.get(0).getTitle());
            navigation.setSelectedItemId(savedInstanceState.getInt("navigationState"));
        } else {
            toolbar.setTitle(R.string.title_home);
            if(isOnline()){
                progressBar.setVisibility(View.VISIBLE);
                offlineMessage.setVisibility(View.INVISIBLE);
                tryAgainBtn.setVisibility(View.INVISIBLE);
                new CategoriesAsyncTask().execute();
            }else {
                progressBar.setVisibility(View.INVISIBLE);
                offlineMessage.setVisibility(View.VISIBLE);
                tryAgainBtn.setVisibility(View.VISIBLE);
            }
        }

        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    progressBar.setVisibility(View.VISIBLE);
                    offlineMessage.setVisibility(View.INVISIBLE);
                    tryAgainBtn.setVisibility(View.INVISIBLE);
                    new CategoriesAsyncTask().execute();
                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                    offlineMessage.setVisibility(View.VISIBLE);
                    tryAgainBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)  // An example device ID
                .build();

        adView.loadAd(request);

    }

    public void fetchCategories() throws IOException {
        RequestInterfaceFirebase requestInterfaceFirebase = APIClientFirebase.getClient().create(RequestInterfaceFirebase.class);
        Call<Category> call;

        for(int i=0; i<6; i++) {
            call = requestInterfaceFirebase.getCategory(i);
            final Category category = call.execute().body();
            categoryArrayList.add(category);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class CategoriesAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

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
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("categories", categoryArrayList);
        outState.putInt("navigationState", navigation.getSelectedItemId());

    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
