package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profiles extends AppCompatActivity {

    TextView textName, textEmail, textPhone, textGender;
    Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        // Initialize views
        textName = findViewById(R.id.text_name);
        textEmail = findViewById(R.id.text_email);
        textPhone = findViewById(R.id.text_phone);
        textGender = findViewById(R.id.text_gender);
        buttonLogout = findViewById(R.id.button_logout);

        // Get user data from intent
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String name = intent.getStringExtra("name");
            String email = intent.getStringExtra("email");
            String phone = intent.getStringExtra("phone");
            String gender = intent.getStringExtra("gender");

            // Set user data to TextViews
            textName.setText("Name: " + name);
            textEmail.setText("Email: " + email);
            textPhone.setText("Phone: " + phone);
            textGender.setText("Gender: " + gender);
        }

        // Logout button click listener
        buttonLogout.setOnClickListener(view -> {
            Intent logoutIntent = new Intent(Profiles.this, LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        });
    }
}
