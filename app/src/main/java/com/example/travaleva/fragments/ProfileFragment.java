package com.example.travaleva.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.travaleva.LoginActivity;
import com.example.travaleva.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class ProfileFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize SharedPreferences to fetch the logged-in user's data
        sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // Find the TextViews for Name and Email
        TextView profileName = view.findViewById(R.id.profile_name);
        TextView profileEmail = view.findViewById(R.id.profile_email);

        // Fetch the user's information from SharedPreferences
        String userName = sharedPreferences.getString("name", "John Doe"); // Retrieve user name (set default value)
        String userEmail = sharedPreferences.getString("email", "johndoe@example.com"); // Retrieve user email (default)

        // Set the user's name and email to the TextViews
        profileName.setText(userName);
        profileEmail.setText(userEmail);

        // Find the logout button and set an OnClickListener
        MaterialButton logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            // Clear user data from SharedPreferences
            clearUserData();

            // Navigate to LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

            // Close current activity
            requireActivity().finish();
        });

        // Find the cards for placeTicket, flightTicket, and hotelTicket
        MaterialCardView placeTicketCard = view.findViewById(R.id.placeTicket);
        MaterialCardView flightTicketCard = view.findViewById(R.id.flightsTicket);
        MaterialCardView hotelTicketCard = view.findViewById(R.id.hotelTicket);

        // Set onClickListeners for the cards
        placeTicketCard.setOnClickListener(v -> openTicketFragment("place"));
        flightTicketCard.setOnClickListener(v -> openTicketFragment("flight"));
        hotelTicketCard.setOnClickListener(v -> openTicketFragment("hotel"));

        return view;
    }

    // Method to open TicketFragment and pass necessary data
    private void openTicketFragment(String ticketType) {
        TicketFragment ticketFragment = new TicketFragment();

        // Pass data using arguments
        Bundle args = new Bundle();
        args.putString("ticketType", ticketType);
        ticketFragment.setArguments(args);

        // Begin the fragment transaction
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, ticketFragment);
        transaction.addToBackStack(null); // Allows back navigation
        transaction.commit();
    }


    // Method to clear the user data from SharedPreferences
    private void clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
