package com.ds.carpooling;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ds.carpooling.databinding.ActivityBottomAdminBinding;

public class Bottom_Admin extends AppCompatActivity {

    private ActivityBottomAdminBinding adminbinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adminbinding = ActivityBottomAdminBinding.inflate(getLayoutInflater());
        setContentView(adminbinding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_approve, R.id.navigation_pending, R.id.navigation_reject)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_bottom_admin);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(adminbinding.navView, navController);
    }
}