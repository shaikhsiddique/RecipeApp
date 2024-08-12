package com.example.recipeapp.Activities.Admin.ui.AddRecipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.recipeapp.Model.Category;
import com.example.recipeapp.databinding.FragmentAddRecipeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRecipeFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText title, duration, description;
    private Button uploadRecipeImage, submitRecipe;
    private Spinner category;
    private Uri imageUri;  // Store the selected image URI
    private FragmentAddRecipeBinding binding;

    private DatabaseReference databaseRef, categoryRef;
    private StorageReference storageRef;
    private ConstraintLayout progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddRecipeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        title = binding.title;
        duration = binding.duration;
        description = binding.description;
        uploadRecipeImage = binding.uploadImage;
        submitRecipe = binding.submitRecipe;
        category = binding.availCategory;
        progressBar = binding.progressBar;
        categoryRef = FirebaseDatabase.getInstance().getReference("categories/");

        databaseRef = FirebaseDatabase.getInstance().getReference("recipes");
        storageRef = FirebaseStorage.getInstance().getReference("recipe_images");

        // Fetch categories from the Realtime Database
        fetchCategories();

        uploadRecipeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        submitRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Your existing code to get recipe details

                // Check if an image is selected
                if (imageUri != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    // Upload the image to Firebase Storage
                    uploadImage();
                } else {
                    Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void fetchCategories() {
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> categoryList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    categoryList.add(category.getTitle());
                }

                // Populate the Spinner with the fetched categories
                populateSpinner(categoryList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Toast.makeText(requireContext(), "Failed to fetch categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSpinner(List<String> categoryList) {
        // Create an ArrayAdapter using the category names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the Spinner
        category.setAdapter(adapter);
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();

            // You can display a preview of the selected image if needed
        }
    }

    private void uploadImage() {
        if (imageUri != null) {
            // Generate a unique key for the image
            String imageKey = databaseRef.push().getKey();

            if (imageKey != null) {
                StorageReference fileReference = storageRef.child(imageKey + ".jpg");

                // Upload the image to Firebase Storage
                fileReference.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            // Get the uploaded image URL
                            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Add the image URL along with other recipe details to the Realtime Database

                                addRecipeToDatabase(uri.toString());
                            });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }

    private void addRecipeToDatabase(String imageUrl) {
        // Get the selected category
        String selectedCategory = category.getSelectedItem().toString();

        // Get other recipe details
        String recipeTitle = title.getText().toString();
        String recipeDuration = duration.getText().toString();
        String recipeDescription = description.getText().toString();
        String recipeIngredients=binding.ingredients.getText().toString();
        String recipeYoutubeLink=binding.youtubelink.getText().toString();
        // Create a Map to store the recipe data
        Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("title", recipeTitle);
        recipeData.put("duration", recipeDuration);
        recipeData.put("description", recipeDescription);
        recipeData.put("ingredients", recipeIngredients);
        recipeData.put("YoutubeLink", recipeYoutubeLink);
        recipeData.put("category", selectedCategory);
        recipeData.put("imageUri", imageUrl);

        // Generate a unique key for the recipe
        String recipeKey = databaseRef.push().getKey();

        // Upload the data to the Realtime Database
        if (recipeKey != null) {
            databaseRef.child(recipeKey).setValue(recipeData);
            progressBar.setVisibility(View.GONE);

            Toast.makeText(requireContext(), "Recipe added successfully", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.GONE);

            Toast.makeText(requireContext(), "Failed to add recipe", Toast.LENGTH_SHORT).show();
        }
    }

    // Other methods

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}