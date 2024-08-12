package com.example.recipeapp.Activities.UserPanel;

import android.os.Bundle;

import com.example.recipeapp.Activities.Admin.AdminActivity;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.AppUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.recipeapp.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {
    private AppUtils appUtils;

    private ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        appUtils = new AppUtils();
        getWindow().getContext().getColor(R.color.purple_500);
        appUtils.setStatusBarColor(UserActivity.this, getResources().getColor(R.color.purple_500));

        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_user_dashboard, R.id.nav_user_category, R.id.nav_user_profile)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_user);
        if (getSupportActionBar() != null) {
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        }
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}