package com.example.recipeapp.Activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Activities.Admin.AdminActivity;
import com.example.recipeapp.Adapters.RecipeAdapter;
import com.example.recipeapp.Model.Recipe;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.AppUtils;
import com.example.recipeapp.databinding.ActivityCategoryItemsBinding; // Update with your actual binding class

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryItems extends AppCompatActivity {

    private ActivityCategoryItemsBinding binding; // Update with your actual binding class
    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipeList;
    private String categoryId;
    private RecyclerView recyclerView;
    private AppUtils appUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryItemsBinding.inflate(getLayoutInflater()); // Update with your actual binding class
        setContentView(binding.getRoot());
        appUtils = new AppUtils();
        getWindow().getContext().getColor(R.color.purple_500);
        appUtils.setStatusBarColor(CategoryItems.this, getResources().getColor(R.color.purple_500));

        recyclerView = binding.itemsRvCategory;
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(this, recipeList);
        recyclerView.setAdapter(recipeAdapter);
        // Get category information from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String categoryTitle = extras.getString("categoryTitle");
          binding.titleOfTheActivity.setText(categoryTitle);
            setTitle(categoryTitle); // Set the activity title to the selected category

            // Fetch recipes for the selected category from Firebase Database
            fetchRecipesForCategory(categoryTitle);
        }
    }

    private void fetchRecipesForCategory(String categoryTitle) {
        DatabaseReference recipesRef = FirebaseDatabase.getInstance().getReference("recipes");

        // Assuming your Recipe model has a "categoryId" field
        recipesRef.orderByChild("category").equalTo(categoryTitle).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        recipeList.add(recipe);
                    }
                }

                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
