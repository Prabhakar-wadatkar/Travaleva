package com.example.travaleva.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.travaleva.R;
import com.example.travaleva.adapter.HotelAdapter;
import com.example.travaleva.model.Hotel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class HotelsFragment extends Fragment {

    private RecyclerView hotelsRecyclerView;
    private HotelAdapter hotelAdapter;
    private List<Hotel> hotelList;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotels, container, false);
        hotelsRecyclerView = view.findViewById(R.id.hotelsRecyclerView);
        hotelsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        hotelList = new ArrayList<>();
        hotelAdapter = new HotelAdapter(hotelList, getContext());
        hotelsRecyclerView.setAdapter(hotelAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchHotelsFromFirebase();
    }

    private void fetchHotelsFromFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("hotels");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hotelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Hotel hotel = dataSnapshot.getValue(Hotel.class);
                    if (hotel != null) {
                        hotel.setHotelId(dataSnapshot.getKey());
                        hotelList.add(hotel);
                    }
                }
                hotelAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}