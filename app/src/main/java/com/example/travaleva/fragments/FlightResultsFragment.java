package com.example.travaleva.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travaleva.R;
import com.example.travaleva.adapter.FlightAdapter;
import com.example.travaleva.model.Flight;

import java.util.ArrayList;
import java.util.List;

public class FlightResultsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FlightAdapter flightAdapter;
    private List<Flight> matchingFlights;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_results, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrieve matching flights from arguments
        if (getArguments() != null) {
            matchingFlights = getArguments().getParcelableArrayList("matchingFlights");
        } else {
            matchingFlights = new ArrayList<>();
        }

        // Set up the adapter
        flightAdapter = new FlightAdapter(matchingFlights);
        recyclerView.setAdapter(flightAdapter);

        return view;
    }
}