package com.example.recipeapp.Activities.UserPanel.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.Activities.LoginActivity;
import com.example.recipeapp.Activities.UserPanel.ContactUs;
import com.example.recipeapp.Adapters.RecipeAdapter;
import com.example.recipeapp.Model.Recipe;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.recipeapp.databinding.FragmentProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;

    private FragmentProfileBinding binding;
    private DatabaseReference databaseReference;
    private Button contactUs;
    private DatabaseReference favReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        favReference = FirebaseDatabase.getInstance().getReference("favourites");
        contactUs = binding.ContactUs;
        contactUs.setOnClickListener(view->{
          startActivity(new Intent(requireContext(), ContactUs.class));
        });
                recyclerView = binding.recipeRv;
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recipeAdapter = new RecipeAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(recipeAdapter);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String email = currentUser.getEmail();
            binding.profileEmail.setText(email);
            // Update UI with user details

            binding.profileEmail.setText(email);

            // Initialize Firebase Database reference
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid);

            // Fetch additional user data from Firebase Database
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Assuming you have a "name" field in your database
                    String additionalName = dataSnapshot.child("userName").getValue(String.class);

                    if (additionalName != null) {
                        // Update UI with additional user data
                        binding.profileName.setText(additionalName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });

        }
        binding.profileLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(requireContext(), LoginActivity.class));
        });

        fetchRecipes();

        return root;
    }

    private void fetchRecipes() {
        favReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Recipe> recipeList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = snapshot.getValue(Recipe.class);
                    if (recipe != null) {
                        binding.empty.setVisibility(View.GONE);

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
