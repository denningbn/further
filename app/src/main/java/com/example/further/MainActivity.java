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

    private Settings currentSettings;
    private long settingsId;

    TextView tv_encrypt;
    Button b_encrypt;

    private Observable<Settings> databaseObservable;
    private Disposable disposable;

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

        initSettings();


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

        b_encrypt.setOnClickListener(v -> {
            initSettings();
            tv_encrypt.setText(currentSettings.toString());
        });

    }

    private void initSettings(){
        databaseObservable = Observable.create(emitter -> {
            appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
            settingsDao = appDatabase.settingsDao();

            currentSettings = settingsDao.getSettingsById(2);

            if (currentSettings == null){
                currentSettings = new Settings(2);
                settingsDao.insert(currentSettings);
            }

            emitter.onNext(settingsDao.getSettingsById(2));
            emitter.onComplete();
        });

        disposable = databaseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    tv_encrypt.setText(s.toString());
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        initSettings();
    }
}