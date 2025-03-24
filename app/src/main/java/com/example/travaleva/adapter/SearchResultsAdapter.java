package com.example.travaleva.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travaleva.R;
import com.example.travaleva.fragments.BookingFragment;
import com.example.travaleva.model.Place;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private Context context;
    private List<Place> searchResultsList;

    public SearchResultsAdapter(Context context, List<Place> searchResultsList) {
        this.context = context;
        this.searchResultsList = searchResultsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = searchResultsList.get(position);

        // Bind basic details
        holder.placeName.setText(place.getTitle());
        holder.placeLocation.setText(place.getCity());
        holder.placeCharges.setText(place.getCharges());

        // Bind expandable section details
        holder.placeDescription.setText(place.getDescription());
        holder.placeAddedBy.setText(place.getAddedBy());

        // Load image using Glide
        if (place.getImageUrl() != null && !place.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(place.getImageUrl())
                    .placeholder(R.drawable.placeholder_image) // Placeholder while loading
                    .error(R.drawable.error_image) // Error image if loading fails
                    .into(holder.placeImage);
        } else {
            // If imageUrl is empty, set a default placeholder
            holder.placeImage.setImageResource(R.drawable.placeholder_image);
        }

        // Handle expand/collapse functionality
        holder.buttonViewDetails.setOnClickListener(v -> {
            if (holder.detailsLayout.getVisibility() == View.GONE) {
                holder.detailsLayout.setVisibility(View.VISIBLE);
            } else {
                holder.detailsLayout.setVisibility(View.GONE);
            }
        });

        // Handle "BOOK PLACE" button click
        holder.buttonBookPlace.setOnClickListener(v -> {
            // Open BookingFragment
            BookingFragment bookingFragment = new BookingFragment();

            // Pass the entire Place object to BookingFragment
            Bundle args = new Bundle();
            args.putSerializable("place", place); // Pass the Place object
            bookingFragment.setArguments(args);

            // Perform fragment transaction
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, bookingFragment);
            fragmentTransaction.addToBackStack(null); // Add to back stack
            fragmentTransaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the dataset
        return searchResultsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, placeLocation, placeCharges, placeDescription, placeParking, placeAddedBy;
        ImageView placeImage;
        LinearLayout detailsLayout;
        MaterialButton buttonViewDetails, buttonBookPlace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            placeName = itemView.findViewById(R.id.place_name);
            placeLocation = itemView.findViewById(R.id.place_location);
            placeCharges = itemView.findViewById(R.id.placeCharges);
            placeDescription = itemView.findViewById(R.id.placeDescription);
            placeParking = itemView.findViewById(R.id.placeParking);
            placeAddedBy = itemView.findViewById(R.id.placeAddedBy);
            placeImage = itemView.findViewById(R.id.place_image);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);
            buttonViewDetails = itemView.findViewById(R.id.buttonViewDetails);
            buttonBookPlace = itemView.findViewById(R.id.BlookPlace);
        }
    }
}