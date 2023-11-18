package com.mobiai.views.beforeafter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
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
    private int heightSize;
    private int widthSize;
    private int viewHeight;
    private int viewWidth;
    // TODO sua them ham set get
    public int scaleType = 0;
    public float curScale = 1.0f;
    public float preScale = 1.0f;
    public int parentHeightMeasureMode;
    public int parentWidthMeasureMode;
    /**
     * this variable is horizontal position of the thumb slider.
     */
    public float sliderPosition = 0;
    /**
     * this variable is horizontal position that afterImage was drawn by onDraw function.
     */
    private float splitPosition = 0;
    // standardizedSizeImage is image whose size is set for view
    private MultiTouchListener multiTouchListener;
    public boolean isScaleEnable;
    public boolean isTranslteEnable;
//    public void setIsScaleEnable(Boolean isScaleEnable){
//        this.multiTouchListener.isScaleEnabled = isScaleEnable;
//    }
//    public void setIsTranslateEnable(Boolean isTranslateEnable){
//        this.multiTouchListener.isTranslateEnabled = isTranslateEnable;
//    }
    public BeforeAfterView(Context context) {
        super(context);
        multiTouchListener = new MultiTouchListener();
        multiTouchListener.isTranslateEnabled = isTranslteEnable;
        multiTouchListener.isScaleEnabled = isScaleEnable;
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
        multiTouchListener.isTranslateEnabled = isTranslteEnable;
        multiTouchListener.isScaleEnabled = isScaleEnable;
    }

    public BeforeAfterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        MultiTouchListener multiTouchListener = new MultiTouchListener();
        multiTouchListener.setBeforeAfterView(this);
        this.setOnTouchListener(multiTouchListener);
        multiTouchListener.isTranslateEnabled = isTranslteEnable;
        multiTouchListener.isScaleEnabled = isScaleEnable;
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

    public Bitmap resizeLargeImageToSmall(Bitmap bitmap){
        if (bitmap.getHeight() * bitmap.getWidth() > 1500 * 1500) {

            float ratio = (float)bitmap.getWidth() / bitmap.getHeight();

            if (ratio > 1) {
                ratio = 1500F / bitmap.getWidth();
            } else {
                ratio = 1500F / bitmap.getHeight();
            }

            Matrix matrix = new Matrix();
            matrix.setScale(ratio, ratio);

            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap.copy(Bitmap.Config.RGB_565,false);
        }
    }
    public void setBeforeImage(Bitmap beforeImage) {
        originImage = resizeLargeImageToSmall(beforeImage);
    }

    public void setAfterImage(Bitmap afterImage) {
//        this.afterImage = afterImage.copy(Bitmap.Config.ARGB_8888,false);
        if (afterImage != null && afterImage.isRecycled()) {
            afterImage.recycle();
            afterImage = null;
        }
        if (normalScaleAfterImage != null && !normalScaleAfterImage.isRecycled()){
            normalScaleAfterImage.recycle();
            normalScaleAfterImage = null;
        }

        this.afterImage = resizeLargeImageToSmall(afterImage);
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
        int margin = 1;
        this.sliderPosition = x;
        if (x <= margin) {
            splitPosition = margin;
        } else if (x < viewWidth-margin) {
            splitPosition = x;
        } else {
            splitPosition = viewWidth - margin;
        }
        if (normalScaleAfterImage != null && !normalScaleAfterImage.isRecycled()) {
            try {
                Bitmap tmp = Bitmap.createBitmap(normalScaleAfterImage,
                        (int) splitPosition,
                        0,
                        Math.min(viewWidth - (int) splitPosition, normalScaleAfterImage.getWidth() - (int) splitPosition),
                        Math.min(viewHeight, normalScaleAfterImage.getHeight()));
                if (tmp != null) {
                    if (viewableImage != null) viewableImage.recycle();
                    viewableImage = tmp;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.invalidate();
    }

    public void setCurScale(float scale) {
        preScale = curScale;
        curScale = scale;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);
        // Set size for view
        if (originImage != null) {
            int pictureWidth = originImage.getWidth();
            int pictureHeight = originImage.getHeight();

            if (parentWidthMeasureMode == MeasureSpec.AT_MOST){
                viewWidth = widthSize;
            }else{
                viewWidth = Math.min(pictureWidth, widthSize);
            }
            if (parentHeightMeasureMode == MeasureSpec.AT_MOST){
                viewHeight = heightSize;
            }else{
                viewHeight = Math.min(pictureHeight, heightSize);
            }

            if (((float)pictureWidth)/widthSize >= ((float)pictureHeight)/heightSize) {
                viewHeight = (int) (((float) pictureHeight / pictureWidth) * viewWidth);
                // if scale type is 0 => normal scale, if scale type is 1 => fill view.
                setMeasuredDimension(viewWidth, viewHeight);
                scaleViewToShow(1,scaleType);
            } else {
                viewWidth = (int) (((float) pictureWidth / pictureHeight) * viewHeight);
                // if scale type is 0 => normal scale, if scale type is 1 => fill view.
                setMeasuredDimension(viewWidth, viewHeight);
                scaleViewToShow(0,scaleType);
            }

            if (normalScaleBeforeImage == null){
                if (pictureWidth == viewWidth && pictureHeight == viewHeight){
                    normalScaleBeforeImage = originImage.copy(Bitmap.Config.ARGB_8888,false);
                }else{
                    normalScaleBeforeImage = Bitmap.createScaledBitmap(originImage,viewWidth, viewHeight, false );
                }
            }
            if (afterImage != null && (normalScaleAfterImage == null||normalScaleBeforeImage.isRecycled())){
                if (afterImage.getWidth() == viewWidth && afterImage.getHeight() == viewHeight){
                    normalScaleAfterImage = afterImage.copy(Bitmap.Config.ARGB_8888,false);
                }else{
                    normalScaleAfterImage = Bitmap.createScaledBitmap(afterImage,viewWidth, viewHeight, false );
                }
                sliderPosition = (float) viewWidth / 2;
                splitPosition = (float) viewWidth / 2;
                setX(sliderPosition);
            }
        }

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
                    scale = (float)widthSize/viewWidth;
                }else {
                    scale = (float)heightSize/viewHeight;
                }

                // check min max for scale
                scale = Math.min(scale, 10000);
                scale = Math.max(scale, 0.0001f);


                BeforeAfterView.this.setCurScale(scale);
                BeforeAfterView.this.setScaleX(scale);
                BeforeAfterView.this.setScaleY(scale);
        }
    }

    public void destroy(){
        if (originImage != null && !originImage.isRecycled()){
            originImage.recycle();
            originImage = null;
        }
        if (afterImage != null && !afterImage.isRecycled()){
            afterImage.recycle();
            afterImage = null;
        }
        if (normalScaleBeforeImage != null && !normalScaleBeforeImage.isRecycled()){
            normalScaleBeforeImage.recycle();
            normalScaleBeforeImage = null;
        }
        if (normalScaleAfterImage != null && !normalScaleAfterImage.isRecycled()){
            normalScaleAfterImage.recycle();
            normalScaleAfterImage = null;
        }
        if (viewableImage != null && !viewableImage.isRecycled()){
            viewableImage.recycle();
            viewableImage = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        destroy();
        super.onDetachedFromWindow();
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
