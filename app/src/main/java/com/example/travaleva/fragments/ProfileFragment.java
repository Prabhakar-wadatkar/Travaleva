package com.example.travaleva.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.example.travaleva.LoginActivity;
import com.example.travaleva.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private ImageView profilePicture;
    private DatabaseReference userRef;
    private StorageReference storageRef;
    private String userId;
    private ActivityResultLauncher<String> imagePickerLauncher;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Storage and Database
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");
        userRef = FirebaseDatabase.getInstance("https://cryptowallet-12b45-default-rtdb.firebaseio.com/").getReference("TravelEvaUsers");

        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                uploadImageToFirebase(uri);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // Find UI elements
        profilePicture = view.findViewById(R.id.profile_picture);
        TextView profileName = view.findViewById(R.id.profile_name);
        TextView profileEmail = view.findViewById(R.id.profile_email);

        // Fetch user data from SharedPreferences
        userId = sharedPreferences.getString("userId", null);
        String userName = sharedPreferences.getString("name", "John Doe");
        String userEmail = sharedPreferences.getString("email", "johndoe@example.com");
        String profileImageUrl = sharedPreferences.getString("profileImageUrl", null);

        // Set name and email
        profileName.setText(userName);
        profileEmail.setText(userEmail);

        // Load profile image
        if (profileImageUrl != null) {
            // Load from SharedPreferences if available
            Glide.with(this).load(profileImageUrl).into(profilePicture);
        } else if (userId != null) {
            // Fetch from Firebase if not in SharedPreferences
            fetchProfileImageFromDatabase();
        }

        // Set OnClickListener for profile picture to update image
        profilePicture.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        // Logout button
        MaterialButton logoutButton = view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            clearUserData();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        // Place ticket card
        MaterialCardView placeTicketCard = view.findViewById(R.id.placeTicket);
        placeTicketCard.setOnClickListener(v -> openTicketFragment("place"));

        return view;
    }

    // Fetch profile image from Firebase Database if not in SharedPreferences
    private void fetchProfileImageFromDatabase() {
        userRef.child(userId).child("profileImageUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String imageUrl = snapshot.getValue(String.class);
                if (imageUrl != null) {
                    // Load image into ImageView
                    Glide.with(ProfileFragment.this).load(imageUrl).into(profilePicture);
                    // Cache in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("profileImageUrl", imageUrl);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Error loading profile image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Open TicketFragment
    private void openTicketFragment(String ticketType) {
        TicketFragment ticketFragment = new TicketFragment();
        Bundle args = new Bundle();
        args.putString("ticketType", ticketType);
        ticketFragment.setArguments(args);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, ticketFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Clear user data from SharedPreferences
    private void clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Upload selected image to Firebase Storage
    private void uploadImageToFirebase(Uri imageUri) {
        if (userId == null) {
            Toast.makeText(getContext(), "User ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference fileRef = storageRef.child(userId + "_profile.jpg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    updateProfileImageInDatabase(downloadUrl);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("profileImageUrl", downloadUrl);
                    editor.apply();
                    Glide.with(this).load(downloadUrl).into(profilePicture);
                    Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
                }))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Update profile image URL in Firebase Database
    private void updateProfileImageInDatabase(String imageUrl) {
        if (userId != null) {
            userRef.child(userId).child("profileImageUrl").setValue(imageUrl)
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to update database: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}