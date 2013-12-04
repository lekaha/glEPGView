package com.john.glepgviewer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import android.util.TypedValue;

import com.john.glepgviewer.util.ColorConverter;

/**
 * Created by john on 11/6/13.
 */
public class EventGenre extends EventComponent{

    protected ColorConverter.GLColor backgroundColor = ColorConverter.toGLColor("#1A1A1A");
    protected ColorConverter.GLColor fontColor = ColorConverter.toGLColor("#FFFFFF");

    protected final static int POSITION_COMPONENT_COUNT = 2;
    protected final static int TEXTURE_COMPONENT_COUNT = 2;
    protected final static float GENRE_MARGIN_TOP = LAYOUT_PADDING_TOP + 2f;
    protected final static float GENRE_MARGIN_RIGHT = 4f;
    protected final static float GENRE_WIDTH = 18f;
    protected final static float GENRE_HEIGHT = 18f;
    protected final static int V = 3;

    protected GLImageView mTextView;
//    protected VertexArray vertexArray;

    @Override
    public void init(){
        DIMENSION = (POSITION_COMPONENT_COUNT + TEXTURE_COMPONENT_COUNT);
        STRIDE = DIMENSION * Constants.BYTES_PER_FLOAT;

        float[] vertexData = {
            // Order of coordinates: X, Y, U, V
            // Triangle Fan: Time minute text block
            GENRE_WIDTH/2f  , (GENRE_MARGIN_TOP + GENRE_HEIGHT)/2f  , 0.5f, 0.5f,
            0f              , (GENRE_MARGIN_TOP + GENRE_HEIGHT)                        , 0.0f, 1.0f,
            GENRE_WIDTH     , (GENRE_MARGIN_TOP + GENRE_HEIGHT)                        , 1.0f, 1.0f,
            GENRE_WIDTH     , GENRE_MARGIN_TOP                                         , 1.0f, 0.0f,
            0f              , GENRE_MARGIN_TOP                                         , 0.0f, 0.0f,
            0f              , (GENRE_MARGIN_TOP + GENRE_HEIGHT)                        , 0.0f, 1.0f
        };
        VERTEX_DATA = vertexData;
    }

    public EventGenre(Context context, String resPath, float[] projectionMatrix){
        float[] matrix = new float[16];
        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);

        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLImageView(context, resPath, 30, 30, vertexArray, matrix);
//        mTextView = new GLTextView(minute, typeface, 12, 64, 64, vertexArray, matrix);
    }

    public EventGenre(Context context, int resId, float left, float right, float upper, float lower, float[] matrix){
        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);
        float width  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                GENRE_WIDTH,
                context.getResources().getDisplayMetrics());
        float height  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                GENRE_HEIGHT,
                context.getResources().getDisplayMetrics());
        float paddingBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                LAYOUT_PADDING_BOTTOM,
                context.getResources().getDisplayMetrics());
        float l = lower + paddingBottom;

        for(int i = 0; i<(VERTEX_DATA.length/DIMENSION); i++){
            VERTEX_DATA[i * DIMENSION + X] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    VERTEX_DATA[i * DIMENSION + X] + GENRE_MARGIN_RIGHT,
                    context.getResources().getDisplayMetrics()) + left;
            VERTEX_DATA[i * DIMENSION + Y] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    VERTEX_DATA[i * DIMENSION + Y],
                    context.getResources().getDisplayMetrics()) + upper;
        }
        VERTEX_DATA[0 * DIMENSION + Y] = (VERTEX_DATA[2 * DIMENSION + Y] + VERTEX_DATA[3 * DIMENSION + Y])/2f;

        if(getTopVerticeYPoint() > l){
            for(int i = 0; i<(VERTEX_DATA.length/DIMENSION); i++){
                VERTEX_DATA[i * DIMENSION + X] = 0f;
                VERTEX_DATA[i * DIMENSION + Y] = 0f;
            }
        }
        else if(getBottomVerticeYPoint() > l){
            Log.d(TAG, "init: Case2");
            float r = Math.abs(l - VERTEX_DATA[3 * DIMENSION + Y])/Math.abs(height);
            VERTEX_DATA[1 * DIMENSION + V] = r;
            VERTEX_DATA[2 * DIMENSION + V] = r;
            VERTEX_DATA[5 * DIMENSION + V] = r;
            VERTEX_DATA[0 * DIMENSION + V] = (VERTEX_DATA[2 * DIMENSION + V] + VERTEX_DATA[3 * DIMENSION + V])/2f;

            VERTEX_DATA[1 * DIMENSION + Y] = l;
            VERTEX_DATA[2 * DIMENSION + Y] = l;
            VERTEX_DATA[5 * DIMENSION + Y] = l;
            VERTEX_DATA[0 * DIMENSION + Y] = (VERTEX_DATA[2 * DIMENSION + Y] + VERTEX_DATA[3 * DIMENSION + Y])/2f;
        }

        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLImageView(context, resId, false, (int)width, (int)height, 0, 0, vertexArray, matrix);
    }

    @Override
    public void set(float px, float py){
        vertexArray.commit();
    }

    public void setMarginLeft(float marginLeft){
        Log.d(TAG, "init: setMarginLeft=" + marginLeft);
        for(int i = 0; i<6; i++){
            VERTEX_DATA[i * DIMENSION + X] += marginLeft;
        }
    }

    @Override
    public void bindData() {
        mTextView.bindData();
    }

    @Override
    public void draw(){
//        mTextView.begin();
        mTextView.draw(GLES20.GL_TRIANGLE_FAN, 0, 6);
        mTextView.end();
    }

    @Override
    public float getCenterVerticeYPoint() {
        if(null != VERTEX_DATA){
            return VERTEX_DATA[0 * DIMENSION + Y];
        }
        return 0;
    }

    @Override
    public float getTopVerticeYPoint() {
        if(null != VERTEX_DATA){
            return VERTEX_DATA[3 * DIMENSION + Y];
        }
        return 0;
    }

    @Override
    public float getBottomVerticeYPoint() {
        if(null != VERTEX_DATA){
            return VERTEX_DATA[1 * DIMENSION + Y];
        }
        return 0;
    }
}
