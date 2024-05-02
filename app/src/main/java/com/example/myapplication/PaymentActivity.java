package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentActivity extends AppCompatActivity {

    private TextView txtWalletBalance, txtFare;
    private EditText edtAddCredits;
    private Button btnAddCredits, btnPayWithWallet;
    private double walletBalance = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setTitle("Payment");

        // Initialize views
        txtWalletBalance = findViewById(R.id.txtWalletBalance);
        txtFare = findViewById(R.id.txtFare);
        edtAddCredits = findViewById(R.id.edtAddCredits);
        btnAddCredits = findViewById(R.id.btnAddCredits);
        btnPayWithWallet = findViewById(R.id.btnPayWithWallet);

        // Get the fare amount passed from TicketSummary activity
        double fareAmount = getIntent().getDoubleExtra("fareAmount", 0.0);
        txtFare.setText("Journey Fare: \u20B9" + fareAmount);

        // Set onClickListener for adding credits to wallet button
        btnAddCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCreditsToWallet();
            }
        });

        // Set onClickListener for paying with wallet button
        btnPayWithWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payWithWallet(fareAmount);
            }
        });
        // Retrieve and display wallet balance
        retrieveWalletBalanceFromFirebase();
    }

    public void addCreditsToWallet() {
        String amountStr = edtAddCredits.getText().toString();
        if (!amountStr.isEmpty()) {
            double amount = Double.parseDouble(amountStr);
            walletBalance += amount;
            txtWalletBalance.setText("Wallet Balance: \u20B9" + walletBalance);
            saveWalletBalanceToFirebase(walletBalance); // Save wallet balance to Firebase
            Toast.makeText(this, "Credits added successfully!", Toast.LENGTH_SHORT).show();
            edtAddCredits.setText(""); // Clear the EditText after adding credits
        } else {
            Toast.makeText(this, "Please enter an amount to add.", Toast.LENGTH_SHORT).show();
        }
    }

    public void payWithWallet(double fareAmount) {
        if (walletBalance >= fareAmount) {
            walletBalance -= fareAmount;
            txtWalletBalance.setText("Wallet Balance: \u20B9" + walletBalance);
            saveWalletBalanceToFirebase(walletBalance); // Save wallet balance to Firebase
            generateTicket(); // Generate ticket upon successful payment
            Toast.makeText(this, "Payment successful! Fare deducted from wallet.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Insufficient balance in wallet.", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateTicket() {
        // Generate a unique transaction ID
        String transactionId = generateTransactionId();

        // Get current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String dateTime = dateFormat.format(new Date());

        // Create the ticket message
        double fareAmount = getIntent().getDoubleExtra("fareAmount", 0.0);
        String source = getIntent().getStringExtra("source");
        String destination = getIntent().getStringExtra("destination");
        String journeyType = getIntent().getStringExtra("journeyType");
        String passengers = getIntent().getStringExtra("passengers");
        String journeyClass = getIntent().getStringExtra("journeyClass");
        txtFare.setText("Journey Fare: \u20B9" + fareAmount);
        String ticketMessage = "Source: " + source + "\n" +
                "Destination: " + destination + "\n" +
                "Transaction ID: " + transactionId + "\n" +
                "Date and Time: " + dateTime + "\n" +
                "JourneyType: " + journeyType + "\n" +
                "Passengers: " + passengers + "\n" +
                "JourneyClass: " + journeyClass + "\n" +
                "Note: The journey should commence within 1 hour from booking.";

        // Save the ticket to Firebase or any other storage mechanism
        saveTicketToFirebase(ticketMessage);
    }

    private String generateTransactionId() {
        // Generate a unique transaction ID using a combination of date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String timestamp = dateFormat.format(new Date());
        return "TX" + timestamp;
    }

    private void saveTicketToFirebase(String ticketMessage) {
        // Get the UID of the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Get a reference to the user's node in the Firebase Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

            // Generate a unique key for the ticket
            String ticketId = userRef.child("tickets").push().getKey();

            // Create a HashMap to store the ticket data
            Map<String, Object> ticketData = new HashMap<>();
            ticketData.put("message", ticketMessage);

            // Save the ticket data to the user's node under the "tickets" child node
            userRef.child("tickets").child(ticketId).setValue(ticketData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Ticket saved successfully
                            Toast.makeText(getApplicationContext(), "Ticket saved to Firebase!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to save ticket
                            Toast.makeText(getApplicationContext(), "Failed to save ticket: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // No user is currently signed in
            Toast.makeText(getApplicationContext(), "No user signed in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveWalletBalanceToFirebase(double balance) {
        // Get the UID of the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Get a reference to the user's node in the Firebase Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

            // Update the wallet balance for the user
            userRef.child("walletBalance").setValue(balance)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Wallet balance saved successfully
                            Toast.makeText(getApplicationContext(), "Wallet balance saved to Firebase!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to save wallet balance
                            Toast.makeText(getApplicationContext(), "Failed to save wallet balance: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // No user is currently signed in
            Toast.makeText(getApplicationContext(), "No user signed in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void retrieveWalletBalanceFromFirebase() {
        // Get the UID of the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Get a reference to the user's node in the Firebase Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

            // Attach a ValueEventListener to the wallet balance node
            userRef.child("walletBalance").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Get the wallet balance value
                    Double balance = dataSnapshot.getValue(Double.class);
                    if (balance != null) {
                        // Update the UI with the wallet balance
                        walletBalance = balance;
                        txtWalletBalance.setText("Wallet Balance: \u20B9" + walletBalance);
                    } else {
                        // Wallet balance is null
                        Toast.makeText(getApplicationContext(), "Wallet balance is null.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    Toast.makeText(getApplicationContext(), "Failed to retrieve wallet balance: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // No user is currently signed in
            Toast.makeText(getApplicationContext(), "No user signed in.", Toast.LENGTH_SHORT).show();
        }
    }

    // Getter method for wallet balance
    public double getWalletBalance() {
        return walletBalance;
    }

    // Setter method for wallet balance
    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }
}
