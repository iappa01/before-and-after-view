package com.example.testproject;

import android.content.Context;
import android.graphics.Canvas;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BeforeAfterSeekBar extends FrameLayout {
    ImageView thumb;
    View view;
    float prePivotX;
    float prePivotY;
    OnHorizontalMoveListener onHorizontalMoveListener;
    String TAG = "My Message";
    OnTouchListener before_after_TouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    prePivotX = motionEvent.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = motionEvent.getX() - prePivotX;
                    horizontalMove(deltaX);
                    if (onHorizontalMoveListener != null){
                        onHorizontalMoveListener.onChange(deltaX);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
            }
            return true;
        }
    };
    OnTouchListener getBefore_after_TouchListenerThumb = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    prePivotX = motionEvent.getX();
                    prePivotY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaY = motionEvent.getY() - prePivotY;
//                    verticalMove(deltaY);
                    float deltaX = motionEvent.getX() - prePivotX;
                    horizontalMove(deltaX);
                    if (onHorizontalMoveListener != null){
                        onHorizontalMoveListener.onChange(deltaX);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
            }
            return true;
        }
    };
    public BeforeAfterSeekBar(@NonNull Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.before_after_seekbar,this);
        thumb = (ImageView)  view.findViewById(R.id.ba_seekbar_thumb);
        this.setOnTouchListener(before_after_TouchListener);
        thumb.setOnTouchListener(getBefore_after_TouchListenerThumb);
    }
    public BeforeAfterSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.before_after_seekbar,this);
        thumb = (ImageView)  view.findViewById(R.id.ba_seekbar_thumb);
        this.setOnTouchListener(before_after_TouchListener);
        thumb.setOnTouchListener(getBefore_after_TouchListenerThumb);
    }

    public void horizontalMove(float deltaX){
        float translateX = this.getTranslationX() + deltaX;
        if (distance > 0){
            if (translateX < -distance){
                translateX = -distance;
            }else if(translateX > distance){
                translateX = distance;
            }
        }else{
            Log.e(TAG, "horizontalMove: < 0");
        }

        this.setTranslationX(translateX);
    }
    public void verticalMove(float deltaY){
        float translateY = thumb.getTranslationY() + deltaY;
        thumb.setTranslationY(translateY);
    }
    public interface OnHorizontalMoveListener {
        void onChange(float deltaX);
    }
    public void setOnHorizontalMoveListener(OnHorizontalMoveListener move){
        onHorizontalMoveListener = move;
    }
    private float distance = 0;
    public void setDistanceMax(float distance){
        this.distance = distance;
    }
}
