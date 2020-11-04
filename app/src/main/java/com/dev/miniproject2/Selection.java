package com.dev.miniproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Selection extends AppCompatActivity {

    LinearLayout layout1, layout2;
    ImageView getPool, getRide;
    TextView t1,t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

    layout1 = findViewById(R.id.layout1);
    layout2 = findViewById(R.id.layout2);
    getPool = findViewById(R.id.getPool);
    getRide = findViewById(R.id.drivePool);
    t1=findViewById(R.id.getPoolText);
    t2=findViewById(R.id.drivePoolText);

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


    }
}