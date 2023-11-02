package com.example.further;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private SettingsDAO settingsDao;

    Button startRun, viewHistory, viewSettings, viewCycles;

    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    public boolean coarseFineAccuracy;
    public boolean slowFastInterval;
    public boolean encrypt;

    TextView tv_encrypt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startRun = findViewById(R.id.start_run);
        viewHistory = findViewById(R.id.view_history);
        viewSettings = findViewById(R.id.view_settings);
        viewCycles = findViewById(R.id.view_cycles);

        coarseFineAccuracy = true;
        slowFastInterval = true;
        encrypt = true;

        appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
        settingsDao = appDatabase.settingsDao();

        Settings settings = new Settings();





        someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent retIntent = result.getData();
                        if (retIntent != null) {
                            coarseFineAccuracy = retIntent.getBooleanExtra("coarseFineAccuracy", true);
                            slowFastInterval = retIntent.getBooleanExtra("slowFastInterval", true);
                            encrypt = retIntent.getBooleanExtra("encrypt", true);
                        }
                    }
                });


        startRun.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RunActivity.class);

            intent.putExtra("coarseFineAccuracy", coarseFineAccuracy);
            intent.putExtra("slowFastInterval", slowFastInterval);
            intent.putExtra("encrypt",encrypt);

            startActivity(intent);
        });


        viewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        viewSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);

            intent.putExtra("coarseFineAccuracy", coarseFineAccuracy);
            intent.putExtra("slowFastInterval", slowFastInterval);
            intent.putExtra("encrypt",encrypt);

            someActivityResultLauncher.launch(intent);
        });

        viewCycles.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CycleActivity.class);
            startActivity(intent);
        });


    }
}