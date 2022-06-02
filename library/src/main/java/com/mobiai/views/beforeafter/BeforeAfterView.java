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
    private Bitmap normalScaleBeforeImage;
    private Bitmap normalScaleAfterImage;
    public float x = 0;
    int scaleType = 0;
    private Bitmap afterImageTmp;

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
            x = a.getFloat(R.styleable.BeforeAfterView_bav_setX, 0);
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

    Paint paint = new Paint();
    Bitmap viewableImage;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (normalScaleBeforeImage != null) {
            canvas.drawBitmap(normalScaleBeforeImage, 0, 0, paint);
        }
        if (viewableImage != null){
            if (!viewableImage.isRecycled()) {
                canvas.drawBitmap(viewableImage, x1, 0, paint);
            }
        }
    }

    // standardizedSizeImage is image whose size is set for view
    Bitmap standardizedSizeImage;
    public void setBeforeImage(Bitmap beforeImage) {
        standardizedSizeImage = beforeImage;
//        requestLayout();
    }

    Bitmap afterImage;
    public void setAfterImage(Bitmap afterImage) {
        this.afterImage = afterImage;
        requestLayout();
    }

    public float getX() {
        return x;
    }

    /**
     *
     * @param x is the horizontal coordinate from which the after_image begins to display
     */
    private float x1 = 0;
    public void setX(float x) {
        long currentTime = System.currentTimeMillis();
        this.x = x;
        if (x < 0) {
            x1 = 0;
        } else if (x < this.getWidth()) {
            x1 = x;
        } else {
            x1 = this.getWidth();
        }
        if (this.getWidth() > (int) x1) {

            if (normalScaleAfterImage != null && !normalScaleAfterImage.isRecycled()) {
                if (viewableImage != null) viewableImage.recycle();
                try {
                    viewableImage = Bitmap.createBitmap(normalScaleAfterImage, (int) x1, 0, BeforeAfterView.this.getWidth() - (int) x1, Math.min(BeforeAfterView.this.getHeight(),normalScaleAfterImage.getHeight()));
                }catch (Exception e){
                    Log.e(TAG, "setX: " + e.getCause() + e.getMessage());
                    viewableImage = null;
                }
            }
        }
        this.invalidate();
        Log.i(BeforeAfterView.class.getName(), "Time setX: " + (System.currentTimeMillis() - currentTime));
    }
    String TAG = "BeforeAfterView";
    public float curScale = 1.0f;
    public float preScale = 1.0f;
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
        if (standardizedSizeImage != null) {
            int pictureWidth = standardizedSizeImage.getWidth();
            int pictureHeight = standardizedSizeImage.getHeight();

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
            if (standardizedSizeImage.getWidth() >= standardizedSizeImage.getHeight()) {
                viewHeight = (int) (((float) standardizedSizeImage.getHeight() / standardizedSizeImage.getWidth()) * viewWidth);
                // if scale type is 0 => normal scale, if scale type is 1 => fill view.
                setMeasuredDimension(viewWidth, viewHeight);
                scaleViewToShow(1,scaleType);
            } else {
                viewWidth = (int) (((float) standardizedSizeImage.getWidth() / standardizedSizeImage.getHeight()) * viewHeight);
                // if scale type is 0 => normal scale, if scale type is 1 => fill view.
//                viewWidth = widthSize;
                setMeasuredDimension(viewWidth, viewHeight);
                scaleViewToShow(0,scaleType);
            }
            createAfterImage();
            normalScaleBeforeImage = Bitmap.createScaledBitmap(standardizedSizeImage,viewWidth, viewHeight, true );
            if (afterImage != null){
                long startTime = System.currentTimeMillis();
                afterImageTmp = afterImage.copy(Bitmap.Config.ARGB_8888,false);
                try {
                    normalScaleAfterImage = Bitmap.createScaledBitmap(afterImageTmp,viewWidth, viewHeight, true );
                }catch (Exception e){
                    normalScaleAfterImage = null;
                    Log.e(TAG, "onMeasure: " + e.getMessage() + e.getCause());
                }
                x = viewWidth/2;
                x1 = viewWidth/2;
                setX(x);
                Log.i(BeforeAfterView.class.getName(), "Time create after image: "+ (System.currentTimeMillis() - startTime));
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
            this.x = bundle.getFloat("x"); // ... load stuff
            setX(x);
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putFloat("x", x);
        return bundle;
    }
}
