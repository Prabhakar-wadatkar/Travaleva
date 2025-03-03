package com.example.travaleva.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travaleva.R;
import com.example.travaleva.adapter.FlightAdapter;
import com.example.travaleva.model.Flight;

import java.util.ArrayList;
import java.util.List;

public class FlightsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FlightAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flights, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Flight> flightList = new ArrayList<>();
        flightList.add(new Flight(R.drawable.airline_logo1, "Air India", "08:30", "10:50", "2h 20m", "₹5,422"));
        flightList.add(new Flight(R.drawable.airline_logo2, "IndiGo", "02:00", "04:20", "2h 20m", "₹5,499"));
        flightList.add(new Flight(R.drawable.airline_logo1, "Air India", "08:30", "10:50", "2h 20m", "₹5,422"));
        flightList.add(new Flight(R.drawable.airline_logo2, "IndiGo", "02:00", "04:20", "2h 20m", "₹5,499"));
        flightList.add(new Flight(R.drawable.airline_logo1, "Air India", "08:30", "10:50", "2h 20m", "₹5,422"));
        flightList.add(new Flight(R.drawable.airline_logo2, "IndiGo", "02:00", "04:20", "2h 20m", "₹5,499"));
        flightList.add(new Flight(R.drawable.airline_logo1, "Air India", "08:30", "10:50", "2h 20m", "₹5,422"));
        flightList.add(new Flight(R.drawable.airline_logo2, "IndiGo", "02:00", "04:20", "2h 20m", "₹5,499"));

        // Add more flights with their respective details

        adapter = new FlightAdapter(flightList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}