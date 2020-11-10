package com.dev.miniproject2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {
    Button logout, tempBtn;
    CardView update;
    FirebaseAuth auth;
    StorageReference storage;
    FirebaseFirestore firestore;
    FirebaseUser user;
    String name, mail, gender, mobile;
    String userID;
    TextView txtName, txtNumber, txtMail, txt2, tempText;
    EditText etName, etNumber, etMail;
    ImageView imageView;
    StorageReference fileRef, profileRef;
    LinearLayout tempLayout;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //---------------------------------------------cast----------------------------------------------//          --------  Buttons  --------
//          --------  ImageView  --------
        imageView = findViewById(R.id.imageView);

//          --------  Buttons  --------
        update = findViewById(R.id.updateBtn);
        tempBtn = findViewById(R.id.tempBtn);

//          --------  Layouts  --------
        tempLayout = findViewById(R.id.tempLayout);
        scrollView = findViewById(R.id.scrollView);


//          --------  Firebase  --------
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = auth.getCurrentUser().getUid();
        user = auth.getCurrentUser();
        storage = FirebaseStorage.getInstance().getReference();


        //          --------  TextViews  --------

        txt2 = findViewById(R.id.txt2);
        tempText = findViewById(R.id.tempText);


//        --------  EditTexts  --------
        etName = (EditText) findViewById(R.id.etName);
        etNumber = (EditText) findViewById(R.id.etNumber);
        etMail = (EditText) findViewById(R.id.etMail);

        //String
        name = "";
        mobile = "";
        mail = "";


        showData();

    }

    public void showData() {

        DocumentReference dr = firestore.collection("users").document(userID);
        dr.addSnapshotListener(EditProfileActivity.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                try {
                    etName.setText(value.getString("name"));
                    etNumber.setText(value.getString("mobile"));
                    etMail.setText(value.getString("mail"));
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


    public void resetPswd(View view) {
        final EditText resetPass = new EditText(view.getContext());
        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
        dialog.setTitle("Change Password");
        dialog.setMessage("Enter new password (at least 6 characters long)");
        dialog.setView(resetPass);

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newPswd = resetPass.getText().toString();
                if (newPswd.length() < 6) {
                    Toast.makeText(EditProfileActivity.this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                }
                user.updatePassword(newPswd).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfileActivity.this, "Password changed  successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Password not changed, " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Do nothing
            }
        });

        dialog.create().show();


    }

    public void updatePicture(View view) {
        Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// uri of particular img
        startActivityForResult(openGallery, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
//                 progressDialog = new ProgressDialog(getApplicationContext());
//                progressDialog.setMessage("Uploading profile picture");
//                progressDialog.show();
                Uri imageUri = data.getData();
                // imageView.setImageURI(imageUri);
                uploadProfilePicture(imageUri);
            }
        }
    }

    public void uploadProfilePicture(Uri imageUri) {
        try {
            //upload image to firebase storage
            fileRef = storage.child("users/" + auth.getCurrentUser().getUid() + "/profile_image");
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(EditProfileActivity.this, "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(imageView);
//                        progressDialog.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, "Error in uploading profile picture", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Exception", "Profile Pic Exception" + e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
        finish();
    }

    public void updateProfile(View view) {

        if (etName.getText().length() == 0) {
            etName.setError("Please fill the name");
        } else {
            name = etName.getText().toString();

        }

        if (etNumber.getText().length() == 0) {
            etNumber.setError("Please fill the name");
        } else {
            mobile = etNumber.getText().toString();

        }
        if (etMail.getText().length() == 0) {
            etMail.setError("Please fill the name");
        } else {
            mail = etMail.getText().toString();

        }

        try {
            DocumentReference updateDetails = firestore.collection("users").document(userID);
            updateDetails.update("name", name);
            updateDetails.update("mobile", mobile);
            updateDetails.update("mail", mail);
            Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception updateException) {
            Log.d("updateProfile: ", "Exception: " + updateException.getMessage());
            Toast.makeText(EditProfileActivity.this, "Error in updating profile, please try again later", Toast.LENGTH_SHORT).show();
        }


        showData();
    }
}