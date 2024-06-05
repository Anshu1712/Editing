package com.example.editing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;

import java.io.File;

public class crop extends AppCompatActivity {

    // Constant to define the name of the cropped image
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the image URI from the intent
        Uri sourceUri = null;
        String uriString = getIntent().getStringExtra("imageUri");
        if (uriString != null) {
            sourceUri = Uri.parse(uriString);
        }

        // If source URI is valid, start the cropping activity
        if (sourceUri != null) {
            Log.d("cropActivity", "sourceUri: " + sourceUri.toString());

            // Create a destination URI for the cropped image
            Uri destinationUri = Uri.fromFile(new File(getCacheDir(), SAMPLE_CROPPED_IMAGE_NAME + System.currentTimeMillis() + ".png"));

            // Configure UCrop with source and destination URIs, set aspect ratio and max size, then start the crop activity
            UCrop.of(sourceUri, destinationUri)
                    .withAspectRatio(0, 0)
                    .withMaxResultSize(2000, 2000)
                    .start(crop.this);
        } else {
            // If source URI is null, log an error and show a toast message, then finish the activity
            Log.e("cropActivity", "Image URI is null");
            Toast.makeText(this, "Image URI is null", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    // Handle the result from the UCrop activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the cropping was successful
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                Log.d("cropActivity", "Cropped image URI: " + resultUri.toString());

                // Return the cropped image URI to the calling activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("croppedImageUri", resultUri.toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            // If there was an error during cropping, log the error message and finish the activity with a canceled result
            final Throwable cropError = UCrop.getError(data);
            if (cropError != null) {
                Log.e("cropActivity", "Crop error: " + cropError.getMessage());
            }
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
