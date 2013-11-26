package com.john.glepgviewer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by john on 10/28/13.
 */
public class TestGLSurfaceView extends GLSurfaceView {
    public TestGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new GLEpgViewRenderer(context));
    }

    public TestGLSurfaceView(Context context, AttributeSet attrs) {
        this(context);
//        setEGLContextClientVersion(2);
//        setRenderer(new GLEpgViewRenderer(context));
    }


}
