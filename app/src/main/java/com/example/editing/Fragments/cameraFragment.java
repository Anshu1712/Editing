package com.example.editing.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.editing.R;

public class cameraFragment extends Fragment {

    // Constant for camera request code
    private static final int CAMERA_REQ_CODE = 1;
    // ImageView to display the captured image
    private ImageView imgCamera;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment and store the view in a variable
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        // Initialize the ImageView and ImageButton
        imgCamera = view.findViewById(R.id.imgCamera);
        ImageButton cameraBtn = view.findViewById(R.id.cameraBtn);

        // Set OnClickListener for the camera button
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to capture an image using the camera
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Start the camera activity for result
                startActivityForResult(iCamera, CAMERA_REQ_CODE);
            }
        });

        return view;
    }

    // Handle the result from the camera intent
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQ_CODE) {
                // Retrieve the captured image as a Bitmap
                Bitmap image = (Bitmap) data.getExtras().get("data");
                // Set the captured image to the ImageView
                imgCamera.setImageBitmap(image);
            }
        }
    }
}
