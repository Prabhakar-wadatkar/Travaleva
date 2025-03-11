package com.example.travaleva.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.travaleva.R;
import com.example.travaleva.adapter.RecentsAdapter;
import com.example.travaleva.adapter.SliderAdapter;
import com.example.travaleva.adapter.TopPlacesAdapter;
import com.example.travaleva.model.Place;
import com.example.travaleva.model.RecentsData;
import com.example.travaleva.model.TopPlacesData;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private ProgressBar loader;
    private RecyclerView recentRecycler, topPlacesRecycler;
    private RecentsAdapter recentsAdapter;
    private TopPlacesAdapter topPlacesAdapter;
    private ViewPager2 viewPager;
    private Handler sliderHandler;
    private List<Integer> images;
    private DatabaseReference recentPlacesRef, topPlacesRef;
    private ValueEventListener recentDataListener, topPlacesListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI components
        loader = view.findViewById(R.id.loader);
        viewPager = view.findViewById(R.id.viewPager);
        recentRecycler = view.findViewById(R.id.recent_recycler);
        topPlacesRecycler = view.findViewById(R.id.top_places_recycler);

        // Show loader
        loader.setVisibility(View.VISIBLE);

        // Set up ViewPager
        images = Arrays.asList(
                R.drawable.main_slider_img3,
                R.drawable.main_slider_img2,
                R.drawable.main_slider_img1,
                R.drawable.main_slider_img4
        );
        SliderAdapter adapter = new SliderAdapter(requireContext(), images);
        viewPager.setAdapter(adapter);
        startAutoScroll();

        // Initialize Firebase Database references
        recentPlacesRef = FirebaseDatabase.getInstance().getReference("Places_details");
        topPlacesRef = FirebaseDatabase.getInstance().getReference("Places_details");

        // Fetch data
        fetchRecentData();
        fetchTopPlacesData();

        // Set click listeners for category cards
        MaterialCardView category1 = view.findViewById(R.id.category1);
        MaterialCardView category2 = view.findViewById(R.id.category2);
        MaterialCardView category3 = view.findViewById(R.id.category3);
        MaterialCardView category4 = view.findViewById(R.id.category4);

        category1.setOnClickListener(v -> openFragment(new ReligiousplacesFragment()));
        category2.setOnClickListener(v -> openFragment(new WaterfallFragment()));
        category3.setOnClickListener(v -> openFragment(new FortsFragment()));
        category4.setOnClickListener(v -> openFragment(new HillstationFragment()));

        return view;
    }

    // Helper method to open a fragment
    private void openFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void startAutoScroll() {
        sliderHandler = new Handler(Looper.getMainLooper());
        sliderHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (viewPager != null) {
                    int currentItem = viewPager.getCurrentItem();
                    int nextItem = (currentItem + 1) % images.size();
                    viewPager.setCurrentItem(nextItem, true);
                    sliderHandler.postDelayed(this, 3000);
                }
            }
        }, 3000);
    }

    private void setRecentRecycler(List<RecentsData> recentsDataList) {
        if (recentsDataList.isEmpty()) {
            Toast.makeText(getContext(), "No recent places found.", Toast.LENGTH_SHORT).show();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recentRecycler.setLayoutManager(layoutManager);
        recentsAdapter = new RecentsAdapter(getContext(), recentsDataList);
        recentRecycler.setAdapter(recentsAdapter);

        // Hide loader when data is loaded
        loader.setVisibility(View.GONE);
    }

    private void setTopPlacesRecycler(List<TopPlacesData> topPlacesDataList) {
        if (topPlacesDataList.isEmpty()) {
            Toast.makeText(getContext(), "No top places found.", Toast.LENGTH_SHORT).show();
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        topPlacesRecycler.setLayoutManager(layoutManager);
        topPlacesAdapter = new TopPlacesAdapter(getContext(), topPlacesDataList);

        // Set click listener for top places
        topPlacesAdapter.setOnItemClickListener(new TopPlacesAdapter.OnItemClickListener() {
            @Override
            public void onViewDetailsClick(TopPlacesData topPlace) {
                showPlaceDetails(topPlace);
            }

            @Override
            public void onBookPlaceClick(TopPlacesData topPlace) {
                openBookingFragment(topPlace);
            }
        });

        topPlacesRecycler.setAdapter(topPlacesAdapter);

        // Hide loader when data is loaded
        loader.setVisibility(View.GONE);
    }

    private void showPlaceDetails(TopPlacesData topPlace) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(topPlace.getTitle());
        builder.setMessage("City: " + topPlace.getCity() + "\nCharges: " + topPlace.getCharges());
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void openBookingFragment(TopPlacesData topPlace) {
        Place place = new Place();
        place.setTitle(topPlace.getTitle());
        place.setCity(topPlace.getCity());
        place.setCharges(topPlace.getCharges());
        place.setImageUrl(topPlace.getImageUrl());

        BookingFragment bookingFragment = BookingFragment.newInstance(place);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, bookingFragment)
                .addToBackStack(null)
                .commit();
    }

    private void fetchRecentData() {
        Query query = recentPlacesRef.orderByKey().limitToLast(5);
        recentDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<RecentsData> recentsDataList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RecentsData recentsData = snapshot.getValue(RecentsData.class);
                    if (recentsData != null) {
                        recentsDataList.add(recentsData);
                    }
                }
                Collections.reverse(recentsDataList);
                setRecentRecycler(recentsDataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loader.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to load recent data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        query.addValueEventListener(recentDataListener);
    }

    private void fetchTopPlacesData() {
        topPlacesListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<TopPlacesData> topPlacesDataList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TopPlacesData topPlacesData = snapshot.getValue(TopPlacesData.class);
                    if (topPlacesData != null) {
                        topPlacesDataList.add(topPlacesData);
                    }
                }
                Collections.shuffle(topPlacesDataList);
                setTopPlacesRecycler(topPlacesDataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loader.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to load top places: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        topPlacesRef.addValueEventListener(topPlacesListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up Firebase listeners
        if (recentDataListener != null) {
            recentPlacesRef.removeEventListener(recentDataListener);
        }
        if (topPlacesListener != null) {
            topPlacesRef.removeEventListener(topPlacesListener);
        }
        // Stop the auto-scroll handler
        if (sliderHandler != null) {
            sliderHandler.removeCallbacksAndMessages(null);
        }
    }
}