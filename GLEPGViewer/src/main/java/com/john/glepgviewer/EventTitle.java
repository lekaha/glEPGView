package com.john.glepgviewer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by john on 11/4/13.
 */
public class EventTitle extends EventText {

//    private static final int FONT_SIZE = 16;
//    private static final int FONT_PADDING = 2;
//    private static final float WIDTH = 145f;
//
//    protected float[] VERTEX_DATA = {
//            // Triangle Fan: Time minute text block
//            -0.00f, 0.2917f, 0.4625f, 0.55f,
//            -0.45f, 0.2222f, 0.0f, 1.1f,
//             0.45f, 0.2222f, 0.925f, 1.1f,
//             0.45f, 0.3611f, 0.925f, 0.0f,
//            -0.45f, 0.3611f, 0.0f, 0.0f,
//            -0.45f, 0.2222f, 0.0f, 1.1f
//    };

//    protected GLTextView mTextView;
//    protected VertexArray vertexArray;

    @Override
    protected void init(boolean nothing){
        float[] vertexData = {
            // Triangle Fan: Time minute text block
            -0.00f, 0.2892f, 0.4625f, 0.55f,
            -0.45f, 0.2372f, 0.0f, 1.1f,
             0.45f, 0.2372f, 0.925f, 1.1f,
             0.45f, 0.3611f, 0.925f, 0.0f,
            -0.45f, 0.3611f, 0.0f, 0.0f,
            -0.45f, 0.2372f, 0.0f, 1.1f
        };
        VERTEX_DATA = vertexData;
    }

    private EventTitle(String title, Typeface typeface){
        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLTextView(title, typeface, 16, 16, 16, vertexArray);
    }

    public EventTitle(Context context, String title, Typeface typeface,
                      float textSize, float width, float height,
                      float upperBound, float lowerBound, float rightBound,
                      float[] projectionMatrix){
        mContext = context;
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                textSize,
                mContext.getResources().getDisplayMetrics());
        width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                width,
                mContext.getResources().getDisplayMetrics());
        width -= 10f;
        float[] matrix = new float[16];
        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
        float upper = upperBound + 0.00f;
        float lower = lowerBound + 0.05f;

        //Pre-load number of lines
        int line = GLTextView.StringFormat(title, (int)(width), (int)(textSize)).length;
        float h = VERTEX_DATA[0 * DIMENSION + Y] - VERTEX_DATA[1 * DIMENSION + Y];
        float bound = upper - (h * 2f * line);
        float d = (upper + bound)/2f;
        int frontColor = Color.parseColor(BIG_TEXT_COLOR);
        int backColor = Color.parseColor(BG_COLOR);
//        int backColor = Color.parseColor("#265626");

        Log.d(TAG, "init: line=" + GLTextView.StringFormat(title, (int)(width), (int)(textSize)).length + ":" + Math.ceil(line)
                + " upperBound=" + upper
                + " lowBound=" + lower
                + " h=" + h
                + " d=" + d
                + " bound=" + bound
        );

        if(upper <= lower){
            Log.d(TAG, "init: Case1");
            bound = upper;
            d = upper;
            VERTEX_DATA[0 * DIMENSION + V] = 0.0f;
            VERTEX_DATA[1 * DIMENSION + V] = 0.0f;
            VERTEX_DATA[2 * DIMENSION + V] = 0.0f;
            VERTEX_DATA[5 * DIMENSION + V] = 0.0f;
        }
        else if( bound <= lower){
            Log.d(TAG, "init: Case2");
            float r = Math.abs(upper - lower)/Math.abs(upper - bound);
//            r += (r/10f);
            bound = lower;
            d = (upper + bound)/2f;
            VERTEX_DATA[0 * DIMENSION + V] = r/2f;
            VERTEX_DATA[1 * DIMENSION + V] = r;
            VERTEX_DATA[2 * DIMENSION + V] = r;
            VERTEX_DATA[5 * DIMENSION + V] = r;

//            Log.d(TAG, "init: r=" + r);
//            Log.d(TAG, "init: upper-" + VERTEX_DATA[3 * 4 + 1] + " center-" + VERTEX_DATA[0 * 4 + 1] + " lower-" + VERTEX_DATA[1 * 4 + 1]);
        }
        else{
            Log.d(TAG, "init: Case3");
//            lower -= 0.05f;
//            upper += (0.04f * w);
//            bound -= (0.04f * w);

            d = (upper + bound)/2f;
            VERTEX_DATA[1 * DIMENSION + V] = 1.0f;
            VERTEX_DATA[2 * DIMENSION + V] = 1.0f;
            VERTEX_DATA[5 * DIMENSION + V] = 1.0f;
            VERTEX_DATA[3 * DIMENSION + V] = 0.0f;
            VERTEX_DATA[4 * DIMENSION + V] = 0.0f;
            VERTEX_DATA[0 * DIMENSION + V] = 0.5f;
        }
        VERTEX_DATA[0 * DIMENSION + Y] = d;
        VERTEX_DATA[1 * DIMENSION + Y] = bound;
        VERTEX_DATA[2 * DIMENSION + Y] = bound;
        VERTEX_DATA[3 * DIMENSION + Y] = upper;
        VERTEX_DATA[4 * DIMENSION + Y] = upper;
        VERTEX_DATA[5 * DIMENSION + Y] = bound;

        VERTEX_DATA[2 * DIMENSION + X] = rightBound - 0.05f;
        VERTEX_DATA[3 * DIMENSION + X] = rightBound - 0.05f;
        VERTEX_DATA[0 * DIMENSION + X] = (VERTEX_DATA[2 * DIMENSION + X] + VERTEX_DATA[1 * DIMENSION + X])/2f;

        Log.d(TAG, "init: text size = " + textSize + " height=" + ((int)textSize + FONT_PADDING + FONT_PADDING ) * line);
        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLTextView(title, typeface,
                (int)textSize, (int)(width), (int)(textSize * line),
                0, 0, frontColor, backColor, vertexArray, matrix);
    }

    public EventTitle(Context context, Typeface typeface, float[] projectionMatrix){
        float[] matrix = new float[16];
        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLTextView(context, R.raw.robot, vertexArray, matrix);
    }

    public float getCenterVerticeYPoint(){return VERTEX_DATA[0 * DIMENSION + Y];}
    public float getTopVerticeYPoint(){return VERTEX_DATA[3 * DIMENSION + Y];}
    public float getBottomVerticeYPoint(){return VERTEX_DATA[1 * DIMENSION + Y];}

    public void bindData() {
        mTextView.bindData();
    }

    public void draw(){
//        mTextView.begin();
        mTextView.draw();
        mTextView.end();
    }
}
