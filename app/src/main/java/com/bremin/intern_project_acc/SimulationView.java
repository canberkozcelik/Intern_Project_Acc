package com.bremin.intern_project_acc;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Canberk on 24/06/2015.
 */

public class SimulationView extends View implements SensorEventListener {

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Display display;
    private Bitmap gameTable;
    private static final int BALL_SIZE = 36;
    private static final int HOLE_SIZE = 102;

    private boolean gameOver = false;

    private float xOrigin;
    private float yOrigin;

    private float horizontalBound;
    private float verticalBound;

    private float sensorX;
    private float sensorY;
    private float sensorZ;
    private long sensorTimeStamp;

    private Particle whiteBall = new Particle();
    private Bitmap whiteBitmap;

    @SuppressWarnings("deprecation")
    public SimulationView(Context context) {
        super(context);

        WindowManager mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        setmDisplay(mWindowManager.getDefaultDisplay());

        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        setmAccelerometer(sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

        Bitmap whiteB = BitmapFactory.decodeResource(getResources(),
                R.drawable.whiteballl);

        whiteBitmap = Bitmap.createScaledBitmap(whiteB, BALL_SIZE, BALL_SIZE,
                true);
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();

        Bitmap table = BitmapFactory.decodeResource(getResources(),
                R.drawable.table);
        gameTable = Bitmap.createScaledBitmap(table, display.getWidth(),
                display.getHeight(), true);

    }

    public void startSimulation() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopSimulation() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            sensorX = event.values[0];
            sensorY = event.values[1];
            sensorZ = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(sensorX + sensorY + sensorZ - last_x
                        - last_y - last_z)
                        / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {

                }
                last_x = sensorX;
                last_y = sensorY;
                last_z = sensorZ;

                sensorTimeStamp = event.timestamp;

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        xOrigin = w * 0.5f;
        yOrigin = h * 0.5f;

        horizontalBound = (w - BALL_SIZE) * 0.5f;
        verticalBound = (h - BALL_SIZE) * 0.5f;

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                getContext());

        //Draw game table
        canvas.drawBitmap(gameTable, 0, 0, null);

        if (!whiteBall.resolveCollisionWithBounds(horizontalBound + HOLE_SIZE,
                verticalBound + HOLE_SIZE)) {
            canvas.drawBitmap(whiteBitmap, (xOrigin - BALL_SIZE / 2)
                    + whiteBall.mPosX, (yOrigin - BALL_SIZE / 2)
                    - whiteBall.mPosY, null);

            whiteBall.updatePosition(sensorX, sensorY, sensorZ,
                    sensorTimeStamp);
        } else {
            dialogBuilder.setTitle("Game Over");
            dialogBuilder.setMessage("You Lose");
            dialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gameOver = true;
                            resetGame(canvas);
                        }
                    });
            dialogBuilder.show();
        }

        invalidate();
    }

    public Sensor getmAccelerometer() {
        return accelerometer;
    }

    public void setmAccelerometer(Sensor mAccelerometer) {
        this.accelerometer = mAccelerometer;
    }

    public Display getmDisplay() {
        return display;
    }

    public void setmDisplay(Display mDisplay) {
        this.display = mDisplay;
    }

    public void resetGame(Canvas canvas) {
        gameOver = false;
        whiteBall.setmPosX(0);
        whiteBall.setmPosY(0);

    }

}