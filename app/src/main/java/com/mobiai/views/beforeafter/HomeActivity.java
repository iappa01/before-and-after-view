package com.mobiai.views.beforeafter;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.BitmapFactory;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BeforeAfter beforeAfter = (BeforeAfter) findViewById(R.id.before_after);
        beforeAfter.setBeforeImage(BitmapFactory.decodeResource(getResources(), R.drawable.b));
        beforeAfter.setAfterImage(BitmapFactory.decodeResource(getResources(), R.drawable.ba));
    }
}