package com.example.afinal; // Defines the package name for the app to group related classes together.

import android.content.Intent; // Used to navigate between activities.
import android.os.Bundle; // Used to pass data to the activity when it is created.
import android.text.method.HideReturnsTransformationMethod; // Allows showing plain text in password fields.
import android.text.method.PasswordTransformationMethod; // Allows hiding text in password fields (dots/stars).
import android.widget.Button; // Represents a button in the UI.
import android.widget.EditText; // Represents a text field for user input.
import android.widget.ImageView; // Represents an image view (for icons, etc.).
import android.widget.TextView; // Represents a text view for displaying text.
import android.widget.Toast; // Used to display temporary messages to the user.

import androidx.appcompat.app.AppCompatActivity; // Provides compatibility support for older Android versions.

import com.google.firebase.auth.FirebaseAuth; // Used to handle authentication with Firebase.
import com.google.firebase.auth.FirebaseUser; // Represents a user authenticated with Firebase.

public class SignInScreen extends AppCompatActivity { // Main activity class for the sign-in screen.

    // Declare UI elements for the email and password input fields.
    private EditText emailEditText, passwordEditText;

    // Declare an ImageView for the password visibility toggle icon.
    private ImageView passwordToggle;

    // Boolean flag to track password visibility state.
    private boolean isPasswordVisible = false;

    // Firebase Authentication instance for managing user authentication.
    private FirebaseAuth mAuth;

    // Admin credentials for hardcoded admin login.
    private static final String ADMIN_EMAIL = "adminhc@gmail.com"; // Admin email.
    private static final String ADMIN_PASSWORD = "adminhc"; // Admin password.

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Called when the activity is created.
        super.onCreate(savedInstanceState); // Calls the parent class's onCreate method.
        setContentView(R.layout.activity_login); // Sets the layout file for this activity.

        // Initialize Firebase Auth.
        mAuth = FirebaseAuth.getInstance(); // Gets the Firebase authentication instance.

        // Initialize UI elements by linking them to views in the XML layout.
        emailEditText = findViewById(R.id.emailEditText); // Email input field.
        passwordEditText = findViewById(R.id.passwordEditText); // Password input field.
        passwordToggle = findViewById(R.id.passwordToggle); // Password visibility toggle icon.

        // Set a click listener for the password toggle to change visibility.
        passwordToggle.setOnClickListener(v -> togglePasswordVisibility()); // Toggles password visibility on click.

        // Set up the sign-in button click listener.
        Button signInButton = findViewById(R.id.signInButton); // Reference to the sign-in button.
        signInButton.setOnClickListener(v -> signIn()); // Calls the signIn method when clicked.

        // Set up the sign-up redirection text click listener.
        TextView signUpText = findViewById(R.id.signUpText); // Reference to the "Sign Up" text.
        signUpText.setOnClickListener(v -> goToSignUp()); // Calls the goToSignUp method when clicked.
    }

    // Toggles the visibility of the password field.
    private void togglePasswordVisibility() {
        if (isPasswordVisible) { // If the password is currently visible:
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance()); // Hide password.
            passwordToggle.setImageResource(R.drawable.ic_eyeoff); // Change icon to "eye-off".
        } else { // If the password is currently hidden:
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance()); // Show password.
            passwordToggle.setImageResource(R.drawable.ic_eye); // Change icon to "eye".
        }

        isPasswordVisible = !isPasswordVisible; // Toggle the password visibility flag.

        passwordEditText.setSelection(passwordEditText.getText().length()); // Keep the cursor at the end of the text.
    }

    // Handles the sign-in process.
    private void signIn() {
        String email = emailEditText.getText().toString().trim(); // Get the entered email and remove extra spaces.
        String password = passwordEditText.getText().toString().trim(); // Get the entered password and remove extra spaces.

        if (email.isEmpty()) { // Check if email is empty.
            emailEditText.setError("Email is required"); // Show an error message.
            return; // Stop further execution.
        }

        if (password.isEmpty()) { // Check if password is empty.
            passwordEditText.setError("Password is required"); // Show an error message.
            return; // Stop further execution.
        }

        // Check if entered credentials match the hardcoded admin credentials.
        if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
            Toast.makeText(SignInScreen.this, "Welcome Admin", Toast.LENGTH_SHORT).show(); // Show a welcome message.
            Intent adminIntent = new Intent(SignInScreen.this, AdminHomeActivity.class); // Navigate to admin screen.
            startActivity(adminIntent); // Start the admin activity.
            finish(); // Close the current activity.
            return; // Stop further execution.
        }

        // Authenticate regular users using Firebase Authentication.
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> { // Listen for the authentication result.
                    if (task.isSuccessful()) { // If sign-in is successful:
                        FirebaseUser user = mAuth.getCurrentUser(); // Get the current user.

                        if (user != null) { // Check if the user is not null.
                            if (user.isEmailVerified()) { // Check if the user's email is verified.
                                Toast.makeText(SignInScreen.this, "Signed In as: " + email, Toast.LENGTH_SHORT).show(); // Show success message.
                                Intent intent = new Intent(SignInScreen.this, ViewAsUserActivity.class); // Navigate to the user profile screen.
                                startActivity(intent); // Start the new activity.
                                finish(); // Close the current activity.
                            } else { // If email is not verified:
                                Toast.makeText(SignInScreen.this, "Please verify your email address before signing in.", Toast.LENGTH_SHORT).show(); // Show a warning.
                            }
                        }
                    } else { // If sign-in fails:
                        Toast.makeText(SignInScreen.this, "Invalid credentials, please try again", Toast.LENGTH_SHORT).show(); // Show an error message.
                    }
                });
    }

    // Redirects the user to the sign-up screen.
    private void goToSignUp() {
        Intent intent = new Intent(SignInScreen.this, SignUpScreen.class); // Create intent for the sign-up screen.
        startActivity(intent); // Start the sign-up activity.
    }
}
