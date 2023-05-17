package com.ds.carpooling;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ds.carpooling.databinding.ActivityBottomNavigationBinding;

public class Bottom_Navigation extends AppCompatActivity {

    private ActivityBottomNavigationBinding userbinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userbinding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(userbinding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_Profile, R.id.navigation_Rides, R.id.navigation_home_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_bottom_navigation);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(userbinding.navView, navController);
    }
}