package com.example.further;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RunActivity extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    TextView tv_node, tv_runid;

    LocationRequest locationRequest;

    FusedLocationProviderClient fusedLocationProviderClient;

    private LocationCallback locationCallback;

    //bool to describe whether it's coarse/0 or fine/1

    //runtime location object storage
    private LocationNode<Location> first;
    private LocationNode<Location> last;

    private boolean paused;
    private String startPausedRun, pauseRun;

    Button b_pause, b_end;

    Observable<Settings> settingsObservable;
    Observable<Run> runObservable;
    Disposable disposable, runDisposable;

    AppDatabase appDatabase;

    SettingsDAO settingsDao;

    RunDAO runDao;

    Settings currentSettings;

    double dis, metersInMile;
    String pace;
    boolean trackingLocation;

    int time;

    double[] currentBests;


    ArrayList<Double> lats;
    ArrayList<Double> longs;
    ImageView dg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        //ui elements
        tv_runid = findViewById(R.id.tv_runid);
        b_end = findViewById(R.id.b_end);
        b_pause = findViewById(R.id.b_pause);
        dg = findViewById(R.id.img_dgmode);


        initVars();

        b_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paused)
                {
                        stopLocationUpdates();
                        paused = true;
                        b_pause.setText(startPausedRun);
                }
                else {
                    startLocationUpdates();
                    paused = false;
                    b_pause.setText(pauseRun);
                }
            }
        });

        b_end.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                stopLocationUpdates();
                saveRun();
            }
        });




        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null) {
                    return;
                }
                locLoop(locationResult.getLocations());
            }
        };

        initTracking();
        updateGPS();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    protected void onResume(){
        super.onResume();

        if (trackingLocation){
            startLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case PERMISSIONS_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                }
                else {
                    Toast.makeText(this,"This app requires permission to work properly." ,Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void updateGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null){
                    }
                    else {
                    }
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }

    private void saveRun(){
        dis = roundTo(dis, 3);
        setTime();
        paceCalculation();
        runSplitLoop();
        saveCoords();

        Run run = new Run(dis, pace);

        runObservable = Observable.create(emitter -> {
            appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
            runDao = appDatabase.runDao();

            long id = runDao.insert(run);

            Run run1 = runDao.getRunById(id);

            emitter.onNext(run1);
            emitter.onComplete();
        });

        runDisposable = runObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                        tv_runid.setText(s.toString());
                });
    }

    private LocationRequest createLocReq(){
        int priority;
        long interval;

        if (currentSettings.coarseFineAccuracy){
            priority = Priority.PRIORITY_HIGH_ACCURACY;
        }
        else{
            priority = Priority.PRIORITY_LOW_POWER;
        }

        if (currentSettings.slowFastInterval){
            interval = 5 * 1000;
        }
        else{
            interval = 20 * 1000;
        }

        return new LocationRequest.Builder(priority, interval).setMinUpdateIntervalMillis(5000).build();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    public void getSettings(){
        settingsObservable = Observable.create(emitter -> {
            appDatabase = AppDatabaseSingleton.getDatabaseInstance(this);
            settingsDao = appDatabase.settingsDao();

            Settings s = settingsDao.getSettingsById(2);

            emitter.onNext(s);
            emitter.onComplete();
        });

        disposable = settingsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    currentSettings = s;

                    if (currentSettings.dgMode){
                        dg.setVisibility(View.VISIBLE);
                    }
                    else {
                        dg.setVisibility(View.INVISIBLE);
                    }

                });
    }

    private void addLocation(Location location){
        last = first.addNode(location);
    }


    private void initVars(){
        //initiate all variables at the beginning of the function
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        first = new LocationNode(null);

        currentSettings = new Settings();
        getSettings();

        locationRequest = createLocReq();

        dis = 0;

        startPausedRun = "Start Paused Run";
        pauseRun = "Pause Run";

        trackingLocation = true;

        metersInMile = 1609.3;

        currentBests = initBests();
    }

    private double[] initBests()
    {
        double[] gottenBests = {-1, -1, -1, -1, -1, -1, -1, -1};

        return gottenBests;
    }

    private void initTracking(){
        if (trackingLocation) {
            startLocationUpdates();
        }
    }

    private void locLoop(List<Location> locationsGotten){
        for (Location location : locationsGotten) {
            updateGPS();
            addLocation(location);
            if ((last.getData() != null) & (last.getPrev().getData() != null)) {
                double distanceBetweenPoints = metersToMiles(last.getData().distanceTo(last.getPrev().getData()));
                double speed = last.getData().getSpeed();

                distanceBetweenPoints = roundTo(distanceBetweenPoints, 3);

                if ((distanceBetweenPoints >0.001) && (speed > 0)) {
                    dis += distanceBetweenPoints;
                    roundTo(dis, 2);
                }

            }
            if (currentSettings.dgMode)
            {
                tv_runid.setText("Not far enough.");
            }
            else {
                tv_runid.setText(Double.toString(dis));
            }
        }
    }

    private double metersToMiles(float meters){
        return meters / 1609.34;
    }

    private double roundTo(double value,int decimalPlaces){

        BigDecimal originalBigDecimal = new BigDecimal(Double.toString(value));
        BigDecimal roundedBigDecimal = originalBigDecimal.setScale(decimalPlaces, RoundingMode.DOWN);

        return roundedBigDecimal.doubleValue();

    }

    private void setTime() {
        long first_time = 0;
        long last_time = 0;
        long total_time = 0;
        if ((first != null) && (last != null))
        {
            Location second = (Location) first.getNext().getData();
            first_time = second.getTime();
            last_time = last.getData().getTime();

            total_time = last_time - first_time;

            time = (int) total_time;
        }

    }

    private void paceCalculation() {

        double minutes = 0;
        double seconds = 0;

        time = time / 1000;

        minutes = time / 60;
        seconds = time % 60;

        String result = String.format("%d:%02d", (int) minutes, (int) seconds);

        pace = result;
    }

    private double splitCalculation(LocationNode<Location> node, double distance)
    {
        double currentDistance = 0;
        LocationNode<Location> currentNode = node;
        LocationNode<Location> next = null;

        while (currentNode.getNext() != null)
        {
            next = currentNode.getNext();
            currentDistance += currentNode.getData().distanceTo(next.getData());

            currentDistance = metersToMiles((float) currentDistance);

            if (currentDistance >= distance)
            {
               return (next.getData().getTime() - node.getData().getTime());
            }
            currentNode = currentNode.getNext();
        }
        return -1; //did not run this distance, so return -1 to show it's not valid
    }

    private void runSplitLoop()
    {
        LocationNode<Location> iter = first.getNext();

        while (iter.getNext() != null)
        {
            runSplitCalculation(iter);
            iter = iter.getNext();
        }
    }

    private void runSplitCalculation(LocationNode<Location> iter)
    {
        double[] bests = new double[8];
        bests[0] = splitCalculation(iter, 1 * metersInMile);
        bests[1] = splitCalculation(iter, 5 * metersInMile);
        bests[2] = splitCalculation(iter, 10 * metersInMile);
        bests[3]= splitCalculation(iter, 13.1 * metersInMile);
        bests[4]= splitCalculation(iter, 26.2 * metersInMile);

        bests[5]= splitCalculation(iter, 1000);
        bests[6]= splitCalculation(iter, 5000);
        bests[7]= splitCalculation(iter, 10000);

        compareBests(bests);
    }

    private void compareBests(double[] calculatedBests)
    {
        for (int i = 0; i < calculatedBests.length; i++)
        {
            if ((calculatedBests[i] < currentBests[i]) && (calculatedBests[i] != -1))
            {
                currentBests[i] = calculatedBests[i]; // if the calculated best is lower than the current best and is not -1, it's the new currentbest
            }
        }
    }

    private void saveCoords()
    {
        lats = new ArrayList<Double>();
        longs = new ArrayList<Double>();
        Location foo = null;

        for (LocationNode<Location> iter = first; iter.getData() != null; iter = iter.getNext())
        {
            foo = (Location) iter.getData();

            lats.add(foo.getLatitude());
            longs.add(foo.getLongitude());
        }


    }
}