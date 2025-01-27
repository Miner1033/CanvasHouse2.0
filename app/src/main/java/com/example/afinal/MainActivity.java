package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Find the button and set a click listener
        Button button = findViewById(R.id.buttonStart);
        button.setOnClickListener(v -> {
            // Show a Toast message when the button is clicked
            Toast.makeText(MainActivity.this, "Button clicked!", Toast.LENGTH_SHORT).show();

            // Start the SignInActivity
            Intent intent = new Intent(MainActivity.this, SignInScreen.class);
            startActivity(intent);
        });
    }
}
