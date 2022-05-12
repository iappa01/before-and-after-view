package com.mobiai.views.beforeafter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import androidx.annotation.NonNull;

public class BeforeAfterText extends androidx.appcompat.widget.AppCompatImageView {
    private Bitmap beforeTextImage;
    private Bitmap afterTextImage;
    public BeforeAfterText(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // Set first value for x
        x = this.getWidth() / 2;
    }

    public void setBeforeTextImage(Bitmap beforeTextImage) {
        this.beforeTextImage = beforeTextImage;
    }

    public void setAfterTextImage(Bitmap afterTextImage) {
        this.afterTextImage = afterTextImage;
        setX(x);
    }

    Paint paint = new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (viewableImageAfter != null){
            canvas.drawBitmap(viewableImageBefore,0,0,paint);
            canvas.drawBitmap(viewableImageAfter,x,0,paint);
        }
    }

    float x;
    Bitmap viewableImageAfter;
    Bitmap viewableImageBefore;
    public void setX(float x) {
        if (x < 1) {
            this.x = 1;
        } else if (x < this.getWidth()) {
            this.x = x;
        } else {
            this.x = this.getWidth();
        }
        x = this.x;
        if (this.getWidth() > (int) x) {
            if (viewableImageBefore != null && !viewableImageAfter.isRecycled()){
                viewableImageAfter.recycle();
            }else{
            }
            if (afterTextImage != null) {
                viewableImageAfter = Bitmap.createBitmap(afterTextImage, (int) x, 0, BeforeAfterText.this.getWidth() - (int) x, BeforeAfterText.this.getHeight());
            }
        }
        if ((int)x > 0){
            if (viewableImageBefore != null) viewableImageBefore.recycle();
            if (beforeTextImage != null){
                viewableImageBefore = Bitmap.createBitmap(beforeTextImage,0,0,(int)x,BeforeAfterText.this.getHeight());
            }
        }
        this.invalidate();
    }
}
