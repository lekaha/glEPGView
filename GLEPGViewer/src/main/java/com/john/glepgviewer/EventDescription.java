package com.john.glepgviewer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by john on 11/5/13.
 */
public class EventDescription extends EventText {

    private static final float DESCRIPTION_WIDTH = 19f;
    private static final float DESCRIPTION_HEIGHT = 16f;

    private static final float WIDTH = 145f;

    @Override
    protected void init(boolean nothing){
        float[] vertexData = {
            // Triangle Fan: Time minute text block
//            0.0f, 0.1728f, 0.4625f, 0.25f,
//            -0.45f, 0.1233f, 0.0f, 0.5f,
//            0.45f, 0.1233f, 0.925f, 0.5f,
//            0.45f, 0.2222f, 0.925f, 0.0f,
//            -0.45f, 0.2222f, 0.0f, 0.0f,
//            -0.45f, 0.1233f, 0.0f, 0.5f

            WIDTH/2f,                       -DESCRIPTION_HEIGHT/2f,   0.5f, 0.5f,
            LAYOUT_PADDING_LEFT,            -DESCRIPTION_HEIGHT,      0.0f, 1.0f,
            WIDTH - LAYOUT_PADDING_RIGHT,   -DESCRIPTION_HEIGHT,      1.0f, 1.0f,
            WIDTH - LAYOUT_PADDING_RIGHT,   0f,                       1.0f, 0.0f,
            LAYOUT_PADDING_LEFT,            0f,                       0.0f, 0.0f,
            LAYOUT_PADDING_LEFT,            -DESCRIPTION_HEIGHT,      0.0f, 1.0f
        };

        VERTEX_DATA = vertexData;

    }

    public EventDescription(String title, Typeface typeface){
        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLTextView(title, typeface, 16, 16, 16, vertexArray);
    }

    public EventDescription(Context context, String title, Typeface typeface,
                            float textSize, float width, float height,
                            float upperBound, float lowerBound, float rightBound,
                            float[] matrix){
        mContext = context;
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            textSize,
            mContext.getResources().getDisplayMetrics());
        width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                width,
                mContext.getResources().getDisplayMetrics());

        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);

        float upper = upperBound + 0.00f;
        float lower = lowerBound + 0.05f;
        float size = Math.round(textSize);

        //Pre-load number of lines
        int line = GLTextView.StringFormat(title, (int)(width), (int)(size)).length;
        float h = VERTEX_DATA[0 * DIMENSION + 1] - VERTEX_DATA[1 * DIMENSION + 1];
        float bound = upper - (h * 2f * line);
        float d = (upper + bound)/2f;
        int frontColor = Color.parseColor(SMALL_TEXT_COLOR);
        int backColor = Color.parseColor(BG_COLOR);
//        int backColor = Color.parseColor("#265626");

        Log.d(TAG, "init: line=" + Math.ceil(line)
                + " upperBound=" + upper
                + " lowBound=" + lower
                + " h=" + h
                + " d=" + d
                + " bound=" + bound
        );

        for(int i = 0; i<6; i++){
            VERTEX_DATA[i * DIMENSION + X] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    VERTEX_DATA[i * DIMENSION + X],
                    mContext.getResources().getDisplayMetrics());
            VERTEX_DATA[i * DIMENSION + Y] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    VERTEX_DATA[i * DIMENSION + Y] * line,
                    mContext.getResources().getDisplayMetrics())
                    + upperBound
                    + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    -LAYOUT_PADDING_TOP,
                    mContext.getResources().getDisplayMetrics());

        }

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
//            r += (.1f);
//            upper += 0.05f;
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
//            upper += (0.05f * w);
//            bound += (0.05f * w);

            d = (upper + bound)/2f;

//            VERTEX_DATA[1 * DIMENSION + V] = 1.1f;
//            VERTEX_DATA[2 * DIMENSION + V] = 1.1f;
//            VERTEX_DATA[5 * DIMENSION + V] = 1.1f;
//            VERTEX_DATA[3 * DIMENSION + V] = 0.0f;
//            VERTEX_DATA[4 * DIMENSION + V] = 0.0f;
//            VERTEX_DATA[0 * DIMENSION + V] = 0.55f;
        }


//        VERTEX_DATA[0 * DIMENSION + Y] = d;
//        VERTEX_DATA[1 * DIMENSION + Y] = bound;
//        VERTEX_DATA[2 * DIMENSION + Y] = bound;
//        VERTEX_DATA[3 * DIMENSION + Y] = upper;
//        VERTEX_DATA[4 * DIMENSION + Y] = upper;
//        VERTEX_DATA[5 * DIMENSION + Y] = bound;
//
//        VERTEX_DATA[2 * DIMENSION + X] = rightBound - 0.05f;
//        VERTEX_DATA[3 * DIMENSION + X] = rightBound - 0.05f;
//        VERTEX_DATA[0 * DIMENSION + X] = (VERTEX_DATA[2 * DIMENSION + X] + VERTEX_DATA[1 * DIMENSION + X])/2f;

        Log.d(TAG, "init: text size = " + size + " height=" + ((int)size + FONT_PADDING + FONT_PADDING ) * line);
        vertexArray = new VertexArray(VERTEX_DATA);
//        backColor = Color.parseColor("#ff268626");
        float dy = mContext.getResources().getDisplayMetrics().density + 0.8f;
        mTextView = new GLTextView(title, typeface,
            (int)((textSize) * dy), (int)(width * dy), (int)((textSize * line) * dy),
            0, -3, frontColor, backColor, vertexArray, projectionMatrix);
    }

    public EventDescription(Context context, Typeface typeface, float[] projectionMatrix){
        float[] matrix = new float[16];
        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
        vertexArray = new VertexArray(VERTEX_DATA);
//        mTextView = new GLTextView(context, R.raw.robot, vertexArray, matrix);
    }

    public float getCenterVerticeYPoint(){return VERTEX_DATA[0 * DIMENSION + 1];}
    public float getTopVerticeYPoint(){return VERTEX_DATA[3 * DIMENSION + 1];}
    public float getBottomVerticeYPoint(){return VERTEX_DATA[1 * DIMENSION + 1];}

    public void bindData() {
        mTextView.bindData();
    }

    public void draw(){
//        mTextView.begin();
        mTextView.draw();
        mTextView.end();
    }
}
