package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.SeekBar;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BeforeAfterView beforeAfterView = (BeforeAfterView) findViewById(R.id.bfat);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                beforeAfterView.setBackground_picture(R.drawable.back);
                beforeAfterView.setForeground_picture(R.drawable.fore);
                beforeAfterView.setxReal(50*beforeAfterView.getWidth()/100);
                beforeAfterView.setX(50*beforeAfterView.getWidth()/100);
                beforeAfterView.invalidate();
            }
        }, 1000);
        MultiTouchListener.setBeforeAfterView(beforeAfterView);
        beforeAfterView.setOnTouchListener(new MultiTouchListener());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int preI = seekBar.getProgress();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int deltaX = (preI - i)*beforeAfterView.getWidth()/seekBar.getMax();
                beforeAfterView.setxReal(beforeAfterView.getxReal() - deltaX);
                if (beforeAfterView.getX() - deltaX < 0){
                    beforeAfterView.setX(0);
                }else if(beforeAfterView.getX() - deltaX < beforeAfterView.getWidth()){
                    beforeAfterView.setX(beforeAfterView.getX() - deltaX);
                }else {
                    beforeAfterView.setX(beforeAfterView.getWidth());
                }
                beforeAfterView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}