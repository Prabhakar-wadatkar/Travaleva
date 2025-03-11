package com.example.travaleva.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.travaleva.BuildConfig;
import com.example.travaleva.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.android.PolyUtil;

import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapFragment";
    private static final float DEFAULT_ZOOM = 15f;
    private static final int POLYLINE_WIDTH = 12;

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private GeoApiContext geoApiContext;

    private Polyline currentRoute;
    private LatLng lastKnownLocation;

    private final ActivityResultLauncher<String> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) enableLocationFeatures();
                else showToast(getString(R.string.location_permission_denied));
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initializeDependencies();
        setupUI(view);
        initializeMap();
        return view;
    }

    private void initializeDependencies() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        geoApiContext = new GeoApiContext.Builder()
                .apiKey(BuildConfig.MAPS_API_KEY)
                .build();
    }

    private void setupUI(View view) {
        view.findViewById(R.id.fab_current_location).setOnClickListener(v -> moveToCurrentLocation());
        view.findViewById(R.id.fab_directions).setOnClickListener(v -> showDirectionsDialog());
        view.findViewById(R.id.fab_map_type).setOnClickListener(v -> showMapTypeSelector());
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        configureMap();
        checkLocationPermission();
    }

    private void configureMap() {
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.setOnMarkerClickListener(this);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableLocationFeatures();
        } else {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void enableLocationFeatures() {
        try {
            googleMap.setMyLocationEnabled(true);
            getDeviceLocation();
        } catch (SecurityException e) {
            Log.e(TAG, "Error enabling location features: " + e.getMessage());
        }
    }

    private void getDeviceLocation() {
        try {
            fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    lastKnownLocation = new LatLng(
                            task.getResult().getLatitude(),
                            task.getResult().getLongitude()
                    );
                    moveToCurrentLocation();
                }
            });
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException: " + e.getMessage());
        }
    }

    private void moveToCurrentLocation() {
        if (lastKnownLocation != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastKnownLocation, DEFAULT_ZOOM));
        }
    }

    private void showDirectionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_directions, null);
        TextInputEditText etDestination = dialogView.findViewById(R.id.et_destination);

        builder.setView(dialogView)
                .setPositiveButton("Route", (dialog, which) -> {
                    String destination = etDestination.getText().toString();
                    if (!destination.isEmpty()) calculateRoute(destination);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void calculateRoute(String destination) {
        if (lastKnownLocation == null) {
            showToast(getString(R.string.current_location_unavailable));
            return;
        }

        Log.d(TAG, "Calculating route from: " + lastKnownLocation + " to: " + destination);

        new Thread(() -> {
            try {
                DirectionsResult result = new DirectionsApiRequest(geoApiContext)
                        .origin(new com.google.maps.model.LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude))
                        .destination(destination)
                        .mode(TravelMode.DRIVING)
                        .await();

                requireActivity().runOnUiThread(() -> {
                    if (result == null || result.routes == null || result.routes.length == 0) {
                        Log.e(TAG, "No routes found in the result");
                        showToast(getString(R.string.no_route_found));
                        return;
                    }
                    drawRoute(result);
                });
            } catch (Exception e) {
                Log.e(TAG, "Directions API error: " + e.getMessage());
                requireActivity().runOnUiThread(() -> showToast(getString(R.string.directions_failed)));
            }
        }).start();
    }

    private void drawRoute(DirectionsResult result) {
        try {
            if (currentRoute != null) currentRoute.remove();

            if (result.routes[0].overviewPolyline == null) {
                Log.e(TAG, "Overview polyline is null");
                showToast(getString(R.string.invalid_route));
                return;
            }

            List<LatLng> path = PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath());
            if (path.isEmpty()) {
                Log.e(TAG, "Decoded path is empty");
                showToast(getString(R.string.invalid_route));
                return;
            }

            currentRoute = googleMap.addPolyline(new PolylineOptions()
                    .addAll(path)
                    .color(ContextCompat.getColor(requireContext(), R.color.route_color))
                    .width(POLYLINE_WIDTH));

            LatLngBounds bounds = calculateBounds(path);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            Log.e(TAG, "Error drawing route: " + e.getMessage());
            showToast(getString(R.string.route_drawing_failed));
        }
    }

    private LatLngBounds calculateBounds(List<LatLng> points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) builder.include(point);
        return builder.build();
    }

    private void showMapTypeSelector() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.select_map_type)
                .setItems(R.array.map_types, (dialog, which) -> {
                    switch (which) {
                        case 0: googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); break;
                        case 1: googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); break;
                        case 2: googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); break;
                    }
                }).show();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        showToast(marker.getTitle());
        return true;
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}