package com.dev.miniproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextView txt1, txt2, txtName, txtMail, txtNumber, txtPs, txtPs2;
    EditText etName, etMail, etNumber, etPs, etPs2;
    Button btnRegister;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String userID;
    String name, number, mail, pswd, pswd2;
    RadioButton radioMale, radioFemale;
    String genderDetect;
    RadioGroup radioGrp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//---------------------------------------------cast----------------------------------------------

//          --------  TextViews  --------
        txtName = findViewById(R.id.txtName);
        txtNumber = findViewById(R.id.txtMobile);
        txtMail = findViewById(R.id.txtMail);
        txtPs = findViewById(R.id.txtPass);
        txtPs2 = findViewById(R.id.txtPassConfirm);
        txt1 = findViewById(R.id.text1);
        txt2 = findViewById(R.id.text2);

//        --------  EditTexts  --------
        etName = findViewById(R.id.etName);
        etNumber = findViewById(R.id.etNumber);
        etMail = findViewById(R.id.etMail);
        etPs = findViewById(R.id.etPs);
        etPs2 = findViewById(R.id.etPs2);

//        --------  Buttons  --------
        btnRegister = findViewById(R.id.btnSignin);
        radioGrp = findViewById(R.id.radioGrp);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);


//        --------  Firebase  --------
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


//---------------------------------------------listeners----------------------------------------------

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString();
                number = etNumber.getText().toString();
                mail = etMail.getText().toString();
                pswd = etPs.getText().toString();
                pswd2 = etPs2.getText().toString();


                //-------------RadioButton---------------//

                radioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        switch (i) {
                            case R.id.radioMale:
                                genderDetect = "male";
                                break;
                            case R.id.radioFemale:
                                genderDetect = "female";
                        }
                    }
                });

                //-------------RadioButton---------------//


                if (TextUtils.isEmpty(name)) {
                    etName.setError("Please enter name ");
                }
                if (TextUtils.isEmpty(number)) {
                    etNumber.setError("Please enter number");
                }
                if (TextUtils.isEmpty(mail)) {
                    etMail.setError("Please enter valid mail address");
                }
                if (pswd.length() < 6) {
                    etPs.setError("Password should be at least 6 characters");
                }
//                if (pswd != pswd2) {
//                    etPs2.setError("Please enter correct password again");
//                }


                if (mail.length() > 0 && pswd.length() > 6) {
                    auth.createUserWithEmailAndPassword(mail, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {                                 //success of email verification link
                                        Toast.makeText(RegisterActivity.this, "Verification Email is sent on your mail, please click on link to confirm", Toast.LENGTH_SHORT).show();
                                        userID = auth.getCurrentUser().getUid();
                                        DocumentReference dr = firestore.collection("users").document(userID);
                                        Map<String, Object> user = new HashMap<>();


                                        user.put("name", name);
                                        user.put("mobile", number);
                                        user.put("gender", genderDetect);
                                        user.put("mail", mail);
                                        user.put("password", pswd);


                                        dr.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {   //success of data addition
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(RegisterActivity.this, "User Profile created successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(RegisterActivity.this,Selection.class));
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {                   //failure of data addition
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this, "On failure email success triggered", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }

                                }).addOnFailureListener(new OnFailureListener() {       //failure of email verification link
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, "Problem in sending verification email", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error in registering user, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });


    }

    public void goToLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}