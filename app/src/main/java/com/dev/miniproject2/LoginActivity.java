package com.dev.miniproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView txt1, txt2, txtMail, txtPs, txt3;
    EditText etMail, etPs;
    CardView btnRegister;
    FirebaseAuth auth;
    String mail, pswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//---------------------------------------------cast----------------------------------------------

//          --------  TextViews  --------

        txt1 = findViewById(R.id.text1);
        txt2 = findViewById(R.id.text2);
        txt3 = findViewById(R.id.text3);

//        --------  EditTexts  --------
        etMail = findViewById(R.id.etMail);
        etPs = findViewById(R.id.etPs);

//        --------  Buttons  --------
        btnRegister = findViewById(R.id.btnSignin);

//        --------  Firebase  --------
        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), SelectionActivity.class));
            finish();
        }

    }

    public void SignIn(View view) {
        mail = etMail.getText().toString();
        pswd = etPs.getText().toString();

        if (mail.length() > 0 && pswd.length() > 6) {
            auth.signInWithEmailAndPassword(mail, pswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, SelectionActivity.class)); //MainActivity
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error Signing in", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, "Logging in failed", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void goToSignIn(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

    public void resetPswd(View view) {
        final EditText resetMail = new EditText(view.getContext());
        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
        dialog.setTitle("Reset Password");
        dialog.setMessage("Enter email to send reset password link");
        dialog.setView(resetMail);

        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //extract email;
                String exMail = resetMail.getText().toString();
                if (exMail.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Enter your Email within Dialog box", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(exMail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(LoginActivity.this, "Password link is sent successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Error in sending reset link " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


//        catch (Exception e){
//            Log.d("Exception",e.getMessage());
//        }
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close the  dialog
            }
        });
        dialog.create().show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}