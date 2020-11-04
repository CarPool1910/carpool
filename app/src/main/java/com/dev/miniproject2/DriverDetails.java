package com.dev.miniproject2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Button;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DriverDetails extends AppCompatActivity {

    FirebaseFirestore firestore;
    RecyclerView driverRV;

    FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        firestore = FirebaseFirestore.getInstance();
        driverRV = findViewById(R.id.driverList);

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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driverlist1, parent, false);
                return new DriverViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull DriverViewHolder holder, int position, @NonNull final DriverDetailsList model) {



//                holder.Dname.setText("Name:" + Drivername);
                holder.Dname.setText("Name:"+model.getName());
                holder.Dsource.setText("Source:" + model.getSource());
                holder.Ddestination.setText("Destination:" + model.getDestination());
                holder.Dfare.setText("Fare:" + model.getFare());


                //Call Onclick
                holder.Dcall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:"+model.getMobile()));
                        if (ActivityCompat.checkSelfPermission(DriverDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(DriverDetails.this, "Please Grant the Permission", Toast.LENGTH_SHORT).show();
                            requestPermission();
                        } else {
                            startActivity(i);
                        }
                    }
                });
            }
        };

        driverRV.setHasFixedSize(true);
        driverRV.setLayoutManager(new LinearLayoutManager(this));
        driverRV.setAdapter(adapter);

    }

    public class DriverViewHolder extends RecyclerView.ViewHolder {
        TextView  Dname,Dsource, Ddestination, Dfare;
        Button Dcall;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);

            Dname=itemView.findViewById(R.id.nameTV);
            Dsource = itemView.findViewById(R.id.sourceTV);
            Ddestination = itemView.findViewById(R.id.destinationTV);
            Dfare = itemView.findViewById(R.id.fareTv);
            Dcall = itemView.findViewById(R.id.callButton);


//            Dcall.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent i = new Intent(Intent.ACTION_CALL);
//                    i.setData(Uri.parse("tel:"+MobileNo));
//                    if (ActivityCompat.checkSelfPermission(DriverDetails.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(DriverDetails.this, "Please Grant the Permission", Toast.LENGTH_SHORT).show();
//                        requestPermission();
//                    } else {
//                        startActivity(i);
//                    }
//                }
//            });
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