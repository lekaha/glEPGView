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
            // Order of coordinates: X, Y, U, V
            // Triangle Fan: Time minute text block
            WIDTH/2f,                       TITLE_HEIGHT/2f,   0.5f, 0.5f,
            LAYOUT_PADDING_LEFT,            TITLE_HEIGHT,      0.0f, 1.0f,
            WIDTH - LAYOUT_PADDING_RIGHT,   TITLE_HEIGHT,      1.0f, 1.0f,
            WIDTH - LAYOUT_PADDING_RIGHT,   0f,                 1.0f, 0.0f,
            LAYOUT_PADDING_LEFT,            0f,                 0.0f, 0.0f,
            LAYOUT_PADDING_LEFT,            TITLE_HEIGHT,      0.0f, 1.0f
        };
        VERTEX_DATA = vertexData;
    }

//    private EventTitle(String title, Typeface typeface){
//        vertexArray = new VertexArray(VERTEX_DATA);
//        mTextView = new GLTextView(title, typeface, 16, 16, 16, vertexArray);
//    }

//    public EventTitle(Context context, Typeface typeface, float[] projectionMatrix){
//        float[] matrix = new float[16];
//        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
//        vertexArray = new VertexArray(VERTEX_DATA);
//        mTextView = new GLTextView(context, R.raw.robot, vertexArray, matrix);
//    }

    public EventTitle(Context context, String title, Typeface typeface,
                      int merge,
                      float upperBound, float lowerBound, float rightBound,
                      float[] matrix){
        mContext = context;
        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);

        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            TITLE_SIZE,
                            mContext.getResources().getDisplayMetrics());
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        WIDTH * merge - LAYOUT_PADDING_RIGHT - LAYOUT_PADDING_LEFT,
                        mContext.getResources().getDisplayMetrics());
        float paddingTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            FONT_PADDING,
                            mContext.getResources().getDisplayMetrics());
        float lineSpacing = paddingTop;
        float paddingBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    LAYOUT_PADDING_BOTTOM,
                    mContext.getResources().getDisplayMetrics());
        float upper = (0f == upperBound)? 0f: upperBound + 0.00f;
        float lower = lowerBound - paddingBottom;
        float size = textSize;
        int line = GLTextView.StringFormat(title, (int)(width), (int)(size)).length;    //Pre-load number of lines
        float h = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    TITLE_HEIGHT,
                    mContext.getResources().getDisplayMetrics()) * line + paddingBottom;
        float bound = upper + h;
        int frontColor = Color.parseColor(BIG_TEXT_COLOR);
        int backColor = Color.parseColor(BG_COLOR);

//        Log.d(TAG, "init: line=" + GLTextView.StringFormat(title, (int)(width), (int)(textSize)).length + ":" + Math.ceil(line)
//                + " upperBound=" + upper
//                + " lowBound=" + lower
//                + " h=" + h
//                + " d=" + d
//                + " bound=" + bound
//                + " density=" + mContext.getResources().getDisplayMetrics().density
//        );

        for(int i = 0; i<(VERTEX_DATA.length/DIMENSION); i++){
            if(0 == i){
                //Maybe make some wrong please ignore it.
                VERTEX_DATA[i * DIMENSION + X] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        (WIDTH * merge)/2f,
                        mContext.getResources().getDisplayMetrics());
            }
            else if((2 == i) || (3 == i)){
                VERTEX_DATA[i * DIMENSION + X] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        WIDTH * merge - LAYOUT_PADDING_RIGHT,
                        mContext.getResources().getDisplayMetrics());
            }
            else {
                VERTEX_DATA[i * DIMENSION + X] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        VERTEX_DATA[i * DIMENSION + X],
                        mContext.getResources().getDisplayMetrics());
            }

            if((1 == i) || (2 == i) || (5 == i)){
                VERTEX_DATA[i * DIMENSION + Y] = h
                                               + upper
                                               + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                                    LAYOUT_PADDING_TOP,
                                                    mContext.getResources().getDisplayMetrics());
            }
            else{
                VERTEX_DATA[i * DIMENSION + Y] =
                        upper
                        + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        LAYOUT_PADDING_TOP,
                        mContext.getResources().getDisplayMetrics());
            }

        }
        VERTEX_DATA[0 * DIMENSION + X] = (VERTEX_DATA[2 * DIMENSION + X] + VERTEX_DATA[1 * DIMENSION + X])/2f;
        VERTEX_DATA[0 * DIMENSION + Y] = (VERTEX_DATA[2 * DIMENSION + Y] + VERTEX_DATA[3 * DIMENSION + Y])/2f;

        if(upper > lower){
            Log.d(TAG, "init: Case1");
            for(int i = 0; i<(VERTEX_DATA.length/DIMENSION); i++){
                VERTEX_DATA[i * DIMENSION + X] = 0f;
                VERTEX_DATA[i * DIMENSION + Y] = 0f;
            }
        }
        else if( bound > lower){
            Log.d(TAG, "init: Case2");
            float r = Math.abs(lower - VERTEX_DATA[3 * DIMENSION + Y])/Math.abs(h);
            VERTEX_DATA[1 * DIMENSION + V] = r;
            VERTEX_DATA[2 * DIMENSION + V] = r;
            VERTEX_DATA[5 * DIMENSION + V] = r;
            VERTEX_DATA[0 * DIMENSION + V] = (VERTEX_DATA[2 * DIMENSION + V] + VERTEX_DATA[3 * DIMENSION + V])/2f;

            VERTEX_DATA[1 * DIMENSION + Y] = lower;
            VERTEX_DATA[2 * DIMENSION + Y] = lower;
            VERTEX_DATA[5 * DIMENSION + Y] = lower;
            VERTEX_DATA[0 * DIMENSION + Y] = (VERTEX_DATA[2 * DIMENSION + Y] + VERTEX_DATA[3 * DIMENSION + Y])/2f;
        }

        Log.d(TAG, "init: text size = " + textSize + " height=" + ((int)textSize + lineSpacing) * line);

        vertexArray = new VertexArray(VERTEX_DATA);
        backColor = Color.parseColor("#ff268626");
        float dy = mContext.getResources().getDisplayMetrics().density + 0.8f;  // Text's density weight
        mTextView = new GLTextView(title, typeface,
                (int)(textSize * dy),   //Text's size
                (int)(width * dy),  //Text's area width
                (int)(((textSize + lineSpacing) * line) * dy),  //Text's area height
                0, (int)paddingTop,  //padding left and padding top
                0f, lineSpacing * dy,   //letter spacing and line spacing
                frontColor, backColor,
                vertexArray, projectionMatrix);
    }

    @Override
    public void set(float px, float py){
        for(int i = 0; i<(VERTEX_DATA.length/DIMENSION); i++){
            VERTEX_DATA[i * DIMENSION + X] += px;
        }
        vertexArray.commit();
    }

    public void bindData() {
        mTextView.bindData();
    }

    public void draw(){
//        mTextView.begin();
        mTextView.draw();
        mTextView.end();
    }
}
