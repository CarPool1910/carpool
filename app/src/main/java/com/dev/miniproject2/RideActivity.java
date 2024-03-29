package com.dev.miniproject2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class RideActivity extends AppCompatActivity {

    TextView t1, t3, t5, t7, t9, t11, t13;
    TextView t2, t4, t6, t8, t10, t12, t14;
    CardView finishride;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String userId;
    LinearLayout Ride1;
    LinearLayout Ride2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        //Source
        t1 = findViewById(R.id.txtSource);
        t2 = findViewById(R.id.txtSetSource);

        //Destination
        t3 = findViewById(R.id.txtDestinatiom);
        t4 = findViewById(R.id.txtSetDestination);

        //Stop1
        t5 = findViewById(R.id.txtStop1);
        t6 = findViewById(R.id.txtSetStop1);

        //Stop2
        t7 = findViewById(R.id.txtStop2);
        t8 = findViewById(R.id.txtSetStop2);

        //Stop3
        t9 = findViewById(R.id.txtStop3);
        t10 = findViewById(R.id.txtSetStop3);

        //Stop4
        t11 = findViewById(R.id.txtStop4);
        t12 = findViewById(R.id.txtSetStop4);

        //Fare
        t13 = findViewById(R.id.txtFare);
        t14 = findViewById(R.id.txtSetFare);

        //Finish Ride Button
        finishride = findViewById(R.id.deletebtn);

        //Layouts
        Ride1 = findViewById(R.id.RideAvailableLayout);
        Ride2 = findViewById(R.id.RideNotAvailableLayout);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        userId = auth.getCurrentUser().getUid();
        DocumentReference dr = firestore.collection("Drivers").document(userId);
        dr.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    t2.setText(value.getString("Source"));
                    t4.setText(value.getString("Destination"));
                    t14.setText(value.getString("Fare"));

                    if(value.getString("Stop1") == ""){
                        t6.setVisibility(View.GONE);
                        t5.setVisibility(View.GONE);
                    }else{
                        t6.setText(value.getString("Stop1"));
                    }

                    if(value.getString("Stop2") == ""){
                        t7.setVisibility(View.GONE);
                        t8.setVisibility(View.GONE);
                    }else{
                        t8.setText(value.getString("Stop2"));
                    }

                    if(value.getString("Stop3") == ""){
                        t9.setVisibility(View.GONE);
                        t10.setVisibility(View.GONE);
                    }else{
                        t10.setText(value.getString("Stop3"));
                    }

                    if(value.getString("Stop4") == ""){
                        t11.setVisibility(View.GONE);
                        t12.setVisibility(View.GONE);
                    }else{
                        t12.setText(value.getString("Stop4"));
                    }




                    if (t2.getText().toString().length() != 0) {
                        Ride1.setVisibility(View.VISIBLE);
                        Ride2.setVisibility(View.GONE);
                    } else {
                        Ride1.setVisibility(View.GONE);
                        Ride2.setVisibility(View.VISIBLE);
                    }


                } catch (Exception e) {
                    Log.e("Database", "Exception: " + e.getMessage());
                }
            }
        });

        finishride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Valuessss", "T2 Value:" + t2.getText().toString());
                firestore.collection("Drivers").document(userId).delete();
                Toast.makeText(RideActivity.this, "Ride finished", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RideActivity.this, SelectionActivity.class));
//        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);
    }
}