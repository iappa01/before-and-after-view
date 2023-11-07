package com.mobiai.views.beforeafter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

public class BeforeAfterSlider extends RelativeLayout {
    ImageView thumb;
    View line;
    View view;
    float prePivotX;
    LinearLayout llText;

    OnMoveHorizontalListener onMoveHorizontalListener;

    OnTouchListener sliderMoveHandle = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    prePivotX = motionEvent.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = motionEvent.getX() - prePivotX;
                    moveSlideAndBeforeAfterView(deltaX);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
            }
            return true;
        }
    };

    public BeforeAfterSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.before_after_seekbar,this);
        thumb = (ImageView) view.findViewById(R.id.ba_seekbar_thumb);
        llText = view.findViewById(R.id.txt_slide);

        line = view.findViewById(R.id.ba_seekbar_line);
        line.setOnTouchListener(sliderMoveHandle);
        thumb.setOnTouchListener(sliderMoveHandle);
    }

    public void moveSlideAndBeforeAfterView(float deltaX) {
        deltaX = moveHorizontal(deltaX);
        if (onMoveHorizontalListener != null){
            onMoveHorizontalListener.onChange(deltaX);
            llText.setVisibility(GONE);
        }
    }

    /**
     * This function to translate slider.
     * @param deltaX is theoretical displacement of slider.
     * @return actual displacement of slider.
     */
    public float moveHorizontal(float deltaX){
        float translateX = this.getTranslationX();
        if (translateX + deltaX < (-1 * distance)){
            deltaX = (-1 * distance) - translateX;
        }else if(translateX + deltaX > distance){
            deltaX = distance - translateX;
        }
        this.setTranslationX(translateX + deltaX);
        return deltaX;
    }

    public interface OnMoveHorizontalListener {
        void onChange(float deltaX);
    }

    public void setOnMoveHorizontalListener(OnMoveHorizontalListener move){
        onMoveHorizontalListener = move;
    }

    private float distance = 0;
    public void setDistanceMax(float distance){
        this.distance = distance;
    }

    public void setHighOfThumb(float hight){thumb.setTranslationY(-hight); }

    public void setHighOfLlText(float hight){
        llText.setTranslationY(-hight);
    }


}
