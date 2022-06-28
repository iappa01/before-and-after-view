package com.mobiai.views.beforeafter;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MultiTouchListener implements OnTouchListener {

    private static final int INVALID_POINTER_ID = -1;
    public boolean isRotateEnabled = false;
    public boolean isTranslateEnabled = true;
    public boolean isScaleEnabled = true;
    public float minimumScale = 1.0f;
    public float maximumScale = 10.0f;
    private int mActivePointerId = INVALID_POINTER_ID;
    private float mPrevX;
    private float mPrevY;
    private ScaleGestureDetectorCustom mScaleGestureDetector;

    public MultiTouchListener() {
        mScaleGestureDetector = new ScaleGestureDetectorCustom(new ScaleGestureListener());
        mScaleGestureDetector = new ScaleGestureDetectorCustom(new ScaleGestureListener());
    }

    private float adjustTranslate(View view,float deltaX, float deltaY){
        float deltaMaxW = view.getWidth()*(beforeAfterView.curScale - 1.0f)/(2);
        float deltaMaxH = view.getHeight()*(beforeAfterView.curScale - 1.0f)/(2);
        float deltaW = view.getTranslationX() + deltaX;
        float deltaH = view.getTranslationY() + deltaY;

        // To calculate the actual travel distance
        if (deltaW < -deltaMaxW){
            deltaX = -deltaMaxW - view.getTranslationX();
        }else if(deltaW > deltaMaxW){
            deltaX = deltaMaxW - view.getTranslationX();
        }
        if (deltaH < -deltaMaxH){
            deltaY = -deltaMaxH - view.getTranslationY();
        }else if(deltaH > deltaMaxH){
            deltaY = deltaMaxH - view.getTranslationY();
        }

        view.setTranslationY(view.getTranslationY() + deltaY);
        view.setTranslationX(view.getTranslationX() + deltaX);
        return deltaX;
    }

    private void move(View view, TransformInfo info) {
        float scale = view.getScaleX() * info.deltaScale;
        scale = Math.max(info.minimumScale, Math.min(info.maximumScale, scale));
        beforeAfterView.setCurScale(scale);

        float deltaX = info.deltaX;
        float deltaY = info.deltaY;
        deltaX = adjustTranslate(view, deltaX, deltaY);

        // Assume that scaling still maintains aspect ratio.
        view.setScaleX(scale);
        view.setScaleY(scale);

        // Keep the slider fixed when operating with 2 fingers
        float d = view.getPivotX() - beforeAfterView.getX();
        float deltaD = d*(1 - beforeAfterView.preScale/beforeAfterView.curScale);
        beforeAfterView.setX(beforeAfterView.getX() + deltaD - deltaX/beforeAfterView.curScale);
    }

    BeforeAfterView beforeAfterView;
    public void setBeforeAfterView(BeforeAfterView view){
        beforeAfterView = view;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(view, event);
        if (!isTranslateEnabled) {
            return true;
        }

        int action = event.getAction();
        switch (action & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                mPrevX = event.getX();
                mPrevY = event.getY();

                // Save the ID of this pointer.
                mActivePointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position.
                int pointerIndex = event.findPointerIndex(mActivePointerId);

                if (pointerIndex != -1) {
                    float currX = event.getX(pointerIndex);
                    float currY = event.getY(pointerIndex);

                    // Only move if the ScaleGestureDetector isn't processing a
                    // gesture.
                    if (!mScaleGestureDetector.isInProgress()) {

                        //  adjustTranslation view
                        float deltaX = currX - mPrevX;
                        float deltaY = currY - mPrevY;

                        deltaX = adjustTranslate(view, deltaX, deltaY);
                        // Keep the slider fixed when operating with 1 fingers
                        beforeAfterView.setX(beforeAfterView.getX() - deltaX/beforeAfterView.curScale);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER_ID;
                break;

            case MotionEvent.ACTION_UP:
                mActivePointerId = INVALID_POINTER_ID;
                break;

            case MotionEvent.ACTION_POINTER_UP: {
                // Extract the index of the pointer that left the touch sensor.
                int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mPrevX = event.getX(newPointerIndex);
                    mPrevY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    private class ScaleGestureListener extends ScaleGestureDetectorCustom.SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector = new Vector2D();
        @Override
        public boolean onScaleBegin(View view, ScaleGestureDetectorCustom detector) {
            mPivotX = detector.getFocusX();
            mPivotY = detector.getFocusY();
            mPrevSpanVector.set(detector.getCurrentSpanVector());
            return true;
        }

        @Override
        public boolean onScale(View view, ScaleGestureDetectorCustom detector) {
            TransformInfo info = new TransformInfo();
            info.deltaScale = isScaleEnabled ? detector.getScaleFactor() : 1.0f;
            info.deltaAngle = isRotateEnabled ? Vector2D.getAngle(mPrevSpanVector, detector.getCurrentSpanVector()) : 0.0f;
            info.deltaX = isTranslateEnabled ? detector.getFocusX() - mPivotX : 0.0f;
            info.deltaY = isTranslateEnabled ? detector.getFocusY() - mPivotY : 0.0f;
            info.pivotX = mPivotX;
            info.pivotY = mPivotY;
            info.minimumScale = minimumScale;
            info.maximumScale = maximumScale;
            move(view, info);
            return false;
        }
    }

    private class TransformInfo {
        public float deltaX;
        public float deltaY;
        public float deltaScale;
        public float deltaAngle;
        public float pivotX;
        public float pivotY;
        public float minimumScale;
        public float maximumScale;
    }
}
