package com.example.job254.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;

import com.example.job254.views.fragments.DashboardFragment;
import com.example.job254.views.fragments.JobsFragment;
import com.example.job254.views.fragments.LearnFragment;
import com.example.job254.views.fragments.ChatFragment;
import com.example.job254.views.fragments.ProfileFragment;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.example.job254.R;

public class DashboardActivity extends AppCompatActivity {

    ChipNavigationBar mBottomNav;
    private Toolbar mMyToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        initComponents();

    }

    private void initComponents() {
        mMyToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mMyToolbar);

        mBottomNav = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DashboardFragment()).commit();
        mBottomNav.setItemSelected(R.id.bottom_nav_dashboard, true);
        listenToEvent();
    }

    private void listenToEvent() {
        mBottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i){
                    case R.id.bottom_nav_dashboard:
                        fragment = new DashboardFragment();
                        break;
                    case R.id.bottom_nav_search:
                        fragment = new LearnFragment();
                        break;
                    case R.id.bottom_nav_jobs:
                        fragment = new JobsFragment();
                        break;
                    case R.id.bottom_nav_chat:
                        fragment = new ChatFragment();
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new ProfileFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
}