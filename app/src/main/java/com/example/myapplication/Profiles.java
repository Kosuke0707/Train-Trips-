package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Profiles extends AppCompatActivity {

    TextView profileName, profileEmail, profilePassword;
    Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePassword = findViewById(R.id.profilePassword);
        editProfile = findViewById(R.id.Btn_EditProfile);

        showAllUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });

    }

    public void showAllUserData(){
        Intent intent = getIntent();
        String emailUser = intent.getStringExtra("email");
        String nameUser = intent.getStringExtra("name");
        String passwordUser = intent.getStringExtra("password");

        profileEmail.setText(emailUser);
        profileName.setText(nameUser);
        profilePassword.setText(passwordUser);
    }

    public void passUserData(){
        String emailUser = profileEmail.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(emailUser);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String emailFromDB = snapshot.child(emailUser).child("email").getValue(String.class);
                    String nameFromDB = snapshot.child(emailUser).child("name").getValue(String.class);
                    String passwordFromDB = snapshot.child(emailUser).child("password").getValue(String.class);

                    Intent intent = new Intent(Profiles.this, EditProfileActivity.class);

                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("password", passwordFromDB);

                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
