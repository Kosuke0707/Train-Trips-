package com.example.myapplication;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Bookings extends AppCompatActivity {

    private Spinner spinnerSource, spinnerDestination, spinnerJourneyType, spinnerPassengers, spinnerPayment, spinnerJourneyClass;
    private DatabaseReference stationsRef;
    private Button btnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        setTitle("Bookings");

        // Initialize spinners
        spinnerSource = findViewById(R.id.spinnerSource);
        spinnerDestination = findViewById(R.id.spinnerDestination);
        spinnerJourneyType = findViewById(R.id.spinnerjtype);
        spinnerPassengers = findViewById(R.id.spinnerpassengers);
        spinnerPayment = findViewById(R.id.spinnerpayment);
        btnBook = findViewById(R.id.Btn_book);
        spinnerJourneyClass = findViewById(R.id.spinnerclass);

        // Initialize Firebase
        stationsRef = FirebaseDatabase.getInstance().getReference().child("stations");

        // Retrieve station data and populate spinners
        retrieveStations();

        // Set click listener for book button
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if source and destination are the same
                String source = spinnerSource.getSelectedItem().toString();
                String destination = spinnerDestination.getSelectedItem().toString();

                if (source.equals(destination)) {
                    showNoTrainsAvailableDialog();
                } else {
                    // Navigate to ticket summary fragment
                    navigateToTicketSummary();
                }
            }
        });
    }

    private void retrieveStations() {
        stationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> stationNames = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String stationName = snapshot.child("name").getValue(String.class);
                    stationNames.add(stationName);
                }

                // Add hint to the beginning of the station names list
                stationNames.add(0, "Select Station");

                // Populate spinners
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Bookings.this, android.R.layout.simple_spinner_item, stationNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Set hint as default item
                spinnerSource.setPrompt("Select Source");
                spinnerDestination.setPrompt("Select Destination");
                spinnerSource.setAdapter(adapter);
                spinnerDestination.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        // Populate journey type spinner
        ArrayAdapter<CharSequence> journeyTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.journey_types, android.R.layout.simple_spinner_item);
        journeyTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJourneyType.setAdapter(journeyTypeAdapter);

        // Populate passengers spinner
        ArrayAdapter<CharSequence> passengersAdapter = ArrayAdapter.createFromResource(this,
                R.array.passenger_numbers, android.R.layout.simple_spinner_item);
        passengersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPassengers.setAdapter(passengersAdapter);

        // Populate payment method spinner
        ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, android.R.layout.simple_spinner_item);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayment.setAdapter(paymentAdapter);

        // Populate class method spinner
        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(this,
                R.array.train_class, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJourneyClass.setAdapter(classAdapter);
    }

    private void navigateToTicketSummary() {
        // Get selected values from spinners
        String source = spinnerSource.getSelectedItem().toString();
        String destination = spinnerDestination.getSelectedItem().toString();
        String journeyType = spinnerJourneyType.getSelectedItem().toString();
        String passengers = spinnerPassengers.getSelectedItem().toString();
        String payment = spinnerPayment.getSelectedItem().toString();
        String journeyClass = spinnerJourneyClass.getSelectedItem().toString();

        // Create intent to start TicketSummaryActivity
        Intent intent = new Intent(Bookings.this, TicketSummary.class);
        // Pass data to TicketSummaryActivity
        intent.putExtra("source", source);
        intent.putExtra("destination", destination);
        intent.putExtra("journeyType", journeyType);
        intent.putExtra("passengers", passengers);
        intent.putExtra("payment", payment);
        intent.putExtra("journeyClass", journeyClass);
        // Start TicketSummaryActivity
        startActivity(intent);
    }


    private void showNoTrainsAvailableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Trains Available");
        builder.setMessage("There are no trains available for the selected pair of stations.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
