package com.example.aida.finalproj.Activities.UserActivities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.aida.finalproj.Activities.Authentication.LoginActivity;
import com.example.aida.finalproj.Adapters.UserViewPagerAdapter;
import com.example.aida.finalproj.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfile extends AppCompatActivity {

    TabLayout tabLayout;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        tabLayout = findViewById(R.id.tablayout2);

        final ViewPager viewPager = findViewById(R.id.viewpager2);
        final UserViewPagerAdapter adapter = new UserViewPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, ConfirmationActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onBackPressed(){
        Intent intent = new Intent(UserProfile.this, UserDashboard.class);
        startActivity(intent);
        finish();
    }
}
