package com.john.glepgviewer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by john on 10/28/13.
 */
public class TestGLSurfaceView extends GLSurfaceView {
    private GLEpgViewRenderer mGLEpgViewRenderer;

    public TestGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mGLEpgViewRenderer = new GLEpgViewRenderer(context);
        setRenderer(mGLEpgViewRenderer);
    }

    public TestGLSurfaceView(Context context, AttributeSet attrs) {
        this(context);
//        setEGLContextClientVersion(2);
//        setRenderer(new GLEpgViewRenderer(context));
    }

    private float mPreviousX;
    private float mPreviousY;
    private float dx;
    private float dy;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

//        float x = e.getX();
//        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreviousX = e.getX();
                mPreviousY = e.getY();
//                mGLEpgViewRenderer.setMove(
//                        0, 0);
//                mGLEpgViewRenderer.setPosition(e.getX() - mPreviousX, e.getY() - mPreviousY);
                break;
            case MotionEvent.ACTION_UP:
                mPreviousX = e.getX();
                mPreviousY = e.getY();
//                mGLEpgViewRenderer.setPosition(mPreviousX, mPreviousY);
                mGLEpgViewRenderer.setPosition(e.getX() - mPreviousX, e.getY() - mPreviousY);
                break;
            case MotionEvent.ACTION_MOVE:
                dx = e.getX() - mPreviousX;
                dy = e.getY() - mPreviousY;

//                // reverse direction of rotation above the mid-line
//                if (y > getHeight() / 2) {
//                    dx = dx * -1 ;
//                }
//
//                // reverse direction of rotation to left of the mid-line
//                if (x < getWidth() / 2) {
//                    dy = dy * -1 ;
//                }

                mGLEpgViewRenderer.setMove(
                        dx, dy);
                requestRender();
        }
        return true;
    }
}
