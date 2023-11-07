package com.mobiai.views.beforeafterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;
import android.widget.VideoView;
import com.mobiai.views.beforeafter.BeforeAfter;
import com.mobiai.views.beforeafter.BeforeAfterRunner;
import com.mobiai.views.beforeafter.BeforeAfterSlider;

import org.jcodec.api.SequenceEncoder;
import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.model.Picture;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {
    int a = -1;

    int RIGHT = 1;
    int LEFT = -1;
    int directory = 1; // 1: RIGHT, -1: LEFT

    int step = 40;


    String output;
//    SequenceEncoder sequenceEncoder;

    AndroidSequenceEncoder androidSequenceEncoder;


    CountDownTimer countDownTimer = null;

    ArrayList<Bitmap> bitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BeforeAfter beforeAfter = (BeforeAfter) findViewById(R.id.before_after);
        beforeAfter.setBeforeImage(BitmapFactory.decodeResource(getResources(), R.drawable.a));
        beforeAfter.setAfterImage(BitmapFactory.decodeResource(getResources(), R.drawable.anh1));


        BeforeAfterSlider beforeAfterSlider = beforeAfter.findViewById(com.mobiai.views.beforeafter.R.id.before_after_slider);


        output = getFilesDir().getAbsolutePath().concat("/out.mp4");






        try {

            androidSequenceEncoder = AndroidSequenceEncoder.create30Fps(new File(output));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        beforeAfter.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (countDownTimer != null) return;


                BeforeAfterRunner runner = new BeforeAfterRunner(beforeAfter, beforeAfter.getMeasuredWidth());
                runner.start();



                countDownTimer = new CountDownTimer(10000, 1) {

                    public void onTick(long millisUntilFinished) {

                        if (directory == RIGHT && beforeAfterSlider.getTranslationX() >= beforeAfter.getMeasuredWidth()/2) {
                            directory = LEFT;
                        }

                        if (directory == LEFT && beforeAfterSlider.getTranslationX() <= (-1) * beforeAfter.getMeasuredWidth()/2) {
                            directory = RIGHT;
                        }

                        if (directory == RIGHT) {
                            a = step;
                        } else if (directory == LEFT) {
                            a =  step * (-1);
                        }

                        beforeAfterSlider.moveSlideAndBeforeAfterView(a);


//                        Bitmap bitmap = loadBitmapFromView(beforeAfter);

//                        bitmaps.add(bitmap);



                    }

                    public void onFinish() {

                        if (androidSequenceEncoder != null) {

                            new Thread() {

                                @Override
                                public void run() {
                                    super.run();

                                    try {

                                        for (int i = 0; i < bitmaps.size();    i++) {
                                            androidSequenceEncoder.encodeImage(bitmaps.get(i));
                                        }
                                        androidSequenceEncoder.finish();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    androidSequenceEncoder = null;

                                }
                            }.start();

                        }

                    }

                };
//                .start();
            }
        });




    }





}