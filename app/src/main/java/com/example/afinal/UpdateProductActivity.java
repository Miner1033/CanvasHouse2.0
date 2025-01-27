package com.example.afinal;

// Import necessary packages
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateProductActivity extends AppCompatActivity {

    // Constant for image selection request code
    private static final int PICK_IMAGE_REQUEST = 1;

    // Declare views for the activity
    private EditText editTextName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextDescription;
    private ImageView imageViewProduct;
    private Button buttonUpdate;
    private Button buttonSelectImage;
    private Button buttonSearch;
    private TextView textViewProductId;

    // Declare instance of DataCenter class for database operations
    private DataCenter databaseCenter;
    private byte[] productImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the activity
        setContentView(R.layout.activity_update_product);

        // Initialize views by finding them by ID
        editTextName = findViewById(R.id.product_name_et);
        editTextPrice = findViewById(R.id.product_price_et);
        editTextQuantity = findViewById(R.id.product_quantity_et);
        editTextDescription = findViewById(R.id.product_description_et);
        imageViewProduct = findViewById(R.id.image_view_product);
        buttonUpdate = findViewById(R.id.button_update);
        buttonSelectImage = findViewById(R.id.button_select_image);
        buttonSearch = findViewById(R.id.button_search);
        textViewProductId = findViewById(R.id.text_view_product_id);

        // Initialize DataCenter instance to handle database operations
        databaseCenter = new DataCenter(this);

        // Set click listeners for the buttons
        buttonSearch.setOnClickListener(view -> searchProduct());
        buttonSelectImage.setOnClickListener(view -> selectImage());
        buttonUpdate.setOnClickListener(view -> updateProduct());
    }

    // Method to search for a product by name
    private void searchProduct() {
        // Get product name from the input field
        String productName = editTextName.getText().toString().trim();

        // If product name is empty, show a Toast and return
        if (productName.isEmpty()) {
            Toast.makeText(this, "Please enter a product name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query the database for the product by name
        Cursor cursor = databaseCenter.getProductByName(productName);

        // Check if the product exists in the database
        if (cursor != null && cursor.moveToFirst()) {
            // Get the product details from the cursor
            int productId = cursor.getInt(cursor.getColumnIndexOrThrow(DataCenter.COL_ID));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DataCenter.COL_PRODUCT_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DataCenter.COL_PRODUCT_QUANTITY));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DataCenter.COL_PRODUCT_DESCRIPTION));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DataCenter.COL_PRODUCT_IMAGE_URI));

            // Set the product details to the corresponding views
            editTextPrice.setText(String.valueOf(price));
            editTextQuantity.setText(String.valueOf(quantity));
            editTextDescription.setText(description);
            textViewProductId.setText("Product ID: " + productId);

            // If an image is available, display it in the ImageView
            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewProduct.setImageBitmap(bitmap);
                productImageByteArray = image;
            }

            // Close the cursor
            cursor.close();
        } else {
            // Show a Toast if the product is not found
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to select an image from the gallery
    private void selectImage() {
        // Create an Intent to pick an image from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the activity for result with a request code
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is for image selection
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected image
            Uri imageUri = data.getData();

            // Convert the image URI to a Bitmap and display it
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewProduct.setImageBitmap(bitmap);

                // Convert the Bitmap to a byte array
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                productImageByteArray = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to update the product details in the database
    private void updateProduct() {
        // Get the product details from the input fields
        String productName = editTextName.getText().toString().trim();
        String productPrice = editTextPrice.getText().toString().trim();
        String productQuantity = editTextQuantity.getText().toString().trim();
        String productDescription = editTextDescription.getText().toString().trim();

        // Validate the input fields
        if (productName.isEmpty() || productPrice.isEmpty() || productQuantity.isEmpty() || productDescription.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse the price and quantity
        double price = Double.parseDouble(productPrice);
        int quantity = Integer.parseInt(productQuantity);

        // Extract the product ID from the TextView
        String productIdText = textViewProductId.getText().toString();
        int productId = Integer.parseInt(productIdText.replaceAll("\\D+", ""));

        // Update the product in the database
        boolean isUpdated = databaseCenter.updateProduct(productId, productName, price, quantity, productDescription, productImageByteArray);

        // Show a Toast based on the result of the update
        if (isUpdated) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Product update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
