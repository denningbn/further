package com.example.further;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import android.util.Log;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<Run> databaseRuns;

    private Observable<List<Run>> databaseObservable;
    private AppDatabase appDatabase;
    private RunDAO runDao;
    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getRuns();
    }

    private void getRuns() {
        databaseObservable = Observable.create(emitter -> {
            appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
            runDao = appDatabase.runDao();

            List<Run> runList = runDao.getSortedRuns();

            emitter.onNext(runList);
            emitter.onComplete();
        });

        disposable = databaseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(runList -> {
                    databaseRuns = new ArrayList<>();
                    databaseRuns.addAll(runList);
                    if (databaseRuns != null) {
                        initAdapter();
                    }
                });
    }

    private void initAdapter()
    {
        RV_RunAdapter adapter = new RV_RunAdapter(this, databaseRuns);

        RecyclerView recyclerView = findViewById(R.id.rv_run);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}

