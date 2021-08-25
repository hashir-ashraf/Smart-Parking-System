package com.tss.smartparking;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_Activity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        bottomNavigationView = findViewById(R.id.bottomnavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            HomeFragment fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.home_id:
                fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                        .commit();
                break;

            case R.id.slots:
                fragment = new SlotsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                        .commit();
                break;
            case R.id.profiles:
                fragment = new ProfileFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                        .commit();
                break;
        }
        return true;
    }
}
