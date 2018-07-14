package com.ktsiounis.example.nearme.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.model.Category;
import com.ktsiounis.example.nearme.model.Place;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Konstantinos Tsiounis on 11-Jul-18.
 */
public class PlaceListRecyclerViewAdapter
        extends RecyclerView.Adapter<PlaceListRecyclerViewAdapter.ViewHolder> {

    private ItemClickListener mOnClickListener;
    private ArrayList<Place> mValues;

    public PlaceListRecyclerViewAdapter(ItemClickListener itemClickListener,
                                        ArrayList<Place> places) {
        mOnClickListener = itemClickListener;
        mValues = places;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.place_name.setText(mValues.get(position).getName());
        holder.place_category.setText(mValues.get(position).getVicinity());
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
        @BindView(R.id.place_name) TextView place_name;
        @BindView(R.id.place_category) TextView place_category;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onItemClickListener(position);
        }
    }

    public interface ItemClickListener {
        void onItemClickListener(int position);
    }

}
