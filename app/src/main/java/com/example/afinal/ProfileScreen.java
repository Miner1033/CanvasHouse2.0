package com.example.afinal;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileScreen extends AppCompatActivity {

    // UI elements
    private TextView nameTextView, emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        // Initialize UI elements
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);

        // Display user data from Firebase
        displayUserData();
    }

    // Method to fetch and display user data
    private void displayUserData() {
        try {
            // Get the currently signed-in user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                // Retrieve and display the user's email
                String email = user.getEmail();
                String name = user.getDisplayName(); // Optional: Firebase user profile name

                // Set the data to TextViews
                nameTextView.setText(" " + (name != null ? name : "Not Available"));
                emailTextView.setText(" " + (email != null ? email : "Not Available"));
            } else {
                // No user is signed in
                Toast.makeText(this, "No user is signed in", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Handle any unexpected exceptions
            Toast.makeText(this, "Error displaying user data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
