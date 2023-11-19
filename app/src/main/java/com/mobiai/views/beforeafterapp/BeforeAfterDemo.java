package com.mobiai.views.beforeafterapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.mobiai.views.beforeafter.BeforeAfter;
import com.mobiai.views.beforeafter.BeforeAfterRunner;
import com.mobiai.views.utils.BitmapsExtKt;

public class BeforeAfterDemo extends AppCompatActivity {

    BeforeAfterRunner runner = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_after);
        BeforeAfter beforeAfter = (BeforeAfter) findViewById(R.id.before_after);

        Slider slider = findViewById(R.id.slider);

        Bitmap before = BitmapFactory.decodeResource(getResources(), R.drawable.anh1);
        Bitmap after = BitmapFactory.decodeResource(getResources(), R.drawable.anh2);
        beforeAfter.setBeforeImage(before);

        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                Bitmap b = BitmapsExtKt.resizeWithLimitWidth(before, 500);
                Bitmap a = BitmapsExtKt.resizeWithLimitWidth(after, 500);

                Bitmap after2 = BitmapsExtKt.mix(b, a, (int) (value  * 100));
                beforeAfter.setAfterImage(after2);

                b.recycle();
                a.recycle();
            }
        });

        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap resultBitmap = BitmapsExtKt.mix(before, after, (int) (slider.getValue()  * 100));
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