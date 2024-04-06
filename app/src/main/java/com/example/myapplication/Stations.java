package com.example.myapplication;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Stations extends AppCompatActivity {

    private androidx.appcompat.widget.SearchView searchView;
    private ListView listView;
    private Button allButton, centralButton, westernButton;

    private DatabaseReference stationsRef;
    private ArrayAdapter<String> adapter;
    private List<String> stationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);

        // Initialize views
        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.list);
        allButton = findViewById(R.id.all);
        centralButton = findViewById(R.id.central);
        westernButton = findViewById(R.id.western);

        // Initialize Firebase
        stationsRef = FirebaseDatabase.getInstance().getReference().child("stations");

        // Initialize station list
        stationList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stationList);
        listView.setAdapter(adapter);

        // Load all stations by default
        loadAllStations();

        // Set click listeners for buttons
        allButton.setOnClickListener(v -> loadAllStations());
        centralButton.setOnClickListener(v -> loadStationsByLine("Central"));
        westernButton.setOnClickListener(v -> loadStationsByLine("Western"));

        // Set search functionality
        setSearchFunctionality();
    }

    private void loadAllStations() {
        stationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String stationName = snapshot.child("name").getValue(String.class);
                    stationList.add(stationName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void loadStationsByLine(String line) {
        stationsRef.orderByChild("line").equalTo(line).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String stationName = snapshot.child("name").getValue(String.class);
                    stationList.add(stationName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void setSearchFunctionality() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    loadAllStations();
                } else {
                    searchStations(newText);
                }
                return true;
            }
        });
    }

    private void searchStations(String query) {
        String lowercaseQuery = query.toLowerCase(); // Convert search query to lowercase

        stationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String stationName = snapshot.child("name").getValue(String.class);
                    if (stationName != null) {
                        // Convert station name to lowercase for comparison
                        String lowercaseStationName = stationName.toLowerCase();
                        // Check if the lowercase station name contains the lowercase query
                        if (lowercaseStationName.contains(lowercaseQuery)) {
                            stationList.add(stationName);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
