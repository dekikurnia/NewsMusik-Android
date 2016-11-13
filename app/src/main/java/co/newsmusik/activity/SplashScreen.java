package co.newsmusik.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import co.newsmusik.R;

/**
 * Created by deki kurnia on 03/04/16.
 */
public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread timerThread = new Thread(){

            public void run() {
                try {
                    sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();

    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
