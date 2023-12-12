package com.example.further;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.further.databinding.ActivityPersonalRecordBinding;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PersonalRecord extends AppCompatActivity {
    private AppDatabase appDatabase;

    private SettingsDAO settingsDao;
    private Observable<Settings> databaseObservable;
    private Settings currentSettings;

    private Disposable disposable;

    private TextView t_bestOneM, t_bestFiveM, t_bestTenM, t_bestHalfMar, t_bestMar, t_bestOneKil, t_bestFiveKil, t_bestTenKil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_record);

        initPRs();
    }
    private void initPRs(){
        databaseObservable = Observable.create(emitter -> {
            appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
            settingsDao = appDatabase.settingsDao();

            currentSettings = settingsDao.getSettingsById(2);

            if (currentSettings == null){
                currentSettings = new Settings(2);
                settingsDao.insert(currentSettings);
            }




            emitter.onNext(currentSettings);
            emitter.onComplete();
        });

        disposable = databaseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getPRs(s);
                });
    }

    private void getPRs(Settings s)
    {

        TextView[] tv_array = new TextView[]{   t_bestOneM, t_bestFiveM, t_bestTenM, t_bestHalfMar,
                                                t_bestMar, t_bestOneKil, t_bestFiveKil, t_bestTenKil};

        for (int i = 0; i < tv_array.length;i++)
        {
            if (s.bests[i] != -1)
                tv_array[i].setText(secondsToMinutes(s.bests[i]));
        }
    }

    @SuppressLint("DefaultLocale")
    private String secondsToMinutes(double sec)
    {
        double minutes =  sec / 60;
        double seconds =  sec % 60;

        return String.format("%d:%02d", (int) minutes, (int) seconds);
    }
}