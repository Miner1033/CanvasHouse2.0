package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewAsUserActivity extends AppCompatActivity {

    private Button goToViewProductButton;
    private ImageView ivmcart, ivaccount, ivinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_as_user);

        // Initialize the button
        goToViewProductButton = findViewById(R.id.button_latest_product);

        // Initialize ImageView variables
        ivmcart = findViewById(R.id.image_view_cart);
        ivaccount = findViewById(R.id.image_view_account);
        ivinfo = findViewById(R.id.image_view_about_us);

        // Set the button click listener to navigate to ViewProductActivity
        goToViewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start ViewProductActivity
                Intent intent = new Intent(ViewAsUserActivity.this, ViewProductActivity.class);
                startActivity(intent);
            }
        });

        // Set click listeners for ImageView components
        ivmcart.setOnClickListener(v -> {
            Intent intent = new Intent(ViewAsUserActivity.this, UserCartActivity.class);
            startActivity(intent);
        });

        ivaccount.setOnClickListener(v -> {
            Intent intent = new Intent(ViewAsUserActivity.this, ProfileScreen.class);
            startActivity(intent);
        });

        ivinfo.setOnClickListener(v -> {
            Intent intent = new Intent(ViewAsUserActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });
    }
}
