package com.mobiai.views.beforeafterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
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

    BeforeAfterRunner runner = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BeforeAfter beforeAfter = (BeforeAfter) findViewById(R.id.before_after);
        beforeAfter.setBeforeImage(BitmapFactory.decodeResource(getResources(), R.drawable.a));
        beforeAfter.setAfterImage(BitmapFactory.decodeResource(getResources(), R.drawable.anh1));


        output = getFilesDir().getAbsolutePath().concat("/out.mp4");


        ((EditText)findViewById(R.id.edtStep)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    runner.setStep(Integer.valueOf(s.toString()));
                } catch (Exception e){

                }

            }
        });


        ((EditText)findViewById(R.id.edtDelay)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                try {
                    runner.setDelay(Integer.valueOf(s.toString()));
                } catch (Exception e){

                }
            }
        });


        ((View)findViewById(R.id.record)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runner.startSlideAndRecord(new BeforeAfterRunner.OnEncodedListener() {
                    @Override
                    public void onStart() {
                        if (!isDestroyed())
                            runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Start record", Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onCompleted(@NonNull String output) {

                        if (!isDestroyed())
                            runOnUiThread(() -> Toast.makeText(HomeActivity.this, "Finish record", Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onEncodedFrame(@NonNull BeforeAfterRunner.Frame frame) {

                    }
                });
            }
        });


        try {
            androidSequenceEncoder = AndroidSequenceEncoder.create30Fps(new File(output));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        beforeAfter.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (runner != null) return;

                runner = new BeforeAfterRunner(beforeAfter, beforeAfter.getMeasuredWidth());

                runner.start();

                getLifecycle().addObserver(runner);


                ((EditText)findViewById(R.id.edtStep)).setText(String.valueOf(runner.getStep()));
                ((EditText)findViewById(R.id.edtDelay)).setText(String.valueOf(runner.getDelay()));


            }
        });




    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}