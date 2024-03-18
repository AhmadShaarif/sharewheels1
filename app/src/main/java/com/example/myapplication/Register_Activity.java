package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register_Activity extends AppCompatActivity {
    private EditText text_email;
    private EditText text_name;
    private EditText text_password;
    private FirebaseAuth auth;

    private FirebaseFirestore fstore;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        text_email = findViewById(R.id.text_email);
        text_name = findViewById(R.id.text_name);
        text_password = findViewById(R.id.text_password);
        Button button_register = findViewById(R.id.button_register);
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        Button back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_Activity.this, MainActivity.class));
            }
        });
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_text_name = text_name.getText().toString();
                String txt_text_email = text_email.getText().toString();
                String txt_text_password = text_password.getText().toString();

                if(TextUtils.isEmpty(txt_text_email) || TextUtils.isEmpty(txt_text_password) || TextUtils.isEmpty(txt_text_name)){
                    Toast.makeText(Register_Activity.this, "Empty Credentials", Toast.LENGTH_SHORT).show();
                } else if (txt_text_password.length() < 6) {
                    Toast.makeText(Register_Activity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_text_email, txt_text_password, txt_text_name);
                }
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registerUser(String text_email, String text_password, String text_name) {
        auth.createUserWithEmailAndPassword(text_email, text_password).addOnCompleteListener(Register_Activity.this , new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Register_Activity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    userID = auth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("fName", text_name);
                    user.put("email", text_email);
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                         @Override
                                                                         public void onSuccess(Void unused) {
                                                                             Log.d("TAG", "onSuccess: user profile is created for"+ userID);
                                                                         }
                                                                     });
                            startActivity(new Intent(Register_Activity.this, Dashboard_Activity.class));
                } else{
                    Toast.makeText(Register_Activity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }}


