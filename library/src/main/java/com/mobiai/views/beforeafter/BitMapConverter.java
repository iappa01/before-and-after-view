package com.mobiai.views.beforeafter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class BitMapConverter extends FrameLayout {
    TextView textViewBefore;
    TextView textViewAfter;
    Bitmap bitmapBefore;
    Bitmap bitmapAfter;
    Context context;
    View viewBefore;
    View viewAfter;
    BeforeAfterText beforeAfterText;
    public BitMapConverter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        beforeAfterText = new BeforeAfterText(context);
        this.addView(beforeAfterText);

        viewBefore = LayoutInflater.from(context).inflate(R.layout.text_before, null);
        textViewBefore = (TextView) viewBefore.findViewById(R.id.text_before);
        textViewBefore.setText("Before");

        viewAfter = LayoutInflater.from(context).inflate(R.layout.text_after,null);
        textViewAfter = (TextView) viewAfter.findViewById(R.id.text_after);
        textViewAfter.setText("After");

        viewBefore.setVisibility(INVISIBLE);
        viewAfter.setVisibility(INVISIBLE);

        this.addView(viewBefore);
        this.addView(viewAfter);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        bitmapBefore = loadBitmapFromView(viewBefore);
        beforeAfterText.setBeforeTextImage(bitmapBefore);

        bitmapAfter = loadBitmapFromView(viewAfter);
        beforeAfterText.setAfterTextImage(bitmapAfter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return false;
    }

    /**
     * This function convert view to bitmap
     * @param view
     * @return bitmap image of view
     */
    public static Bitmap loadBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
            //does not have background drawable, then draw white background on the canvas
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public void setTextBefore(String text){
        textViewBefore.setText(text);
        requestLayout();
    }

    public void setTextAfter(String text){
        textViewAfter.setText(text);
        requestLayout();
    }

    public void setTextBackground(int drawableSource){
        Drawable background = context.getDrawable(drawableSource);
        textViewBefore.setBackground(background);
        textViewAfter.setBackground(background);
        requestLayout();
    }

    public void setTextColor(int color){
        textViewBefore.setTextColor(color);
        textViewAfter.setTextColor(color);
        requestLayout();
    }

    public void setTextSize(int size){
        textViewBefore.setTextSize(size);
        textViewAfter.setTextSize(size);
    }
}
