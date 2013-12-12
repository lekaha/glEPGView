package com.john.glepgviewer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;

/**
 * Created by john on 11/5/13.
 */
public class EventDescription extends EventText {

    private static final float DESCRIPTION_SIZE = 14f;
//    private static final float DESCRIPTION_WIDTH = 19f;
    private static final float DESCRIPTION_HEIGHT = DESCRIPTION_SIZE + FONT_PADDING;

    private static final float WIDTH = 145f;

    private float mLayoutPaddingLeft;
    private float mUpperBound;

    @Override
    protected void init(boolean nothing){
        float[] vertexData = {
            // Order of coordinates: X, Y, U, V
            // Triangle Fan: Time minute text block
            WIDTH/2f,                       DESCRIPTION_HEIGHT/2f,   0.5f, 0.5f,
            LAYOUT_PADDING_LEFT,            DESCRIPTION_HEIGHT,      0.0f, 1.0f,
            WIDTH - LAYOUT_PADDING_RIGHT,   DESCRIPTION_HEIGHT,      1.0f, 1.0f,
            WIDTH - LAYOUT_PADDING_RIGHT,   0f,                       1.0f, 0.0f,
            LAYOUT_PADDING_LEFT,            0f,                       0.0f, 0.0f,
            LAYOUT_PADDING_LEFT,            DESCRIPTION_HEIGHT,      0.0f, 1.0f
        };

        VERTEX_DATA = vertexData;

    }

//    public EventDescription(String title, Typeface typeface){
//        vertexArray = new VertexArray(VERTEX_DATA);
//        mTextView = new GLTextView(title, typeface, 16, 16, 16, vertexArray);
//    }

//    public EventDescription(Context context, Typeface typeface, float[] projectionMatrix){
//        float[] matrix = new float[16];
//        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
//        vertexArray = new VertexArray(VERTEX_DATA);
////        mTextView = new GLTextView(context, R.raw.robot, vertexArray, matrix);
//    }

    public EventDescription(Context context, String title, Typeface typeface,
                            int merge,
                            float upperBound, float lowerBound, float rightBound,
                            float[] matrix){
        mContext = context;
//        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);
        projectionMatrix = matrix;

        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    DESCRIPTION_SIZE,
                    mContext.getResources().getDisplayMetrics());
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        WIDTH * merge - LAYOUT_PADDING_RIGHT - LAYOUT_PADDING_LEFT,
                        mContext.getResources().getDisplayMetrics());
        float lineSpacing = FONT_PADDING;
        float paddingTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            FONT_PADDING,
                            mContext.getResources().getDisplayMetrics());
        float paddingBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                LAYOUT_PADDING_BOTTOM,
                                mContext.getResources().getDisplayMetrics());
        mLayoutPaddingLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                LAYOUT_PADDING_LEFT,
                mContext.getResources().getDisplayMetrics());
        mUpperBound = upperBound;

        float upper = (0f == upperBound)? 0f: upperBound + 0.00f;
        float lower = lowerBound - paddingBottom;
        float size = textSize;
        int line = GLTextView.StringFormat(title, (int)(width), (int)(size)).length;    //Pre-load number of lines
        float h = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    DESCRIPTION_HEIGHT,
                    mContext.getResources().getDisplayMetrics()) * line + paddingBottom;
        float bound = upper + h;
        int frontColor = Color.parseColor(SMALL_TEXT_COLOR);
        int backColor = Color.parseColor(BG_COLOR);

//        Log.d(TAG, "init: line=" + Math.ceil(line)
//                + " upperBound=" + upper
//                + " lowBound=" + lower
//                + " h=" + h
//                + " d=" + d
//                + " bound=" + bound
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

            VERTEX_DATA[i * DIMENSION + Y] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                                VERTEX_DATA[i * DIMENSION + Y],
                                                mContext.getResources().getDisplayMetrics()) * line
                                             + upper
                                             + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                                LAYOUT_PADDING_TOP,
                                                mContext.getResources().getDisplayMetrics());

        }
        VERTEX_DATA[0 * DIMENSION + X] = (VERTEX_DATA[2 * DIMENSION + X] + VERTEX_DATA[1 * DIMENSION + X])/2f;

        // It can't show because not enough event's height.
        if((0 == upper) || (upper > lower)){
//            Log.d(TAG, "init: Case1");

            for(int i = 0; i<(VERTEX_DATA.length/DIMENSION); i++){
                VERTEX_DATA[i * DIMENSION + X] = 0f;
                VERTEX_DATA[i * DIMENSION + Y] = 0f;
            }
        }
        // It only can show part of entire information because not enough event's height.
        else if( bound > lower){
//            Log.d(TAG, "init: Case2");
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

//        Log.d(TAG, "init: text size = " + size + " height=" + ((int)size + lineSpacing ) * line);
        mHeight = VERTEX_DATA[2 * DIMENSION + Y] - VERTEX_DATA[3 * DIMENSION + Y];
        mWidth = VERTEX_DATA[2 * DIMENSION + X] - VERTEX_DATA[1 * DIMENSION + X];

        vertexArray = new VertexArray(VERTEX_DATA);
//        backColor = Color.parseColor("#ff265626");
        float dy = mContext.getResources().getDisplayMetrics().density + 0.8f;  // Text's density weight
        mTextView = new GLTextView(title, typeface,
            (int)(textSize * dy),   //Text's size
            (int)(width * dy),  //Text's area width
            (int)((textSize + lineSpacing) * dy) * line,    //Text's area height
            0, (int)paddingTop,  //padding left and padding top
            0f, lineSpacing * dy,   //letter spacing and line spacing
            frontColor, backColor,
            vertexArray, projectionMatrix);
    }

    @Override
    public void set(float px, float py){
        pstX = px + mLayoutPaddingLeft;
        pstY = py + mUpperBound;
    }


    @Override
    public void move(float px, float py){
        VERTEX_DATA[1 * DIMENSION + X] = px + pstX;
        VERTEX_DATA[2 * DIMENSION + X] = px + (pstX + mWidth);
        VERTEX_DATA[3 * DIMENSION + X] = px + (pstX + mWidth);
        VERTEX_DATA[4 * DIMENSION + X] = px + pstX;
        VERTEX_DATA[5 * DIMENSION + X] = px + pstX;
        VERTEX_DATA[0 * DIMENSION + X] = (VERTEX_DATA[1 * DIMENSION + X] + VERTEX_DATA[2 * DIMENSION + X])/2f;

        VERTEX_DATA[1 * DIMENSION + Y] = py + (pstY + mHeight);
        VERTEX_DATA[2 * DIMENSION + Y] = py + (pstY + mHeight);
        VERTEX_DATA[3 * DIMENSION + Y] = py + pstY;
        VERTEX_DATA[4 * DIMENSION + Y] = py + pstY;
        VERTEX_DATA[5 * DIMENSION + Y] = py + (pstY + mHeight);
        VERTEX_DATA[0 * DIMENSION + Y] = (VERTEX_DATA[2 * DIMENSION + Y] + VERTEX_DATA[3 * DIMENSION + Y])/2f;

        vertexArray.commit();
    }

    @Override
    public void bindData() {
        mTextView.bindData();
    }

    @Override
    public void draw(){
//        mTextView.begin();
        mTextView.draw();
        mTextView.end();
    }
}
