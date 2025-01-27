package com.example.afinal; // Package name for organizing the code

// Importing required classes for database operations, UI components, image handling, and app functionality
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity; // Provides compatibility support for older Android versions

// Main activity class for deleting a product
public class DeleteProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1; // Constant for potential image selection feature (not used here)

    // UI components for interacting with the user
    private EditText editTextName; // Input field for entering product name
    private TextView textViewProductPrice; // Text view to display the product price
    private TextView textViewProductQuantity; // Text view to display the product quantity
    private TextView textViewProductId; // Text view to display the product ID
    private ImageView imageViewProduct; // Image view to display the product image
    private Button buttonDelete; // Button to delete the product
    private Button buttonSearch; // Button to search for the product

    // Database helper object for managing product data
    private DataCenter databaseCenter;
    private byte[] productImageByteArray; // Byte array to store product image data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call the superclass's onCreate method
        setContentView(R.layout.activity_delete_product); // Set the layout for this activity

        // Link UI components to their corresponding views in the layout file
        editTextName = findViewById(R.id.edit_view_product_name);
        textViewProductPrice = findViewById(R.id.text_view_product_price);
        textViewProductQuantity = findViewById(R.id.text_view_product_quantity);
        textViewProductId = findViewById(R.id.text_view_product_id);
        imageViewProduct = findViewById(R.id.image_view_product);
        buttonDelete = findViewById(R.id.button_delete);
        buttonSearch = findViewById(R.id.button_search);

        // Initialize the database helper for database operations
        databaseCenter = new DataCenter(this);

        // Set a click listener for the search button to handle search operations
        buttonSearch.setOnClickListener(view -> searchProduct());

        // Set a click listener for the delete button to handle delete operations
        buttonDelete.setOnClickListener(view -> deleteProduct());
    }

    // Method to search for a product in the database
    private void searchProduct() {
        // Retrieve the product name from the input field
        String productName = editTextName.getText().toString().trim();

        // Check if the input field is empty
        if (productName.isEmpty()) {
            // Show a toast message prompting the user to enter a product name
            Toast.makeText(this, "Please enter a product name to search", Toast.LENGTH_SHORT).show();
            return; // Exit the method if no name is provided
        }

        // Query the database for the product by name
        Cursor cursor = databaseCenter.getProductByName(productName);

        // Check if the product exists in the database
        if (cursor != null && cursor.moveToFirst()) {
            // Extract product details from the cursor
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DataCenter.COL_ID)); // Get product ID
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DataCenter.COL_PRODUCT_PRICE)); // Get product price
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DataCenter.COL_PRODUCT_QUANTITY)); // Get product quantity
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DataCenter.COL_PRODUCT_IMAGE_URI)); // Get product image

            // Display product details in the corresponding text views
            textViewProductPrice.setText(String.valueOf(price));
            textViewProductQuantity.setText(String.valueOf(quantity));
            textViewProductId.setText("Product ID: " + productId);

            // If an image exists, decode it into a Bitmap and display it in the ImageView
            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewProduct.setImageBitmap(bitmap); // Set the decoded image in the ImageView
                productImageByteArray = image; // Store the image byte array for future reference
            }
            cursor.close(); // Close the cursor to free resources
        } else {
            // Show a toast message if the product is not found
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to delete a product from the database
    private void deleteProduct() {
        // Retrieve the product name from the input field
        String productName = editTextName.getText().toString().trim();

        // Delete the product from the database using the provided name
        int rowsDeleted = databaseCenter.deleteProduct(productName);

        // Check if the deletion was successful
        if (rowsDeleted > 0) {
            // Show a success message and clear the fields
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
            clearFields(); // Clear the input and output fields after deletion
        } else {
            // Show a failure message if the product could not be deleted
            Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to clear all input and output fields
    private void clearFields() {
        editTextName.setText(""); // Clear the product name input field
        textViewProductPrice.setText(""); // Clear the product price display
        textViewProductQuantity.setText(""); // Clear the product quantity display
        textViewProductId.setText(""); // Clear the product ID display
        imageViewProduct.setImageDrawable(null); // Clear the product image
        productImageByteArray = null; // Reset the image byte array
    }
}
