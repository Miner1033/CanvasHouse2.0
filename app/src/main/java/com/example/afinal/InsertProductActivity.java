package com.example.afinal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InsertProductActivity extends AppCompatActivity {

    // Declare UI components
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productQuantityEditText;
    private EditText productDescriptionEditText;
    private ImageView selectedImageView;
    private Button selectImageButton;
    private Button insertProductButton;
    private DataCenter dataCenter;  // Instance of DataCenter class to interact with the database
    private byte[] imageByteArray;  // Store the image data as a byte array

    // ActivityResultLauncher to handle the image picker result
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);  // Set the layout for this activity

        // Initialize views by finding them by their IDs
        productNameEditText = findViewById(R.id.product_name_et);
        productPriceEditText = findViewById(R.id.product_price_et);
        productQuantityEditText = findViewById(R.id.product_quantity_et);
        productDescriptionEditText = findViewById(R.id.product_description_et);
        selectedImageView = findViewById(R.id.select_image_iv);
        selectImageButton = findViewById(R.id.select_image_btn);
        insertProductButton = findViewById(R.id.insert_product_btn);

        // Initialize the DataCenter object to interact with the database
        dataCenter = new DataCenter(this);

        // Initialize image picker launcher with result callback
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();  // Get the image URI from the result
                try {
                    // Decode the image URI into a Bitmap and set it in the ImageView
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    selectedImageView.setImageBitmap(imageBitmap);
                    imageByteArray = bitmapToByteArray(imageBitmap);  // Convert Bitmap to byte array
                } catch (IOException e) {
                    e.printStackTrace();  // Print stack trace if image decoding fails
                }
            }
        });

        // Set click listener for the "Select Image" button
        selectImageButton.setOnClickListener(view -> showImageSelectionDialog());

        // Set click listener for the "Insert Product" button
        insertProductButton.setOnClickListener(view -> insertProduct());

        // Optionally, you can insert test data for development purposes
        // insertTestProduct();
    }

    // Method to launch the image selection dialog
    private void showImageSelectionDialog() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  // Intent to pick image
        pickIntent.setType("image/*");  // Only allow image selection
        imagePickerLauncher.launch(pickIntent);  // Launch the image picker activity
    }

    // Method to convert a Bitmap image to a byte array (JPEG format)
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  // Create an output stream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);  // Compress the image into the output stream
        return byteArrayOutputStream.toByteArray();  // Return the byte array
    }

    // Method to insert a product into the database
    private void insertProduct() {
        // Retrieve data from the input fields
        String name = productNameEditText.getText().toString().trim();
        String priceString = productPriceEditText.getText().toString().trim();
        String quantityString = productQuantityEditText.getText().toString().trim();
        String description = productDescriptionEditText.getText().toString().trim();

        // Check if all fields are filled and an image is selected
        if (name.isEmpty() || priceString.isEmpty() || quantityString.isEmpty() || description.isEmpty() || imageByteArray == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();  // Show error if any field is empty
            return;
        }

        // Convert price and quantity to correct types
        double price;
        int quantity;

        try {
            price = Double.parseDouble(priceString);  // Parse the price as double
            quantity = Integer.parseInt(quantityString);  // Parse the quantity as integer
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price or quantity", Toast.LENGTH_SHORT).show();  // Show error if parsing fails
            return;
        }

        // Insert the product into the database
        boolean isInserted = dataCenter.insertProduct(name, price, quantity, description, imageByteArray);
        if (isInserted) {
            Toast.makeText(this, "Product inserted successfully", Toast.LENGTH_SHORT).show();  // Show success message
            clearFields();  // Clear input fields and image view after insertion
        } else {
            Toast.makeText(this, "Error inserting product", Toast.LENGTH_SHORT).show();  // Show error if insertion fails
        }
    }

    // Method to clear all input fields and the image view
    private void clearFields() {
        productNameEditText.setText("");  // Clear product name field
        productPriceEditText.setText("");  // Clear product price field
        productQuantityEditText.setText("");  // Clear product quantity field
        productDescriptionEditText.setText("");  // Clear product description field
        selectedImageView.setImageResource(android.R.color.transparent);  // Clear the image view
        imageByteArray = null;  // Clear the byte array of the image
    }

    // Optionally, you can use this method to insert test data during development/testing
//    private void insertTestProduct() {
//        byte[] dummyImage = new byte[0];  // Replace with actual byte array of a test image
//        boolean isInserted = dataCenter.insertProduct("Test Product", 9.99, 10, "Test Description", dummyImage);
//        if (isInserted) {
//            Toast.makeText(this, "Test Product inserted successfully", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Error inserting Test Product", Toast.LENGTH_SHORT).show();
//        }
//    }
}
