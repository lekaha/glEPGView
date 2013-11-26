package com.john.glepgviewer.program;

/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material,
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose.
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/

import android.opengl.GLES20;

import com.john.glepgviewer.helper.ShaderHelper;

import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;

abstract class ShaderProgram {
    // Uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_COLOR = "u_Color";

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    // Uniform locations
    protected final int uMatrixLocation;
    protected final int uTextureUnitLocation;

    // Shader program
    protected final int program;
//    protected ShaderProgram(Context context, int vertexShaderResourceId,
//                            int fragmentShaderResourceId) {
//        // Compile the shaders and link the program.
//        program = ShaderHelper.buildProgram(
//                TextResourceReader.readTextFileFromResource(
//                        context, vertexShaderResourceId),
//                TextResourceReader.readTextFileFromResource(
//                        context, fragmentShaderResourceId));
//    }

    protected ShaderProgram(String vertexShaderSource, String fragmentShaderSource){
        program = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);
    }

    public void useProgram() {
        // Set the current OpenGL shader program to this program.
        glUseProgram(program);
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


}
