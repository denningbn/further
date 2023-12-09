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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private SettingsDAO settingsDao;

    Button startRun, viewHistory, viewSettings, viewCycles, viewPersonal;

    private ActivityResultLauncher<Intent> someActivityResultLauncher;


    private Settings currentSettings;


    private Observable<List<Run>> databaseObservable;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startRun = findViewById(R.id.start_run);
        viewHistory = findViewById(R.id.view_history);
        viewSettings = findViewById(R.id.view_settings);
        viewPersonal = findViewById(R.id.b_personal);



        initSettings();


        startRun.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RunActivity.class);

            startActivity(intent);
        });

        viewPersonal.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PersonalRecord.class);

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


            RunDAO runDao = appDatabase.runDao();


            //emitter.onNext(settingsDao.getSettingsById(2));
            emitter.onNext(runDao.getSortedRuns());
            emitter.onComplete();
        });

        disposable = databaseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {

                    ArrayList<Run> rList = new ArrayList<>();

                    rList.addAll(s);
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        initSettings();
    }
}