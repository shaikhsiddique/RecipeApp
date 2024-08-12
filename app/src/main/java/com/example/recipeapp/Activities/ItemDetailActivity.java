package com.example.recipeapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.recipeapp.Activities.Admin.AdminActivity;
import com.example.recipeapp.Activities.UserPanel.UserActivity;
import com.example.recipeapp.Model.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.AppUtils;
import com.example.recipeapp.databinding.ActivityItemDetailBinding;
import com.example.recipeapp.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemDetailActivity extends AppCompatActivity {
    private String recipeTitle, recipeDesc, recipeDuration, recipeType, recipeImage,recipeIngredients,recipeLink;
    private ActivityItemDetailBinding binding;
    private AppUtils appUtils;
     private DatabaseReference databaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appUtils = new AppUtils();
        getWindow().getContext().getColor(R.color.purple_500);
        appUtils.setStatusBarColor(ItemDetailActivity.this, getResources().getColor(R.color.purple_500));

        recipeTitle = getIntent().getStringExtra("RecipeName");
        recipeDesc = getIntent().getStringExtra("RecipeDesc");
        recipeDuration = getIntent().getStringExtra("RecipeDuration");
        recipeIngredients=getIntent().getStringExtra("RecipeIngredients");
        recipeType = getIntent().getStringExtra("RecipeType");
        recipeImage = getIntent().getStringExtra("RecipeImgUri");
        recipeLink = getIntent().getStringExtra("YoutubeLink");
        databaseRef = FirebaseDatabase.getInstance().getReference("favourites");

        Glide.with(this).load(recipeImage).into(binding.recipeDetailImage);
        binding.recipeTime.setText(recipeDuration.toString());
        binding.ingredientsOfRecipe.setText(recipeIngredients.toString());
        binding.stepsOfRecipe.setText(recipeDesc.toString());
        binding.nameOfRecipe.setText(recipeTitle.toString());
        binding.openOnYoutube.setOnClickListener(view->{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(recipeLink)));
        });
        if (recipeType.equals("admin")){
            binding.detailButton.setText("Delete Recipe");
            binding.detailButton.setOnClickListener(view->{
                 deleteRecipe();
                });

        binding.left.setOnClickListener(view->{
            startActivity(new Intent(this, AdminActivity.class));
        });

        }else{
            binding.detailButton.setText("Add To Favourite ❤️");
            binding.detailButton.setOnClickListener(view->{
                addToFavorites();

            });
            binding.left.setOnClickListener(view->{
                startActivity(new Intent(this, UserActivity.class));
            });
        }


    }

    private void deleteRecipe() {
        FirebaseDatabase.getInstance().getReference("recipes").orderByChild("YoutubeLink").equalTo(recipeLink).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Delete the recipe
                    snapshot.getRef().removeValue();

                    // Intent to another activity
                    Intent intent = new Intent(ItemDetailActivity.this, UserActivity.class);
                    startActivity(intent);
                    finish(); // Optional: Finish the current activity if needed
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error if any
                Log.e("Firebase", "Error deleting recipe: " + databaseError.getMessage());
            }
        });

    }

    private void addToFavorites() {
        // Get the user's email (you need to have a way to retrieve the user's email)

        // Get a reference to the "favorites" node in Firebase with the user's email
        DatabaseReference userFavoritesRef = databaseRef;

        // Create a unique key for the new favorite recipe

        // Create a Recipe object with the necessary details
        Recipe favoriteRecipe = new Recipe(
           recipeTitle,recipeDuration,recipeDesc,recipeIngredients,recipeType,recipeImage,recipeLink
        );

        // Add the favorite recipe to the "favorites" node under the user's email
        userFavoritesRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(recipeTitle).setValue(favoriteRecipe).addOnCompleteListener(command -> {
            if (command.isSuccessful()){
                Toast.makeText(ItemDetailActivity.this, "Added to Favorites", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ItemDetailActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        });

           }
}