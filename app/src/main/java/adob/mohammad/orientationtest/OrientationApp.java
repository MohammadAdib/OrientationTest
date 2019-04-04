package adob.mohammad.orientationtest;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OrientationApp extends Application implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mRotationSensor;
    private int mLastOrientation;
    private static OrientationApp sInstance;
    private List<OnSensorOrientationChangeListener> mOrientationChangeListeners;

    private static final int SENSOR_DELAY = 500000; // 500ms

    public static OrientationApp getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mOrientationChangeListeners = new ArrayList<>();
        try {
            mSensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
            mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY);
        } catch (Exception e) {
            Toast.makeText(this, "Hardware compatibility issue", Toast.LENGTH_LONG).show();
        }
    }

    public void addSensorOrientationChangeListener(OnSensorOrientationChangeListener listener) {
        mOrientationChangeListeners.add(listener);
        listener.onSensorOrientationChanged(mLastOrientation);
    }

    public void removeSensorOrientationChangeListener(OnSensorOrientationChangeListener listener) {
        mOrientationChangeListeners.remove(listener);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mRotationSensor) {
            if (event.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                update(truncatedRotationVector);
            } else {
                update(event.values);
            }
        }
    }

    private void update(float[] vectors) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
        double pitch = Math.toDegrees(orientation[1]);
        double roll = Math.toDegrees(orientation[2]);
        if (Math.abs(roll) > 70 && Math.abs(pitch) < 70) {
            onSensorOrientationChange(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (Math.abs(roll) < 20 && Math.abs(pitch) < 70) {
            onSensorOrientationChange(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void onSensorOrientationChange(int orientation) {
        if(mLastOrientation != orientation) {
            for (OnSensorOrientationChangeListener listener : mOrientationChangeListeners) {
                if (listener != null)
                    listener.onSensorOrientationChanged(orientation);
            }
            mLastOrientation = orientation;
        }
    }
}
