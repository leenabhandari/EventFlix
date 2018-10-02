package com.example.leena.mypills;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

public class StartAnimation extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_animation);

        lottieAnimationView = (LottieAnimationView) findViewById(R.id.animation_view);

        lottieAnimationView.playAnimation();

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(StartAnimation.this, LoginActivity.class));
                finish();
            }

        }, 6000L);

    }


}
