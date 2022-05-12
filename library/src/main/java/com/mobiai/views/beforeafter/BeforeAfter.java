package com.mobiai.views.beforeafter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by dungHt on
 */

public class BeforeAfter extends FrameLayout {
    int widthSliderLine;
    int srcSliderThumb;
    int colorSliderLine;
    int marginLeftBeforeText;
    int marginRightAfterText;
    int marginTopText;

    BeforeAfterView beforeAfterView;
    BeforeAfterSlider beforeAfterSlider;
    BitMapConverter bitMapConverter;
    BeforeAfterText beforeAfterText;
    View view;

    public BeforeAfter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        view = LayoutInflater.from(context).inflate(R.layout.layout_before_after, this);
        beforeAfterView = view.findViewById(R.id.before_after_view);
        beforeAfterSlider = view.findViewById(R.id.before_after_slider);
        bitMapConverter = view.findViewById(R.id.before_after_get_bit_map);
        beforeAfterText = bitMapConverter.beforeAfterText;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BeforeAfter);
        colorSliderLine = typedArray.getColor(R.styleable.BeforeAfter_color_slider_line, 000000);
        widthSliderLine = typedArray.getDimensionPixelSize(R.styleable.BeforeAfter_width_slider_line, 2);
        srcSliderThumb = typedArray.getResourceId(R.styleable.BeforeAfter_src_slider_thumb, R.drawable.ba_seekbar_thumb);
        marginLeftBeforeText = typedArray.getDimensionPixelSize(R.styleable.BeforeAfter_margin_left_text_before, 2);
        marginRightAfterText = typedArray.getDimensionPixelSize(R.styleable.BeforeAfter_margin_right_text_after, 2);
        marginTopText = typedArray.getDimensionPixelSize(R.styleable.BeforeAfter_margin_top_text, 2);

        if (srcSliderThumb != 0) {
            Drawable drawable = AppCompatResources.getDrawable(getContext(), srcSliderThumb);
            beforeAfterSlider.thumb.setImageDrawable(drawable);
        }

        beforeAfterView.scaleType = typedArray.getInteger(R.styleable.BeforeAfter_typeScale, 0);
        RelativeLayout.LayoutParams lineLayoutParams = (RelativeLayout.LayoutParams) beforeAfterSlider.line.getLayoutParams();
        lineLayoutParams.width = widthSliderLine;
        beforeAfterSlider.line.setLayoutParams(lineLayoutParams);
        beforeAfterSlider.line.setBackgroundColor(colorSliderLine);
        beforeAfterSlider.line.requestLayout();

        ConstraintLayout.LayoutParams textBefore = (ConstraintLayout.LayoutParams) bitMapConverter.textViewBefore.getLayoutParams();
        textBefore.setMargins(marginLeftBeforeText, marginTopText, 0, 0);
        bitMapConverter.textViewBefore.setLayoutParams(textBefore);
        bitMapConverter.textViewBefore.requestLayout();

        ConstraintLayout.LayoutParams textAfter = (ConstraintLayout.LayoutParams) bitMapConverter.textViewAfter.getLayoutParams();
        textAfter.setMargins(0, marginTopText, marginRightAfterText, 0);
        bitMapConverter.textViewAfter.setLayoutParams(textAfter);
        bitMapConverter.textViewAfter.requestLayout();

        typedArray.recycle();

        setDistanceMax();
        setOnHorizontalMove();
    }

    public void setBeforeImage(Bitmap beforeImage) {
        beforeAfterView.setBeforeImage(beforeImage);
    }

    public void setAfterImage(Bitmap afterImage) {
        beforeAfterView.setAfterImage(afterImage);
    }

    /**
     *
     * @param x is the horizontal coordinate from which the after_image begins to display
     */
    public void setBeforeAfterX(float x) {
        beforeAfterView.setX(x);
    }

    public float getBeforeAfterX() {
        return beforeAfterView.getX();
    }

    public float getCurScale() {
        return beforeAfterView.curScale;
    }

    /**
     * This function sets the maximum horizontal distance from the first position the slider can move.
     */
    public void setDistanceMax() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                beforeAfterSlider.setDistanceMax(BeforeAfter.this.getWidth() / 2);
            }
        }, 1000);
    }

    /**
     * this function set event handled for slider
     */
    public void setOnHorizontalMove() {
        beforeAfterSlider.setOnMoveHorizontalListener(new BeforeAfterSlider.OnMoveHorizontalListener() {
            @Override
            public void onChange(float deltaX) {
                BeforeAfter.this.setBeforeAfterX(BeforeAfter.this.getBeforeAfterX() + deltaX / BeforeAfter.this.getCurScale());
                beforeAfterText.setX(beforeAfterText.x + deltaX);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        beforeAfterView.parentHeight = this.getHeight();
        beforeAfterView.parentWidth = this.getWidth();
        setHighThumb(this.getHeight()/5);
    }

    /**
     *
     * @param text, default is "before"
     */
    public void setTextBefore(String text){
        bitMapConverter.setTextBefore(text);
    }

    /**
     *
     * @param text, default is "after"
     */
    public void setTextAfter(String text){
        bitMapConverter.setTextAfter(text);
    }

    /**
     *
     * @param drawable, default is bg_text_before_after.xml
     */
    public void setTextBackground(int drawable){
        bitMapConverter.setTextBackground(drawable);
    }

    /**
     *
     * @param size, default is 20sp
     */
    public void setTextSize(int size){
        bitMapConverter.setTextSize(size);
    }

    /**
     *
     * @param color, default is white
     */
    public void setTextColor(int color){
        bitMapConverter.setTextColor(color);
    }

    /**
     *
     * @param hight, default is 1/5 * height
     */
    public void setHighThumb(float hight){
        beforeAfterSlider.setHighOfThumb(hight);
    }
}
