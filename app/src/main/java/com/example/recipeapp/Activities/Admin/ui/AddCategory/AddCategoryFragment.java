package com.example.recipeapp.Activities.Admin.ui.AddCategory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.recipeapp.Model.Category;
import com.example.recipeapp.databinding.FragmentAddCategoryBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class AddCategoryFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FragmentAddCategoryBinding binding;
    private Button upload, submit;
    private EditText title;

    private Uri imageUri;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private ConstraintLayout progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        title = binding.title;
        upload = binding.uploadRecipe;
        submit = binding.submitCategory;
        progressBar=binding.progressBar;
        storageRef = FirebaseStorage.getInstance().getReference("recipe_images");
        databaseRef = FirebaseDatabase.getInstance().getReference("categories");

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                uploadData();
            }
        });

        return root;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            // You can display a preview of the selected image if needed
            // imageView.setImageURI(imageUri);
        }
    }

    private void uploadData() {
        if (imageUri != null) {
            StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            String recipeTitle = title.getText().toString();

                            // Create a Recipe object with title and image URL
                            Category recipe = new Category(recipeTitle, imageUrl);


// Store the recipe data in Realtime Database
                            databaseRef.child(recipeTitle).setValue(recipe);
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(requireContext(), "Recipe uploaded successfully", Toast.LENGTH_SHORT).show();
                                    });
                                    })
                                    .addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                                    } else {
                                    Toast.makeText(requireContext(), "No file selected", Toast.LENGTH_SHORT).show();
                                    }
                                    }

private String getFileExtension(Uri uri) {
        // Get the file extension from the URI
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(requireContext().getContentResolver().getType(uri));
        }

@Override
public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        }
        }