package com.dev.miniproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Selection extends AppCompatActivity {

    LinearLayout layout1, layout2;
    ImageView getPool, getRide;
    TextView t1, t2;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        getPool = findViewById(R.id.getPool);
        getRide = findViewById(R.id.drivePool);

        t1 = findViewById(R.id.getPoolText);
        t2 = findViewById(R.id.drivePoolText);

        navigationView = findViewById(R.id.navigationDrawer);
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        //Layout OnClicks
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Selection.this, Passenger.class));
            }
        });

        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Selection.this, Driver.class));
            }
        });

        //Navigation Drawer Listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.idHome:
                        startActivity(new Intent(Selection.this, Selection.class));
                        finish();
                        break;
                    case R.id.idProfile:
                        startActivity(new Intent(Selection.this, ProfileActivity.class));
                        finish();
                        break;
                    case R.id.idGetPool:
                        startActivity(new Intent(Selection.this, Passenger.class));
                        finish();
                        break;
                    case R.id.idDrivePool:
                        startActivity(new Intent(Selection.this, Driver.class));
                        finish();
                        break;
                    case R.id.idContact:
                        Intent contactIntent = new Intent();
                        contactIntent.setAction(Intent.ACTION_SEND);
                        contactIntent.setData(Uri.parse("email"));
                        String[] contact = {"carpool1910@gmail.com"};
                        contactIntent.putExtra(Intent.EXTRA_EMAIL, contact);
                        contactIntent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion/Feedback/Review about CarPool");
                        contactIntent.setType("message/rfc822");
                        Intent chooser = Intent.createChooser(contactIntent, "Please select Gmail");
                        startActivity(chooser);
                        break;
                    case R.id.idAbout:
                        startActivity(new Intent(Selection.this, AboutActivity.class));
                        finish();
                        break;
                    case R.id.idShare:
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        Intent chooser2 = Intent.createChooser(shareIntent, "Share via: ");
                        startActivity(chooser2);
                        break;
                    case R.id.idSettings:
                        Toast.makeText(Selection.this, "Settings are not yet finalised", Toast.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
           // moveTaskToBack(true);
            finish();
        }
    }

}