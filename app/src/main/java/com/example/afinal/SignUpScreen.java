package com.example.afinal; // Defines the package name for organizing related classes.

import android.content.Intent; // Used to transition between different activities.
import android.os.Bundle; // Used to pass data during activity creation.
import android.widget.Button; // Represents a clickable button in the UI.
import android.widget.EditText; // Represents an input field for user text.
import android.widget.ImageView; // Represents an image in the UI (e.g., icons).
import android.widget.Toast; // Displays short messages (toasts) to users.
import androidx.appcompat.app.AppCompatActivity; // Provides compatibility support for various Android versions.

import com.google.firebase.auth.FirebaseAuth; // Firebase Auth module for handling user authentication.
import com.google.firebase.auth.FirebaseUser; // Firebase User object to represent authenticated users.

public class SignUpScreen extends AppCompatActivity { // Main activity for the sign-up screen.

    // Declare UI elements for user input and button interactions.
    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    Button signupButton, signinButton; // Buttons for signing up and navigating to sign-in screen.
    ImageView passwordToggle, confirmPasswordToggle; // Image views for toggling password visibility.

    private FirebaseAuth firebaseAuth; // Firebase Authentication instance for user authentication.

    @Override
    protected void onCreate(Bundle savedInstanceState) { // Called when the activity is created.
        super.onCreate(savedInstanceState); // Calls the parent class's onCreate method.
        setContentView(R.layout.activity_sign_up_screen); // Sets the layout for this activity.

        // Initialize Firebase Authentication instance.
        firebaseAuth = FirebaseAuth.getInstance(); // Retrieves the FirebaseAuth instance.

        // Link UI elements to their corresponding views in the layout.
        nameEditText = findViewById(R.id.nameEditText); // Name input field.
        emailEditText = findViewById(R.id.emailEditText); // Email input field.
        passwordEditText = findViewById(R.id.passwordEditText); // Password input field.
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText); // Confirm password field.
        signupButton = findViewById(R.id.signupButton); // Sign-up button.
        signinButton = findViewById(R.id.signinButton); // Sign-in button (to navigate back to sign-in).
        passwordToggle = findViewById(R.id.passwordToggle); // Password visibility toggle for the password field.
        confirmPasswordToggle = findViewById(R.id.confirmPasswordToggle); // Password visibility toggle for the confirm password field.

        // Set up password visibility toggle functionality for both password fields.
        passwordToggle.setOnClickListener(v -> togglePasswordVisibility(passwordEditText, passwordToggle)); // Toggle for the password field.
        confirmPasswordToggle.setOnClickListener(v -> togglePasswordVisibility(confirmPasswordEditText, confirmPasswordToggle)); // Toggle for the confirm password field.

        // Set onClickListener for the sign-up button.
        signupButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim(); // Get name input and trim extra spaces.
            String email = emailEditText.getText().toString().trim(); // Get email input and trim extra spaces.
            String password = passwordEditText.getText().toString().trim(); // Get password input and trim extra spaces.
            String confirmPassword = confirmPasswordEditText.getText().toString().trim(); // Get confirm password input and trim extra spaces.

            // Validate inputs for name, email, password, and confirm password.
            if (name.isEmpty()) { // Check if name field is empty.
                nameEditText.setError("Name is required"); // Show an error message.
                nameEditText.requestFocus(); // Focus on the name field for user correction.
                return; // Stop further execution.
            }

            if (!name.matches("^[A-Za-z ]+$")) { // Validate name to only contain letters and spaces.
                nameEditText.setError("Invalid name. Use only letters and spaces."); // Show an error message.
                nameEditText.requestFocus(); // Focus on the name field.
                return; // Stop further execution.
            }

            if (email.isEmpty()) { // Check if email field is empty.
                emailEditText.setError("Email is required"); // Show an error message.
                emailEditText.requestFocus(); // Focus on the email field.
                return; // Stop further execution.
            }

            if (!email.matches("^[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+$")) { // Validate email format.
                emailEditText.setError("Invalid email format"); // Show an error message for invalid format.
                emailEditText.requestFocus(); // Focus on the email field.
                return; // Stop further execution.
            }

            if (password.isEmpty()) { // Check if password field is empty.
                passwordEditText.setError("Password is required"); // Show an error message.
                passwordEditText.requestFocus(); // Focus on the password field.
                return; // Stop further execution.
            }

            if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")) { // Validate password strength.
                passwordEditText.setError("Password must be at least 8 characters, include uppercase, lowercase, a number, and a special character."); // Show an error message.
                passwordEditText.requestFocus(); // Focus on the password field.
                return; // Stop further execution.
            }

            if (confirmPassword.isEmpty()) { // Check if confirm password field is empty.
                confirmPasswordEditText.setError("Please confirm your password"); // Show an error message.
                confirmPasswordEditText.requestFocus(); // Focus on the confirm password field.
                return; // Stop further execution.
            }

            if (!password.equals(confirmPassword)) { // Check if passwords match.
                confirmPasswordEditText.setError("Passwords do not match"); // Show an error message.
                confirmPasswordEditText.requestFocus(); // Focus on the confirm password field.
                return; // Stop further execution.
            }

            // Firebase Authentication - Create user with email and password.
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> { // Listen for the completion of the task.
                        if (task.isSuccessful()) { // If sign-up is successful:
                            FirebaseUser user = firebaseAuth.getCurrentUser(); // Get the current authenticated user.
                            if (user != null) { // Check if the user is not null.
                                // Send email verification to the user.
                                user.sendEmailVerification()
                                        .addOnCompleteListener(task1 -> { // Listen for email verification completion.
                                            if (task1.isSuccessful()) { // If email verification is sent successfully:
                                                Toast.makeText(SignUpScreen.this, "Verification email sent. Please check your inbox.", Toast.LENGTH_SHORT).show(); // Show success message.
                                            } else { // If there was an issue sending the email:
                                                Toast.makeText(SignUpScreen.this, "Error sending verification email.", Toast.LENGTH_SHORT).show(); // Show error message.
                                            }
                                        });
                            }

                            // Navigate to SignInScreen after successful sign-up.
                            Toast.makeText(SignUpScreen.this, "Sign-up successful. Check your email for verification.", Toast.LENGTH_SHORT).show(); // Show a success message.
                            Intent intent = new Intent(SignUpScreen.this, SignInScreen.class); // Create an intent to navigate to SignInScreen.
                            startActivity(intent); // Start the SignInScreen activity.
                            finish(); // Close the SignUpScreen activity.
                        } else { // If sign-up fails:
                            Toast.makeText(SignUpScreen.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show(); // Show an error message with failure details.
                        }
                    });
        });

        // Set onClickListener for the sign-in button.
        signinButton.setOnClickListener(v -> {
            // Navigate to SignInScreen for existing users.
            Intent intent = new Intent(SignUpScreen.this, SignInScreen.class); // Create an intent for the sign-in screen.
            startActivity(intent); // Start the SignInScreen activity.
            finish(); // Close the SignUpScreen activity.
        });
    }

    // Method to toggle password visibility.
    private void togglePasswordVisibility(EditText editText, ImageView toggleIcon) {
        if (editText.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) { // If password is hidden:
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); // Show password in plain text.
            toggleIcon.setImageResource(R.drawable.ic_eyeoff); // Change icon to "eye-off".
        } else { // If password is visible:
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD); // Hide password (show as dots/stars).
            toggleIcon.setImageResource(R.drawable.ic_eye); // Change icon to "eye".
        }
        editText.setSelection(editText.getText().length()); // Keep the cursor at the end of the text.
    }
}
