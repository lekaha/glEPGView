package com.john.glepgviewer.program;

import android.opengl.GLES20;

import com.john.glepgviewer.helper.CheckErrorHelper;

import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class TextureShaderProgram extends ShaderProgram {
    private final String TAG = "TextureShaderProgram";
    // Uniform locations
//    private final int uMatrixLocation;
//    private final int uTextureUnitLocation;
//    private final int uColorLocation;

    // Attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public TextureShaderProgram(String vertexShaderSource, String fragmentShaderSource) {
        super(vertexShaderSource, fragmentShaderSource);

//        // Retrieve uniform locations for the shader program.
//        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
//        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        CheckErrorHelper.checkGlError(TAG, "glGetAttribLocation aPosition");
        if (aPositionLocation == -1) {
            throw new RuntimeException("Could not get attrib location for aPosition");
        }

        aTextureCoordinatesLocation =
                glGetAttribLocation(program, A_TEXTURE_COORDINATES);
        CheckErrorHelper.checkGlError(TAG, "glGetAttribLocation aTextureCoord");
        if (aTextureCoordinatesLocation == -1) {
            throw new RuntimeException("Could not get attrib location for aTextureCoord");
        }

//        uColorLocation = glGetAttribLocation(program, U_COLOR);
//        CheckErrorHelper.checkGlError(TAG, "glGetAttribLocation uColorLocation");
//        if (uColorLocation == -1) {
//            throw new RuntimeException("Could not get attrib location for uColorLocation");
//        }
    }

    public void setUniforms(float[] matrix, int textureId) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
//    public int getColorAttributeLocation() { return uColorLocation; }
    public int getMatrixAttributeLocation() { return uMatrixLocation;}
}
