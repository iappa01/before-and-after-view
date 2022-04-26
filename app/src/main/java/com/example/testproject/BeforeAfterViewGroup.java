package com.example.testproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BeforeAfterViewGroup extends ConstraintLayout {
    Context context;
    BeforeAfterView beforeAfterView;
    private String stringBefore = "Before";
    private String stringAfter = "After";
    TextView tvStringBefore;
    TextView tvStringAfter;
    public BeforeAfterViewGroup(@NonNull Context context) {
        super(context);
        beforeAfterView = new BeforeAfterView(context);
        this.addView(beforeAfterView);
    }

    public BeforeAfterViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        beforeAfterView = new BeforeAfterView(context);
        this.addView(beforeAfterView);
    }

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.YELLOW);
        paint.setTextSize(100);
        Log.e("My String", "onDraw: ");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public BeforeAfterViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        beforeAfterView = new BeforeAfterView(context);
    }
    public void setBeforeImage(int beforeImage){
        beforeAfterView.setBackground_picture(beforeImage);
    }
    public void setAfterImage(int afterImage){
        beforeAfterView.setForeground_picture(afterImage);
    }
    public void setBeforeAfterX(float x){
        beforeAfterView.setX(x);
    }
    public float getBeforeAfterX(){
        return beforeAfterView.getX();
    }
    public float getCurScale(){
        return beforeAfterView.curScale;
    }

    public void setStringBefore(String stringBefore) {
        this.stringBefore = stringBefore;
    }

    public void setStringAfter(String stringAfter) {
        this.stringAfter = stringAfter;
    }
}
