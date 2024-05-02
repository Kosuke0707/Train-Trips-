package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TicketSummary extends AppCompatActivity {

    private TextView txtSource, txtDestination, txtJourneyType, txtPassengers, txtPayment, txtJourneyClass, txtFare;
    private Button btn_pay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketsummary);
        setTitle("Ticket Summary");

        // Initialize views
        txtSource = findViewById(R.id.txtSource);
        txtDestination = findViewById(R.id.txtDestination);
        txtJourneyType = findViewById(R.id.txtJourneyType);
        txtPassengers = findViewById(R.id.txtPassengers);
        txtPayment = findViewById(R.id.txtPayment);
        txtJourneyClass = findViewById(R.id.txtJourneyClass);
        txtFare = findViewById(R.id.txtFare);
        btn_pay = findViewById(R.id.Btn_pay);

        // Get data from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String source = extras.getString("source");
            String destination = extras.getString("destination");
            String journeyType = extras.getString("journeyType");
            String passengers = extras.getString("passengers");
            String payment = extras.getString("payment");
            String journeyClass = extras.getString("journeyClass");

            // Get source and destination positions from Firebase Realtime Database
            DatabaseReference stationRef = FirebaseDatabase.getInstance().getReference().child("stations");
            stationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int sourcePos = 0;
                    int destPos = 0;
                    for (DataSnapshot stationSnapshot : dataSnapshot.getChildren()) {
                        String stationName = stationSnapshot.child("name").getValue(String.class);
                        if (stationName.equals(source)) {
                            sourcePos = stationSnapshot.child("pos").getValue(Integer.class);
                        } else if (stationName.equals(destination)) {
                            destPos = stationSnapshot.child("pos").getValue(Integer.class);
                        }
                    }
                    double fare = calculateFare(journeyType, passengers, journeyClass, sourcePos, destPos);

                    // Display ticket details
                    txtSource.setText("Source: " + source);
                    txtDestination.setText("Destination: " + destination);
                    txtJourneyType.setText("Journey Type: " + journeyType);
                    txtPassengers.setText("Passengers: " + passengers);
                    txtPayment.setText("Payment Method: " + payment);
                    txtJourneyClass.setText("Journey Class: " + journeyClass);
                    txtFare.setText("Fare: \u20B9" + fare);

                    // Set click listener for pay button
                    btn_pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Redirect to PaymentActivity and pass the fare amount
                            Intent intent = new Intent(TicketSummary.this, PaymentActivity.class);
                            intent.putExtra("fareAmount", fare);
                            intent.putExtra("source", source);
                            intent.putExtra("destination", destination);
                            intent.putExtra("journeyType", journeyType);
                            intent.putExtra("passengers", passengers);
                            intent.putExtra("journeyClass", journeyClass);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }

    private double calculateFare(String journeyType, String passengers, String journeyClass, int sourcePos, int destPos) {
        double baseFare = 10.0; // Base fare per passenger
        int passengerCount = Integer.parseInt(passengers);
        double farePerPassenger = baseFare;

        // Calculate fare based on journey type
        if (journeyType.equals("Journey")) {
            // Additional fare based on position difference
            int posDifference = Math.abs(destPos - sourcePos);
            if (posDifference >= 5) {
                farePerPassenger += 5.0; // Additional fare for stations more than 5 away from source
            }
        } else if (journeyType.equals("Return")) {
            // Calculate fare without round-trip discount first
            double oneWayFare = baseFare;

            // Additional fare based on position difference
            int posDifference = Math.abs(destPos - sourcePos);
            if (posDifference >= 5) {
                oneWayFare += 5.0; // Additional fare for stations more than 5 away from source
            }
            // Additional fare for line change
            if ((sourcePos <= 15 && destPos >= 21) || (sourcePos >= 21 && destPos <= 15)) {
                oneWayFare += 5.0; // Additional fare for line change (Central to Western or vice versa)
            }

            // Additional fare for first class
            if (journeyClass.equals("First Class(I)")) {
                oneWayFare *= 3.0; // First class surcharge
            }

            // Calculate return fare with round-trip discount
            farePerPassenger = 2.0 * oneWayFare;
        }

        return farePerPassenger * passengerCount;
    }
}
