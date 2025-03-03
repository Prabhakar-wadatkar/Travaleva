package com.example.travaleva;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.travaleva.fragments.CategoryFragment;
import com.example.travaleva.fragments.HomeFragment;
import com.example.travaleva.fragments.MapFragment;
import com.example.travaleva.fragments.FlightsFragment;
import com.example.travaleva.fragments.HotelsFragment;
import com.example.travaleva.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Day (Light) Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        // Load HomeFragment as the default fragment
        loadFragment(new HomeFragment());

        // Set up BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.category) {
                selectedFragment = new CategoryFragment();
            } else if (item.getItemId() == R.id.map) {
                selectedFragment = new MapFragment();
            } else if (item.getItemId() == R.id.flights) {
                selectedFragment = new FlightSearchFragment();
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
}
