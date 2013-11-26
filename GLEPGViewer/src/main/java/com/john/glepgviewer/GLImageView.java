package com.john.glepgviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.john.glepgviewer.helper.CheckErrorHelper;
import com.john.glepgviewer.helper.TextureHelper;
import com.john.glepgviewer.program.TextureShaderProgram;
import com.john.glepgviewer.util.ColorConverter;

import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.orthoM;

/**
 * Created by john on 11/6/13.
 */
public class GLImageView {
    private static final String TAG = "GLImageView";
    private Context mContext;

    private static final String vertexShaderCode =
//                    "uniform mat4 u_MVPMatrix[24];      \n"     // An array representing the combined
                    "uniform mat4 u_Matrix;      \n"     // An array representing the combined
                    // model/view/projection matrices for each sprite

//                    + "attribute float a_MVPMatrixIndex; \n"	// The index of the MVPMatrix of the particular sprite
                    + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
                    + "attribute vec2 a_TextureCoordinates;\n"     // Per-vertex texture coordinate information we will pass in
                    + "varying vec2 v_TextureCoordinates;  \n"   // This will be passed into the fragment shader.
                    + "void main()                    \n"     // The entry point for our vertex shader.
                    + "{                              \n"
//                    + "   int mvpMatrixIndex = int(a_MVPMatrixIndex); \n"
                    + "   v_TextureCoordinates = a_TextureCoordinates; \n"
                    + "   gl_Position = u_Matrix * a_Position;   \n"     // gl_Position is a special variable used to store the final position.
//                    + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
                    // normalized screen coordinates.
                    + "}                              \n";


    private static final String fragmentShaderCode =
                    "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                    + "uniform sampler2D u_TextureUnit;       \n"    // The input texture.

                    // precision in the fragment shader.
                    + "uniform vec4 u_Color;          \n"
                    + "varying vec2 v_TextureCoordinates;  \n" // Interpolated texture coordinate per fragment.

                    + "void main()                    \n"     // The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);\n" // texture is grayscale so take only grayscale value from
//                    + "   gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates).w * u_Color;\n" // texture is grayscale so take only grayscale value from
                    // it when computing color output (otherwise font is always black)
                    + "}\n";
    private final String CLEAR_COLOR = "#ff268626";
    private ColorConverter.GLColor clearColor = ColorConverter.toGLColor(CLEAR_COLOR);

    private int mTexture;
    private TextureShaderProgram mTextureShaderProgram;
    private final float[] projectionMatrix = new float[16];
//    private final float[] modelMatrix = new float[16];

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;
    private VertexArray vertexArray;

    public GLImageView(Context context, String resPath, int width, int height, VertexArray vertices, float[] matrix){
//        init(text, typeface, size, width, height, 0, 0);
        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);
        vertexArray = vertices;
    }

    public GLImageView(Context context, int resId, int width, int height, int paddingLeft, int paddingRight, VertexArray vertices, float[] matrix){
        init(context, resId);
        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);
        vertexArray = vertices;
    }

    private void init(Bitmap bitmap){
        mTextureShaderProgram = new TextureShaderProgram(vertexShaderCode, fragmentShaderCode);
//        mTexture = TextureHelper.loadTexture(bitmap);
        mTexture = TextureHelper.loadTexture(bitmap);
    }

    private void init(Context context, int resId){
        mTextureShaderProgram = new TextureShaderProgram(vertexShaderCode, fragmentShaderCode);
//        mTexture = TextureHelper.loadTexture(bitmap);
        mTexture = TextureHelper.loadTexture(context, resId);
    }

    public void begin(){
        // set color TODO: only alpha component works, text is always black #BUG
//        float[] color = {clearColor.Red, clearColor.Green, clearColor.Blue, 0};
//        GLES20.glUniform4fv(mTextureShaderProgram.getColorAttributeLocation(), 1, color , 0);
//        GLES20.glEnableVertexAttribArray(mTextureShaderProgram.getColorAttributeLocation());

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);  // Set the active texture unit to texture unit 0

        glBindTexture(GLES20.GL_TEXTURE_2D, mTexture); // Bind the texture to this unit

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0
        GLES20.glUniform1i(mTextureShaderProgram.getTextureCoordinatesAttributeLocation(), 0);
        GLES20.glEnableVertexAttribArray(mTextureShaderProgram.getTextureCoordinatesAttributeLocation());

        // create a model matrix based on x, y and angleDeg
        float[] modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 1, 1, 1);
        Matrix.rotateM(modelMatrix, 0, 0, 0, 0, 1);
        Matrix.rotateM(modelMatrix, 0, 0, 1, 0, 0);
        Matrix.rotateM(modelMatrix, 0, 0, 0, 1, 0);
        orthoM(modelMatrix, 0, -1f, 1f, -0.3f, 0.3f, -1f, 1f);

        // bind MVP matrices array to shader
        glUniformMatrix4fv(mTextureShaderProgram.getMatrixAttributeLocation(), 1, false, modelMatrix, 0);
        GLES20.glEnableVertexAttribArray(mTextureShaderProgram.getMatrixAttributeLocation());

    }

    public void end(){
        // Unbind from the texture.
        glBindTexture(GLES20.GL_TEXTURE_2D, 0);
//        glDeleteTextures(GLES20.GL_TEXTURE_2D, mTexture);
    }

    public void draw(int type, int first, int count){
        glDrawArrays(type, first, count);
    }

    public void bindData() {
        mTextureShaderProgram.useProgram();
        CheckErrorHelper.checkGlError(TAG, "glUseProgram");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexture);

        vertexArray.setVertexAttribPointer(
                0,
                mTextureShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                mTextureShaderProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);

//        mTextureShaderProgram.setUniforms(projectionMatrix, mTexture);
        // create a model matrix based on x, y and angleDeg
//        float[] modelMatrix = new float[16];
//        Matrix.setIdentityM(modelMatrix, 0);
//        Matrix.translateM(modelMatrix, 0, 1, 1, 1);
//        Matrix.rotateM(modelMatrix, 0, 0, 0, 0, 1);
//        Matrix.rotateM(modelMatrix, 0, 0, 1, 0, 0);
//        Matrix.rotateM(projectionMatrix, 0, 45, 0, 1, 0);

//        Matrix.setLookAtM(projectionMatrix, 0, 0f, 0.5f, 0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        glUniformMatrix4fv(mTextureShaderProgram.getMatrixAttributeLocation(), 1, false, projectionMatrix, 0);
    }

}
