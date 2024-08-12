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
import com.example.recipeapp.Activities.CategoryItems;
import com.example.recipeapp.Activities.ItemDetailActivity;
import com.example.recipeapp.Model.Category;
import com.example.recipeapp.Model.Recipe;
import com.example.recipeapp.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImg;
        private TextView titleTextView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImg = itemView.findViewById(R.id.categoryImg);
            titleTextView = itemView.findViewById(R.id.title_identity);
            itemView.setOnClickListener(view -> {
                itemView.getContext().startActivity(new Intent(itemView.getContext(),  CategoryItems.class).putExtra("categoryTitle",titleTextView.getText().toString()));
            });
        }

        public void bind(Category category) {
            Glide.with(itemView.getContext()).load(category.getImageUri()).into(categoryImg);
            titleTextView.setText(category.getTitle());


        }
    }
}
