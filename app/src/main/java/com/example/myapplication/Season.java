package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class Season extends AppCompatActivity {
    EditText nameEditText, emailEditText, idProofEditText, phoneEditText, dobEditText;
    RadioGroup genderRadioGroup;
    RadioButton maleRadioButton, femaleRadioButton, othersRadioButton;

    DatabaseReference databaseReference;
    FirebaseUser user;
    Calendar dobCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email_address);
        idProofEditText = findViewById(R.id.idproof);
        phoneEditText = findViewById(R.id.Phone);
        dobEditText = findViewById(R.id.DoB);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        maleRadioButton = findViewById(R.id.Male);
        femaleRadioButton = findViewById(R.id.Female);
        othersRadioButton = findViewById(R.id.Others);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            loadUserData();
        }

        dobCalendar = Calendar.getInstance();
        updateDobLabel();

        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Season.this, dobDatePickerListener, dobCalendar.get(Calendar.YEAR),
                        dobCalendar.get(Calendar.MONTH), dobCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        findViewById(R.id.Btn_season_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
        showAlertDialog();
    }

    private void loadUserData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String idProof = snapshot.child("idProof").getValue(String.class);
                    String phone = snapshot.child("phoneNumber").getValue(String.class);
                    String dob = snapshot.child("dob").getValue(String.class);
                    String gender = snapshot.child("gender").getValue(String.class);

                    nameEditText.setText(name);
                    emailEditText.setText(email);
                    idProofEditText.setText(idProof);
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
                Toast.makeText(Season.this,"Error please enter valid credentials!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserProfile() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String idProof = idProofEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();

        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedGenderRadioButton = findViewById(selectedGenderId);
        String gender = selectedGenderRadioButton.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(idProof)
                || TextUtils.isEmpty(phone) || TextUtils.isEmpty(dob) || gender.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idProof.length() != 12) {
            Toast.makeText(this, "Please enter a valid 12-digit Aadhar card number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save user profile to Firebase
        databaseReference.child("name").setValue(name);
        databaseReference.child("email").setValue(email);
        databaseReference.child("idProof").setValue(idProof);
        databaseReference.child("phoneNumber").setValue(phone);
        databaseReference.child("dob").setValue(dob);
        databaseReference.child("gender").setValue(gender);

        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateDobLabel() {
        if (dobEditText != null) {
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            dobEditText.setText(sdf.format(dobCalendar.getTime()));
        }
    }
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention");
        builder.setMessage("This feature will be available very soon!!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private DatePickerDialog.OnDateSetListener dobDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dobCalendar.set(Calendar.YEAR, year);
            dobCalendar.set(Calendar.MONTH, month);
            dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDobLabel();
        }
    };
}