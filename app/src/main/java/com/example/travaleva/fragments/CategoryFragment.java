package com.example.travaleva.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travaleva.R;
import com.example.travaleva.adapter.CategoryAdapter;
import com.example.travaleva.model.Category;

import java.util.ArrayList;
import java.util.List;

import com.example.travaleva.fragments.CategoryClickListener;

public class CategoryFragment extends Fragment implements CategoryClickListener {


    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Find the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("FORTS", R.drawable.fort_image));
        categories.add(new Category("WATERFALLS", R.drawable.waterfall_image));
        categories.add(new Category("HILL STATIONS", R.drawable.hill_station_image));
        categories.add(new Category("BEACHES", R.drawable.beach_image));
        categories.add(new Category("RELIGIOUS PLACES", R.drawable.religious_place_image));

        categoryAdapter = new CategoryAdapter(categories, this);
        recyclerView.setAdapter(categoryAdapter);

        return view;
    }

    @Override
    public void onCategoryClick(String categoryName) {
        if (categoryName.equals("FORTS")) {
            openFragment(new FortsFragment());
        } else if (categoryName.equals("WATERFALLS")) {
            openFragment(new WaterfallFragment());
        } else if (categoryName.equals("HILL STATIONS")) {
            openFragment(new HillstationFragment());
        } else if (categoryName.equals("BEACHES")) {
            openFragment(new BeachesFragment());
        } else if (categoryName.equals("RELIGIOUS PLACES")) {
            openFragment(new ReligiousplacesFragment());
        }
    }


    private void openFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment) // Replace with your fragment container ID
                .addToBackStack(null)
                .commit();
    }
}