package adob.mohammad.orientationtest;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class RealTestActivity extends AppCompatActivity implements OnSensorOrientationChangeListener {

    private static int setOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_test);
        getSupportActionBar().hide();
        OrientationApp.getInstance().addSensorOrientationChangeListener(this);
        findViewById(R.id.flip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flipOrientation();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void flipOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            setOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        } else {
            // In portrait
            setOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        setRequestedOrientation(setOrientation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OrientationApp.getInstance().removeSensorOrientationChangeListener(this);
    }

    @Override
    public void onSensorOrientationChanged(int orientation) {
        if (setOrientation == orientation) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
                    setRequestedOrientation(setOrientation);
                    findViewById(R.id.switched).setVisibility(View.VISIBLE);
                }
            }, 1000);
        }
    }
}
