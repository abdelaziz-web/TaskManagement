package com.example.taskmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    Button cancel;
    String userEmail;
    Button reset;

    private EditText editTextFirstName;
    private EditText editTextFamilyName;
    private EditText editTextPhoneNumber;
    private Button buttonSave;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        // Initialize views
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextFamilyName = findViewById(R.id.editTextFamilyName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonSave = findViewById(R.id.buttonSave);


        userEmail = mAuth.getCurrentUser().getEmail();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });


        reset = findViewById(R.id.resetpass);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail(userEmail);
            }
        });

        Button cancelButton = findViewById(R.id.buttonCancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Settings.this,TasksActivity.class);
                startActivity(in);
            }
        });


    }

    public void changeusername(String username) {

        if (username.equals("")) {
            Toast.makeText(Settings.this,"No username detected",Toast.LENGTH_SHORT).show();

        }

    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Settings.this,"Password reset email sent.",Toast.LENGTH_SHORT).show();
                            Log.d("PasswordResetActivity", "Password reset email sent.");
                            // Handle successful password reset email sent
                        } else {
                            Toast.makeText(Settings.this,"Error sending password reset email.",Toast.LENGTH_SHORT).show();
                            Log.e("PasswordResetActivity", "Error sending password reset email.", task.getException());
                            // Handle error sending password reset email
                        }
                    }


                });
    }


    private void saveUserData() {
        String firstName = editTextFirstName.getText().toString().trim();
        String familyName = editTextFamilyName.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(firstName)) {
            editTextFirstName.setError("Enter first name");
            editTextFirstName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(familyName)) {
            editTextFamilyName.setError("Enter family name");
            editTextFamilyName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            editTextPhoneNumber.setError("Enter phone number");
            editTextPhoneNumber.requestFocus();
            return;
        }

        // Get the current user email
        String userEmail = mAuth.getCurrentUser().getEmail();

        // Create a map with updated user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("prenom", firstName);
        userData.put("nom", familyName);
        userData.put("tel", phoneNumber);


        db.collection("user").document(userEmail)
                .update(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Settings.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Settings.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
