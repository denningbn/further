package com.example.further;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class SettingsActivity extends FragmentActivity implements NoticeDialogFragment.NoticeDialogListener{
    boolean coarseFineAccuracy = true;
    boolean slowFastInterval = true;

    boolean encrypt = true;
    boolean dgMode = false;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch sw_interval, sw_location, sw_encrypt, sw_dgmode;



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
        sw_dgmode = findViewById(R.id.sw_dgmode);

        currentSettings = new Settings(2);

        initSettings();

        sw_encrypt.setChecked(currentSettings.encrypt);
        sw_interval.setChecked(currentSettings.slowFastInterval);
        sw_location.setChecked(currentSettings.coarseFineAccuracy);
        sw_dgmode.setChecked(currentSettings.dgMode);

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

                if (currentSettings.encrypt){
                    showEncryptDialog();
                }
            }
        });

        sw_dgmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings.dgMode = sw_dgmode.isChecked();
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
                    sw_dgmode.setChecked(s.dgMode);
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
                    sw_dgmode.setChecked(s.dgMode);
                });
    }

    private void showEncryptDialog()
    {
        DialogFragment dialogFragment = new NoticeDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "ENCRYPT_DIALOG");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        currentSettings.encrypt = true;
        sw_encrypt.setChecked(true);

        Log.d("ENCRYPT", Boolean.toString(currentSettings.encrypt));
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        currentSettings.encrypt = false;
        sw_encrypt.setChecked(false);
        Log.d("ENCRYPT", Boolean.toString(currentSettings.encrypt));
    }
}