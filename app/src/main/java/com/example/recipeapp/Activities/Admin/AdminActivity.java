package com.example.recipeapp.Activities.Admin;
import android.os.Bundle;

import com.example.recipeapp.Activities.LoginActivity;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.AppUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class AdminActivity extends AppCompatActivity {
    private AppUtils appUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        appUtils = new AppUtils();
        getWindow().getContext().getColor(R.color.purple_500);
        appUtils.setStatusBarColor(AdminActivity.this, getResources().getColor(R.color.purple_500));

        BottomNavigationView navView = findViewById(R.id.nav_view);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_add_recipe, R.id.nav_add_category)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_admin);

        if (getSupportActionBar() != null) {
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        }

        NavigationUI.setupWithNavController(navView, navController);
    }
}