package com.example.travaleva.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travaleva.R;
import com.example.travaleva.fragments.BookingFragment; // Import the BookingFragment
import com.example.travaleva.model.RecentsData;

import java.util.List;

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.RecentsViewHolder> {

    private Context context;
    private List<RecentsData> recentsDataList;

    public RecentsAdapter(Context context, List<RecentsData> recentsDataList) {
        this.context = context;
        this.recentsDataList = recentsDataList;
    }

    @NonNull
    @Override
    public RecentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recents_row_item, parent, false);
        return new RecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentsViewHolder holder, int position) {
        RecentsData currentItem = recentsDataList.get(position);

        // Bind data to views
        holder.placeName.setText(currentItem.getTitle());
        holder.city.setText(currentItem.getCity());  // Correctly bind the city
        holder.price.setText("From " + currentItem.getCharges());  // Set charges

        // Use Glide to load image from URL
        Glide.with(context)
                .load(currentItem.getImageUrl())  // Load image from URL
                .into(holder.placeImage);  // Set it into ImageView

        // Handle item click
        holder.itemView.setOnClickListener(view -> {
            // Create a Bundle to pass data to the BookingFragment
            Bundle bundle = new Bundle();
            bundle.putString("placeTitle", currentItem.getTitle()); // Pass place name
            bundle.putString("placeDescription", currentItem.getDescription());
            bundle.putString("placeImageUrl", currentItem.getImageUrl());
            bundle.putString("placeCity", currentItem.getCity());
            bundle.putString("placeCharges", currentItem.getCharges());

            // Log the data being passed
            Log.d("RecentsAdapter", "Place Title: " + currentItem.getTitle());
            Log.d("RecentsAdapter", "Place Description: " + currentItem.getDescription());
            Log.d("RecentsAdapter", "Place Image URL: " + currentItem.getImageUrl());
            Log.d("RecentsAdapter", "Place City: " + currentItem.getCity());
            Log.d("RecentsAdapter", "Place Charges: " + currentItem.getCharges());

            // Create an instance of BookingFragment
            BookingFragment bookingFragment = new BookingFragment();
            bookingFragment.setArguments(bundle);

            // Use FragmentManager to replace the current fragment with BookingFragment
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, bookingFragment); // Replace 'fragment_container' with your container ID
            transaction.addToBackStack(null); // Optional: Add to back stack
            transaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return recentsDataList.size();
    }

    public static class RecentsViewHolder extends RecyclerView.ViewHolder {

        ImageView placeImage;
        TextView placeName, city, price;

        public RecentsViewHolder(@NonNull View itemView) {
            super(itemView);

            placeImage = itemView.findViewById(R.id.place_image);
            placeName = itemView.findViewById(R.id.place_name);
            city = itemView.findViewById(R.id.place_location);  // Use place_location for city
            price = itemView.findViewById(R.id.placeCharges);  // Use placeCharges for price
        }
    }
}