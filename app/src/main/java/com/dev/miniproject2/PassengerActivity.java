package com.dev.miniproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PassengerActivity extends AppCompatActivity {
    TextView t1, t2, t3, t4, t5;
    TextInputEditText e1, e2, e3;
    RadioGroup rg;
    RadioButton rb1, rb2;
    CardView b1;
    String source, destination, vehicle, passengers;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);

//        t1 = findViewById(R.id.sourceText);
        e1 = findViewById(R.id.sourceEdit);
//        t2 = findViewById(R.id.destinationText);
        e2 = findViewById(R.id.destianationEdit);
        t3 = findViewById(R.id.vehicleText);
        rg = findViewById(R.id.vehicleRadioGrp);
        rb1 = findViewById(R.id.radioBike);
        rb2 = findViewById(R.id.radioCar);
        t4 = findViewById(R.id.passengerheading);
       // t5 = findViewById(R.id.passengerNumberText);
        e3 = findViewById(R.id.passengerNumberEdit);
        b1 = findViewById(R.id.btnSearchRide);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioBike:
                      //  t5.setVisibility(View.GONE);
                        e3.setVisibility(View.GONE);
                        break;
                    case R.id.radioCar:
                      //  t5.setVisibility(View.VISIBLE);
                        e3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PassengerActivity.this, SelectionActivity.class));
        finish();
    }

    public void searchRide(View view) {
        source = e1.getText().toString();
        destination = e2.getText().toString();
        passengers = e3.getText().toString();

        RadioButton vehiclebtn = (RadioButton) findViewById(rg.getCheckedRadioButtonId());
        vehicle = vehiclebtn.getText().toString();

        if (TextUtils.isEmpty(source)) {
            e1.setError("Please Enter Source");
        }
        if (TextUtils.isEmpty(destination)) {
            e2.setError("Please Enter Destination");
        }
        if (TextUtils.isEmpty(passengers)) {
            e3.setError("Enter Number of Passengers");
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching driver..");
        progressDialog.show();

        userId = auth.getCurrentUser().getUid();
        DocumentReference dr = firestore.collection("passengers").document(userId);
        Map<String, Object> user = new HashMap<>();
        user.put("Source", source);
        user.put("Destination", destination);
        user.put("Vehicle", vehicle);
        user.put("Number of Passengers", passengers);

        dr.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                    Toast.makeText(Passenger.this,"Details Saved",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PassengerActivity.this, DriverDetailsActivity.class);
                startActivity(i);
                progressDialog.dismiss();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PassengerActivity.this, "Invalid Details", Toast.LENGTH_SHORT).show();
            }
        });
    }

}