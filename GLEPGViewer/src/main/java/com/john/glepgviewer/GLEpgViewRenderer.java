package com.john.glepgviewer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.john.glepgviewer.util.ColorConverter;

import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

/**
 * Created by john on 10/25/13.
 */
public class GLEpgViewRenderer extends GLRenderer {
    private static final String TAG = "GLEpgViewRenderer";
    private final static int SIZE = 20;

    private Context mContext;
    private GLEventView mGLEventView;
    private int mWidth = 0;
    private int mHeight = 0;
    private ColorConverter.GLColor clearColor = ColorConverter.toGLColor("#333333");
    private GLEventView[] mGLEventViewArray = new GLEventView[SIZE];

    public GLEpgViewRenderer(Context context){
        mContext = context;
    }

    @Override
    public void onCreate(int width, int height, boolean contextLost) {
        Log.d(TAG, "[onCreate] Entry");
        glClearColor(clearColor.Red, clearColor.Green, clearColor.Blue, 0.0f);
        mWidth = width;
        mHeight = height;
        glViewport(0 , 0 , width, height);

        float[] projectionMatrix = new float[16];
        orthoM(projectionMatrix, 0, 0, width, height, 0, -1f, 1f);
        for(int i = 0; i<4; i++){
            String str = "";
            for(int j = 0; j<4; j++)
                str += (projectionMatrix[i*4+j] + " ");
            Log.d(TAG, "init: " + str);
        }

        mGLEventView = new GLEventView(mContext);
        mGLEventView.bind(
                50f, 150f,
                1,
                "00",
                "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
                "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
                R.drawable.epg_icon_recording_status_period,
                R.drawable.epg_dropdown_menu_genre_icon_0_all,
                true,
                projectionMatrix);

        for(int i = 0; i<SIZE; i++){
            if((i % 2) == 1){
                mGLEventViewArray[i] = new GLEventView(mContext);
                mGLEventViewArray[i].bind(
                        0f, 0f + ((i + 1) * 20),
                        1,
                        "00",
                        "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
                        "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
                        R.drawable.epg_icon_recording_status_period,
                        R.drawable.epg_dropdown_menu_genre_icon_0_all,
                        true,
                        projectionMatrix);
            }
            else{
                mGLEventViewArray[i] = new GLEventView(mContext);
                mGLEventViewArray[i].bind(
                        0f + ((i + 1) * 30), 0f,
                        1,
                        "00",
                        "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
                        "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
                        R.drawable.epg_icon_recording_status_period,
                        R.drawable.epg_dropdown_menu_genre_icon_0_all,
                        true,
                        projectionMatrix);
            }
        }
    }

    @Override
    public void onDrawFrame(boolean firstDraw) {
        // Clear the rendering surface.
        glClear(GLES20.GL_COLOR_BUFFER_BIT);
        glViewport((int)mdX , (int)mdY , mWidth, mHeight);

        mGLEventView.draw();
        for(int i = 0; i<SIZE; i++){
            mGLEventViewArray[i].draw();
        }
    }

    private float mdX = 0f;
    private float mdY = 0f;
    private float mPreviousX = 0f;
    private float mPreviousY = 0f;
    public void setMove(float dx, float dy){
        mdX = mPreviousX + dx;
         mdY = mPreviousY - dy;
        Log.d(TAG, "[setMove] Dx = " + mdX + " Dy = " + mdY);
        glViewport((int)mdX , (int)mdY , mWidth, mHeight);
    }


}
