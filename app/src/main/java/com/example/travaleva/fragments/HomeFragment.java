package com.example.travaleva.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

        return view;
    }

    private void startAutoScroll() {
        sliderHandler = new Handler(Looper.getMainLooper());
        sliderHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = (currentItem + 1) % images.size();
                viewPager.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 3000);
            }
        }, 3000);
    }

    private void setRecentRecycler(List<RecentsData> recentsDataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recentRecycler.setLayoutManager(layoutManager);
        recentsAdapter = new RecentsAdapter(getContext(), recentsDataList);
        recentRecycler.setAdapter(recentsAdapter);

        // Hide loader when data is loaded
        loader.setVisibility(View.GONE);
    }

    private void setTopPlacesRecycler(List<TopPlacesData> topPlacesDataList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        topPlacesRecycler.setLayoutManager(layoutManager);
        topPlacesAdapter = new TopPlacesAdapter(getContext(), topPlacesDataList);

        // Set click listener for top places
        topPlacesAdapter.setOnItemClickListener(topPlace -> {
            // Convert TopPlacesData to Place (assuming similar structure)
            Place place = new Place();
            place.setTitle(topPlace.getTitle());
            place.setCity(topPlace.getCity());
            place.setCharges(topPlace.getCharges());
            place.setImageUrl(topPlace.getImageUrl());
            // Add other necessary fields...

            // Open BookingFragment with the clicked place
            BookingFragment bookingFragment = BookingFragment.newInstance(place);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, bookingFragment)
                    .addToBackStack(null)
                    .commit();
        });

        topPlacesRecycler.setAdapter(topPlacesAdapter);

        // Hide loader when data is loaded
        loader.setVisibility(View.GONE);
    }

    private void fetchRecentData() {
        Query query = recentPlacesRef.orderByKey().limitToLast(5);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
            public void onCancelled(DatabaseError databaseError) {
                loader.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to load recent data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchTopPlacesData() {
        topPlacesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
            public void onCancelled(DatabaseError databaseError) {
                loader.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to load top places.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}