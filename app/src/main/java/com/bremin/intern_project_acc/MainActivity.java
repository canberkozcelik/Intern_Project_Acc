package com.bremin.intern_project_acc;

import android.app.Activity;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {

    private static final String TAG = "com.intern.accelerometer.MainActivity";

    private PowerManager.WakeLock wakeLock;
    private SimulationView simulationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);

        simulationView = new SimulationView(this);
        setContentView(simulationView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
        simulationView.startSimulation();

    }

    @Override
    protected void onPause() {
        super.onPause();
        simulationView.stopSimulation();
        wakeLock.release();
    }
}