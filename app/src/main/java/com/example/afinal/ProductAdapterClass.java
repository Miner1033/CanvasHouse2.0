package com.example.afinal;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.afinal.DataCenter;

public class ProductAdapterClass extends CursorAdapter {

    // Constructor to initialize the adapter with the context, cursor, and flags
    public ProductAdapterClass(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);  // Passing context, cursor, and flags to the superclass
    }

    // This method is called to create a new view for each item in the list
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Getting a LayoutInflater object to inflate the item layout
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the product_item_layout XML layout and return it as a View object
        return inflater.inflate(R.layout.product_item_layout, parent, false);
    }

    // This method is called to bind data from the cursor to the views in the item layout
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Getting references to the views in the item layout (product name, price, and image)
        TextView nameTextView = view.findViewById(R.id.product_name);  // TextView for product name
        TextView priceTextView = view.findViewById(R.id.product_price);  // TextView for product price
        ImageView productImageView = view.findViewById(R.id.product_image);  // ImageView for product image

        // Getting the product name from the cursor and setting it to the name TextView
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DataCenter.COL_PRODUCT_NAME));

        // Getting the product price from the cursor and setting it to the price TextView
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DataCenter.COL_PRODUCT_PRICE));

        // Getting the product image from the cursor as a byte array
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DataCenter.COL_PRODUCT_IMAGE_URI));

        // Setting the product name to the name TextView
        nameTextView.setText(name);

        // Setting the product price to the price TextView
        priceTextView.setText(String.valueOf(price));

        // If there is an image, convert it from byte array to Bitmap and set it to the ImageView
        if (imageBytes != null && imageBytes.length > 0) {
            // Converting byte array to Bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            // Setting the Bitmap as the image source for the product image
            productImageView.setImageBitmap(bitmap);
        } else {
            // If no image is available, set a placeholder image
            productImageView.setImageResource(R.drawable.placeholder_image);
        }
    }
}
