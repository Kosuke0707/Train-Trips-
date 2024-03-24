package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupPassword, confirm, phone;
    TextView loginRedirectText;
    Button signupButton;
    RadioGroup genderRadioGroup;
    RadioButton maleRadioButton, femaleRadioButton, othersRadioButton;
    CheckBox termsCheckBox;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupName = findViewById(R.id.username);
        signupEmail = findViewById(R.id.email_address);
        signupPassword = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        phone = findViewById(R.id.Phone);
        loginRedirectText = findViewById(R.id.LoginRedirectText);
        signupButton = findViewById(R.id.Btn_Register);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        maleRadioButton = findViewById(R.id.Male);
        femaleRadioButton = findViewById(R.id.Female);
        othersRadioButton = findViewById(R.id.Others);
        termsCheckBox = findViewById(R.id.termsCheckBox);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateFields()) {
                    return;
                }

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String password = signupPassword.getText().toString();
                String confirmPassword = confirm.getText().toString();
                String phoneNumber = phone.getText().toString();
                String gender = getSelectedGender();

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!termsCheckBox.isChecked()) {
                    Toast.makeText(SignupActivity.this, "Please accept terms and conditions!", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseHelper databasehelper = new DatabaseHelper(name, email, password, phoneNumber, gender);
                reference.child(name).setValue(databasehelper);

                Toast.makeText(SignupActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateFields() {
        if (signupName.getText().toString().isEmpty() ||
                signupEmail.getText().toString().isEmpty() ||
                signupPassword.getText().toString().isEmpty() ||
                confirm.getText().toString().isEmpty() ||
                phone.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
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
}
