package com.ktsiounis.example.nearme.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.activities.MainActivity;
import com.ktsiounis.example.nearme.model.Place;

import java.util.ArrayList;

/**
 * Created by Konstantinos Tsiounis on 20-Jul-18.
 */
public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private static ArrayList<Place> places = new ArrayList<>();
    private Context context;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    private static void populateListItem() {
        places = WidgetFetchArticlesService.listItemList;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.row_widget_list);

        if(places != null) {
            Place place = places.get(position);
            remoteViews.setTextViewText(R.id.fav_title, place.getName());
            remoteViews.setTextViewText(R.id.fav_address, place.getVicinity());

            Intent intent = new Intent(context, MainActivity.class);

            remoteViews.setOnClickFillInIntent(R.id.fav_title, intent);

        }

        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
