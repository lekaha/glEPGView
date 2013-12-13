package com.john.glepgviewer;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import android.util.LruCache;
import android.util.TypedValue;

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
    private final static int SIZE = 500;
    private final static int CACHE_SIZE = 100;

    private Context mContext;
    private GLEventView mGLEventView;
    private GLEventView mGLEventView2;
    private GLEventView mGLEventView3;
    private int mWidth = 0;
    private int mHeight = 0;
    private ColorConverter.GLColor clearColor = ColorConverter.toGLColor("#333333");
//    private GLEventView[] mGLEventViewArray = new GLEventView[SIZE];
    private float[] projectionMatrix = new float[16];
    private EventInfo[] mEventInfoArray = new EventInfo[SIZE];
    private LruCache<EventInfo, GLEventView> mCache;

    public GLEpgViewRenderer(Context context){
        mContext = context;
    }

    private int viewWidth;
    private int viewHeight;
    private int WIDTH;
    private int HEIGHT;
    private int times = 1;
    private float viewWidthOneProgram;
    private float mDensity;

    @Override
    public void onCreate(int width, int height, boolean contextLost) {
        Log.d(TAG, "[onCreate] Entry");
        glClearColor(clearColor.Red, clearColor.Green, clearColor.Blue, 0.0f);
        viewWidth = width;
        viewHeight = height;
        mWidth = viewWidth * times;
        mHeight = viewHeight * times;
        WIDTH = viewWidth * 4;
        HEIGHT = viewHeight * 4;

        mCache = new LruCache<EventInfo, GLEventView>(CACHE_SIZE);

        viewWidthOneProgram = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                145f,
                mContext.getResources().getDisplayMetrics());
        mDensity = mContext.getResources().getDisplayMetrics().density;


        glViewport(0 , -(mHeight - viewHeight), mWidth, mHeight);
        glEnable(GLES20.GL_CULL_FACE);
        glCullFace(GLES20.GL_BACK);

        Log.d(TAG, "onCreate: Width = " + width + " Height = " + height);
        Log.d(TAG, "onCreate: View Width = " + mWidth + " View Height = " + mHeight);

        orthoM(projectionMatrix, 0, 0, mWidth, mHeight, 0, -1f, 1f);
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
                            145f * 5 + 1, 0f,
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
                            0f, 201f,
                            1,
                            "02",
                            "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
                            "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
                            R.drawable.epg_icon_recording_status_period,
                            R.drawable.epg_dropdown_menu_genre_icon_0_all,
                            true,
                            projectionMatrix);

                        }
            }, "preparing glview").start();



        }
        else{
        new Thread(new Runnable(){
            @Override
            public synchronized void run() {
                synchronized(this){
                    Log.d(TAG, "[initial] Preparing...");
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
//                        mGLEventViewArray[i] = new GLEventView(mContext);
//                        mGLEventViewArray[i].bind(
////                                (x != 0)?x + 1:x, (y != 0)?y + 1:y,
//                                x, y,
//                                1,
//                                h,
//                                String.format("%02d", i%100),
//                                "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
//                                "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
//                                R.drawable.epg_icon_recording_status_period,
//                                R.drawable.epg_dropdown_menu_genre_icon_0_all,
//                                true,
//                                projectionMatrix);
                        mEventInfoArray[i] = new EventInfo(
                                i,
                                x, y, 1, h,
                                String.format("%02d", i%100),
                                "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
                                "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
                                true,
                                R.drawable.epg_dropdown_menu_genre_icon_0_all,
                                R.drawable.epg_icon_recording_status_period
                            );
                        y += (h + 1);
                        if( y > HEIGHT){
                            y = 0f;
                            x += 146f;
                        }

//                        mGLEventViewArray[i].draw();
                        if(0 == (mCache.putCount() % 100))
                            Log.d(TAG, "[initial] Cache count = " + mCache.putCount());

                        try {
                            wait(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }// end of for
                    Log.d(TAG, "[initial] Prepared");
                }//synchronized
            }
        }, "preparing glview"){}.start();
        }
    }

    public GLEventView addViewToMemoryCache(EventInfo key, GLEventView view) {
        if (getViewFromMemCache(key) == null) {
            Log.d(TAG, "[addViewToMemoryCache] add");
            return mCache.put(key, view);
        }
        return view;
    }

    public GLEventView getViewFromMemCache(EventInfo key) {
        return mCache.get(key);
    }

    private boolean draw = false;
    private int count = 0;
    @Override
    public void onDrawFrame(boolean firstDraw) {
        // Clear the rendering surface.
        glClear(GLES20.GL_COLOR_BUFFER_BIT);
//        glViewport((int)mdX , -(mHeight - viewHeight) + (int)mdY , mWidth, mHeight);

        if(isSingleMode){
            if(null != mGLEventView) {

                if(mdX != 0 && mdY != 0){
                    mGLEventView.draw(mdX, -mdY);
                    mGLEventView2.draw(mdX, -mdY);
                    mGLEventView3.draw(mdX, -mdY);
                }
                else{
                    mGLEventView.draw();
                    mGLEventView2.draw();
                    mGLEventView3.draw();
                }
            }
        }
        else{
            if(!draw){
                count = 0;
                for(int i = 0; i<SIZE; i++){
                    if(null != mEventInfoArray[i]){
//                        if(mdX != 0 && mdY != 0)
                        {
                            if(((Math.ceil(-mdX)) <= ((mEventInfoArray[i].getX() * mDensity) + (mEventInfoArray[i].getWidth() * mDensity)))
                                    && ((Math.ceil(-mdX) + viewWidth) >= mEventInfoArray[i].getX()))
                                if(((Math.ceil(mdY)) <= (mEventInfoArray[i].getY() * mDensity) + (mEventInfoArray[i].getHeight() * mDensity))
                                        && ((Math.ceil(mdY) + viewHeight) >= mEventInfoArray[i].getY()))
                                {
//                                    mGLEventViewArray[i].draw(mdX, -mdY);
                                    GLEventView view = getViewFromMemCache(mEventInfoArray[i]);
                                    if(null != view){
//                                        Log.d(TAG, "[onDrawFrame] draw");
                                        view.draw(mdX, -mdY);
                                    }
                                    else{
                                        view = new GLEventView(mContext);
                                        view.bind(mEventInfoArray[i], projectionMatrix);
                                        view.draw(mdX, -mdY);
                                        addViewToMemoryCache(mEventInfoArray[i], view);
                                    }
                                }

                        }
                    }
                }

            }
        }
    }

    private float mdX = 0f;
    private float mdY = 0f;
    private float mPreviousX = 0f;
    private float mPreviousY = 0f;
    public void setMove(float dx, float dy){
        if((-(WIDTH - viewWidth) <= (mPreviousX + dx) && (3 >= (mPreviousX + dx)))){
            mdX = mPreviousX + dx;
        }

        if((-3 <= (mPreviousY - dy)) && ((HEIGHT - viewHeight) >= (mPreviousY - dy)))
            mdY = mPreviousY - dy;
//        Log.d(TAG, "[setMove] Dx = " + dx + " Dy = " + dy + " PreviousX = " + mPreviousX + " PreviousY = " + mPreviousY + " mDx = " + mdX + " mDy = " + mdY);
//        Log.d(TAG, "[setMove] mDx = " + mdX + " mDy = " + mdY);
    }

    public void setPosition(float x, float y){
        mPreviousX = mdX + x;
        mPreviousY = mdY - y;
//        Log.d(TAG, "[setPosition] setMove x = " + mdX + " y = " + mdY);
    }


}
