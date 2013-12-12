package com.john.glepgviewer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.TypedValue;

/**
 * Created by john on 11/6/13.
 */
public class EventRecord extends EventComponent{
    protected final static int V = 3;
    protected final static int POSITION_COMPONENT_COUNT = 2;
    protected final static int TEXTURE_COMPONENT_COUNT = 2;

    private final static float WIDTH = 32f;
    private final static float HEIGHT = 32f;

    protected GLImageView mTextView;
//    protected VertexArray vertexArray;
    protected boolean isClipping = false;

    private float mRightBound;
    private float mUpperBound;

    @Override
    public void init(){
        DIMENSION = (POSITION_COMPONENT_COUNT + TEXTURE_COMPONENT_COUNT);
        STRIDE = DIMENSION * Constants.BYTES_PER_FLOAT;

        float[] vertexData = {
            // Triangle
            0, HEIGHT, 1.0f, 1.0f,
            0, 0, 1.0f, 0.0f,
            -WIDTH, 0, 0.0f, 0.0f,

            // Triangle Fan: Time minute text block
//            0.3759f, 0.4000f, 0.5f, 0.5f,
//            0.2517f, 0.3000f, 0.0f, 1.0f,
//            0.5000f, 0.3000f, 1.0f, 1.0f,
//            0.5000f, 0.5000f, 1.0f, 0.0f,
//            0.2517f, 0.5000f, 0.0f, 0.0f,
//            0.2517f, 0.3000f, 0.0f, 1.0f

            0.2517f, 0.5000f, 0.0f, 0.0f,
            0.2517f, 0.4000f, 0.0f, 1.0f,
            0.5000f, 0.4000f, 1.0f, 1.0f,
            0.2517f, 0.5000f, 0.0f, 0.0f,
            0.5000f, 0.4000f, 1.0f, 1.0f,
            0.5000f, 0.5000f, 1.0f, 0.0f
        };

        VERTEX_DATA = vertexData;
    }

    public EventRecord(Context context, String resPath, float[] matrix){
//        float[] matrix = new float[16];
//        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
        projectionMatrix = matrix;

        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLImageView(context, resPath, 30, 30, vertexArray, projectionMatrix);
//        mTextView = new GLTextView(minute, typeface, 12, 64, 64, vertexArray, matrix);
    }

    public EventRecord(Context context, int resId,
                float upperBound, float lowerBound, float rightBound,
                float[] matrix){
//        float[] matrix = new float[16];
//        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
        projectionMatrix = matrix;

        float width  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                WIDTH,
                context.getResources().getDisplayMetrics());
        float height  = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                HEIGHT,
                context.getResources().getDisplayMetrics());
        width = WIDTH;
        height = HEIGHT;
        mRightBound = rightBound;
        mUpperBound = upperBound;
//        Log.d(TAG, "init: " + getBottomVerticeYPoint() + " vs " + lowerBound + " vs " + rightBound);
        isClipping = false;

        if(!isClipping){
            for(int i = 0; i<3; i++){
                VERTEX_DATA[i * DIMENSION + X] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        VERTEX_DATA[i * DIMENSION + X],
                        context.getResources().getDisplayMetrics()) + mRightBound;
                VERTEX_DATA[i * DIMENSION + Y] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        VERTEX_DATA[i * DIMENSION + Y],
                        context.getResources().getDisplayMetrics()) + mUpperBound;
            }
        }

        if(getBottomVerticeYPoint() > lowerBound){
            float d = (lowerBound - getTopVerticeYPoint())/(getBottomVerticeYPoint() - getTopVerticeYPoint());
            VERTEX_DATA[0 * DIMENSION + Y] = lowerBound;
//            Log.d(TAG, "init: " + getBottomVerticeYPoint() + " vs " + lowerBound + " d = " + d);
            VERTEX_DATA[0 * DIMENSION + V] = d;
        }

//        float w = Math.abs(VERTEX_DATA[1 * DIMENSION + X] - VERTEX_DATA[2 * DIMENSION + X]);
//        VERTEX_DATA[0 * DIMENSION + X] = VERTEX_DATA[1 * DIMENSION + X] = rightBound;
//        VERTEX_DATA[2 * DIMENSION + X] = VERTEX_DATA[0 * DIMENSION + X] - w;
        mHeight = VERTEX_DATA[0 * DIMENSION + Y] - VERTEX_DATA[1 * DIMENSION + Y];
        mWidth = VERTEX_DATA[2 * DIMENSION + X] - VERTEX_DATA[1 * DIMENSION + X];

        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLImageView(context, resId, false, (int)width, (int)height, 0, 0, vertexArray, projectionMatrix);
    }

    @Override
    public void set(float px, float py){
        pstY = py;
        pstX = px + mRightBound - mWidth;
    }

    @Override
    public void move(float px, float py){
        VERTEX_DATA[0 * DIMENSION + X] = px + pstX + mWidth;
        VERTEX_DATA[1 * DIMENSION + X] = px + pstX + mWidth;
        VERTEX_DATA[2 * DIMENSION + X] = px + pstX;

        VERTEX_DATA[0 * DIMENSION + Y] = py + pstY + (mHeight);
        VERTEX_DATA[1 * DIMENSION + Y] = py + pstY;
        VERTEX_DATA[2 * DIMENSION + Y] = py + pstY;
        vertexArray.commit();
    }

    @Override
    public void bindData() {
        mTextView.bindData();
    }

    @Override
    public void draw(){
//        mTextView.begin();
        if(isClipping){
            mTextView.draw(GLES20.GL_TRIANGLES, 3, 6);
        }
        else{
            mTextView.draw(GLES20.GL_TRIANGLES, 0, 3);
        }
        mTextView.end();
    }

    @Override
    public float getCenterVerticeYPoint(){
        return (VERTEX_DATA[0 * DIMENSION + Y] + VERTEX_DATA[1 * DIMENSION + Y])/2f;
    }

    @Override
    public float getTopVerticeYPoint(){
        return VERTEX_DATA[1 * DIMENSION + Y];
    }

    @Override
    public float getBottomVerticeYPoint(){
        return VERTEX_DATA[0 * DIMENSION + Y];
    }
}
