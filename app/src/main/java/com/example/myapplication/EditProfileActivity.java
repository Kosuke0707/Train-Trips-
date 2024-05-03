package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    EditText nameEditText, emailEditText, phoneEditText, dobEditText;
    RadioGroup genderRadioGroup;
    RadioButton maleRadioButton, femaleRadioButton, othersRadioButton;
    MaterialButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Edit Profile");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        nameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email_address);
        phoneEditText = findViewById(R.id.Phone);
        dobEditText = findViewById(R.id.udob);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        maleRadioButton = findViewById(R.id.Male);
        femaleRadioButton = findViewById(R.id.Female);
        othersRadioButton = findViewById(R.id.Others);
        saveButton = findViewById(R.id.saveButton);

        // Retrieve current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

            // Load existing user data into EditText fields
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String phone = snapshot.child("phoneNumber").getValue(String.class);
                        String dob = snapshot.child("dob").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);

                        nameEditText.setText(name);
                        emailEditText.setText(email);
                        phoneEditText.setText(phone);
                        dobEditText.setText(dob);

                        if ("Male".equals(gender)) {
                            maleRadioButton.setChecked(true);
                        } else if ("Female".equals(gender)) {
                            femaleRadioButton.setChecked(true);
                        } else if ("Others".equals(gender)) {
                            othersRadioButton.setChecked(true);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled event
                    Toast.makeText(EditProfileActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Save button click listener
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
    }

    private void saveUserProfile() {
        String newName = nameEditText.getText().toString().trim();
        String newEmail = emailEditText.getText().toString().trim();
        String newPhone = phoneEditText.getText().toString().trim();
        String newDob = dobEditText.getText().toString().trim();

        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedGenderRadioButton = findViewById(selectedGenderId);
        String newGender = selectedGenderRadioButton.getText().toString();

        if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newPhone)
                || TextUtils.isEmpty(newDob) || TextUtils.isEmpty(newGender)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save updated data to Firebase Realtime Database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            reference.child("name").setValue(newName);
            reference.child("email").setValue(newEmail);
            reference.child("phoneNumber").setValue(newPhone);
            reference.child("dob").setValue(newDob);
            reference.child("gender").setValue(newGender);

            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show();
        }
    }
}
