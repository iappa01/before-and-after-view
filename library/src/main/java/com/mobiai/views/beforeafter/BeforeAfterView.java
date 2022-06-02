package com.mobiai.views.beforeafter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class BeforeAfterView extends View {
    private Bitmap originImage;
    private Bitmap afterImage;
    private Bitmap normalScaleBeforeImage;
    private Bitmap normalScaleAfterImage;
    private Bitmap viewableImage;
    private Paint paint = new Paint();
    private String TAG = "BeforeAfterView";
    // TODO sua them ham set get
    public int scaleType = 0;
    public float curScale = 1.0f;
    public float preScale = 1.0f;
    /**
     * this variable is horizontal position of the thumb slider.
     */
    public float sliderPosition = 0;
    /**
     * this variable is horizontal position that afterImage was drawn by onDraw function.
     */
    private float splitPosition = 0;
    // standardizedSizeImage is image whose size is set for view

    public BeforeAfterView(Context context) {
        super(context);
        MultiTouchListener multiTouchListener = new MultiTouchListener();
        multiTouchListener.setBeforeAfterView(this);
        this.setOnTouchListener(multiTouchListener);
    }

    public BeforeAfterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        MultiTouchListener multiTouchListener = new MultiTouchListener();
        multiTouchListener.setBeforeAfterView(this);
        this.setOnTouchListener(multiTouchListener);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BeforeAfterView);
        try {
            sliderPosition = a.getFloat(R.styleable.BeforeAfterView_bav_setX, 0);
        } finally {
            a.recycle();
        }
    }

    public BeforeAfterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        MultiTouchListener multiTouchListener = new MultiTouchListener();
        multiTouchListener.setBeforeAfterView(this);
        this.setOnTouchListener(multiTouchListener);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (normalScaleBeforeImage != null) {
            canvas.drawBitmap(normalScaleBeforeImage, 0, 0, paint);
        }
        if (viewableImage != null){
            if (!viewableImage.isRecycled()) {
                canvas.drawBitmap(viewableImage, splitPosition, 0, paint);
            }
        }
    }

    public void setBeforeImage(Bitmap beforeImage) {
        originImage = beforeImage.copy(Bitmap.Config.ARGB_8888,false);
//        requestLayout();
    }

    public void setAfterImage(Bitmap afterImage) {
        this.afterImage = afterImage.copy(Bitmap.Config.ARGB_8888,false);
        requestLayout();
    }

    public float getX() {
        return sliderPosition;
    }

    /**
     *
     * @param x is the horizontal coordinate from which the after_image begins to display
     */
    public void setX(float x) {
        long currentTime = System.currentTimeMillis();
        int margin = 10;
        this.sliderPosition = x;
        if (x <= margin) {
            splitPosition = margin;
        } else if (x < this.getWidth()-margin) {
            splitPosition = x;
        } else {
            splitPosition = this.getWidth() - margin;
        }
            if (normalScaleAfterImage != null && !normalScaleAfterImage.isRecycled()) {
                if (viewableImage != null) viewableImage.recycle();
                try {
                    viewableImage = Bitmap.createBitmap(normalScaleAfterImage, (int) splitPosition, 0, BeforeAfterView.this.getWidth() - (int) splitPosition, Math.min(BeforeAfterView.this.getHeight(),normalScaleAfterImage.getHeight()));
                }catch (Exception e){
                    Log.e(TAG, "setX: " + e.getCause() + e.getMessage());
                    viewableImage = null;
                }
            }
        this.invalidate();
        Log.i(BeforeAfterView.class.getName(), "Time setX: " + (System.currentTimeMillis() - currentTime));
    }

    public void setCurScale(float scale) {
        preScale = curScale;
        curScale = scale;
    }

    float parentHeight = 0;
    float parentWidth = 0;
    int viewHeight;
    int viewWidth;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // Set size for view
        if (originImage != null) {
            int pictureWidth = originImage.getWidth();
            int pictureHeight = originImage.getHeight();

            //Measure Width
            if (widthMode == MeasureSpec.EXACTLY) {
                //Must be this size
                viewWidth = widthSize;
            } else if (widthMode == MeasureSpec.AT_MOST) {
                //Can't be bigger than...
                viewWidth = Math.min(pictureWidth, widthSize);
            } else {
                //Be whatever you want
                viewWidth = pictureWidth;
            }

            //Measure Height
            if (heightMode == MeasureSpec.EXACTLY) {
                //Must be this size
                viewHeight = heightSize;
            } else if (heightMode == MeasureSpec.AT_MOST) {
                //Can't be bigger than...
                viewHeight = Math.min(pictureHeight, heightSize);
            } else {
                //Be whatever you want
                viewHeight = pictureHeight;
            }
            if (originImage.getWidth() >= originImage.getHeight()) {
                viewHeight = (int) (((float) originImage.getHeight() / originImage.getWidth()) * viewWidth);
                // if scale type is 0 => normal scale, if scale type is 1 => fill view.
                setMeasuredDimension(viewWidth, viewHeight);
                scaleViewToShow(1,scaleType);
            } else {
                viewWidth = (int) (((float) originImage.getWidth() / originImage.getHeight()) * viewHeight);
                // if scale type is 0 => normal scale, if scale type is 1 => fill view.
//                viewWidth = widthSize;
                setMeasuredDimension(viewWidth, viewHeight);
                scaleViewToShow(0,scaleType);
            }
            createAfterImage();
            try {
                normalScaleBeforeImage = Bitmap.createScaledBitmap(originImage,viewWidth, viewHeight, false );
                if (afterImage != null){
                    long startTime = System.currentTimeMillis();
                    try {
                        if (afterImage.getWidth() != viewWidth && afterImage.getHeight() != viewHeight){
                            normalScaleAfterImage = Bitmap.createScaledBitmap(afterImage,viewWidth, viewHeight, false );
                        }else{
                            normalScaleAfterImage = afterImage.copy(Bitmap.Config.ARGB_8888,false);
                        }
                    }catch (Exception e){
                        normalScaleAfterImage = null;
                        Log.e(TAG, "onMeasure: " + e.getMessage() + e.getCause());
                    }
                    sliderPosition = viewWidth/2;
                    splitPosition = viewWidth/2;
                    setX(sliderPosition);
                    Log.i(BeforeAfterView.class.getName(), "Time create after image: "+ (System.currentTimeMillis() - startTime));
                }
            }catch (Exception e){
                    Log.e(TAG, "onMeasure: " + e.getCause());
            }

        }

    }

    private void createAfterImage() {


    }

    /**
     * This function to scale view depend on scale type to show.
     * @param direction is direction to fill, horizontal is 0, vertical is 1.
     * @param scaleType : normal is 0, fill view is 1.
     */
    void scaleViewToShow(int direction, int scaleType){
        switch (scaleType){
            case 0:
                return;
            case 1:
                float scale;
                if (direction == 0){
                    scale = parentWidth/viewWidth;
                }else {
                    scale = parentHeight/viewHeight;
                }
                BeforeAfterView.this.setCurScale(scale);
                BeforeAfterView.this.setScaleX(scale);
                BeforeAfterView.this.setScaleY(scale);
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) // implicit null check
        {
            Bundle bundle = (Bundle) state;
            this.sliderPosition = bundle.getFloat("x"); // ... load stuff
            setX(sliderPosition);
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putFloat("x", sliderPosition);
        return bundle;
    }
}
