package codetivelab.bluetoothaurdino;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import codetivelab.bluetoothaurdino.helper.ShrdPrfrncsHlpr;

public class SplashActivity extends AppCompatActivity {
    BluetoothAdapter mBtAdapter;
    CountDownTimer countDownTimer;
    ShrdPrfrncsHlpr mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Toast.makeText(SplashActivity.this, "This app does not support your device", Toast.LENGTH_LONG).show();
        }
        mHelper = new ShrdPrfrncsHlpr(getApplicationContext());

        supportsBluetooth();
        if (mHelper.getBoolean()) {
            final int sec = 1000;
            countDownTimer = new CountDownTimer(sec, 1000) {

                public void onTick(long millisUntilFinished) {
                    if (!mBtAdapter.isEnabled()) {
                        mBtAdapter.enable();
                    }
                }

                public void onFinish() {
                    if (mBtAdapter.isEnabled()) {

                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    } else {
                        countDownTimer.start();
                    }
                }

            };
            countDownTimer.start();
        } else {
            final int sec = 700;
            countDownTimer = new CountDownTimer(sec, 1000) {

                public void onTick(long millisUntilFinished) {
                    if (!mBtAdapter.isEnabled()) {
                        mBtAdapter.enable();
                    }
                }

                public void onFinish() {
                    startActivity(new Intent(SplashActivity.this, InformActivity.class));
                    finish();
                }
            };
            countDownTimer.start();
        }
        ;
    }

    private void supportsBluetooth() {

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            finish();
            Toast.makeText(SplashActivity.this,"Your Device Doesnot has Bluetooth",Toast.LENGTH_LONG).show();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBtAdapter.enable();
            }
        }).start();
    }
}
