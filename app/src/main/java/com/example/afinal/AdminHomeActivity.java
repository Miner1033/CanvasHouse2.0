package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_home);
        Button btnInsert = findViewById(R.id.insert_btn);
        Button btnUpdate= findViewById(R.id.update_btn);
        Button btnDelete = findViewById(R.id.delete_btn);
        Button btnView = findViewById(R.id.view_btn);
        btnInsert.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, InsertProductActivity.class);
            startActivity(intent);
        });
        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, UpdateProductActivity.class);
            startActivity(intent);
        });
        btnDelete.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, DeleteProductActivity.class);
            startActivity(intent);
        });
        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, ViewProductActivity.class);
            startActivity(intent);
        });

    }
}