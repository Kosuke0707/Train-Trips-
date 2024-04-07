package com.example.myapplication;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Bookings extends AppCompatActivity {

    private Spinner spinnerSource, spinnerDestination;
    private DatabaseReference stationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        setTitle("Bookings");
        // Initialize spinners
        spinnerSource = findViewById(R.id.spinnerSource);
        spinnerDestination = findViewById(R.id.spinnerDestination);

        // Initialize Firebase
        stationsRef = FirebaseDatabase.getInstance().getReference().child("stations");

        // Retrieve station data and populate spinners
        retrieveStations();
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
                // Populate spinners
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Bookings.this, android.R.layout.simple_spinner_item, stationNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSource.setAdapter(adapter);
                spinnerDestination.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
