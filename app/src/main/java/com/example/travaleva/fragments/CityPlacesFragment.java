package com.example.travaleva.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.travaleva.R;
import com.example.travaleva.adapter.PlaceAdapter;
import com.example.travaleva.model.Place;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class CityPlacesFragment extends Fragment {

    private static final String ARG_CITY = "city";
    private static final String TAG = "CityPlacesFragment";

    private String city;
    private RecyclerView recyclerView;
    private PlaceAdapter placeAdapter;
    private List<Place> placeList;

    public CityPlacesFragment() {
        // Required empty public constructor
    }

    public static CityPlacesFragment newInstance(String city) {
        CityPlacesFragment fragment = new CityPlacesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            city = getArguments().getString(ARG_CITY);
            Log.d(TAG, "City passed to fragment: " + city);
        }
        placeList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_city_places, container, false);

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.cityPlacesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        placeAdapter = new PlaceAdapter(getActivity(), placeList);
        recyclerView.setAdapter(placeAdapter);

        // Retrieve data from Firebase
        fetchPlacesForCity();

        return rootView;
    }

    private void fetchPlacesForCity() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://cryptowallet-12b45-default-rtdb.firebaseio.com/")
                .getReference("Places_details");
        databaseReference.orderByChild("city").equalTo(city).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                placeList.clear();
                Log.d(TAG, "Query results for " + city + ": " + dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    if (place != null) {
                        place.setId(snapshot.getKey());
                        Log.d(TAG, "Found place: " + place.getTitle() + ", City: " + place.getCity());
                        placeList.add(place);
                    } else {
                        Log.d(TAG, "Failed to parse place for key: " + snapshot.getKey());
                    }
                }
                placeAdapter.updatePlaceList(placeList);
                if (placeList.isEmpty()) {
                    Toast.makeText(getContext(), "No places found for " + city, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No places matched query for: " + city);
                } else {
                    Log.d(TAG, "Places found: " + placeList.size());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error retrieving data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }
}