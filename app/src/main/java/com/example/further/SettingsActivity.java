package com.example.further;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    boolean coarseFineAccuracy = true;
    boolean slowFastInterval = true;

    boolean encrypt = true;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw_interval, sw_location, sw_encrypt;

    Button b_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sw_interval = findViewById(R.id.sw_interval);
        sw_location = findViewById(R.id.sw_location);
        sw_encrypt = findViewById(R.id.sw_encrypt);

        b_settings = findViewById(R.id.b_settings);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            coarseFineAccuracy = extras.getBoolean("coarseFineAccuracy");
            slowFastInterval = extras.getBoolean("slowFastInterval");
            encrypt = extras.getBoolean("encrypt");
        }


        sw_interval.setChecked(coarseFineAccuracy);
        sw_location.setChecked(slowFastInterval);
        sw_encrypt.setChecked(encrypt);

        sw_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slowFastInterval = sw_interval.isChecked();
            }
        });


        sw_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coarseFineAccuracy = sw_location.isChecked();
            }
        });


        sw_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                encrypt = sw_encrypt.isChecked();
            }
        });

        b_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra("encrypt", encrypt);
                resultIntent.putExtra("coarseFineAccuracy", coarseFineAccuracy);
                resultIntent.putExtra("slowFastInterval", slowFastInterval);
                setResult(RESULT_OK, resultIntent);
            }
        });
    }
}