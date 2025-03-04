package com.example.travaleva;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton signInButton;
    private TextView notRegisteredTextView, forgotPasswordTextView;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable full-screen layout
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_login);

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("TravelEvaUsers");

        // Initialize UI components
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        signInButton = findViewById(R.id.btn_sign_in);
        notRegisteredTextView = findViewById(R.id.notRegistered);
        forgotPasswordTextView = findViewById(R.id.forgotPassword); // Forgot Password TextView

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        // Check if user is already logged in
        if (isLoggedIn()) {
            openDashboard();
        }

        // Set up the login button click listener
        signInButton.setOnClickListener(v -> loginUser());

        // Set up the "Not registered? Register here" TextView click listener
        notRegisteredTextView.setOnClickListener(v -> {
            // When clicked, open the RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Set up the "Forgot Password?" TextView click listener
        forgotPasswordTextView.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private void loginUser() {
        // Retrieve user input
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check user credentials in Firebase Realtime Database
        usersRef.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Iterate through matching users
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String dbPassword = userSnapshot.child("password").getValue(String.class);
                                if (password.equals(dbPassword)) {
                                    // Successful login
                                    String userId = userSnapshot.getKey(); // Get the user ID (or any unique identifier)
                                    String name = userSnapshot.child("name").getValue(String.class);
                                    String email = userSnapshot.child("email").getValue(String.class);

                                    // Save user data and login status to SharedPreferences
                                    saveUserData(userId, name, email);
                                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                    openDashboard();
                                    return;
                                }
                            }
                            // If no password matches
                            Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        } else {
                            // If no user found
                            Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Database error: " + databaseError.getMessage());
                        Toast.makeText(LoginActivity.this, "Database error. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showForgotPasswordDialog() {
        // Create a dialog for entering email
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_forgot_password, null);
        builder.setView(dialogView);

        EditText emailInput = dialogView.findViewById(R.id.email_input);
        MaterialButton resetButton = dialogView.findViewById(R.id.reset_button);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Handle reset button click
        resetButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the email exists in the Realtime Database
            usersRef.orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Email exists in the database
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    String userId = userSnapshot.getKey();

                                    // Show a dialog to enter a new password
                                    showResetPasswordDialog(userId);
                                    dialog.dismiss();
                                }
                            } else {
                                // Email does not exist
                                Toast.makeText(LoginActivity.this, "No user found with this email", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "Database error: " + databaseError.getMessage());
                            Toast.makeText(LoginActivity.this, "Database error. Try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void showResetPasswordDialog(String userId) {
        // Create a dialog for entering a new password
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_reset_password, null);
        builder.setView(dialogView);

        EditText newPasswordInput = dialogView.findViewById(R.id.new_password_input);
        EditText confirmPasswordInput = dialogView.findViewById(R.id.confirm_password_input);
        MaterialButton saveButton = dialogView.findViewById(R.id.save_button);

        AlertDialog dialog = builder.create();
        dialog.show();

        // Handle save button click
        saveButton.setOnClickListener(v -> {
            String newPassword = newPasswordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Please enter and confirm your new password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the password in the Realtime Database
            usersRef.child(userId).child("password").setValue(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(this, "Failed to update password. Try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void saveUserData(String userId, String name, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("userId", userId); // Save userId
        editor.putString("name", name);
        editor.putString("email", email);
        editor.apply();
    }

    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void openDashboard() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}