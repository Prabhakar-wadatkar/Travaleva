package com.example.travaleva.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travaleva.R;
import com.example.travaleva.adapter.TicketAdapter;
import com.example.travaleva.model.Ticket;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TicketFragment extends Fragment {
    private RecyclerView recyclerViewTicket;
    private TicketAdapter ticketAdapter;
    private List<Ticket> ticketList;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);
        recyclerViewTicket = view.findViewById(R.id.recyclerViewTicket);
        recyclerViewTicket.setLayoutManager(new LinearLayoutManager(getContext()));
        ticketList = new ArrayList<>();
        ticketAdapter = new TicketAdapter(ticketList);
        recyclerViewTicket.setAdapter(ticketAdapter);

        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // Get the current logged-in user ID
        currentUserId = sharedPreferences.getString("userId", null);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (currentUserId != null) {
            fetchTicketsFromFirebase(currentUserId);
        } else {
            Toast.makeText(getContext(), "You must be logged in to view tickets", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchTicketsFromFirebase(String userId) {
        databaseReference = FirebaseDatabase.getInstance().getReference("bookings").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ticketList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Ticket ticket = dataSnapshot.getValue(Ticket.class);
                    if (ticket != null) {
                        ticketList.add(ticket);
                    }
                }
                ticketAdapter.notifyDataSetChanged();

                if (ticketList.isEmpty()) {
                    Toast.makeText(getContext(), "No bookings found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}