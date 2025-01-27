package com.example.afinal;

import android.content.ContentValues; // Importing ContentValues for storing key-value pairs
import android.content.Context; // Importing Context for accessing system services
import android.database.Cursor; // Importing Cursor for querying the database
import android.database.sqlite.SQLiteDatabase; // Importing SQLiteDatabase for interacting with SQLite
import android.database.sqlite.SQLiteOpenHelper; // Importing SQLiteOpenHelper to manage database creation and version management
import android.util.Log; // Importing Log for logging information and errors

public class DataCenter extends SQLiteOpenHelper {
    // Database name and version
    public static final String DATABASE_NAME = "CanvasHouse_DB";
    public static final int DATABASE_VERSION = 2;

    // Table and column names for the products table
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_ID = "_id";
    public static final String COL_PRODUCT_NAME = "productName";
    public static final String COL_PRODUCT_PRICE = "productPrice";
    public static final String COL_PRODUCT_QUANTITY = "productQuantity";
    public static final String COL_PRODUCT_DESCRIPTION = "productDescription";
    public static final String COL_PRODUCT_IMAGE_URI = "productImageUri";

    // Constructor to initialize the database helper
    public DataCenter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the products table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // Column for auto-incrementing ID
                COL_PRODUCT_NAME + " TEXT, " + // Column for product name
                COL_PRODUCT_PRICE + " REAL, " + // Column for product price
                COL_PRODUCT_QUANTITY + " INTEGER, " + // Column for product quantity
                COL_PRODUCT_DESCRIPTION + " TEXT, " + // Column for product description
                COL_PRODUCT_IMAGE_URI + " BLOB)"; // Column for product image (stored as BLOB)
        db.execSQL(CREATE_PRODUCTS_TABLE); // Executing the SQL query to create the table
    }

    // Called when the database version is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing products table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        // Create the products table again with the new schema
        onCreate(db);
    }

    // Method to insert a new product into the products table
    public boolean insertProduct(String name, double price, int quantity, String description, byte[] imageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase(); // Getting a writable database
        ContentValues values = new ContentValues(); // Creating a ContentValues object to store data
        values.put(COL_PRODUCT_NAME, name); // Putting product name into the values
        values.put(COL_PRODUCT_PRICE, price); // Putting product price into the values
        values.put(COL_PRODUCT_QUANTITY, quantity); // Putting product quantity into the values
        values.put(COL_PRODUCT_DESCRIPTION, description); // Putting product description into the values
        values.put(COL_PRODUCT_IMAGE_URI, imageByteArray); // Putting product image into the values
        long result = db.insert(TABLE_PRODUCTS, null, values); // Inserting the values into the database table
        db.close(); // Closing the database connection

        if (result == -1) { // Checking if the insert operation was unsuccessful
            Log.e("DataCenter", "Error inserting product into database"); // Logging an error if the insert failed
        } else {
            Log.i("DataCenter", "Product inserted successfully with ID: " + result); // Logging the success message with the inserted product's ID
        }
        return result != -1; // Returning true if insertion was successful, otherwise false
    }

    // Method to fetch all products from the products table
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase(); // Getting a readable database
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null); // Executing a query to fetch all products
    }

    // Method to fetch a specific product by its name
    public Cursor getProductByName(String productName) {
        SQLiteDatabase db = this.getReadableDatabase(); // Getting a readable database
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_NAME + " = ?", new String[]{productName}); // Querying the database by product name
    }

    // Method to update a product's details
    public boolean updateProduct(int productId, String productName, double price, int quantity, String productDescription, byte[] productImageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase(); // Getting a writable database
        ContentValues values = new ContentValues(); // Creating a ContentValues object to store updated data
        values.put(COL_PRODUCT_NAME, productName); // Putting updated product name into the values
        values.put(COL_PRODUCT_PRICE, price); // Putting updated product price into the values
        values.put(COL_PRODUCT_QUANTITY, quantity); // Putting updated product quantity into the values
        values.put(COL_PRODUCT_DESCRIPTION, productDescription); // Putting updated product description into the values
        values.put(COL_PRODUCT_IMAGE_URI, productImageByteArray); // Putting updated product image into the values

        // Updating the product in the database based on its ID
        int rowsAffected = db.update(TABLE_PRODUCTS, values, COL_ID + " = ?", new String[]{String.valueOf(productId)});
        db.close(); // Closing the database connection
        return rowsAffected > 0; // Returning true if the product was updated successfully
    }

    // Method to delete a product by its name
    public int deleteProduct(String productName) {
        SQLiteDatabase db = this.getWritableDatabase(); // Getting a writable database
        String whereClause = COL_PRODUCT_NAME + "=?"; // Defining the condition to match the product name
        String[] whereArgs = {productName}; // Defining the arguments for the condition
        int deletedRows = db.delete(TABLE_PRODUCTS, whereClause, whereArgs); // Deleting the product from the database
        db.close(); // Closing the database connection
        return deletedRows; // Returning the number of deleted rows (should be 1 if successful)
    }
}
