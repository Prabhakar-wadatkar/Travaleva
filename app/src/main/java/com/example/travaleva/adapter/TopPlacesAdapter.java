package com.example.travaleva.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travaleva.DetailsActivity;
import com.example.travaleva.R;
import com.example.travaleva.model.TopPlacesData;

import java.util.List;

public class TopPlacesAdapter extends RecyclerView.Adapter<TopPlacesAdapter.TopPlacesViewHolder> {

    private Context context;
    private List<TopPlacesData> topPlacesDataList;
    private OnItemClickListener listener;

    // Add click listener interface
    public interface OnItemClickListener {
        void onTopPlaceClick(TopPlacesData topPlace);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TopPlacesAdapter(Context context, List<TopPlacesData> topPlacesDataList) {
        this.context = context;
        this.topPlacesDataList = topPlacesDataList;
    }

    @NonNull
    @Override
    public TopPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_places_row_item, parent, false);
        return new TopPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPlacesViewHolder holder, int position) {
        TopPlacesData currentItem = topPlacesDataList.get(position);

        holder.placeName.setText(currentItem.getTitle());
        holder.city.setText(currentItem.getCity());
        holder.price.setText("Charges From " + currentItem.getCharges());

        Glide.with(context)
                .load(currentItem.getImageUrl())
                .into(holder.placeImage);

        // Add click listener
        holder.itemView.setOnClickListener(view -> {
            if (listener != null) {
                listener.onTopPlaceClick(currentItem);
            }
        });
    }

    // Rest of the code remains unchanged
    @Override
    public int getItemCount() {
        return topPlacesDataList.size();
    }

    public static class TopPlacesViewHolder extends RecyclerView.ViewHolder {
        ImageView placeImage;
        TextView placeName, city, price;

        public TopPlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.place_image);
            placeName = itemView.findViewById(R.id.place_name);
            city = itemView.findViewById(R.id.place_location);
            price = itemView.findViewById(R.id.placeCharges);
        }
    }
}