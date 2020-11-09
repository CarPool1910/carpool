package com.dev.miniproject2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class DriverDetailsActivity extends AppCompatActivity {
    FirebaseFirestore firestore, firestore2;
    RecyclerView driverRV;
    FirebaseAuth auth;
    String passengerID, passengerSource, passengerDest;
    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        firestore = FirebaseFirestore.getInstance();
        firestore2 = FirebaseFirestore.getInstance();

        driverRV = findViewById(R.id.driverList);
        auth = FirebaseAuth.getInstance();
        passengerID = auth.getCurrentUser().getUid();

        //Passenger Details
        DocumentReference passDetails = firestore2.collection("passengers").document(passengerID);
        passDetails.addSnapshotListener(DriverDetailsActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                passengerSource = value.getString("Source");
                passengerDest = value.getString("Destination");
                Log.d("Passenger", "onEvent: " + passengerSource + passengerDest);
            }
        });

        //Query
        Query query = firestore.collection("Drivers");
//        Log.d("Mobile", " "+MobileNo);

        //RecyclerOptions
        FirestoreRecyclerOptions<DriverDetailsList> options = new FirestoreRecyclerOptions.Builder<DriverDetailsList>()
                .setQuery(query, DriverDetailsList.class)
                .build();

        //RecyclerAdapter
        adapter = new FirestoreRecyclerAdapter<DriverDetailsList, DriverViewHolder>(options) {
            @NonNull
            @Override
            public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // Log.d("COUNT", "onCreateViewHolder: "+viewType);
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driverlist1, parent, false);
                return new DriverViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DriverViewHolder holder, int position, @NonNull final DriverDetailsList model) {
                Log.d("COUNT", "onCreateViewHolder: " + position);


                if (passengerSource.equalsIgnoreCase(model.getSource()) || passengerSource.equalsIgnoreCase(model.getStop1()) || passengerSource.equalsIgnoreCase(model.getStop2()) || passengerSource.equals(model.getStop3())
                        || passengerSource.equalsIgnoreCase(model.getStop4()) || passengerSource.equalsIgnoreCase(model.getDestination())) {
                    if (passengerDest.equalsIgnoreCase(model.getStop1()) || passengerDest.equalsIgnoreCase(model.getStop2()) || passengerDest.equalsIgnoreCase(model.getStop3())
                            || passengerDest.equalsIgnoreCase(model.getStop4()) || passengerDest.equalsIgnoreCase(model.getDestination())) {

                        holder.cardView.setVisibility(View.VISIBLE);
                        holder.noDrivers.setVisibility(View.GONE);

                        Log.d("Passenger", "onBindViewHolder: " + passengerSource + passengerDest);

                        if (model.getStop1() == "") {
                            holder.Dstop1.setVisibility(View.GONE);
                        } else {
                            holder.Dstop1.setText("Stop 1: " + model.getStop1());
                        }

                        if (model.getStop2() == "") {
                            holder.Dstop2.setVisibility(View.GONE);
                        } else {
                            holder.Dstop2.setText("Stop 2: " + model.getStop2());
                        }

                        if (model.getStop3() == "") {
                            holder.Dstop3.setVisibility(View.GONE);
                        } else {
                            holder.Dstop3.setText("Stop 3: " + model.getStop3());
                        }

                        if (model.getStop4() == "") {
                            holder.Dstop4.setVisibility(View.GONE);
                        } else {
                            holder.Dstop4.setText("Stop 4: " + model.getStop4());
                        }


                        holder.Dname.setText("Name:" + model.getName());
                        holder.Dsource.setText("Source:" + model.getSource());
                        holder.Ddestination.setText("Destination:" + model.getDestination());
                        holder.Dfare.setText("Fare:" + model.getFare());


                        holder.Dcall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Intent.ACTION_CALL);
                                i.setData(Uri.parse("tel:" + model.getMobile()));
                                if (ActivityCompat.checkSelfPermission(DriverDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    Toast.makeText(DriverDetailsActivity.this, "Please Grant the Permission", Toast.LENGTH_SHORT).show();
                                    requestPermission();
                                } else {
                                    startActivity(i);
                                }
                            }
                        });
                    }
                } else {
                    holder.cardView.setVisibility(View.GONE);
                    if (position == 0) {
                        holder.noDrivers.setVisibility(View.VISIBLE);
                    }
                }
            }

        };

        driverRV.setHasFixedSize(true);
        driverRV.setLayoutManager(new LinearLayoutManager(this));
        driverRV.setAdapter(adapter);

    }


    public class DriverViewHolder extends RecyclerView.ViewHolder {
        TextView Dname, Dsource, Ddestination, Dfare, Dstop1, Dstop2, Dstop3, Dstop4, noDrivers;
        ImageView Dcall;
        CardView cardView;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);

            Dname = itemView.findViewById(R.id.nameTV);
            Dsource = itemView.findViewById(R.id.sourceTV);
            Ddestination = itemView.findViewById(R.id.destinationTV);
            Dfare = itemView.findViewById(R.id.fareTv);
            Dcall = itemView.findViewById(R.id.callButton);
            Dstop1 = itemView.findViewById(R.id.stop1Text);
            Dstop2 = itemView.findViewById(R.id.stop2Text);
            Dstop3 = itemView.findViewById(R.id.stop3Text);
            Dstop4 = itemView.findViewById(R.id.stop4Text);
            cardView = itemView.findViewById(R.id.cardView);
            noDrivers = itemView.findViewById(R.id.noDriversText);

        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}