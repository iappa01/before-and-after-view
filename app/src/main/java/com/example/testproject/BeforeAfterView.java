package com.example.testproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
public class BeforeAfterView extends View{
    private int background_picture;
    private int foreground_picture;
    private Bitmap back;
    private Bitmap fore;
    private float x = this.getWidth()/2;
    Context context;
    public float scale = 1.0f;

    public float c;

    public BeforeAfterView(Context context) {
        super(context);
        this.context = context;
    }

    public BeforeAfterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context =context;
    }

    public BeforeAfterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    Paint paint = new Paint();
    PorterDuff.Mode mode = PorterDuff.Mode.DST_OVER;
    PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(mode);
    Bitmap resizedBitmapImage;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (back != null) {
            canvas.drawBitmap(back, 0, 0, paint);
        }
        if (resizedBitmapImage != null) {
            canvas.drawBitmap(resizedBitmapImage, x, 0, paint);
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
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public int getBackground_picture() {
        return background_picture;
    }

    public void setBackground_picture(int background_picture) {
        this.background_picture = background_picture;
        Bitmap backtmp = BitmapFactory.decodeResource(getResources(), background_picture);
        back = getResizedBitmap(backtmp, this.getWidth(), this.getHeight());
    }

    public int getForeground_picture() {
        return foreground_picture;
    }

    public void setForeground_picture(int foreground_picture) {
        this.foreground_picture = foreground_picture;
        Bitmap foretmp = BitmapFactory.decodeResource(getResources(), foreground_picture);
        fore = getResizedBitmap(foretmp, this.getWidth(), this.getHeight());
    }
    private float deltaX = 0.0f;
    private float xReal = 0.0f;
    public  float getDeltaX(){
        return this.deltaX;
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
            resizedBitmapImage = Bitmap.createBitmap(fore, (int)x, 0, this.getWidth()-(int)x, this.getHeight());
        }
        this.invalidate();
    }

    public float getxReal() {
        return xReal;
    }

    public void setxReal(float xReal) {
        this.xReal = xReal;
        setX(xReal + deltaX);
    }

    public void setDeltaX(float deltaX) {
        this.deltaX = deltaX;
        setX(xReal + deltaX);
    }
}
