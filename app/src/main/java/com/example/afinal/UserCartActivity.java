package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserCartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_cart);
        Button btn=findViewById(R.id.order_confirm_button);
        btn.setOnClickListener(v->{
            Toast.makeText(UserCartActivity.this, "Order Confirmed", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(UserCartActivity.this, FinalOrderActivity.class);
            startActivity(intent);
        });

    }
}