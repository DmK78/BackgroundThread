package ru.job4j.backgroundthread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private volatile boolean stopThread = false;
    private TextView textView;
    private int count = 0;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        LoadImage loadImage = new LoadImage();
        new Thread(loadImage).start();

    }

    public void startThread(View view) {
        Toast.makeText(this, "Thread was started", Toast.LENGTH_SHORT).show();
        TestRunnable runnable = new TestRunnable(10);
        new Thread(runnable).start();


    }

    public void stopThread(View view) {
        Toast.makeText(this, "Thread was stopped", Toast.LENGTH_SHORT).show();
        if (!stopThread) {
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

            while (count != times) {
                if (count != 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(String.valueOf(count * 10) + " %");
                        }
                    });
                }
                if (stopThread) {
                    stopThread = false;
                    break;
                }
                Log.d(TAG, "start thread " + count);
                count++;
                if (count == 10) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap loadImageFromNetwork(String url) {
        Bitmap bitmap = null;
        try {
            bitmap=BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return bitmap;
    }

    class LoadImage implements Runnable {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = loadImageFromNetwork("https://static.boredpanda.com/blog/wp-content/uploads/2014/03/cute-smiling-animals-2.jpg ");
                    imageView.setImageBitmap(bitmap);
                    imageView.animate().alpha(1).setDuration(10000);
                    Toast.makeText(getApplicationContext(), "The image was loaded", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}