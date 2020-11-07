package com.dev.miniproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Driver extends AppCompatActivity {

    TextInputEditText te1, te2, inputStop1, inputStop2, inputStop3, inputStop4;

    LinearLayout stop1Layout, stop2Layout, stop3Layout, stop4Layout;

    TextView t1, t2, t3, t4, t5, t6;
    TextInputEditText e1, e2, e4;
    EditText e3;
    RadioGroup radioGroup;
    RadioButton rb1, rb2;
    Button btnSearch;
    String name, mobile, source, destination, vehicle = "", fare;
    String userId;
    String passengers;
    String stop1, stop2, stop3, stop4;
    //    Spinner s1;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ImageView imgStop, imgStop1, imgStop2, imgStop3, imgStop4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        //TextInputs
        te1 = findViewById(R.id.driverNameTV);
        te2 = findViewById(R.id.driverMobileTV);
        inputStop1 = findViewById(R.id.stop1);
        inputStop2 = findViewById(R.id.stop2);
        inputStop3 = findViewById(R.id.stop3);
        inputStop4 = findViewById(R.id.stop4);

        //TextInputLayout
        stop1Layout = findViewById(R.id.stop1Layout);
        stop2Layout = findViewById(R.id.stop2Layout);
        stop3Layout = findViewById(R.id.stop3Layout);
        stop4Layout = findViewById(R.id.stop4Layout);

        //ImageView
        imgStop = findViewById(R.id.addStop);
        imgStop1 = findViewById(R.id.addStop1);
        imgStop2 = findViewById(R.id.addStop2);
        imgStop3 = findViewById(R.id.addStop3);

        t1 = findViewById(R.id.text1);
        t4 = findViewById(R.id.txtVehicler);
        t5 = findViewById(R.id.txtPassenger);

        e1 = findViewById(R.id.etSrc);
        e2 = findViewById(R.id.etDest);
        e3 = findViewById(R.id.etPassenger);
        e4 = findViewById(R.id.etFare);

//        s1=findViewById(R.id.passengerSpinner);

        radioGroup = findViewById(R.id.radioGrp);
        rb1 = findViewById(R.id.radioBike);
        rb2 = findViewById(R.id.radioCar);


        btnSearch = findViewById(R.id.btnSearch);
//        btnSearch.setEnabled(false);


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioBike:
                        t5.setVisibility(View.GONE);
                        e3.setVisibility(View.GONE);
                        break;
                    case R.id.radioCar:
                        t5.setVisibility(View.VISIBLE);
                        e3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        //onclick stop method
        stops();


//        btnSearch.setEnabled(true);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = te1.getText().toString();
                mobile = te2.getText().toString();
                source = e1.getText().toString();
                destination = e2.getText().toString();
                passengers = e3.getText().toString();
                fare = e4.getText().toString();

                //Validation
                if (TextUtils.isEmpty(name)) {
                    te1.setError("Please enter Name");
                } else if (TextUtils.isEmpty(mobile)) {
                    te2.setError("Please enter mobile No");
                } else if (TextUtils.isEmpty(source)) {
                    e1.setError("Please Enter Source");
                } else if (TextUtils.isEmpty(destination)) {
                    e2.setError("Please Enter Destination");
                } else if (TextUtils.isEmpty(passengers)) {
                    e3.setError("Please Enter Valid Number of Passengers");
                } else if (TextUtils.isEmpty(fare)) {
                    e4.setError("Please Enter the Fare");
                } else {

                    //Taking input from stops
                    //Stop1
                    if (inputStop1.getText().length() != 0) {
                        stop1 = inputStop1.getText().toString();
                    } else {
                        stop1 = "";
                    }

                    //stop2
                    if (inputStop2.getText().length() != 0) {
                        stop2 = inputStop2.getText().toString();
                    } else {
                        stop2 = "";
                    }

                    //stop3
                    if (inputStop3.getText().length() != 0) {
                        stop3 = inputStop3.getText().toString();
                    } else {
                        stop3 = "";
                    }

                    //stop4
                    if (inputStop4.getText().length() != 0) {
                        stop4 = inputStop4.getText().toString();
                    } else {
                        stop4 = "";
                    }

                    //Vehicle Dtermination
                    RadioButton vehiclebtn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    vehicle = vehiclebtn.getText().toString();
                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            switch (i) {
                                case R.id.radioBike:
                                    vehicle = "Bike";
                                    break;
                                case R.id.radioCar:
                                    vehicle = "Car";
                                    break;
                            }
                        }
                    });


                    userId = auth.getCurrentUser().getUid();
                    DocumentReference dr = firestore.collection("Drivers").document(userId);
                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", name);
                    user.put("Mobile", mobile);
                    user.put("Source", source);
                    user.put("Stop1", stop1);
                    user.put("Stop2", stop2);
                    user.put("Stop3", stop3);
                    user.put("Stop4", stop4);
                    user.put("Destination", destination);
                    user.put("Vehicle", vehicle);
                    user.put("Passengers", passengers);
                    user.put("Fare", fare);

                    dr.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Driver.this, "Details Saved", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Driver.this, RideActivity.class);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Driver.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void stops() {

        // For 1st Stop
        imgStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop1Layout.setVisibility(View.VISIBLE);
            }
        });

        // For 2nd Stop
        imgStop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop2Layout.setVisibility(View.VISIBLE);
            }
        });


        // For 3rd Stop
        imgStop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop3Layout.setVisibility(View.VISIBLE);
            }
        });

        // For 4th Stop
        imgStop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stop4Layout.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Driver.this, Selection.class));
        finish();
    }
}
