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

public class FinalOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_final_order);
        Button btn1=findViewById(R.id.bkash_btn);
        Button btn2=findViewById(R.id.nagad_btn);
        Button btn3=findViewById(R.id.dbbl_btn);
        Button btn4=findViewById(R.id.cod_btn);
        btn1.setOnClickListener(v->{
            Toast.makeText(FinalOrderActivity.this, "Payment Done ", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(FinalOrderActivity.this, EndOrderActivity.class);
            startActivity(intent);
        });
        btn2.setOnClickListener(v->{
            Toast.makeText(FinalOrderActivity.this, "Payment Done ", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(FinalOrderActivity.this, EndOrderActivity.class);
            startActivity(intent);
        });
        btn3.setOnClickListener(v->{
            Toast.makeText(FinalOrderActivity.this, "Payment Done", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(FinalOrderActivity.this, EndOrderActivity.class);
            startActivity(intent);
        });
        btn4.setOnClickListener(v->{
            Toast.makeText(FinalOrderActivity.this, "Payment Done", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(FinalOrderActivity.this, EndOrderActivity.class);
            startActivity(intent);
        });
    }
}