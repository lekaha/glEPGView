package com.john.glepgviewer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by john on 11/6/13.
 */
public class EventRecord extends EventComponent{

//    protected float[] VERTEX_DATA = {
//            // Triangle Fan: Time minute text block
////            -0.25f, 0.38f, 0.5f, 0.5f,
////            -0.30f, 0.33f, 0.0f, 1.0f,
////            -0.20f, 0.33f, 1.0f, 1.0f,
////            -0.20f, 0.43f, 1.0f, 0.0f,
////            -0.30f, 0.43f, 0.0f, 0.0f,
////            -0.30f, 0.33f, 0.0f, 1.0f
//
//            // Triangle Fan
//            0.50f, 0.30f, 1.0f, 1.0f,
//            0.50f, 0.50f, 1.0f, 0.0f,
//            0.2517f, 0.50f, 0.0f, 0.0f
//    };
    protected final static int V = 3;
    protected final static int POSITION_COMPONENT_COUNT = 2;
    protected final static int TEXTURE_COMPONENT_COUNT = 2;

    protected GLImageView mTextView;
    protected VertexArray vertexArray;
    protected boolean isClipping = false;

    @Override
    public void init(){
        DIMENSION = (POSITION_COMPONENT_COUNT + TEXTURE_COMPONENT_COUNT);
        STRIDE = DIMENSION * Constants.BYTES_PER_FLOAT;

        float[] vertexData = {
            // Triangle
            0.50f, 0.30f, 1.0f, 1.0f,
            0.50f, 0.50f, 1.0f, 0.0f,
            0.2517f, 0.50f, 0.0f, 0.0f,

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

    public EventRecord(Context context, String resPath, float[] projectionMatrix){
        float[] matrix = new float[16];
        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLImageView(context, resPath, 30, 30, vertexArray, matrix);
//        mTextView = new GLTextView(minute, typeface, 12, 64, 64, vertexArray, matrix);
    }

    public EventRecord(Context context, int resId,
                float upperBound, float lowerBound, float rightBound,
                float[] projectionMatrix){
        float[] matrix = new float[16];
        System.arraycopy(projectionMatrix, 0, matrix, 0, matrix.length);
        Log.d(TAG, "init: " + getBottomVerticeYPoint() + " vs " + lowerBound + " vs " + rightBound);
        isClipping = false;
        if(getBottomVerticeYPoint() < lowerBound){
            float d = (getTopVerticeYPoint() - lowerBound)/(getTopVerticeYPoint() - getBottomVerticeYPoint());
            VERTEX_DATA[0 * DIMENSION + Y] = lowerBound;
//            Log.d(TAG, "init: " + getBottomVerticeYPoint() + " vs " + lowerBound + " d = " + d);
            VERTEX_DATA[0 * DIMENSION + V] = d;
        }

        float w = Math.abs(VERTEX_DATA[1 * DIMENSION + X] - VERTEX_DATA[2 * DIMENSION + X]);
        VERTEX_DATA[0 * DIMENSION + X] = VERTEX_DATA[1 * DIMENSION + X] = rightBound;
        VERTEX_DATA[2 * DIMENSION + X] = VERTEX_DATA[0 * DIMENSION + X] - w;


        vertexArray = new VertexArray(VERTEX_DATA);
        mTextView = new GLImageView(context, resId, 30, 30, 0, 0, vertexArray, matrix);
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
