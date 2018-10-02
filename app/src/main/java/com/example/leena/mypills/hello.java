package com.example.leena.mypills;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class hello extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view_2);

        lottieAnimationView.playAnimation();

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(hello.this, "Authentication successful.",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(hello.this, Map_data.class));
                finish();

            }

        }, 1000L);
    }
}
