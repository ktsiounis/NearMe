package com.ktsiounis.example.nearme.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.adapters.FavoritesWidgetAdapter;
import com.ktsiounis.example.nearme.model.Place;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The configuration screen for the {@link FavoritesWidget FavoritesWidget} AppWidget.
 */
public class FavoritesWidgetConfigureActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "com.ktsiounis.example.nearme.NewAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_PREFIX_TITLE_KEY = "appwidget_title_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public static ArrayList<Place> places = new ArrayList<>();
    private static FavoritesWidgetAdapter adapter;

    @BindView(R.id.place_list) RecyclerView place_list;

    public FavoritesWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.putString(PREF_PREFIX_TITLE_KEY + appWidgetId, "Favorites");
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static Bundle loadTitlePref(Context context, int appWidgetId) {

        Bundle values = new Bundle();

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_TITLE_KEY + appWidgetId, null);
        String textValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        values.putString("title", titleValue);
        values.putString("text", textValue);
        if (titleValue != null && textValue != null) {
            return values;
        } else {
            values.putString("title", context.getString(R.string.appwidget_text));
            values.putString("text", context.getString(R.string.appwidget_text));
            return values;
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.favorites_widget_configure);

        ButterKnife.bind(this);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "You need to log in first", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if(icicle != null) {
            places = icicle.getParcelableArrayList("favorites");
            Log.d("Widget", "onCreate: Places fetched from state");
        } else {
            new FavoritesWidgetTask().execute();
        }

        adapter = new FavoritesWidgetAdapter(places);
        place_list.setAdapter(adapter);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_favorites, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_items:
                saveItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void saveItems() {

        StringBuilder favoriteslist = new StringBuilder();

        for (int i=0; i<places.size(); i++){
            if (places.get(i).getChecked()){
                favoriteslist.append("\n").append(places.get(i).getName());
            }
        }

        final Context context = FavoritesWidgetConfigureActivity.this;

        // When the button is clicked, store the string locally
        saveTitlePref(context, mAppWidgetId, favoriteslist.toString());

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        FavoritesWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();

    }

    @SuppressLint("StaticFieldLeak")
    public static class FavoritesWidgetTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot favoriteSnapshot: dataSnapshot.getChildren()){
                        Place place = favoriteSnapshot.getValue(Place.class);
                        places.add(place);
                        Log.d("FavoritesTask", "onDataChange: " + place.getName());
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favorites")
                    .addValueEventListener(postListener);

            return null;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("favorites", places);
    }
}

