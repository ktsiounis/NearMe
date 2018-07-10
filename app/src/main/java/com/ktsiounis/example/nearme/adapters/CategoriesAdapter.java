package com.ktsiounis.example.nearme.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.ktsiounis.example.nearme.R;
import com.ktsiounis.example.nearme.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Konstantinos Tsiounis on 09-Jul-18.
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private ItemClickListener mListener;
    private Context mContext;
    private ArrayList<Category> mCategories = new ArrayList<>();

    public CategoriesAdapter(Context context, ItemClickListener listener){
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(mContext).inflate(R.layout.category_card, parent,false);
        return new CategoriesViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoriesViewHolder holder, int position) {
        holder.categoryTitle.setText(mCategories.get(position).getTitle());

        Picasso.with(mContext)
                .load(mCategories.get(position).getThumbnail())
                .into(holder.categoryThumbnail);

    }

    @Override
    public int getItemCount() {
        if(mCategories == null) return 0;
        else return mCategories.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.category_thumbnail)
        ImageView categoryThumbnail;
        @BindView(R.id.category_title)
        TextView categoryTitle;

        public CategoriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mListener.onItemClickListener(position);
        }
    }

    public interface ItemClickListener{
        void onItemClickListener(int position);
    }

    public void swapList(ArrayList<Category> categories){
        if( mCategories != null ){
            mCategories.clear();
            mCategories.addAll(categories);
        }
        else {
            mCategories = categories;
        }

        Log.d("ADAPTER", "swapList: Data changed " + mCategories.get(2).getTitle());

        notifyDataSetChanged();
    }

}
