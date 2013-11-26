package com.john.glepgviewer;

import android.util.Log;

/**
 * Created by john on 11/12/13.
 */
public abstract class EventComponent {
    protected final String TAG = getClass().getSimpleName();

    protected final static int X = 0, Y = 1;
    protected final static String MIN_COLOR = "#69a5d7";
    protected final static String BG_COLOR = "#262626";
    protected final int FONT_PADDING = 2;

    /* Need to initialize */
    protected int DIMENSION;
    protected int STRIDE;
    protected float[] VERTEX_DATA;
    protected VertexArray vertexArray;

    public EventComponent(){
        Log.d(TAG, "ctor: EventComponent");
        init();
    }

//    public EventComponent(String title, Typeface typeface){}
//    public EventComponent(String title, Typeface typeface, float textSize, float width, float height, float[] projectionMatrix, float upperBound, float lowBound){}
//    public EventComponent(Context context, Typeface typeface, float[] projectionMatrix){}

    public abstract float getCenterVerticeYPoint();
    public abstract float getTopVerticeYPoint();
    public abstract float getBottomVerticeYPoint();

    public abstract void init();
    public abstract void bindData();
    public abstract void draw();

    public class EventRec{
        public float getTopBound(){
            return getTopVerticeYPoint();
        }

        public float getRightBound(){
            return getTopVerticeYPoint();
        }

        public float getBottomBound(){
            return getBottomVerticeYPoint();
        }

        public float getLeftBound(){
            return getTopVerticeYPoint();
        }
    }
}
