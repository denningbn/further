package com.example.further;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button startRun, viewHistory, viewSettings, viewCycles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startRun = findViewById(R.id.start_run);
        viewHistory = findViewById(R.id.view_history);
        viewSettings = findViewById(R.id.view_settings);
        viewCycles = findViewById(R.id.view_cycles);

        startRun.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RunActivity.class);
            startActivity(intent);
        });


        viewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        viewSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        viewCycles.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CycleActivity.class);
            startActivity(intent);
        });
    }
}