package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EndOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_end_order);

        // Find the Continue Shopping TextView by its ID
        TextView continueShoppingTextView = findViewById(R.id.continue_shopping_tv);

        // Set an onClickListener to the Continue Shopping TextView
        continueShoppingTextView.setOnClickListener(v -> {
            // Create an Intent to navigate to ViewAsUserActivity
            Intent intent = new Intent(EndOrderActivity.this, ViewAsUserActivity.class);
            startActivity(intent);  // Start the new activity
        });

        // Find the Logout TextView by its ID and set an onClickListener for logout if needed
        TextView logoutTextView = findViewById(R.id.logout_tv);
        logoutTextView.setOnClickListener(v -> {
            // Handle logout logic, like clearing user data and navigating to login screen
            Intent intent = new Intent(EndOrderActivity.this, SignInScreen.class);
            startActivity(intent);  // Navigate to LoginActivity
        });
    }
}
