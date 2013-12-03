package com.john.glepgviewer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;

/**
 * Created by john on 10/28/13.
 */
public class EventMinute extends EventText{
    private static final float TIME_SIZE = 16f;
    //    private static final float TITLE_WIDTH = 19f;
    private static final float TIME_HEIGHT = TIME_SIZE + FONT_PADDING;
    private static final float WIDTH = 19f;

    @Override
    protected void init(boolean nothing){
        float[] vertexData = {
                // Order of coordinates: X, Y, U, V
                // Triangle Fan: Time minute text block
                LAYOUT_PADDING_LEFT + WIDTH / 2f   , -(LAYOUT_PADDING_TOP + TIME_HEIGHT / 2f)  , 0.5f  , 0.5f,
                LAYOUT_PADDING_LEFT                , -(LAYOUT_PADDING_TOP + TIME_HEIGHT)       , 0.0f  , 1.0f,
                LAYOUT_PADDING_LEFT + WIDTH        , -(LAYOUT_PADDING_TOP + TIME_HEIGHT)       , 1.0f  , 1.0f,
                LAYOUT_PADDING_LEFT + WIDTH        , -(LAYOUT_PADDING_TOP)                     , 1.0f  , 0.0f,
                LAYOUT_PADDING_LEFT                , -(LAYOUT_PADDING_TOP)                     , 0.0f  , 0.0f,
                LAYOUT_PADDING_LEFT                , -(LAYOUT_PADDING_TOP + TIME_HEIGHT)       , 0.0f  , 1.0f
        };
        VERTEX_DATA = vertexData;

    }

    private EventMinute(String minute, Typeface typeface){
        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLTextView(minute, typeface, 16, 16, 16, vertexArray);
    }

    private EventMinute(Context context, Typeface typeface, float[] projectionMatrix){
        float[] matrix = new float[16];
        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLTextView(context, R.raw.robot, vertexArray, matrix);
    }

    public EventMinute(Context context, String minute, Typeface typeface,
                       float upperBound, float lowerBound,
                       float[] matrix){
        mContext = context;
        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            TIME_SIZE,
                            mContext.getResources().getDisplayMetrics());
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        WIDTH,
                        mContext.getResources().getDisplayMetrics());
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            TIME_HEIGHT,
                            mContext.getResources().getDisplayMetrics());
        float lineSpacing = FONT_PADDING;
        float paddingTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            LAYOUT_PADDING_TOP,
                            mContext.getResources().getDisplayMetrics());
        float paddingBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                LAYOUT_PADDING_BOTTOM,
                                mContext.getResources().getDisplayMetrics());
        float minHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                            MIN_EVENT_HEIGHT,
                            mContext.getResources().getDisplayMetrics());
        float upper = upperBound + 0.00f;
        float lower = lowerBound + paddingBottom;
        float size = textSize;
        float h = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    TIME_HEIGHT,
                    mContext.getResources().getDisplayMetrics()) + paddingBottom;
        float bound = upper - h;
        int frontColor = Color.parseColor(BIG_TEXT_COLOR);
        int backColor = Color.parseColor(BG_COLOR);

        for(int i = 0; i<(VERTEX_DATA.length/DIMENSION); i++){
            VERTEX_DATA[i * DIMENSION + X] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    VERTEX_DATA[i * DIMENSION + X],
                    mContext.getResources().getDisplayMetrics());
            VERTEX_DATA[i * DIMENSION + Y] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    VERTEX_DATA[i * DIMENSION + Y],
                    mContext.getResources().getDisplayMetrics()) + upper;

        }

        Log.d(TAG, "init: getTopVerticeYPoint:" + getTopVerticeYPoint() + " h:" + h);
        if(getTopVerticeYPoint() < lower){
            for(int i = 0; i<(VERTEX_DATA.length/DIMENSION); i++){
                VERTEX_DATA[i * DIMENSION + X] = 0f;
                VERTEX_DATA[i * DIMENSION + Y] = 0f;
            }
        }
        else if(getBottomVerticeYPoint() < lower){
            Log.d(TAG, "init: Case2");
            float r = Math.abs(VERTEX_DATA[3 * DIMENSION + Y] - lower)/Math.abs(h);
            VERTEX_DATA[1 * DIMENSION + V] = r;
            VERTEX_DATA[2 * DIMENSION + V] = r;
            VERTEX_DATA[5 * DIMENSION + V] = r;
            VERTEX_DATA[0 * DIMENSION + V] = (VERTEX_DATA[2 * DIMENSION + V] + VERTEX_DATA[3 * DIMENSION + V])/2f;

            VERTEX_DATA[1 * DIMENSION + Y] = lower;
            VERTEX_DATA[2 * DIMENSION + Y] = lower;
            VERTEX_DATA[5 * DIMENSION + Y] = lower;
            VERTEX_DATA[0 * DIMENSION + Y] = (VERTEX_DATA[2 * DIMENSION + Y] + VERTEX_DATA[3 * DIMENSION + Y])/2f;


            if(minHeight >= (Math.abs(VERTEX_DATA[3 * DIMENSION + Y] - lower) + paddingTop) ){
                backColor = Color.parseColor(MIN_COLOR);
            }
        }

//        textSize = Math.round(textSize);
//        float line = (float)Math.ceil(((minute.length()/2) * textSize)/width);
//        Log.d(TAG, "init: " + line);

        vertexArray = new VertexArray(VERTEX_DATA);
//        backColor = Color.parseColor("#ff268626");
        float dy = mContext.getResources().getDisplayMetrics().density + 0.8f;
        mTextView = new GLTextView(minute, typeface,
                (int)((int)textSize * dy), (int)(width * dy), (int)(((int)height * dy)),
                0, 0,
                0f, lineSpacing,
                frontColor, backColor,
                vertexArray, matrix);
    }


    @Override
    public void set(float px, float py){
        for(int i = 0; i<(VERTEX_DATA.length/DIMENSION); i++){
            VERTEX_DATA[i * DIMENSION + X] += px;

        }
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
