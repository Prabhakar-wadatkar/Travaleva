package com.example.travaleva;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.travaleva.fragments.CategoryFragment;
import com.example.travaleva.fragments.FlightSearchFragment;
import com.example.travaleva.fragments.HomeFragment;
import com.example.travaleva.fragments.MapFragment;

import com.example.travaleva.fragments.HotelsFragment;
import com.example.travaleva.fragments.ProfileFragment;
import com.example.travaleva.fragments.SearchResultsFragment;
import com.example.travaleva.model.Place;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText searchEditText;
    private TextInputLayout searchLayout;
    private DatabaseReference placesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Day (Light) Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Database reference
        placesRef = FirebaseDatabase.getInstance().getReference("Places_details");

        // Initialize search components
        searchEditText = findViewById(R.id.searchEditText);
        searchLayout = findViewById(R.id.searchLayout);

        // Set up search functionality
        setupSearch();

        // Load HomeFragment as the default fragment
        loadFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.category) {
                selectedFragment = new CategoryFragment();
            } else if (item.getItemId() == R.id.map) {
                selectedFragment = new MapFragment();
//            } else if (item.getItemId() == R.id.flights) {
//                selectedFragment = new FlightSearchFragment();
            } else if (item.getItemId() == R.id.hotels) {
                selectedFragment = new HotelsFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
        // Get the user profile ImageView and set an OnClickListener
        ImageView userProfile = findViewById(R.id.user_profile);
        userProfile.setOnClickListener(v -> {
            // When the profile icon is clicked, open ProfileFragment
            openProfileFragment();
        });
    }

    // Method to set up search functionality
    private void setupSearch() {
        // Handle search action when the user presses the search button on the keyboard
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        // Handle search box click
        searchEditText.setOnClickListener(v -> {
            // Open SearchResultsFragment with an empty query
            openSearchResultsFragment("");
        });

        // Handle clear text icon click
        searchLayout.setEndIconOnClickListener(v -> {
            searchEditText.setText(""); // Clear the search box
        });
    }

    // Method to perform search
    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (!query.isEmpty()) {
            // Query Firebase for places matching the searched city name
            searchPlacesByCity(query);
        } else {
            Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchPlacesByCity(String cityName) {
        Query query = placesRef.orderByChild("city").equalTo(cityName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Place> searchResults = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Place place = snapshot.getValue(Place.class);
                    if (place != null) {
                        place.setId(snapshot.getKey()); // Set the Firebase key as the place ID
                        searchResults.add(place);
                    }
                }

                if (searchResults.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No places found for " + cityName, Toast.LENGTH_SHORT).show();
                } else {
                    // Open SearchResultsFragment with the search results
                    openSearchResultsFragment(cityName, searchResults);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to search: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to open SearchResultsFragment with search results
    private void openSearchResultsFragment(String query, List<Place> searchResults) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString("query", query); // Pass the search query
        args.putSerializable("searchResults", new ArrayList<>(searchResults)); // Pass the search results
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Overloaded method to open SearchResultsFragment with just the query
    private void openSearchResultsFragment(String query) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString("query", query); // Pass the search query
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Method to load fragments
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    // Method to open ProfileFragment
    private void openProfileFragment() {
        ProfileFragment profileFragment = new ProfileFragment();
        loadFragment(profileFragment);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Helper method to clear focus from the search box
    private void clearSearchFocus() {
        searchEditText.clearFocus();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view != null) {
                // Check if the touch event is outside the search box
                if (!isPointInsideView(event.getRawX(), event.getRawY(), searchEditText)) {
                    hideKeyboard();
                    clearSearchFocus();
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    // Helper method to check if a point is inside a view
    private boolean isPointInsideView(float x, float y, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        // Check if the point is within the view's bounds
        return (x > viewX && x < (viewX + view.getWidth())) &&
                (y > viewY && y < (viewY + view.getHeight()));
    }

//    @Override
//    public void onFlightSearchFragmentLoaded() {
//        // Hide the search bar when FlightSearchFragment is loaded
//        searchLayout.setVisibility(View.GONE);
//    }

//    @Override
//    public void onFlightSearchFragmentDetached() {
//        // Show the search bar when FlightSearchFragment is detached
//        searchLayout.setVisibility(View.VISIBLE);
//    }
}