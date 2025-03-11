package com.example.travaleva.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travaleva.R;
import com.example.travaleva.fragments.BookingFragment;
import com.example.travaleva.model.Place;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

    private static final String PREF_NAME = "TravalevaPrefs";
    private static final String PLACE_LIST_KEY = "PlaceList";

    private Context context;
    private List<Place> placeList;

    public PlaceAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
        savePlaceListToSharedPreferences(); // Save list on adapter creation
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_place_card, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);

        // Set text details
        holder.titleTextView.setText(place.getTitle());
        holder.descriptionTextView.setText(place.getDescription());
        holder.chargesTextView.setText("₹" + place.getCharges());
        holder.parkingTextView.setText(place.getParkingAvailable());
        holder.addedByTextView.setText(place.getAddedBy());

        // Set city text
        holder.cityTextView.setText(place.getCity());

        // Load image using Glide
        if (place.getImageUrl() != null && !place.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(place.getImageUrl())
                    .placeholder(R.drawable.placeholder_image) // Placeholder image
                    .error(R.drawable.error_image) // Error image
                    .into(holder.placeImageView);
        } else {
            holder.placeImageView.setImageResource(R.drawable.placeholder_image);
        }

        // "VIEW DETAILS" button functionality (expand/collapse)
        holder.buttonViewDetails.setOnClickListener(v -> {
            if (holder.detailsLayout.getVisibility() == View.GONE) {
                holder.detailsLayout.setVisibility(View.VISIBLE);
                holder.buttonViewDetails.setText("HIDE DETAILS");
            } else {
                holder.detailsLayout.setVisibility(View.GONE);
                holder.buttonViewDetails.setText("VIEW DETAILS");
            }
        });

        // "VIEW IN MAP" button functionality
        holder.buttonViewMap.setOnClickListener(v -> {
            double latitude = place.getLatitude();
            double longitude = place.getLongitude();

            Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + Uri.encode(place.getTitle()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(mapIntent);
            }
        });

        // "BOOK NOW" button functionality
        holder.buttonBookNow.setOnClickListener(v -> {
            // Open Booking Fragment
            AppCompatActivity activity = (AppCompatActivity) context;
            BookingFragment bookingFragment = BookingFragment.newInstance(place);

            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, bookingFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, chargesTextView, parkingTextView, addedByTextView, cityTextView;
        ImageView placeImageView;
        MaterialButton buttonViewDetails, buttonViewMap, buttonBookNow;
        LinearLayout detailsLayout; // For expand/collapse feature

        public PlaceViewHolder(View itemView) {
            super(itemView);

            // Initialize views
            titleTextView = itemView.findViewById(R.id.Place_title);
            descriptionTextView = itemView.findViewById(R.id.placeDescription);
            chargesTextView = itemView.findViewById(R.id.placeCharges);
            parkingTextView = itemView.findViewById(R.id.placeParking);
            addedByTextView = itemView.findViewById(R.id.placeAddedBy);
            placeImageView = itemView.findViewById(R.id.placeImage);
            buttonViewDetails = itemView.findViewById(R.id.buttonViewDetails);
            buttonViewMap = itemView.findViewById(R.id.buttonViewMap);
            buttonBookNow = itemView.findViewById(R.id.buttonBookPlace);
            detailsLayout = itemView.findViewById(R.id.detailsLayout);

            // Initialize cityTextView
            cityTextView = itemView.findViewById(R.id.placeCity);
        }
    }

    // Save the place list to SharedPreferences
    private void savePlaceListToSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(placeList);
        editor.putString(PLACE_LIST_KEY, json);
        editor.apply();
    }

    // Retrieve the place list from SharedPreferences
    public static List<Place> getPlaceListFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(PLACE_LIST_KEY, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Place>>() {}.getType();
            return gson.fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    // Update the place list in the adapter and save it to SharedPreferences
    public void updatePlaceList(List<Place> newPlaceList) {
        this.placeList = newPlaceList;
        savePlaceListToSharedPreferences();
        notifyDataSetChanged();
    }
}