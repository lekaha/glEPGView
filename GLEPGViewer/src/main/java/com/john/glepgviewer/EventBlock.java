package com.john.glepgviewer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.TypedValue;

import com.john.glepgviewer.program.ColorShaderProgram;
import com.john.glepgviewer.util.ColorConverter;

import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by john on 10/28/13.
 */
public class EventBlock extends EventComponent{

    private final String vertexShaderSource =
            "uniform mat4 u_Matrix;\n" +
                    "attribute vec4 a_Position;\n" +
                    "attribute vec4 a_Color;\n" +
                    "varying vec4 v_Color;\n" +
                    "void main() {\n" +
                    "  v_Color = a_Color;\n" +
                    "  gl_Position = u_Matrix * a_Position;\n" +
                    "  gl_PointSize = 10.0;\n" +
                    "}";

    private final String fragmentShaderSource =
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "void main() {" +
                    "  gl_FragColor = v_Color;" +
                    "}";
    protected final static String MIN_COLOR = "#69a5d7";
    protected final static String BG_COLOR = "#262626";
    protected final static String LEFT_BORDER_COLOR = "#4b4b4b";
    protected final static String TOP_BORDER_COLOR = "#4b4b4b";
    protected final static String RIGHT_BORDER_COLOR = "#000000";

    protected ColorConverter.GLColor miniBackgroundColro = ColorConverter.toGLColor(MIN_COLOR);
    protected ColorConverter.GLColor backgroundColor = ColorConverter.toGLColor(BG_COLOR); //#262626
    protected ColorConverter.GLColor leftBorderColor = ColorConverter.toGLColor(LEFT_BORDER_COLOR); //#505050
    protected ColorConverter.GLColor topBorderColor = ColorConverter.toGLColor(TOP_BORDER_COLOR);  //6e6e6e
    protected ColorConverter.GLColor rightAndBottomBorderColor = ColorConverter.toGLColor(RIGHT_BORDER_COLOR);

    protected static final int X = 0, Y = 1, R = 2, G = 3, B = 4;
    protected static final int POSITION_COMPONENT_COUNT = 2;
    protected static final int COLOR_COMPONENT_COUNT = 3;
    //    protected static final int DIMENSION = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT);
    private final float[] projectionMatrix = new float[16];


    protected ColorShaderProgram colorProgram;
    //    protected VertexArray vertexArray;

    private int mMerge;
    private float mHeight;
    private Context mContext;

    @Override
    public void init(){
        DIMENSION = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT);
        STRIDE = DIMENSION * Constants.BYTES_PER_FLOAT;
        miniBackgroundColro = ColorConverter.toGLColor(MIN_COLOR);
        backgroundColor = ColorConverter.toGLColor(BG_COLOR);
        leftBorderColor = ColorConverter.toGLColor(LEFT_BORDER_COLOR);
        topBorderColor = ColorConverter.toGLColor(TOP_BORDER_COLOR);
        rightAndBottomBorderColor = ColorConverter.toGLColor(RIGHT_BORDER_COLOR);

        float[] vertexData = {
                // Order of coordinates: X, Y, R, G, B
                // Triangle Fan: Entire block
                0.5f, -1.25f, backgroundColor.Red, backgroundColor.Green, backgroundColor.Blue,
                0.0f, -1.5f, backgroundColor.Red, backgroundColor.Green, backgroundColor.Blue,
                1.0f, -1.5f, backgroundColor.Red, backgroundColor.Green, backgroundColor.Blue,
                1.0f, 0.0f, backgroundColor.Red, backgroundColor.Green, backgroundColor.Blue,
                0.0f, 0.0f, backgroundColor.Red, backgroundColor.Green, backgroundColor.Blue,
                0.0f, -1.5f, backgroundColor.Red, backgroundColor.Green, backgroundColor.Blue,

                // Border left
                -0.5f, 0.5f, leftBorderColor.Red, leftBorderColor.Green, leftBorderColor.Blue,
                -0.5f, -1.0f, leftBorderColor.Red, leftBorderColor.Green, leftBorderColor.Blue,

                // Border Top
                -0.5f, 0.5f, topBorderColor.Red, topBorderColor.Green, topBorderColor.Blue,
                0.5f, 0.5f, topBorderColor.Red, topBorderColor.Green, topBorderColor.Blue,

                // Border Right
                0.5f, 0.5f, rightAndBottomBorderColor.Red, rightAndBottomBorderColor.Green, rightAndBottomBorderColor.Blue,
                0.5f, -1.0f, rightAndBottomBorderColor.Red, rightAndBottomBorderColor.Green, rightAndBottomBorderColor.Blue,

                // Border Bottom
                0.5f, -1.0f, rightAndBottomBorderColor.Red, rightAndBottomBorderColor.Green, rightAndBottomBorderColor.Blue,
                -0.5f, -1.0f, rightAndBottomBorderColor.Red, rightAndBottomBorderColor.Green, rightAndBottomBorderColor.Blue,
        };

        VERTEX_DATA = vertexData;


    }

//    public EventBlock(){
//        super();
//    }

    private float mWidth;
    public EventBlock(Context context, int merge, float height, float[] matrix){
//        super();
        mContext = context;
        mMerge = merge - 1;
        mHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                height,
                mContext.getResources().getDisplayMetrics());
        mWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                145f,
                mContext.getResources().getDisplayMetrics());

//        Log.d(TAG, "ctor: " + mMerge + " " + mHeight);
        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);

//        float h = mHeight/TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                180f,
//                mContext.getResources().getDisplayMetrics());
        float h = mHeight * projectionMatrix[Y * 4 + Y];

        VERTEX_DATA[0 * DIMENSION + Y] = (-h/2f + 0.5f);
        VERTEX_DATA[1 * DIMENSION + Y] = -h + 0.5f;
        VERTEX_DATA[2 * DIMENSION + Y] = -h + 0.5f;
        VERTEX_DATA[5 * DIMENSION + Y] = -h + 0.5f;
        VERTEX_DATA[7 * DIMENSION + Y] = -h + 0.5f;
        VERTEX_DATA[11 * DIMENSION + Y] = -h + 0.5f;
        VERTEX_DATA[12 * DIMENSION + Y] = -h + 0.5f;
        VERTEX_DATA[13 * DIMENSION + Y] = -h + 0.5f;

//        VERTEX_DATA[0 * DIMENSION + Y] = -h/2f;
//        VERTEX_DATA[1 * DIMENSION + Y] = -h;
//        VERTEX_DATA[2 * DIMENSION + Y] = -h;
//        VERTEX_DATA[5 * DIMENSION + Y] = -h;
//        VERTEX_DATA[7 * DIMENSION + Y] = -h;
//        VERTEX_DATA[11 * DIMENSION + Y] = -h;
//        VERTEX_DATA[12 * DIMENSION + Y] = -h;
//        VERTEX_DATA[13 * DIMENSION + Y] = -h;

//        float w = VERTEX_DATA[2 * DIMENSION + X] - VERTEX_DATA[1 * DIMENSION + X];
        float w = mWidth * projectionMatrix[X * 4 + X];

//        VERTEX_DATA[2 * DIMENSION + X] += (mMerge * w);
//        VERTEX_DATA[3 * DIMENSION + X] += (mMerge * w);
//        VERTEX_DATA[9 * DIMENSION + X] += (mMerge * w);
//        VERTEX_DATA[10 * DIMENSION + X] += (mMerge * w);
//        VERTEX_DATA[11 * DIMENSION + X] += (mMerge * w);
//        VERTEX_DATA[12 * DIMENSION + X] += (mMerge * w);

        VERTEX_DATA[2 * DIMENSION + X] = (mMerge * w);
        VERTEX_DATA[3 * DIMENSION + X] = (mMerge * w);
        VERTEX_DATA[9 * DIMENSION + X] = (mMerge * w);
        VERTEX_DATA[10 * DIMENSION + X] = (mMerge * w);
        VERTEX_DATA[11 * DIMENSION + X] = (mMerge * w);
        VERTEX_DATA[12 * DIMENSION + X] = (mMerge * w);
        VERTEX_DATA[0 * DIMENSION + X] = (VERTEX_DATA[2 * DIMENSION + X] - VERTEX_DATA[1 * DIMENSION + X])/2f;

        if(mHeight <= 15f){
            for(int i = 0; i<6; i++){
                VERTEX_DATA[i * DIMENSION + R] = miniBackgroundColro.Red;
                VERTEX_DATA[i * DIMENSION + G] = miniBackgroundColro.Green;
                VERTEX_DATA[i * DIMENSION + B] = miniBackgroundColro.Blue;
            }
        }

        colorProgram = new ColorShaderProgram(vertexShaderSource, fragmentShaderSource);
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    @Override
    public void bindData() {
        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_POSITION_LOCATION.
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        // Bind our data, specified by the variable vertexData, to the vertex
        // attribute at location A_COLOR_LOCATION.
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                colorProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
    }

    @Override
    public void draw(){
        colorProgram.useProgram();
        colorProgram.setUniforms(projectionMatrix);

        //Draw block
        glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);

        //Draw border
        glDrawArrays(GLES20.GL_LINES, 6, 2);    //left
        glDrawArrays(GLES20.GL_LINES, 8, 2);   //top
        glDrawArrays(GLES20.GL_LINES, 10, 2);   //right
        glDrawArrays(GLES20.GL_LINES, 12, 2);   //bottom
    }

    @Override
    public float getCenterVerticeYPoint(){
        return VERTEX_DATA[0 * DIMENSION + 1];
    }

    @Override
    public float getTopVerticeYPoint(){
        return VERTEX_DATA[3 * DIMENSION + 1];
    }

    @Override
    public float getBottomVerticeYPoint(){
        return VERTEX_DATA[1 * DIMENSION + 1];
    }

    public float getLeftVerticeXPoint(){
        return VERTEX_DATA[1 * DIMENSION + X];
    }

    public float getRightVerticeXPoint(){
        return VERTEX_DATA[2 * DIMENSION + X];
    }
}
