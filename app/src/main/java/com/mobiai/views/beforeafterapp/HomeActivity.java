package com.mobiai.views.beforeafterapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import com.mobiai.views.beforeafter.BeforeAfter;
import com.mobiai.views.beforeafter.BeforeAfterRunner;

public class HomeActivity extends AppCompatActivity {

    BeforeAfterRunner runner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BeforeAfter beforeAfter = (BeforeAfter) findViewById(R.id.before_after);
        beforeAfter.setBeforeImage(BitmapFactory.decodeResource(getResources(), R.drawable.a));
        beforeAfter.setAfterImage(BitmapFactory.decodeResource(getResources(), R.drawable.anh1));



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


        beforeAfter.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (runner != null) return;

                runner = new BeforeAfterRunner(beforeAfter, beforeAfter.getMeasuredWidth());

//                runner.start();

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