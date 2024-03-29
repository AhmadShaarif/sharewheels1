package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard_Activity extends AppCompatActivity {
    private ImageButton settings;
    private ImageButton cardetails;
    private ImageButton prevrides;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        settings = findViewById(R.id.settings);
        cardetails = findViewById(R.id.cardetails);
        prevrides = findViewById(R.id.prevrides);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.dashboard) {

                return true;
            } else if (item.getItemId() == R.id.nav_my_car) {
                startActivity(new Intent(getApplicationContext(), my_car.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.inbox) {
                startActivity(new Intent(getApplicationContext(), inbox.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.car_pool) {

                startActivity(new Intent(getApplicationContext(), carpool.class));
                overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                finish();
                return true;
            }
            return false;
        });

        prevrides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard_Activity.this, Prevrides_Activity.class));
            }
        });
        cardetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard_Activity.this, Cardetails_Activity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard_Activity.this, Settings_Activity.class));
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}