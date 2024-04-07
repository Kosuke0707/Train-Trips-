package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profiles extends AppCompatActivity {

    TextView nameTextView, emailTextView, phoneTextView, genderTextView, dobTextView;
    Button Btn_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);
        setTitle("Profile");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        nameTextView = findViewById(R.id.nameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        genderTextView = findViewById(R.id.genderTextView);
        dobTextView = findViewById(R.id.DoBTextView);
        Btn_edit = findViewById(R.id.Btn_edit);

        Btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to edit profile activity
                startActivity(new Intent(Profiles.this, EditProfileActivity.class));
            }
        });

        // Retrieve user data from Firebase Realtime Database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String phone = snapshot.child("phoneNumber").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);
                        String dob = snapshot.child("dob").getValue(String.class);
                        // Set retrieved data to TextViews
                        nameTextView.setText(name);
                        emailTextView.setText(email);
                        phoneTextView.setText(phone);
                        genderTextView.setText(gender);
                        dobTextView.setText(dob);
                    } else {
                        // If user data doesn't exist in database
                        Toast.makeText(Profiles.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled event
                    Toast.makeText(Profiles.this, "Failed to retrieve user data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // If no user is currently logged in
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show();
        }
    }
}
