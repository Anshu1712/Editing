package com.example.editing;

import android.annotation.SuppressLint;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class adjustment extends AppCompatActivity {

    // Declare SeekBars for brightness, contrast, and saturation adjustments
    private SeekBar seekBarBrightness, seekBarContrast, seekBarSaturation;

    // Declare ImageView to display the photo
    private ImageView photoView;

    // Declare variables to hold brightness, contrast, and saturation values
    private float brightness = 0, contrast = 1, saturation = 1;

    // Declare a ColorMatrix to apply color adjustments
    private ColorMatrix colorMatrix = new ColorMatrix();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);

        // Set the content view to the layout activity_adjustment
        setContentView(R.layout.activity_adjustment);

        // Ensure that the view with ID 'main' exists
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            // Set an OnApplyWindowInsetsListener to adjust padding based on system window insets
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } else {
            // Log an error and show a toast if the view with ID 'main' is not found, then finish the activity
            Log.e("adjustmentActivity", "View with ID 'main' not found");
            Toast.makeText(this, "Error initializing the view", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize the ImageView to display the photo
        photoView = findViewById(R.id.photoView);

        // Initialize SeekBars for brightness, contrast, and saturation adjustments
        seekBarBrightness = findViewById(R.id.seekBarBrightness);
        seekBarContrast = findViewById(R.id.seekBarContras);
        seekBarSaturation = findViewById(R.id.seekBarSaturation);

        // Handle the URI from the intent
        String uriString = getIntent().getStringExtra("imageUri");
        Uri sourceUri = null;
        if (uriString != null) {
            // Parse the URI string into a Uri object
            sourceUri = Uri.parse(uriString);
        } else {
            // Log an error and show a toast if the URI is null, then finish the activity
            Log.e("adjustmentActivity", "Image URI is null");
            Toast.makeText(this, "Image URI is null", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load the image into an ImageView using Glide
        ImageView imageView = findViewById(R.id.photoView);
        Glide.with(this)
                .load(sourceUri) // Load the image from the source URI
                .into(imageView); // Set the loaded image into the ImageView

        // Log the source URI for debugging purposes
        Log.d("adjustmentActivity", "sourceUri: " + sourceUri.toString());

        // Call the method to setup seek bar listeners
        setupSeekBarListeners();
    }

    // Method to setup listeners for the SeekBars
    private void setupSeekBarListeners() {
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Calculate brightness value based on seek bar progress
                brightness = (progress - 100) * 0.01f;
                // Apply the adjustments to the photo
                applyAdjustments();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Calculate contrast value based on seek bar progress
                contrast = progress / 100f;
                // Apply the adjustments to the photo
                applyAdjustments();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarSaturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Calculate saturation value based on seek bar progress
                saturation = progress / 100f;
                // Apply the adjustments to the photo
                applyAdjustments();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    // Method to apply brightness, contrast, and saturation adjustments to the photo
    private void applyAdjustments() {
        // Create a ColorMatrix for brightness adjustment
        ColorMatrix brightnessMatrix = new ColorMatrix();
        brightnessMatrix.set(new float[]{
                1, 0, 0, 0, brightness * 255,
                0, 1, 0, 0, brightness * 255,
                0, 0, 1, 0, brightness * 255,
                0, 0, 0, 1, 0
        });

        // Create a ColorMatrix for contrast adjustment
        ColorMatrix contrastMatrix = new ColorMatrix();
        contrastMatrix.set(new float[]{
                contrast, 0, 0, 0, 128 * (1 - contrast),
                0, contrast, 0, 0, 128 * (1 - contrast),
                0, 0, contrast, 0, 128 * (1 - contrast),
                0, 0, 0, 1, 0
        });

        // Create a ColorMatrix for saturation adjustment
        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);

        // Reset the main color matrix and concatenate all adjustments
        colorMatrix.reset();
        colorMatrix.postConcat(brightnessMatrix);
        colorMatrix.postConcat(contrastMatrix);
        colorMatrix.postConcat(saturationMatrix);

        // Apply the color matrix filter to the photoView
        photoView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }
}
