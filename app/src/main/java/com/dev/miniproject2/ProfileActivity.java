package com.dev.miniproject2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    CardView edit, tempBtn;
    FirebaseAuth auth;
    StorageReference storage;
    FirebaseFirestore firestore;
    FirebaseUser user;
    String name, mail, mobile;
    String userID;
    TextView nameText, nameSet, mobileText, mobileSet, mailText, mailSet, tempText;
    ImageView imageView;
    StorageReference fileRef, profileRef;
    LinearLayout tempLayout;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        //TextViews
        tempText = findViewById(R.id.tempText);
        nameText = findViewById(R.id.nameText);
        nameSet = findViewById(R.id.nameSet);
        mobileText = findViewById(R.id.mobileText);
        mobileSet = findViewById(R.id.mobileSet);
        mailText = findViewById(R.id.mailText);
        mailSet = findViewById(R.id.mailSet);

        //CardView
        tempBtn = findViewById(R.id.tempBtn);
        edit = findViewById(R.id.editBtn);

        //Layouts
        tempLayout = findViewById(R.id.tempLayout);
        scrollView = findViewById(R.id.scrollView);

        //Firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();
        userID = auth.getCurrentUser().getUid();

        //ImageView
        imageView = findViewById(R.id.imageView);


        if (!user.isEmailVerified()) {
            tempLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            scrollView.setVisibility(View.VISIBLE);
            tempLayout.setVisibility(View.GONE);
            showData();
        }

    }

    public void showData() {

        DocumentReference dr = firestore.collection("users").document(userID);
        dr.addSnapshotListener(ProfileActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                try {
                    nameSet.setText(value.getString("name"));
                    mobileSet.setText(value.getString("mobile"));
                    mailSet.setText(value.getString("mail"));

                } catch (Exception e) {
                    Log.e("DTS 1", "Exception caught " + e.getMessage());

                }

                try {
                    profileRef = storage.child("users/" + auth.getCurrentUser().getUid() + "/profile_image");
                    profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(imageView);
                        }
                    });
                } catch (Exception e) {
                    Log.d("Exception", "Profile Pic Exception" + e.getMessage());
                }
            }
        });

    }

    public void editProfile(View view) {
     startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
     finish();
    }


    public void logOut(View view) {
        auth.signOut();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public void verifyMail(View view) {
        user = auth.getCurrentUser();
        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ProfileActivity.this, "Verification link is sent to your email address, click on the link to verify", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Error in sending email..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProfileActivity.this, SelectionActivity.class));
        finish();
    }
}