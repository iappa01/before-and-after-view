package com.mobiai.views.beforeafter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

public class BeforeAfter extends FrameLayout {
    int widthSliderLine;
    int backgroundSliderThumb;
    int backgroundSliderLine;
    int colorSliderLine;
    int marginLeftBeforeText;
    int marginRightAfterText;
    int marginTopText;
    boolean visibilityText;
    int distanceMax = -1;
    boolean invisibleText;
    boolean isHeightThumbSetDefault = true;
    boolean useBackgroundImage;
    float thumbHeight;
    boolean isTranslateEnabled;
    boolean isScaleEnabled;
    BeforeAfterView beforeAfterView;
    BeforeAfterSlider beforeAfterSlider;
    BitMapConverter bitMapConverter;
    BeforeAfterText beforeAfterText;
    ImageView imgBackground;
    View view;

    public BeforeAfter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        view = LayoutInflater.from(context).inflate(R.layout.layout_before_after, this);
        beforeAfterView = view.findViewById(R.id.before_after_view);
        beforeAfterSlider = view.findViewById(R.id.before_after_slider);
        bitMapConverter = view.findViewById(R.id.before_after_get_bit_map);
        imgBackground = view.findViewById(R.id.background);
        beforeAfterText = bitMapConverter.beforeAfterText;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BeforeAfter);
        colorSliderLine = typedArray.getColor(R.styleable.BeforeAfter_color_slider_line, 000000);
        widthSliderLine = typedArray.getDimensionPixelSize(R.styleable.BeforeAfter_width_slider_line, 2);
        backgroundSliderThumb = typedArray.getResourceId(R.styleable.BeforeAfter_background_slider_thumb, R.drawable.ba_seekbar_thumb);
        marginLeftBeforeText = typedArray.getDimensionPixelSize(R.styleable.BeforeAfter_margin_left_text_before, 2);
        marginRightAfterText = typedArray.getDimensionPixelSize(R.styleable.BeforeAfter_margin_right_text_after, 2);
        marginTopText = typedArray.getDimensionPixelSize(R.styleable.BeforeAfter_margin_top_text, 2);
        visibilityText = typedArray.getBoolean(R.styleable.BeforeAfter_visibility_text, false);
        invisibleText = typedArray.getBoolean(R.styleable.BeforeAfter_invisible_text,false);
        backgroundSliderLine = typedArray.getResourceId(R.styleable.BeforeAfter_background_slider_line, R.color.white);
        useBackgroundImage = typedArray.getBoolean(R.styleable.BeforeAfter_use_background_image, false);
        thumbHeight = typedArray.getFloat(R.styleable.BeforeAfter_height_thumb,0.2f);
        isTranslateEnabled = typedArray.getBoolean(R.styleable.BeforeAfter_is_translate_enabled, true);
        isScaleEnabled = typedArray.getBoolean(R.styleable.BeforeAfter_is_scale_enabled, true);
        beforeAfterView.isScaleEnable = isScaleEnabled;
        beforeAfterView.isTranslteEnable = isTranslateEnabled;
        Drawable drawable = AppCompatResources.getDrawable(getContext(), backgroundSliderThumb);
        beforeAfterSlider.thumb.setImageDrawable(drawable);

        beforeAfterView.scaleType = typedArray.getInteger(R.styleable.BeforeAfter_typeScale, 0);
        RelativeLayout.LayoutParams lineLayoutParams = (RelativeLayout.LayoutParams) beforeAfterSlider.line.getLayoutParams();
        lineLayoutParams.width = widthSliderLine;
        beforeAfterSlider.line.setLayoutParams(lineLayoutParams);
        beforeAfterSlider.line.setBackgroundColor(colorSliderLine);
        if (useBackgroundImage){
            beforeAfterSlider.line.setBackgroundResource(backgroundSliderLine);
        }
        beforeAfterSlider.line.requestLayout();

        if (invisibleText){
            bitMapConverter.setVisibility(INVISIBLE);
        }
        ConstraintLayout.LayoutParams textBefore = (ConstraintLayout.LayoutParams) bitMapConverter.textViewBefore.getLayoutParams();
        textBefore.setMargins(marginLeftBeforeText, marginTopText, 0, 0);
        bitMapConverter.textViewBefore.setLayoutParams(textBefore);
        bitMapConverter.textViewBefore.requestLayout();

        ConstraintLayout.LayoutParams textAfter = (ConstraintLayout.LayoutParams) bitMapConverter.textViewAfter.getLayoutParams();
        textAfter.setMargins(0, marginTopText, marginRightAfterText, 0);
        bitMapConverter.textViewAfter.setLayoutParams(textAfter);
        bitMapConverter.textViewAfter.requestLayout();
        if (visibilityText){
            beforeAfterSlider.llText.setVisibility(VISIBLE);
        }else {
            beforeAfterSlider.llText.setVisibility(GONE);
        }

        typedArray.recycle();

        setOnHorizontalMove();
    }

    public ImageView getBackgroundImageView() {
        return imgBackground;
    }

    public void setBeforeImage(Bitmap beforeImage) {beforeAfterView.setBeforeImage(beforeImage); }

    public void setAfterImage(Bitmap afterImage) {
        beforeAfterView.setAfterImage(afterImage);
    }

    public void reset() {
        beforeAfterSlider.setTranslationX(0);
        beforeAfterView.destroy();
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
    public void setDistanceMax(int distanceMax) {
        this.distanceMax = distanceMax;
        beforeAfterSlider.setDistanceMax(distanceMax);
    }

    /**
     * this function set event handled for slider
     */
    public void setOnHorizontalMove() {
        beforeAfterSlider.setOnMoveHorizontalListener(new BeforeAfterSlider.OnMoveHorizontalListener() {
            @Override
            public void onChange(float deltaX) {
                BeforeAfter.this.setBeforeAfterX(getBeforeAfterX() + deltaX /getCurScale());
                beforeAfterText.setX(beforeAfterText.x + deltaX);
            }
        });
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        beforeAfterView.parentHeightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        beforeAfterView.parentWidthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;
        distanceMax = width/2;
        beforeAfterSlider.setDistanceMax(distanceMax);
        if (isHeightThumbSetDefault){
            setHighThumb(height*thumbHeight);
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     *
     * @param text, default is "before"
     */
    public void setTextBefore(String text){bitMapConverter.setTextBefore(text); }

    /**
     *
     * @param text, default is "after"
     */
    public void setTextAfter(String text){bitMapConverter.setTextAfter(text); }

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
    public void setTextSize(int size){bitMapConverter.setTextSize(size); }

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
        isHeightThumbSetDefault = false;
        beforeAfterSlider.setHighOfThumb(hight);
        setHighLayoutText(hight + 15f);

    }

    public void setHighLayoutText(float hight){
        beforeAfterSlider.setHighOfLlText(hight);
    }

}
