package com.example.afinal;

import android.content.Intent; // Importing Intent for starting new activities
import android.database.Cursor; // Importing Cursor for fetching data from the database
import android.os.Bundle; // Importing Bundle to handle saved instance states
import android.view.View; // Importing View to handle user interactions
import android.widget.Button; // Importing Button for handling button clicks
import android.widget.ListView; // Importing ListView to display product list
import android.widget.Toast; // Importing Toast for displaying messages

import androidx.appcompat.app.AppCompatActivity; // Importing AppCompatActivity to use basic activity features

public class ViewProductActivity extends AppCompatActivity {
    private ListView listViewProducts; // Declaring a ListView to display products
    private DataCenter databaseCenter; // Declaring DataCenter object for database operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Calling the superclass's onCreate method
        setContentView(R.layout.activity_view_product); // Setting the layout for this activity

        // Linking UI elements with the corresponding views in the layout
        listViewProducts = findViewById(R.id.list_view_products);
        Button buttonUpdate = findViewById(R.id.button_update);
        Button buttonDelete = findViewById(R.id.button_delete);
        databaseCenter = new DataCenter(this); // Initializing the database handler

        displayProducts(); // Calling the method to display products when the activity is created

        // Setting onClickListener for the Update button
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUpdate(); // Calling the handleUpdate method when the button is clicked
            }
        });

        // Setting onClickListener for the Delete button
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDelete(); // Calling the handleDelete method when the button is clicked
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume(); // Calling the superclass's onResume method
        displayProducts(); // Refreshing the product list when the activity is resumed
    }

    // Method to display the products in the ListView
    private void displayProducts() {
        // Fetching all products from the database
        Cursor cursor = databaseCenter.getAllProducts();
        if (cursor != null && cursor.getCount() > 0) {
            // If products are found, set the ListView adapter to display them
            ProductAdapterClass adapter = new ProductAdapterClass(this, cursor, 0);
            listViewProducts.setAdapter(adapter); // Setting the adapter to the ListView
        } else {
            // If no products are available, show a Toast message
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle the update action
    private void handleUpdate() {
        // Starting the UpdateProductActivity for updating a product
        Intent intent = new Intent(ViewProductActivity.this, UpdateProductActivity.class);
        startActivity(intent); // Launching the UpdateProductActivity
    }

    // Method to handle the delete action
    private void handleDelete() {
        // Starting the DeleteProductActivity for deleting a product
        Intent intent = new Intent(ViewProductActivity.this, DeleteProductActivity.class);
        startActivity(intent); // Launching the DeleteProductActivity
        Toast.makeText(this, "Delete button clicked", Toast.LENGTH_SHORT).show(); // Showing a Toast message indicating delete
    }
}
