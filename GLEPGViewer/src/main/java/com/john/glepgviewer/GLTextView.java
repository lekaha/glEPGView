package com.john.glepgviewer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.os.Build;

import com.john.glepgviewer.helper.CheckErrorHelper;
import com.john.glepgviewer.helper.TextureHelper;
import com.john.glepgviewer.program.TextureShaderProgram;
import com.john.glepgviewer.util.ColorConverter;

import java.util.Vector;

import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.orthoM;

/**
 * Created by john on 10/28/13.
 */
public class GLTextView {
    private static final String TAG = "GLTextView";
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
//                    + "uniform vec4 u_Color;          \n"
                    + "varying vec2 v_TextureCoordinates;  \n" // Interpolated texture coordinate per fragment.

                    + "void main()                    \n"     // The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);\n" // texture is grayscale so take only grayscale value from
//                    + "   gl_FragColor *= 0.1f;\n"
                    // it when computing color output (otherwise font is always black)
                    + "}\n";
    private final String CLEAR_COLOR = "#ffffffff";
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

    float fontHeight;                                  // Font Height (Actual; Pixels)
    float fontAscent;                                  // Font Ascent (Above Baseline; Pixels)
    float fontDescent;                                 // Font Descent (Below Baseline; Pixels)
    float charWidthMax;
    float fontScaleX;
    float fontScaleY;
    int fontColor = Color.parseColor("#FFFFFFFF");
    int backgroundColor = Color.parseColor("#FF1A1A1A");

    public GLTextView(String text, Typeface typeface, int size, int width, int height, VertexArray vertices){
        init(text, typeface, size, width, height, 0, 0);

        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;
        if (width > height) {
            // Landscape
            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
        } else {
            // Portrait or square
            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
        }
        vertexArray = vertices;
        fontScaleX = aspectRatio;
        fontScaleY = 1;
        charWidthMax = 0;
    }

    public GLTextView(String text, Typeface typeface, int size, int width, int height, VertexArray vertices, float[] matrix){
        vertexArray = vertices;
        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);
        init(text, typeface, size, width, height, 0, 0);
    }

    public GLTextView(String text, Typeface typeface, int size, int width, int height, int paddingLeft, int paddingTop, VertexArray vertices, float[] matrix){
        vertexArray = vertices;
        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);
        init(text, typeface, size, width, height, paddingLeft, paddingTop);
    }

    public GLTextView(String text, Typeface typeface, int size, int width, int height, int paddingLeft, int paddingTop, int frontColor, int backColor, VertexArray vertices, float[] matrix){
        vertexArray = vertices;
        fontColor = frontColor;
        backgroundColor = backColor;
        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);
        init(text, typeface, size, width, height, paddingLeft, paddingTop);
    }

    public GLTextView(Context context, int resId, VertexArray vertices, float[] matrix){
        mContext = context;
        vertexArray = vertices;
        System.arraycopy(matrix, 0, projectionMatrix, 0, projectionMatrix.length);
        init(context, resId);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init(String text, Typeface typeface, int originSize, int originWidth, int originHeight, int paddingLeft, int paddingTop){
        fontScaleX = (projectionMatrix[0] != 0f)? 1.0f/projectionMatrix[0]: 1f;
        fontScaleY = (projectionMatrix[5] != 0f)? 1.0f/projectionMatrix[5]: 1f;
        charWidthMax = 0;

//        for(int i = 0; i<4; i++){
//            String str = "";
//            for(int j = 0; j<4; j++)
//                str += (projectionMatrix[i*4+j] + " ");
//            Log.d(TAG, "init: " + str);
//        }

        int width  = (int)((float)originWidth * fontScaleX);
        int height  = (int)((float)originHeight * (fontScaleY - 0.12f ));

        float size = originSize;
//        Log.d(TAG, "init: project=" + projectionMatrix[0] + "*" + projectionMatrix[5]);
//        Log.d(TAG, "init: scale=" + fontScaleX + "*" + fontScaleY);
//        Log.d(TAG, "init: view =" + width + "*" + height);
//        Log.d(TAG, "init: originSize=" + originSize + " size=" + size);

        Bitmap bitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888);  // Create Bitmap
//        bitmap.setHasAlpha(true);
//        bitmap.setHasMipMap(true);
        bitmap.eraseColor(backgroundColor);                // Set Transparent Background (ARGB)
        Canvas canvas = new Canvas( bitmap );           // Create Canvas for Rendering to Bitmap
        Paint paint = new Paint();                      // Create Android Paint Instance
        paint.setFilterBitmap(true);
        paint.setAntiAlias( true );                     // Enable Anti Alias
        paint.setTextSize( size );                      // Set Text Size
        paint.setColor( fontColor);                     // Set ARGB (White, Opaque)
        paint.setTextScaleX(fontScaleX);
        paint.setTypeface( typeface );


        // get font metrics
        Paint.FontMetrics fm = paint.getFontMetrics();  // Get Font Metrics
        fontHeight = (float)Math.ceil(fm.descent - fm.top + 1f);  // Calculate Font Height
        fontAscent = (float)Math.ceil( Math.abs( fm.ascent ) );  // Save Font Ascent
        fontDescent = (float)Math.ceil( Math.abs( fm.descent ) );  // Save Fo0nt Descent

        Matrix m = new Matrix();
        m.postTranslate(0f, -(0.2143f * size));//m.postTranslate(0f, (size-25.5f));
        canvas.setMatrix(m);

        paddingTop++;
        String[] lines = StringFormat(text, (width), (int)(size * fontScaleX));
        for(int i = 1; i<=lines.length; i++){
//            Log.d(TAG, "init: " + lines[i-1] + " fontHeight=" + fontHeight);
            if(1 == i)
                canvas.drawText( lines[i-1], paddingLeft, paddingTop + ((fontHeight) * (i)), paint );        // Draw Character
            else
                canvas.drawText( lines[i-1], paddingLeft, paddingTop + ((fontHeight) * (i)) + 1, paint );        // Draw Character
        }

        bitmap.prepareToDraw();
        init(bitmap);

        // Recycle the bitmap, since its data has been loaded into
        // OpenGL.
        bitmap.recycle();
    }

    private void init(Bitmap bitmap){
        mTextureShaderProgram = new TextureShaderProgram(vertexShaderCode, fragmentShaderCode);
//        mTexture = TextureHelper.loadTexture(bitmap);
        mTexture = TextureHelper.loadTexture(bitmap);
        glEnable(GLES20.GL_BLEND);
        glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void init(Context context, int resId){
        mTextureShaderProgram = new TextureShaderProgram(vertexShaderCode, fragmentShaderCode);
//        mTexture = TextureHelper.loadTexture(bitmap);
        mTexture = TextureHelper.loadTexture(context, resId);
    }

    public void begin(){
        // set color TODO: only alpha component works, text is always black #BUG
        float[] color = {clearColor.Red, clearColor.Green, clearColor.Blue, 0};
//        GLES20.glUniform4fv(mTextureShaderProgram.getColorAttributeLocation(), 1, color , 0);
//        GLES20.glEnableVertexAttribArray(mTextureShaderProgram.getColorAttributeLocation());

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);  // Set the active texture unit to texture unit 0

        glBindTexture(GLES20.GL_TEXTURE_2D, mTexture); // Bind the texture to this unit

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0
        GLES20.glUniform1i(mTextureShaderProgram.getTextureCoordinatesAttributeLocation(), 0);
        GLES20.glEnableVertexAttribArray(mTextureShaderProgram.getTextureCoordinatesAttributeLocation());

        // create a model matrix based on x, y and angleDeg
//        float[] modelMatrix = new float[16];
//        Matrix.setIdentityM(modelMatrix, 0);
//        Matrix.translateM(modelMatrix, 0, 1, 1, 1);
//        Matrix.rotateM(modelMatrix, 0, 0, 0, 0, 1);
//        Matrix.rotateM(modelMatrix, 0, 0, 1, 0, 0);
//        Matrix.rotateM(modelMatrix, 0, 0, 0, 1, 0);
//        orthoM(modelMatrix, 0, -1f, 1f, -0.3f, 0.3f, -1f, 1f);
//
//        // bind MVP matrices array to shader
//        glUniformMatrix4fv(mTextureShaderProgram.getMatrixAttributeLocation(), 1, false, modelMatrix, 0);
//        GLES20.glEnableVertexAttribArray(mTextureShaderProgram.getMatrixAttributeLocation());

    }

    public void end(){
        // Unbind from the texture.
        glBindTexture(GLES20.GL_TEXTURE_2D, 0);
//        glDeleteTextures(GLES20.GL_TEXTURE_2D, mTexture);
        glDisable(GLES20.GL_BLEND);
    }

    public void draw(){
        glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
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

    public static String[] StringFormat(String text, int maxWidth, int fontSize) {

        String[] result = null;
        Vector<String> tempR = new Vector<String>();
        int lines = 0;
        int len = text.length();
        int index0 = 0;
        int index1 = 0;
        boolean wrap;
        while (true) {
            int widthes = 0;
            wrap = false;
            for (index0 = index1; index1 < len; index1++) {
                if (text.charAt(index1) == '\n') {
                    index1++;
                    wrap = true;
                    break;
                }
                widthes = fontSize + widthes;
                if (widthes > maxWidth) {
                    break;
                }
            }
            if(text.charAt(index0) == '　'){
                index0++;
//                Log.d(TAG, "[StringFormat] index from " + index0 + " to " + index1 + " has wrap = " + wrap);
            }

            // If this condition is true, so that mean to a character in the last line.
            if((index0 + 1) == index1){
//                Log.d(TAG, "[StringFormat] the last line (" + index0 + "," + (index1 - 1) + ")" + text.charAt(index0));
                String last = tempR.lastElement();
                last += String.valueOf(text.charAt(index0));
                tempR.set(tempR.size() - 1, last);
//                Log.d(TAG, "[StringFormat] the last line = " + last);
                break;
            }

            lines++;
            if (wrap) {
                tempR.addElement(text.substring(index0, index1 - 1));
//                Log.d(TAG, "[StringFormat] (" + index0 + "," + (index1 - 1) + ")" + text.substring(index0, index1 - 1));
            } else {
                tempR.addElement(text.substring(index0, index1));
//                Log.d(TAG, "[StringFormat] (" + index0 + "," + index1 + ")" + text.substring(index0, index1));

            }
            if (index1 >= len) {
                break;
            }
        }
        result = new String[lines];
        tempR.copyInto(result);
        return result;
    }
}

