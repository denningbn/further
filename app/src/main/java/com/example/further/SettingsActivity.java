package com.example.further;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class SettingsActivity extends AppCompatActivity {
    boolean coarseFineAccuracy = true;
    boolean slowFastInterval = true;

    boolean encrypt = true;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw_interval, sw_location, sw_encrypt;

    Button b_settings;

    TextView tv_encrypt2;

    private BehaviorSubject<Settings> settingsSubject;

    private AppDatabase appDatabase;

    private SettingsDAO settingsDao;

    private Settings currentSettings;

    private Settings initialSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sw_interval = findViewById(R.id.sw_interval);
        sw_location = findViewById(R.id.sw_location);
        sw_encrypt = findViewById(R.id.sw_encrypt);

        b_settings = findViewById(R.id.b_settings);

        tv_encrypt2 = findViewById(R.id.tv_encrypt2);

        //get the database DAO
        appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
        settingsDao = appDatabase.settingsDao();

        settingsSubject = BehaviorSubject.create();

        initialSettings = settingsDao.getSettings();

        settingsSubject.onNext(initialSettings);

        Disposable disposable = settingsSubject
                .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(updateSettings ->{
                                    settingsDao.update(updateSettings);
                                });



        sw_interval.setChecked(currentSettings.slowFastInterval);
        sw_location.setChecked(currentSettings.coarseFineAccuracy);
        sw_encrypt.setChecked(currentSettings.encrypt);

        sw_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.slowFastInterval = sw_interval.isChecked();

                settingsSubject.onNext(currentSettings);
            }
        });


        sw_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.coarseFineAccuracy = sw_location.isChecked();

                settingsSubject.onNext(currentSettings);
            }
        });


        sw_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.encrypt = sw_encrypt.isChecked();

                settingsSubject.onNext(currentSettings);
            }
        });
    }
}