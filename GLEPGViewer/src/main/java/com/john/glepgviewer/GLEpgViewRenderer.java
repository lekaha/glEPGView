package com.john.glepgviewer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.john.glepgviewer.util.ColorConverter;

import java.util.Random;

import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glCullFace;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.orthoM;

/**
 * Created by john on 10/25/13.
 */
public class GLEpgViewRenderer extends GLRenderer {
    private static final String TAG = "GLEpgViewRenderer";
    private static final boolean isSingleMode = false;

    //Ex: ISDB-S total program = 51,
    // suppose:
    // 1. Average a event per half-hour.
    // 2. suppose two seconds of programs have the same event.
    //
    // => total events = (51 * 0.5 * 24 * 2) * 0.5 + (51 * 0.5 * 24 * 2) =
//    private final static int SIZE = 1836;
    private final static int SIZE = 100;

    private Context mContext;
    private GLEventView mGLEventView;
    private GLEventView mGLEventView2;
    private GLEventView mGLEventView3;
    private int mWidth = 0;
    private int mHeight = 0;
    private ColorConverter.GLColor clearColor = ColorConverter.toGLColor("#333333");
    private GLEventView[] mGLEventViewArray = new GLEventView[SIZE];
    private float[] projectionMatrix = new float[16];


    public GLEpgViewRenderer(Context context){
        mContext = context;
    }

    private int viewWidth;
    private int viewHeight;

    @Override
    public void onCreate(int width, int height, boolean contextLost) {
        Log.d(TAG, "[onCreate] Entry");
        glClearColor(clearColor.Red, clearColor.Green, clearColor.Blue, 0.0f);
        viewWidth = width;
        viewHeight = height;
        mWidth = viewWidth * 2;
        mHeight = viewHeight * 2;

        glViewport(0 , -viewHeight, mWidth, mHeight);
        glEnable(GLES20.GL_CULL_FACE);
        glCullFace(GLES20.GL_BACK);

        orthoM(projectionMatrix, 0, 0, mWidth , mHeight, 0, -1f, 1f);
        for(int i = 0; i<4; i++){
            String str = "";
            for(int j = 0; j<4; j++)
                str += (projectionMatrix[i*4+j] + " ");
            Log.d(TAG, "init: " + str);
        }
        if(isSingleMode){
            new Thread(new Runnable(){
                @Override
                public synchronized void run() {
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

                    mGLEventView2 = new GLEventView(mContext);
                    mGLEventView2.bind(
                            viewWidth/2, viewHeight/2,
                            1,
                            "01",
                            "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
                            "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
                            R.drawable.epg_icon_recording_status_period,
                            R.drawable.epg_dropdown_menu_genre_icon_0_all,
                            true,
                            projectionMatrix);
                    mGLEventView3 = new GLEventView(mContext);
                    mGLEventView3.bind(
                            viewWidth, viewHeight,
                            1,
                            "02",
                            "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
                            "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
                            R.drawable.epg_icon_recording_status_period,
                            R.drawable.epg_dropdown_menu_genre_icon_0_all,
                            true,
                            projectionMatrix);
                        }
            }, "preparing glview"){}.start();



        }
        else{
        new Thread(new Runnable(){
            @Override
            public synchronized void run() {
                float x = 0f;
                float y = 0f;
                Random r = new Random(System.currentTimeMillis());
                for(int i = 0, j = 0; i<SIZE; i++, j++){
                    //chessboard layout
//                    if((i != 0) && ((i % 20) == 0)){
//                        y += 50f;
//                        j = -1;
//                    }
//                    else{
//                        int interval = 60;
//                        mGLEventViewArray[i] = new GLEventView(mContext);
//                        mGLEventViewArray[i].bind(
//                                0f + ((j) * interval), y,
//                                1,
//                                "00",
//                                "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
//                                "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
//                                R.drawable.epg_icon_recording_status_period,
//                                R.drawable.epg_dropdown_menu_genre_icon_0_all,
//                                true,
//                                projectionMatrix);
//                    }

                    //Simulated EPG layout
                    float h = (float)(Math.abs(r.nextInt()) % 280) + 20f;
                    mGLEventViewArray[i] = new GLEventView(mContext);
                    mGLEventViewArray[i].bind(
                            x, y,
                            1,
                            h,
                            "00",
                            "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
                            "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
                            R.drawable.epg_icon_recording_status_period,
                            R.drawable.epg_dropdown_menu_genre_icon_0_all,
                            true,
                            projectionMatrix);
                    y += h;
                    if( y > mHeight){
                        y = 0f;
                        x += 145f;
                    }
                }// end of for
            }
        }, "preparing glview"){}.start();
        }
    }
    boolean draw = false;
    @Override
    public void onDrawFrame(boolean firstDraw) {
        // Clear the rendering surface.
        glClear(GLES20.GL_COLOR_BUFFER_BIT);
        glViewport((int)mdX , -viewHeight + (int)mdY , mWidth, mHeight);

        if(isSingleMode){
            if(null != mGLEventView)
                mGLEventView.draw();
            if(null != mGLEventView2)
                mGLEventView2.draw();
            if(null != mGLEventView3)
                mGLEventView3.draw();
        }
        else{
            if(!draw){
                for(int i = 0; i<SIZE; i++){
                    if(null != mGLEventViewArray[i])
                        mGLEventViewArray[i].draw();
                }
            }
        }
    }

    private float mdX = 0f;
    private float mdY = 0f;
    private float mPreviousX = 0f;
    private float mPreviousY = 0f;
    public void setMove(float dx, float dy){
        if((-mWidth <= (dx - mPreviousX) || (0 >= (dx - mPreviousX))))
            mdX = dx - mPreviousX;
        if((0 <= (mPreviousY - dy)) || (mHeight >= (mPreviousY - dy)))
            mdY = mPreviousY - dy;
        Log.d(TAG, "[setMove] Dx = " + dx + " Dy = " + dy + " PreviousX = " + mPreviousX + " PreviousY = " + mPreviousY + " mDx = " + mdX + " mDy = " + mdY);
//        glViewport((int)mdX , (int)mdY , mWidth, mHeight);
    }

    public void setPosition(float x, float y){
        mPreviousX = mdX + x;
        mPreviousY = mdY - y;
        Log.d(TAG, "[setPosition] setMove x = " + mdX + " y = " + mdY);
    }


}
