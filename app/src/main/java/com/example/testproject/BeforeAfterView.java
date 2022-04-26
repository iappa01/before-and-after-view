package com.example.testproject;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
public class BeforeAfterView extends View{
    private int background_picture;
    private int foreground_picture;
    private Bitmap bitmapBefore;
    private Bitmap bitmapAfter;
    String stringBefore = "Before";
    String stringAfter = "After";
    public float x = 0;
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
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.BeforeAfterView);
        try {
            x = a.getFloat(R.styleable.BeforeAfterView_bav_setX,0);
        }finally {
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
        x = this.getWidth()/2;
    }
    Paint paint = new Paint();
    Bitmap resizedBitmapImage;
    Paint stringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        stringPaint.setTextSize(50);
        stringPaint.setColor(Color.YELLOW);
        if (bitmapBefore != null) {
            canvas.drawBitmap(bitmapBefore, 0, 0, paint);
//            canvas.drawText(stringBefore,100,100,stringPaint);
        }
        if (resizedBitmapImage != null) {
            canvas.drawBitmap(resizedBitmapImage, x, 0, paint);
//            canvas.drawText(stringAfter,500,100,stringPaint);
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private float pictureWidth;
    private float pictureHeight;
    public void setBackground_picture(int background_picture) {
        this.background_picture = background_picture;
        Bitmap backtmp = BitmapFactory.decodeResource(getResources(), background_picture);
//        pictureHeight = backtmp.getHeight();
//        pictureWidth = backtmp.getWidth();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bitmapBefore = getResizedBitmap(backtmp, BeforeAfterView.this.getWidth(), BeforeAfterView.this.getHeight());
//                setX(BeforeAfterView.this.x);
            }
        },1000);
    }

    public void setForeground_picture(int foreground_picture) {
        this.foreground_picture = foreground_picture;
        Bitmap foretmp = BitmapFactory.decodeResource(getResources(), foreground_picture);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bitmapAfter = getResizedBitmap(foretmp, BeforeAfterView.this.getWidth(), BeforeAfterView.this.getHeight());
                setX(BeforeAfterView.this.x);
            }
        },1000);
    }

    public float getX() {
        return x;
    }
    public void setX(float x) {
        if (x < 0){
            this.x = 0;
        }else if(x < this.getWidth()){
            this.x = x;
        }else{
            this.x = this.getWidth();
        }
        x = this.x;
        if (this.getWidth() > (int) x) {
            if (resizedBitmapImage != null) resizedBitmapImage.recycle();
            if (bitmapAfter != null){
                resizedBitmapImage = Bitmap.createBitmap(bitmapAfter, (int)x, 0, this.getWidth()-(int)x, this.getHeight());
            }
        }
        this.invalidate();
    }

    public float curScale = 1.0f;
    public float preScale = 1.0f;
    public void setCurScale(float scale){
        preScale = curScale;
        curScale = scale;
    }
}
