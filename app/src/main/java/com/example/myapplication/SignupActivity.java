package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Locale;
public class SignupActivity extends AppCompatActivity {
    TextView loginRedirectText;
    EditText signupName, signupEmail, signupPassword, confirm, phone,dobEditText;
    Button signupButton;
    RadioGroup genderRadioGroup;
    RadioButton maleRadioButton, femaleRadioButton, othersRadioButton;
    CheckBox termsCheckBox;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    Calendar dobCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        loginRedirectText = findViewById(R.id.LoginRedirectText);
        signupName = findViewById(R.id.username);
        signupEmail = findViewById(R.id.email_address);
        signupPassword = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        phone = findViewById(R.id.Phone);
        signupButton = findViewById(R.id.Btn_Register);
        dobEditText = findViewById(R.id.udob);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        maleRadioButton = findViewById(R.id.Male);
        femaleRadioButton = findViewById(R.id.Female);
        othersRadioButton = findViewById(R.id.Others);
        termsCheckBox = findViewById(R.id.termsCheckBox);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        dobCalendar = Calendar.getInstance();
        updateDobLabel();

        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignupActivity.this, dobDatePickerListener, dobCalendar.get(Calendar.YEAR),
                        dobCalendar.get(Calendar.MONTH), dobCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }



    private void createUser() {
        final String name = signupName.getText().toString().trim();
        final String email = signupEmail.getText().toString().trim();
        final String password = signupPassword.getText().toString().trim();
        final String confirmPassword = confirm.getText().toString().trim();
        final String phoneNumber = phone.getText().toString().trim();
        final String gender = getSelectedGender();
        final String dob = dobEditText.getText().toString().trim();

        if (!validateFields(name, email, password, confirmPassword, phoneNumber)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserData(user.getUid(), name, email, phoneNumber, gender, dob);
                            Toast.makeText(SignupActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignupActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateFields(String name, String email, String password, String confirmPassword, String phoneNumber) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!termsCheckBox.isChecked()) {
            Toast.makeText(this, "Please accept terms and conditions!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String getSelectedGender() {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId == maleRadioButton.getId()) {
            return "Male";
        } else if (selectedId == femaleRadioButton.getId()) {
            return "Female";
        } else if (selectedId == othersRadioButton.getId()) {
            return "Others";
        } else {
            return "";
        }
    }

    private void saveUserData(String userId, String name, String email, String phoneNumber, String gender, String dob) {
        User user = new User( name, email, phoneNumber, gender, dob);
        mDatabase.child("users").child(userId).setValue(user);
    }
    private void updateDobLabel() {
        if (dobEditText != null) {
            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            dobEditText.setText(sdf.format(dobCalendar.getTime()));
        }
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

