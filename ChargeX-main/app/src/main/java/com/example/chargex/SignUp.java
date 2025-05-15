package com.example.chargex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void SignUp(View v){
        TextView email_view = findViewById(R.id.SignUpEmailAddress);
        TextView pw_view = findViewById(R.id.SignUpPassword);

        String email = email_view.getText().toString().trim();
        String pw = pw_view.getText().toString();

        // Validate email format using Android's pattern
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pw.length() < 8) {
            Toast.makeText(this, "Password has to be at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", pw);

        // Save user data with document ID = email (you might want to lowercase it for consistency)
        DocumentReference doc = db.collection("Person").document(email.toLowerCase());

        doc.set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUp.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(getApplicationContext(), LogIn.class);
                    startActivity(login);
                    finish(); // Close signup activity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignUp.this, "Signup failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void LogIn(View v){
        Intent login = new Intent(getApplicationContext(), LogIn.class);
        startActivity(login);
    }
}
