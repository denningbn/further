package com.example.further;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class SettingsActivity extends AppCompatActivity {
    boolean coarseFineAccuracy = true;
    boolean slowFastInterval = true;

    boolean encrypt = true;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw_interval, sw_location, sw_encrypt;


    TextView tv_match;

    private BehaviorSubject<Settings> settingsSubject;

    private AppDatabase appDatabase;

    private SettingsDAO settingsDao;

    private Settings currentSettings;

    private Settings initialSettings;

    private Observable<Settings> databaseObservable;
    private Observable<Settings> updateObservable;

    private Disposable disposable;
    private Disposable updateDisposable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sw_interval = findViewById(R.id.sw_interval);
        sw_location = findViewById(R.id.sw_location);
        sw_encrypt = findViewById(R.id.sw_encrypt);


        tv_match = findViewById(R.id.tv_match);

        currentSettings = new Settings(2);

        initSettings();

        sw_encrypt.setChecked(currentSettings.encrypt);
        sw_interval.setChecked(currentSettings.slowFastInterval);
        sw_location.setChecked(currentSettings.coarseFineAccuracy);

        sw_interval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.slowFastInterval = sw_interval.isChecked();
                saveSettings();
            }
        });


        sw_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.coarseFineAccuracy = sw_location.isChecked();
                saveSettings();
            }
        });


        sw_encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.encrypt = sw_encrypt.isChecked();
                saveSettings();
            }
        });

    }

    private void saveSettings() {
        databaseObservable = Observable.create(emitter -> {
            appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
            settingsDao = appDatabase.settingsDao();

            settingsDao.update(currentSettings);

            emitter.onNext(settingsDao.getSettingsById(2));
            emitter.onComplete();
        });

        disposable = databaseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{
                    currentSettings = s;

                    sw_encrypt.setChecked(s.encrypt);
                    sw_interval.setChecked(s.slowFastInterval);
                    sw_location.setChecked(s.coarseFineAccuracy);
                });
    }

    private void initSettings() {
        databaseObservable = Observable.create(emitter -> {
            appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
            settingsDao = appDatabase.settingsDao();

            Settings s = settingsDao.getSettingsById(2);

            if (s == null){
                s = currentSettings;
                settingsDao.insert(s);
            }
            else{
                currentSettings = s;
            }

            emitter.onNext(s);
            emitter.onComplete();
        });

        disposable = databaseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{
                    sw_encrypt.setChecked(s.encrypt);
                    sw_interval.setChecked(s.slowFastInterval);
                    sw_location.setChecked(s.coarseFineAccuracy);
                });
    }

}