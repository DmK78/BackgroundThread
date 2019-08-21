package ru.job4j.backgroundthread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startThread(View view) {
        Toast.makeText(this, "Thread was started", Toast.LENGTH_SHORT).show();
        TestRunnable runnable = new TestRunnable(10);
        new Thread(runnable).start();
    }

    public void stopThread(View view) {
        Toast.makeText(this, "Thread was stopped", Toast.LENGTH_SHORT).show();
        if(!stopThread){
            stopThread = true;
        }
    }

    class TestRunnable implements Runnable {
        int times;

        public TestRunnable(int times) {
            this.times = times;
        }

        @Override
        public void run() {
            int count = 0;
            while (count != times) {
                if (stopThread) {
                    stopThread=false;
                    break;
                }
                Log.d(TAG, "start thread " + count);
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
