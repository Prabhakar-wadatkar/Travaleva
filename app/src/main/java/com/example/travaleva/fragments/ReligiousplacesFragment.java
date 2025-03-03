package com.example.travaleva.fragments;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travaleva.R;
import com.example.travaleva.adapter.PlaceAdapter;
import com.example.travaleva.model.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReligiousplacesFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private List<Place> placeList;

    public ReligiousplacesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_religiousplaces, container, false);

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        placeList = new ArrayList<>();
        placeAdapter = new PlaceAdapter(getActivity(), placeList);
        recyclerView.setAdapter(placeAdapter);

        // Retrieve data from Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Places_details");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                placeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    if (place != null && place.getCategories() != null && place.getCategories().contains("Religious Places")) {
                        // Add only places with category containing "Religious Places"
                        placeList.add(place);
                    }
                }
                placeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error retrieving data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });

        // Adapter item click listener
//        placeAdapter.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
//            @Override
//            public void onViewDetailsClick(Place place) {
//                fetchLocationFromPlaceName(place.getTitle());
//            }
//        });

        return rootView;
    }

    private void fetchLocationFromPlaceName(String placeName) {
        Geocoder geocoder = new Geocoder(getContext());
        try {
            List<Address> addresses = geocoder.getFromLocationName(placeName, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                // Open the location in Google Maps
                openMapWithLocation(latitude, longitude, placeName);
            } else {
                Toast.makeText(getContext(), "Location not found for " + placeName, Toast.LENGTH_SHORT).show();
                Log.e("ReligiousplacesFragment", "Geocoder returned no results for: " + placeName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error fetching location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openMapWithLocation(double latitude, double longitude, String placeName) {
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + Uri.encode(placeName));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(getActivity(), "Google Maps is not installed", Toast.LENGTH_SHORT).show();
        }
    }

}
