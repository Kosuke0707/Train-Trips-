package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView mapCard;
    private CardView bookCard;
    private CardView historyCard;
    private CardView profileCard;
    private CardView seasonCard;
    private CardView expressCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapCard = findViewById(R.id.mapCard);
        bookCard = findViewById(R.id.bookCard);
        historyCard = findViewById(R.id.historyCard);
        profileCard = findViewById(R.id.profileCard);
        seasonCard = findViewById(R.id.seasonCard);
        expressCard = findViewById(R.id.expressCard);

        mapCard.setOnClickListener(this);
        bookCard.setOnClickListener(this);
        historyCard.setOnClickListener(this);
        profileCard.setOnClickListener(this);
        seasonCard.setOnClickListener(this);
        expressCard.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.item1) {
            startActivity(new Intent(MainActivity.this, SignupActivity.class));
            return true;
        } else if (itemId == R.id.item2) {
            startActivity(new Intent(MainActivity.this, Notifications.class));
            return true;
        } else if (itemId == R.id.item3) {
            startActivity(new Intent(MainActivity.this, AboutUs.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.mapCard) {
            startActivity(new Intent(MainActivity.this, Maps.class));
            Toast.makeText(this, "Maps", Toast.LENGTH_SHORT).show();
        } else if (viewId == R.id.bookCard) {
            startActivity(new Intent(MainActivity.this, Bookings.class));
            Toast.makeText(this, "Bookings", Toast.LENGTH_SHORT).show();
        } else if (viewId == R.id.historyCard) {
            startActivity(new Intent(MainActivity.this, History.class));
            Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
        } else if (viewId == R.id.seasonCard) {
            startActivity(new Intent(MainActivity.this, Season.class));
            Toast.makeText(this, "Season Pass", Toast.LENGTH_SHORT).show();
        } else if (viewId == R.id.profileCard) {
            startActivity(new Intent(MainActivity.this, Profiles.class));
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        } else if (viewId == R.id.expressCard) {
            startActivity(new Intent(MainActivity.this, Express.class));
            Toast.makeText(this, "Express Booking", Toast.LENGTH_SHORT).show();
        }
    }
}
