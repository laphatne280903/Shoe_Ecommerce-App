package com.example.giaodienchinh_doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.giaodienchinh_doan.MainActivity;
import com.example.giaodienchinh_doan.User;
import com.example.giaodienchinh_doan.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class Register extends AppCompatActivity {
    private static final String EMAIL = "email";
    EditText textEmail,textPass;
    Button Reg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    CallbackManager callbackManager;
    LoginButton FbLogin;

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(Register.this, "Account existed.",
                    Toast.LENGTH_SHORT).show();
            Intent intent= new Intent(getApplicationContext(), com.example.giaodienchinh_doan.Login.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textEmail=findViewById(R.id.editTextUsername);
        textPass = findViewById(R.id.editTextPassword);
        Reg =findViewById(R.id.buttonRegister);
        mAuth= FirebaseAuth.getInstance();
        progressBar= findViewById(R.id.progressBar);
        textView=findViewById(R.id.textViewLogin);
        callbackManager = CallbackManager.Factory.create();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), com.example.giaodienchinh_doan.Login.class);
                startActivity(intent);
                finish();
            }
        });



        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email,pass;
                email=String.valueOf(textEmail.getText());
                pass = String.valueOf(textPass.getText());
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this,"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(Register.this,"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Tạo người dùng thành công trong Authentication
                                    Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT).show();

                                    // Tạo một người dùng mới trong Realtime Database
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userId = user.getUid();
                                        User newUser = new User(email, "defaultName", "defaultPhone", userId, "FCM", "https://firebasestorage.googleapis.com/v0/b/shoe-f5860.appspot.com/o/images%2Flogo.jpg?alt=media&token=c77954b5-ca5d-454d-a736-4a4cd6cf92b0&_gl=1*1i7zkeo*_ga*MTU4ODI1NjA2OC4xNjk1NjkyOTM3*_ga_CW55HF8NVT*MTY5OTQ0NDE5OS40OC4xLjE2OTk0NDQyNjMuNTkuMC4w");
                                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                                        usersRef.child(userId).setValue(newUser)
                                                .addOnSuccessListener(aVoid -> {
                                                    Log.d("TAG", "New user added to Realtime Database");
                                                    // Chuyển sang màn hình đăng nhập
                                                    Intent intent = new Intent(getApplicationContext(), com.example.giaodienchinh_doan.Login.class);
                                                    startActivity(intent);
                                                    finish();
                                                })
                                                .addOnFailureListener(e -> Log.e("TAG", "Failed to add user to Realtime Database: " + e.getMessage()));
                                    }

                                } else {
                                    // Nếu quá trình đăng ký thất bại, hiển thị thông báo cho người dùng.
                                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}