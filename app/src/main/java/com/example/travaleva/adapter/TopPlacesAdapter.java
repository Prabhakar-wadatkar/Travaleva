package com.example.travaleva.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Add this import
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travaleva.R;
import com.example.travaleva.model.TopPlacesData;

import java.util.List;

public class TopPlacesAdapter extends RecyclerView.Adapter<TopPlacesAdapter.TopPlacesViewHolder> {
    private Context context;
    private List<TopPlacesData> topPlacesDataList;
    private OnItemClickListener listener;

    public TopPlacesAdapter(Context context, List<TopPlacesData> topPlacesDataList) {
        this.context = context;
        this.topPlacesDataList = topPlacesDataList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
        holder.placeDescription.setText(currentItem.getDescription());
        holder.placeCity.setText(currentItem.getCity());
        holder.placeCharges.setText(currentItem.getCharges());

        // Load image using Glide or any other image-loading library
        Glide.with(context)
                .load(currentItem.getImageUrl())
                .into(holder.placeImage);

        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewDetailsClick(currentItem);
            }
        });

        holder.bookButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookPlaceClick(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topPlacesDataList.size();
    }

    public static class TopPlacesViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, placeDescription, placeCity, placeCharges;
        ImageView placeImage;
        Button bookButton; // Button is now recognized

        public TopPlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.place_name);
            placeDescription = itemView.findViewById(R.id.place_description);
            placeCity = itemView.findViewById(R.id.place_city);
            placeCharges = itemView.findViewById(R.id.place_charges);
            placeImage = itemView.findViewById(R.id.place_image);
            bookButton = itemView.findViewById(R.id.book_button); // Ensure this ID matches your layout
        }
    }

    public interface OnItemClickListener {
        void onViewDetailsClick(TopPlacesData topPlace);
        void onBookPlaceClick(TopPlacesData topPlace);
    }
}