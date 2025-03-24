package com.example.travaleva.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.travaleva.R;
import com.google.android.material.card.MaterialCardView;

public class CityWisePlacesFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static CityWisePlacesFragment newInstance(String param1, String param2) {
        CityWisePlacesFragment fragment = new CityWisePlacesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CityWisePlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_wise_places, container, false);

        // Find card views
        MaterialCardView cardAmravati = view.findViewById(R.id.category1);
        MaterialCardView cardNagpur = view.findViewById(R.id.category2);
        MaterialCardView cardShegaon = view.findViewById(R.id.category3); // Third card
        MaterialCardView cardChikhaldara = view.findViewById(R.id.category4);

        // Set click listeners to open CityPlacesFragment
        cardAmravati.setOnClickListener(v -> openCityPlacesFragment("Amravati"));
        cardNagpur.setOnClickListener(v -> openCityPlacesFragment("Nagpur"));
        cardShegaon.setOnClickListener(v -> openCityPlacesFragment("Shegaon")); // Shegaon on third card
        cardChikhaldara.setOnClickListener(v -> openCityPlacesFragment("Chikhaldara"));

        return view;
    }

    private void openCityPlacesFragment(String city) {
        CityPlacesFragment fragment = CityPlacesFragment.newInstance(city);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}