package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class History extends AppCompatActivity {

    private TextView txtShowHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("Show Ticket");

        // Initialize views
        txtShowHistory = findViewById(R.id.txtShowHistory);

        // Retrieve ticket history from Firebase
        retrieveTicketHistoryFromFirebase();
    }

    private void retrieveTicketHistoryFromFirebase() {
        // Get the UID of the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            // Get a reference to the user's node in the Firebase Realtime Database
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

            // Attach a ValueEventListener to the tickets node
            userRef.child("tickets").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Iterate through the ticket data and append it to the TextView
                    StringBuilder ticketHistory = new StringBuilder();
                    for (DataSnapshot ticketSnapshot : dataSnapshot.getChildren()) {
                        String ticketMessage = ticketSnapshot.child("message").getValue(String.class);
                        ticketHistory.append(ticketMessage).append("\n\n");
                    }

                    // Display the ticket history in the TextView
                    txtShowHistory.setText(ticketHistory.toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors
                    txtShowHistory.setText("Failed to retrieve ticket history: " + databaseError.getMessage());
                }
            });
        } else {
            // No user is currently signed in
            txtShowHistory.setText("No user signed in.");
        }
    }
}
