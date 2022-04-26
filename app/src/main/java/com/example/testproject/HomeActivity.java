package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BeforeAfterView beforeAfterView = (BeforeAfterView) findViewById(R.id.bfat);
        beforeAfterView.setBackground_picture(R.drawable.back);
        beforeAfterView.setForeground_picture(R.drawable.fore);
        BeforeAfterSeekBar beforeAfterSeekBar = (BeforeAfterSeekBar) findViewById(R.id.bfat_sb);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                beforeAfterSeekBar.setDistanceMax(beforeAfterView.getWidth()/2);
            }
        },1000);
        beforeAfterSeekBar.setOnHorizontalMoveListener(new BeforeAfterSeekBar.OnHorizontalMoveListener() {
            @Override
            public void onChange(float deltaX) {
                beforeAfterView.setX(beforeAfterView.getX() + deltaX / beforeAfterView.curScale);
            }
        });
    }
}