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

    private static final float TITLE_SIZE = 16f;
//    private static final float TITLE_WIDTH = 19f;
    private static final float TITLE_HEIGHT = TITLE_SIZE + FONT_PADDING;

    private static final float WIDTH = 145f;

    @Override
    protected void init(boolean nothing){
        float[] vertexData = {
            // Triangle Fan: Time minute text block
            WIDTH/2f,                       -TITLE_HEIGHT/2f,   0.5f, 0.5f,
            LAYOUT_PADDING_LEFT,            -TITLE_HEIGHT,      0.0f, 1.0f,
            WIDTH - LAYOUT_PADDING_RIGHT,   -TITLE_HEIGHT,      1.0f, 1.0f,
            WIDTH - LAYOUT_PADDING_RIGHT,   0f,                 1.0f, 0.0f,
            LAYOUT_PADDING_LEFT,            0f,                 0.0f, 0.0f,
            LAYOUT_PADDING_LEFT,            -TITLE_HEIGHT,      0.0f, 1.0f
        };
        VERTEX_DATA = vertexData;
    }

    private EventTitle(String title, Typeface typeface){
        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLTextView(title, typeface, 16, 16, 16, vertexArray);
    }

    public EventTitle(Context context, String title, Typeface typeface,
                      int merge,
                      float upperBound, float lowerBound, float rightBound,
                      float[] matrix){
        mContext = context;
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            TITLE_SIZE,
                            mContext.getResources().getDisplayMetrics());
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    WIDTH * merge - LAYOUT_PADDING_RIGHT - LAYOUT_PADDING_LEFT,
                    mContext.getResources().getDisplayMetrics());

        float lineSpacing = FONT_PADDING;

        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);

        float upper = upperBound + 0.00f;
        float lower = lowerBound + 0.05f;

        //Pre-load number of lines
        int line = GLTextView.StringFormat(title, (int)(width), (int)(textSize)).length;
        float h = Math.abs((VERTEX_DATA[3 * DIMENSION + Y] - VERTEX_DATA[2 * DIMENSION + Y])) * line;
        float bound = upper - h;
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
                + " density=" + mContext.getResources().getDisplayMetrics().density
        );

        for(int i = 0; i<6; i++){
            VERTEX_DATA[i * DIMENSION + X] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    VERTEX_DATA[i * DIMENSION + X],
                    mContext.getResources().getDisplayMetrics());
            VERTEX_DATA[i * DIMENSION + Y] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    VERTEX_DATA[i * DIMENSION + Y] * line,
                    mContext.getResources().getDisplayMetrics())
                    + upper
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
//            VERTEX_DATA[1 * DIMENSION + V] = 1.0f;
//            VERTEX_DATA[2 * DIMENSION + V] = 1.0f;
//            VERTEX_DATA[5 * DIMENSION + V] = 1.0f;
//            VERTEX_DATA[3 * DIMENSION + V] = 0.0f;
//            VERTEX_DATA[4 * DIMENSION + V] = 0.0f;
//            VERTEX_DATA[0 * DIMENSION + V] = 0.5f;
        }

        Log.d(TAG, "init: text size = " + textSize + " height=" + ((int)textSize + lineSpacing) * line);
        vertexArray = new VertexArray(VERTEX_DATA);
//        backColor = Color.parseColor("#ff268626");
        float dy = mContext.getResources().getDisplayMetrics().density + 0.8f;  // Text's density weight
        mTextView = new GLTextView(title, typeface,
                (int)(textSize * dy),   //Text's size
                (int)(width * dy),  //Text's area width
                (int)(((textSize + lineSpacing) * dy) * line),  //Text's area height
                0, -3,  //padding left and padding top
                0f, lineSpacing * dy,   //letter spacing and line spacing
                frontColor, backColor,
                vertexArray, projectionMatrix);
    }

    public EventTitle(Context context, Typeface typeface, float[] projectionMatrix){
        float[] matrix = new float[16];
        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLTextView(context, R.raw.robot, vertexArray, matrix);
    }

    public float getCenterVerticeYPoint(){return VERTEX_DATA[0 * DIMENSION + Y];}
    public float getTopVerticeYPoint(){return VERTEX_DATA[3 * DIMENSION + Y];}
    public float getBottomVerticeYPoint(){return VERTEX_DATA[1 * DIMENSION
            + Y];}

    public void bindData() {
        mTextView.bindData();
    }

    public void draw(){
//        mTextView.begin();
        mTextView.draw();
        mTextView.end();
    }
}
