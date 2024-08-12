package com.example.recipeapp.Activities.UserPanel.ui.dashboard;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Adapters.RecipeAdapter;
import com.example.recipeapp.Model.Recipe;
import com.example.recipeapp.databinding.FragmentDashboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserDashboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private DatabaseReference databaseRef;
    private FragmentDashboardBinding binding;
    private EditText searchBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        recyclerView = binding.itemsRv;
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recipeAdapter = new RecipeAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(recipeAdapter);
        searchBar = binding.searchBar;
        databaseRef = FirebaseDatabase.getInstance().getReference("recipes");

        // Fetch and display recipes
        fetchRecipes();
        return root;
    }

    private void fetchRecipes() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Recipe> recipeList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        binding.progressBar.setVisibility(View.GONE);
                        recipeList.add(recipe);
                    }
                }

                // Update the adapter with the fetched recipes
                recipeAdapter = new RecipeAdapter(requireContext(), recipeList);
                recyclerView.setAdapter(recipeAdapter);
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