package com.mobiai.views.beforeafterapp;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.mobiai.views.beforeafter.BeforeAfter;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BeforeAfter beforeAfter = (BeforeAfter) findViewById(R.id.before_after);
        beforeAfter.setBeforeImage(BitmapFactory.decodeResource(getResources(), R.drawable.b));
        beforeAfter.setAfterImage(BitmapFactory.decodeResource(getResources(), R.drawable.anh_nang));
    }
}