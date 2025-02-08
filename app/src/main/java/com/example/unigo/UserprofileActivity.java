package com.example.unigo;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class UserprofileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImage;
    private EditText usernameEditText, emailEditText, idEditText, phoneEditText;
    private Button saveButton;
    private Uri imageUri;
    private DatabaseHelper dbHelper; // Assuming you are using SQLite

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        // Initialize UI components
        profileImage = findViewById(R.id.profileImage);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        idEditText = findViewById(R.id.idEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        saveButton = findViewById(R.id.saveButton);
        dbHelper = new DatabaseHelper(this);

        // Load saved user data from DB
        loadUserData();

        // Profile Image Click - Open Gallery
        profileImage.setOnClickListener(view -> openGallery());

        // Save Button Click
        saveButton.setOnClickListener(view -> saveProfileData());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadUserData() {
        // Fetch user details from database
        User user = dbHelper.getUser(); // Assume getUser() fetches the stored user data

        if (user != null) {
            usernameEditText.setText(user.getUsername());
            emailEditText.setText(user.getEmail());
            idEditText.setText(user.getId());
            phoneEditText.setText(user.getPhone());

            // Load profile image if stored
            if (user.getImageUri() != null) {
                profileImage.setImageURI(Uri.parse(user.getImageUri()));
            }
        }
    }

    private void saveProfileData() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String id = idEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String imagePath = (imageUri != null) ? imageUri.toString() : null;

        // Save updated data to database
        boolean isUpdated = dbHelper.updateUserProfile(username, email, id, phone, imagePath);

        if (isUpdated) {
            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error Updating Profile", Toast.LENGTH_SHORT).show();
        }
    }
}
