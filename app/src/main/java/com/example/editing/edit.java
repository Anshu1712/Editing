package com.example.editing;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class edit extends AppCompatActivity {

    // Constant for the request crop code
    private static final int REQUEST_CROP = 1;
    // ImageView to display the selected image
    private ImageView imgCamera;
    // URI of the selected image
    private Uri selectedUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Retrieve the image URI from the intent
        String uriString = getIntent().getStringExtra("imageUri");
        if (uriString != null) {
            selectedUri = Uri.parse(uriString);
            // Find the ImageView in the layout
            imgCamera = findViewById(R.id.imgCamera);
            try {
                // Set the selected image URI to the ImageView
                imgCamera.setImageURI(selectedUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Show a toast message if the image URI could not be retrieved
            Toast.makeText(this, "Failed to retrieve image URI", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to start the crop activity
    public void cropImage(View view) {
        // Create an intent to start the crop activity
        Intent intent = new Intent(this, crop.class);
        if (selectedUri != null) {
            // Pass the selected image URI to the crop activity
            intent.putExtra("imageUri", selectedUri.toString());
            // Start the crop activity for result
            startActivityForResult(intent, REQUEST_CROP);
        } else {
            Log.e("edit", "selectedUri is null");
        }
    }

    // Handle the result from the crop activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if the result is from the crop request and if it was successful
        if (resultCode == RESULT_OK && requestCode == REQUEST_CROP) {
            if (data != null) {
                // Get the cropped image URI from the result data
                String result = data.getStringExtra("croppedImageUri");
                if (result != null) {
                    Uri resultUri = Uri.parse(result);
                    // Set the cropped image URI to the ImageView
                    imgCamera.setImageURI(resultUri);
                }
            }
        }
    }

    public void Adjust(View view) {
        // Create an intent to start the Adjustment activity
        Intent intent = new Intent(this, adjustment.class);
        if (selectedUri != null) {
            // Pass the selected image URI to the crop activity
            intent.putExtra("imageUri", selectedUri.toString());
            // Start the crop activity for result
            startActivityForResult(intent, REQUEST_CROP);
        } else {
            Log.e("edit", "selectedUri is null");
        }
    }
}
