package com.example.recipeapp.Activities.UserPanel.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Adapters.CategoryAdapter;
import com.example.recipeapp.Adapters.RecipeAdapter;
import com.example.recipeapp.Model.Category;
import com.example.recipeapp.Model.Recipe;
import com.example.recipeapp.databinding.FragmentCategoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private DatabaseReference databaseRef;

    private FragmentCategoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        recyclerView = binding.categoryRv;
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(),2));
        categoryAdapter = new CategoryAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(categoryAdapter);

        databaseRef = FirebaseDatabase.getInstance().getReference("categories");

        // Fetch and display recipes
        fetchCategory();
        return root;
    }
    private void fetchCategory() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Category> categoryList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Category category = snapshot.getValue(Category.class);
                    if (category != null) {
                        binding.progressBar.setVisibility(View.GONE);
                        categoryList.add(category);
                    }
                }

                // Update the adapter with the fetched recipes
                categoryAdapter = new CategoryAdapter(requireContext(), categoryList);
                recyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}