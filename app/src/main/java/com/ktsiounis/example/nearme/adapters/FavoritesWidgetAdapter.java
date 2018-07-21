package com.ktsiounis.example.nearme.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.model.Place;
import com.ktsiounis.example.nearme.widget.FavoritesWidget;
import com.ktsiounis.example.nearme.widget.FavoritesWidgetConfigureActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesWidgetAdapter extends RecyclerView.Adapter<FavoritesWidgetAdapter.ViewHolder> {

    private ArrayList<Place> mValues;
    private Context context;

    public FavoritesWidgetAdapter(ArrayList<Place> places) {
        mValues = places;
    }

    @Override
    public FavoritesWidgetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_content, parent, false);

        context = parent.getContext();

        return new FavoritesWidgetAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoritesWidgetAdapter.ViewHolder holder, int position) {
        holder.place_name.setText(mValues.get(position).getName());
        holder.place_vicinity.setText(mValues.get(position).getVicinity());
        if(!mValues.get(position).getPlacePhotos().isEmpty()){
            Picasso.with(context)
                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&maxheight=400&photoreference=" +
                            mValues.get(position).getPlacePhotos().get(0).getPhoto_reference() + "&key=" + context.getResources().getString(R.string.API_KEY))
                    .into(holder.place_photo);
        }
    }

    @Override
    public int getItemCount() {
        if(mValues != null) {
            return mValues.size();
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.place_name)
        TextView place_name;
        @BindView(R.id.place_vicinity) TextView place_vicinity;
        @BindView(R.id.place_photo)
        ImageView place_photo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if(mValues.get(position).getChecked()){
                mValues.get(position).setChecked(false);
                v.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            } else {
                mValues.get(position).setChecked(true);
                v.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));
            }

        }
    }

}
