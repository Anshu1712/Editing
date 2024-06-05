package com.example.editing.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.editing.R;
import com.example.editing.collage;
import com.example.editing.edit;

import java.util.ArrayList;

public class addFragment extends Fragment {

    // Constant for gallery request code
    private static final int GALLERY_REQ_CODE = 1;
    // Variable to hold the selected image URI(s)
    private ArrayList<Uri> selectedUris = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the LinearLayouts in the layout and set click listeners on them
        LinearLayout linearLayout = view.findViewById(R.id.linearLayout);
        LinearLayout linearLayout2 = view.findViewById(R.id.linearLayout2);

        // Click listener for the first linear layout
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gallery to select single image
                openGallery(false);
            }
        });

        // Click listener for the second linear layout
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gallery to select multiple images
                openGallery(true);
            }
        });
    }

    // Method to open the gallery
    private void openGallery(boolean allowMultiple) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        // Set whether multiple selection is allowed
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Pictures"), GALLERY_REQ_CODE);
    }

    // Handle the result from the gallery intent
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the gallery request and if it was successful
        if (requestCode == GALLERY_REQ_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                // Multiple images selected
                int count = data.getClipData().getItemCount();
                // Loop through the selected images and add their URIs to the list
                for (int i = 0; i < count; i++) {
                    selectedUris.add(data.getClipData().getItemAt(i).getUri());
                }
                // Start collage activity with selected images
                startCollageActivity();
            } else if (data.getData() != null) {
                // Single image selected
                // Add the URI of the selected image to the list
                selectedUris.add(data.getData());
                // Start edit activity with the selected image
                startEditActivity(data.getData());
            }
        }
    }

    // Method to start the edit activity
    private void startEditActivity(Uri imageUri) {
        Intent intent = new Intent(getActivity(), edit.class);
        if (imageUri != null) {
            // Pass the URI of the selected image to the edit activity
            intent.putExtra("imageUri", imageUri.toString());
            startActivity(intent);
        } else {
            // Show toast if failed to get image URI
            Toast.makeText(getActivity(), "Failed to get image URI", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to start the collage activity
    private void startCollageActivity() {
        Intent intent = new Intent(getActivity(), collage.class);
        if (!selectedUris.isEmpty()) {
            // Pass the list of selected image URIs to the collage activity
            intent.putParcelableArrayListExtra("imageUris", selectedUris);
            startActivity(intent);
        } else {
            // Show toast if failed to get image URIs
            Toast.makeText(getActivity(), "Failed to get image URIs", Toast.LENGTH_SHORT).show();
        }
    }
}
