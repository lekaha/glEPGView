package com.john.glepgviewer;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;
import android.util.TypedValue;

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

    private Context mContext;

    ColorConverter.GLColor clearColor = ColorConverter.toGLColor("#333333");

    private GLEventView mGLEventView;
    private GLEventView mGLEventView2;

    public GLEpgViewRenderer(Context context){
        mContext = context;
    }

    @Override
    public void onCreate(int width, int height, boolean contextLost) {
        Log.d(TAG, "[onCreate] Entry");
        glClearColor(clearColor.Red, clearColor.Green, clearColor.Blue, 0.0f);
        glViewport(0 - (width/2), 0 + (height/2), width, height);

        Resources r = mContext.getResources();
        float px_W = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, //Convert to dp value
                145f, //px value
                r.getDisplayMetrics());
        float px_H = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, //Convert to dp value
                180f, //px value
                r.getDisplayMetrics());
        float aspectRatio_W = (float)width;
        float aspectRatio_H = (float)height;

        float[] projectionMatrix = new float[16];

//        orthoM(projectionMatrix, 0, -aspectRatio_W, aspectRatio_W, -aspectRatio_H, aspectRatio_H, -1f, 1f);
        orthoM(projectionMatrix, 0, -width/2f, width/2f, -height/2f, height/2f, -1f, 1f);
//        orthoM(projectionMatrix, 0, -1, 1, -1, 1, -1f, 1f);
//        float[] projectionMatrix2 = new float[16];
//        orthoM(projectionMatrix2, 0, -aspectRatio_H, aspectRatio_H, -aspectRatio_W, aspectRatio_W, -1f, 1f);
        Log.d(TAG, "init: aspectRatio_W = " + aspectRatio_W + " aspectRatio_H = " + aspectRatio_H);
        for(int i = 0; i<4; i++){
            String str = "";
            for(int j = 0; j<4; j++)
                str += (projectionMatrix[i*4+j] + " ");
            Log.d(TAG, "init: " + str);
        }

        mGLEventView = new GLEventView(mContext);
        mGLEventView.bind(
                0f, 0f,
                1,
                "00",
                "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
                "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
                R.drawable.epg_icon_recording_status_period,
                R.drawable.epg_dropdown_menu_genre_icon_0_all,
                true,
                projectionMatrix);
    }

    @Override
    public void onDrawFrame(boolean firstDraw) {
        // Clear the rendering surface.
        glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mGLEventView.draw();
//        mGLEventView2.draw();
    }
}
