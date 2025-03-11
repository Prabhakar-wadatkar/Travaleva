package com.example.travaleva.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travaleva.R;
import com.example.travaleva.adapter.SearchResultsAdapter;
import com.example.travaleva.model.Place;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends Fragment {

    private RecyclerView searchResultsRecycler;
    private SearchResultsAdapter searchResultsAdapter;
    private List<Place> searchResultsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        // Initialize RecyclerView
        searchResultsRecycler = view.findViewById(R.id.searchResultsRecycler);
        searchResultsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up adapter
        searchResultsAdapter = new SearchResultsAdapter(getContext(), searchResultsList);
        searchResultsRecycler.setAdapter(searchResultsAdapter);

        // Get search query and results from arguments
        Bundle args = getArguments();
        if (args != null) {
            String query = args.getString("query", "");
            List<Place> searchResults = (List<Place>) args.getSerializable("searchResults");
            if (searchResults != null) {
                searchResultsList.clear();
                searchResultsList.addAll(searchResults);
                searchResultsAdapter.notifyDataSetChanged();
            }
        }

        return view;
    }
}