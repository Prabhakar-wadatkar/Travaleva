package com.example.travaleva;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    // Firebase Realtime Database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Day (Light) Theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("TravelEvaUsers");

        // Find the "Already registered?" TextView
        TextView alreadyRegisteredText = findViewById(R.id.alreadyRegistered);

        // Navigate to LoginActivity
        alreadyRegisteredText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Register Button OnClickListener
        MaterialButton registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            // Retrieve input fields
            String name = ((TextInputEditText) findViewById(R.id.name)).getText().toString();
            String email = ((TextInputEditText) findViewById(R.id.username)).getText().toString();
            String password = ((TextInputEditText) findViewById(R.id.password)).getText().toString();
            String confirmPassword = ((TextInputEditText) findViewById(R.id.password2)).getText().toString();

            // Validate form fields
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate email format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(RegisterActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate password and confirm password
            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save user data to Firebase Realtime Database
            String userId = databaseReference.push().getKey(); // Generate unique ID
            User user = new User(userId, name, email, password);

            if (userId != null) {
                databaseReference.child(userId).setValue(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            // Navigate to LoginActivity
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Close RegisterActivity
                        })
                        .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}

// User model class
class User {
    public String userId;
    public String name;
    public String email;
    public String password;

    // Default constructor required for Firebase
    public User() {}

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
