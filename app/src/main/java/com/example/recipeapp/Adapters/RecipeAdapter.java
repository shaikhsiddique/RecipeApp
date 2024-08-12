package com.example.recipeapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recipeapp.Activities.ItemDetailActivity;
import com.example.recipeapp.Model.Recipe;
import com.example.recipeapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;
    private List<Recipe> originalRecipeList; // Add a list to store the original unfiltered recipes

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
        this.originalRecipeList = new ArrayList<>(recipeList); // Initialize the original list
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void setRecipes(List<Recipe> sRecipeList) {
        recipeList.clear();
        recipeList.addAll(sRecipeList);
        notifyDataSetChanged();
    }

    // Add a method to reset the adapter to its original state
    public void resetAdapter() {
        recipeList = new ArrayList<>(originalRecipeList);
        notifyDataSetChanged();
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private ImageView recipeImageView;
        private TextView titleTextView, durationOfRecipe;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImageView = itemView.findViewById(R.id.recipe_img);
            titleTextView = itemView.findViewById(R.id.title_identity);
            durationOfRecipe = itemView.findViewById(R.id.durationOfRecipe);
        }

        public void bind(Recipe recipe) {
            Glide.with(itemView.getContext()).load(recipe.getImageUri()).into(recipeImageView);
            titleTextView.setText(recipe.getTitle());
            durationOfRecipe.setText(recipe.getDuration());
            itemView.setOnClickListener(view -> {
                Intent intent=new Intent(itemView.getContext(), ItemDetailActivity.class);
                intent.putExtra("RecipeName",recipe.getTitle());
                intent.putExtra("RecipeDesc",recipe.getDescription());
                intent.putExtra("RecipeIngredients",recipe.getIngredients());
                intent.putExtra("RecipeImgUri",recipe.getImageUri());
                intent.putExtra("YoutubeLink",recipe.getYoutubeLink());
                if ( FirebaseAuth.getInstance().getCurrentUser()!=null){
                    intent.putExtra("RecipeType","user");


                }else {
                    intent.putExtra("RecipeType","admin");

                }
                intent.putExtra("RecipeDuration",recipe.getDuration());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}

