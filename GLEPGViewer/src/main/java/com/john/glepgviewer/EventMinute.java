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
//    private static final String TAG = "EventMinute";

//    protected ColorConverter.GLColor backgroundColor = ColorConverter.toGLColor("#1A1A1A");
//    protected ColorConverter.GLColor fontColor = ColorConverter.toGLColor("#FFFFFF");

//    protected float[] VERTEX_DATA = {
//            // Triangle Fan: Time minute text block
//            -0.3788f, 0.4095f, 0.25f, 0.45f,
//            -0.45f, 0.3567f, 0.0f, 0.9f,
//            -0.3076f, 0.3567f, 0.5f, 0.9f,
//            -0.3076f, 0.4622f, 0.5f, 0.0f,
//            -0.45f, 0.4622f, 0.0f, 0.0f,
//            -0.45f, 0.3567f, 0.0f, 0.9f
//    };

//    private static final int FONT_SIZE = 16;
//    private static final int FONT_PADDING = 2;
//    private static final float WIDTH = 35f;

//    protected GLTextView mTextView;
//    protected VertexArray vertexArray;

    private static final float TIME_WIDTH = 19f;
    private static final float TIME_HEIGHT = 18f;
    private static final float TIME_TEXT_SIZE = 16f;
    private static final float TIME_TEXT_LINESPACING = 1f;

    protected float mWidth;
    protected float mHeight;

    @Override
    protected void init(boolean nothing){
        float[] vertexData = {
                // Order of coordinates: X, Y, U, V
                // Triangle Fan: Time minute text block
//                -0.3788f, 0.4095f, 0.25f, 0.5f,
//                -0.45f,   0.3567f, 0.0f,  1.0f,
//                -0.3076f, 0.3567f, 0.5f,  1.0f,
//                -0.3076f, 0.4622f, 0.5f,  0.0f,
//                -0.45f,   0.4622f, 0.0f,  0.0f,
//                -0.45f,   0.3567f, 0.0f,  1.0f

                LAYOUT_PADDING_LEFT + TIME_WIDTH / 2f   , -(LAYOUT_PADDING_TOP + TIME_HEIGHT / 2f)  , 0.5f  , 0.5f,
                LAYOUT_PADDING_LEFT                     , -(LAYOUT_PADDING_TOP + TIME_HEIGHT)       , 0.0f  , 1.0f,
                LAYOUT_PADDING_LEFT + TIME_WIDTH        , -(LAYOUT_PADDING_TOP + TIME_HEIGHT)       , 1.0f  , 1.0f,
                LAYOUT_PADDING_LEFT + TIME_WIDTH        , -(LAYOUT_PADDING_TOP)                     , 1.0f  , 0.0f,
                LAYOUT_PADDING_LEFT                     , -(LAYOUT_PADDING_TOP)                     , 0.0f  , 0.0f,
                LAYOUT_PADDING_LEFT                     , -(LAYOUT_PADDING_TOP + TIME_HEIGHT)       , 0.0f  , 1.0f
        };
        VERTEX_DATA = vertexData;



    }

    private EventMinute(String minute, Typeface typeface){
        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLTextView(minute, typeface, 16, 16, 16, vertexArray);
    }

    public EventMinute(Context context, String minute, Typeface typeface,
                       float textSize, float width, float height, float upper, float lower,
                       float[] projectionMatrix){
        mContext = context;
        float[] matrix = new float[16];
        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            textSize,
            mContext.getResources().getDisplayMetrics());
        mWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            width,
            mContext.getResources().getDisplayMetrics());
        mHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            height,
            mContext.getResources().getDisplayMetrics());


        int frontColor = Color.parseColor(BIG_TEXT_COLOR);
        int backColor = Color.parseColor(BG_COLOR);

        float l = lower + LAYOUT_PADDING_BOTTOM;
        for(int i = 0; i<6; i++){
            VERTEX_DATA[i * DIMENSION + X] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    VERTEX_DATA[i * DIMENSION + X],
                    mContext.getResources().getDisplayMetrics());
            VERTEX_DATA[i * DIMENSION + Y] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    VERTEX_DATA[i * DIMENSION + Y],
                    mContext.getResources().getDisplayMetrics());

        }

        Log.d(TAG, "init: getTopVerticeYPoint:" + getTopVerticeYPoint() + " l:" + l);
        if(getTopVerticeYPoint() < l){
            for(int i = 0; i<6; i++){
                VERTEX_DATA[i * DIMENSION + X] = 0f;
                VERTEX_DATA[i * DIMENSION + Y] = 0f;
            }
        }
        else if(getBottomVerticeYPoint() < l){
            float r = (VERTEX_DATA[3 * DIMENSION + Y] - l)/(VERTEX_DATA[3 * DIMENSION + Y] - VERTEX_DATA[1 * DIMENSION + Y]);
            float d = r * 1.0f;
//            VERTEX_DATA[0 * DIMENSION + Y] = (l + VERTEX_DATA[3 * DIMENSION + Y])/2f;
//            VERTEX_DATA[1 * DIMENSION + Y] = l;
//            VERTEX_DATA[2 * DIMENSION + Y] = l;
//            VERTEX_DATA[5 * DIMENSION + Y] = l;
//
//            VERTEX_DATA[0 * DIMENSION + V] = d/2f;
//            VERTEX_DATA[1 * DIMENSION + V] = d;
//            VERTEX_DATA[2 * DIMENSION + V] = d;
//            VERTEX_DATA[5 * DIMENSION + V] = d;



            if(lower >= l ){
                backColor = Color.parseColor(MIN_COLOR);
            }
        }

//        textSize = Math.round(textSize);
        float line = (float)Math.ceil(((minute.length()/2) * textSize)/mWidth);
        Log.d(TAG, "init: " + line);

        vertexArray = new VertexArray(VERTEX_DATA);
//        backColor = Color.parseColor("#ff268626");
        float dy = mContext.getResources().getDisplayMetrics().density + 0.8f;
        mTextView = new GLTextView(minute, typeface,
                (int)((int)textSize * dy), (int)(mWidth * dy), (int)(((int)mHeight * dy) * line),
                0, 0,
                0f, 8f,
                frontColor, backColor,
                vertexArray, matrix);
    }

    private EventMinute(Context context, Typeface typeface, float[] projectionMatrix){
        float[] matrix = new float[16];
        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLTextView(context, R.raw.robot, vertexArray, matrix);
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
