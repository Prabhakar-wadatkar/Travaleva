package com.example.travaleva.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // Get the current logged-in user ID
        currentUserId = sharedPreferences.getString("userId", null);

        // Log the currentUserId for debugging
        Log.d("TicketFragment", "Current User ID: " + currentUserId);

        // Pass context and currentUserId to the adapter
        ticketAdapter = new TicketAdapter(ticketList, currentUserId, getContext());
        recyclerViewTicket.setAdapter(ticketAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check if the user is logged in
        if (currentUserId != null) {
            fetchTicketsFromFirebase(currentUserId);
        } else {
            Toast.makeText(getContext(), "You must be logged in to view tickets", Toast.LENGTH_SHORT).show();
            // Optionally, redirect the user to the login screen
            redirectToLogin();
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
                        // Check if the ticket is canceled in SharedPreferences
                        if (!sharedPreferences.contains(ticket.getTicketNumber())) {
                            ticketList.add(ticket); // Only add non-canceled tickets
                        }
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

    private void redirectToLogin() {
        // Replace with your login screen navigation logic
        Toast.makeText(getContext(), "Redirecting to login screen...", Toast.LENGTH_SHORT).show();
        // Example: Use NavController to navigate to the login fragment
        // NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        // navController.navigate(R.id.loginFragment);
    }
}