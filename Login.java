package com.ds.carpooling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private TextView tvLogin, tvForgotPassword, tvAdmin;
    private EditText email, password;
    private Button login;
    private FirebaseAuth mAuth;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.edt_emailID);
        password = findViewById(R.id.edt_pwd);
        login = findViewById(R.id.btn_loginHere);
        tvForgotPassword = findViewById(R.id.tv_forgotPassword);
        sp = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }

            private void loginUser() {
                String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("email", mail);
                editor.putString("password", pass);
                editor.commit();

                if (mail.isEmpty()) {
                    email.setError("Email Address is Required");
                    email.requestFocus();
                    return;
                }

                if (pass.isEmpty()) {
                    password.setError("Password is Required");
                    password.requestFocus();
                    return;
                }

                if (pass.length() < 6) {
                    password.setError("Min password length should be 6 char");
                    password.requestFocus();
                    return;
                }
                if (mail.equals("adminlogin123@gmail.com") && pass.equals("admin123")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting to admin dashboard...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, Bottom_Admin.class);
                    startActivity(intent);
                } else {
                    mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser().isEmailVerified()) {
                                Intent intent = new Intent(Login.this, Bottom_Navigation.class);
                                startActivity(intent);
                                Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Login.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString().trim();
                if (mail.isEmpty()) {
                    email.setError("Please enter your email for reset");
                    email.requestFocus();
                    return;
                }
                mAuth.sendPasswordResetEmail(email.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Link has been sent to your registered email", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                        });
            }
        });
//
//        tvAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Login.this, Login.class);
//                startActivity(intent);            }
//        });
    }
}