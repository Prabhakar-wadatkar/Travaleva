package com.example.travaleva;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"); // Enhanced regex pattern

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_register);

        databaseReference = FirebaseDatabase.getInstance().getReference("TravelEvaUsers");

        TextView alreadyRegisteredText = findViewById(R.id.alreadyRegistered);
        alreadyRegisteredText.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        MaterialButton registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            String name = ((TextInputEditText) findViewById(R.id.name)).getText().toString().trim();
            String email = ((TextInputEditText) findViewById(R.id.username)).getText().toString().trim();
            String password = ((TextInputEditText) findViewById(R.id.password)).getText().toString();
            String confirmPassword = ((TextInputEditText) findViewById(R.id.password2)).getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!EMAIL_PATTERN.matcher(email).matches()) {
                Toast.makeText(RegisterActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = databaseReference.push().getKey();
            User user = new User(userId, name, email, password);

            if (userId != null) {
                databaseReference.child(userId).setValue(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}

class User {
    public String userId;
    public String name;
    public String email;
    public String password;

    public User() {}

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
