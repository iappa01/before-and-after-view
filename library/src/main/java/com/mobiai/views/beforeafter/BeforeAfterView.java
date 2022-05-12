package com.mobiai.views.beforeafter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BeforeAfterView extends View {
    private Bitmap normalScaleBeforeImage;
    private Bitmap normalScaleAfterImage;
    public float x = 0;
    int scaleType = 0;

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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // Set first value for x
        x1 = this.getWidth() / 2;
        x = x1;
    }

    Paint paint = new Paint();
    Bitmap viewableImage;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (normalScaleBeforeImage != null) {
            canvas.drawBitmap(normalScaleBeforeImage, 0, 0, paint);
        }
        if (viewableImage != null) {
            canvas.drawBitmap(viewableImage, x1, 0, paint);
        }
    }

    // standardizedSizeImage is image whose size is set for view
    Bitmap standardizedSizeImage;
    public void setBeforeImage(Bitmap beforeImage) {
        standardizedSizeImage = beforeImage;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Normalize the size of picture
                normalScaleBeforeImage = Bitmap.createScaledBitmap(standardizedSizeImage,BeforeAfterView.this.getWidth(), BeforeAfterView.this.getHeight(), true );
                requestLayout();
            }
        }, 1000);
    }

    public void setAfterImage(Bitmap afterImage) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Normalize the size of picture
                normalScaleAfterImage = Bitmap.createScaledBitmap(afterImage,BeforeAfterView.this.getWidth(), BeforeAfterView.this.getHeight(), true );
                setX(BeforeAfterView.this.x);
            }
        }, 1000);
    }

    public float getX() {
        return x;
    }

    /**
     *
     * @param x is the horizontal coordinate from which the after_image begins to display
     */
    private float x1;
    public void setX(float x) {
        this.x = x;
        if (x < 0) {
            x1 = 0;
        } else if (x < this.getWidth()) {
            x1 = x;
        } else {
            x1 = this.getWidth();
        }
        if (this.getWidth() > (int) x1) {
            if (viewableImage != null) viewableImage.recycle();
            if (normalScaleAfterImage != null) {
                viewableImage = Bitmap.createBitmap(normalScaleAfterImage, (int) x1, 0, BeforeAfterView.this.getWidth() - (int) x1, BeforeAfterView.this.getHeight());
            }
        }
        this.invalidate();
    }

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
            viewWidth = 0;
            viewHeight = 0;

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
                scaleViewToShow(1,scaleType);
            } else {
//                viewWidth = (int) (((float) standardizedSizeImage.getWidth() / standardizedSizeImage.getHeight()) * viewHeight);
                // if scale type is 0 => normal scale, if scale type is 1 => fill view.
                viewWidth = widthSize;
                scaleViewToShow(0,scaleType);
            }
            //MUST CALL THIS
            setMeasuredDimension(viewWidth, viewHeight);
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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                },100);
        }
    }
}
