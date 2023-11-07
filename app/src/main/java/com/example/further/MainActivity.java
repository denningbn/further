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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private SettingsDAO settingsDao;

    Button startRun, viewHistory, viewSettings, viewCycles;

    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    public boolean coarseFineAccuracy;
    public boolean slowFastInterval;
    public boolean encrypt;

    private Settings settings;
    private long settingsId;

    TextView tv_encrypt;
    Button b_encrypt;

    private Observable<Settings> databaseObservable;
    private Settings initialSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startRun = findViewById(R.id.start_run);
        viewHistory = findViewById(R.id.view_history);
        viewSettings = findViewById(R.id.view_settings);
        viewCycles = findViewById(R.id.view_cycles);


        tv_encrypt = findViewById(R.id.tv_encrypt);
        b_encrypt = findViewById(R.id.b_encrypt);

        appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
        settingsDao = appDatabase.settingsDao();

        initialSettings = new Settings();

        databaseObservable  = Observable.create(emitter -> {
                if (settingsDao.getSettings() == null){
                    settingsDao.update(initialSettings);
                }

                emitter.onNext(settingsDao.getSettings());
                emitter.onComplete();
        });

        Disposable disposable = databaseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(settings -> {

                    tv_encrypt.setText(Boolean.toString(settings.encrypt));
                });

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

            someActivityResultLauncher.launch(intent);
        });

        viewCycles.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CycleActivity.class);
            startActivity(intent);
        });

    }

    private void checkDatabase()
    {
        //check if SettingsDatabase exists,
        // if not, initialize it

            Settings settings = settingsDao.getSettings();

            if (settings == null){
                settings.setEncrypt(true);
                settings.setCoarseFineAccuracy(true);
                settings.setSlowFastInterval(true);

                settingsDao.update(settings);
            }
    }


}