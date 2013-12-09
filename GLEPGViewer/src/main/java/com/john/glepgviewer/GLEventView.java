package com.john.glepgviewer;

import android.content.Context;
import android.util.TypedValue;

import com.john.glepgviewer.util.FontHandle;

/**
 * Created by john on 11/15/13.
 */
public class GLEventView {
    private Context mContext;
    private EventBlock mEventBlock;
    private EventComponent mEventMinute;
    private EventComponent mEventTitle;
    private EventComponent mEventDescription;
    private EventComponent mEventGenre;
    private EventComponent mEventRecord;

    private float mpX;
    private float mpY;
    private float mpXdp;
    private float mpYdp;
    private float[] mProjectionMatrix;
    private String minute;
    private String title;
    private String description;
    private int recordResId;
    private int genreResId;
    private boolean favorGenre;
    private int mergeCount = 0;
    private float height = 200f;


    public GLEventView(Context context){
        mContext = context;
    }



    public void bind(
            float x,
            float y,
            int merge,
            String eventMinute,
            String eventTitle,
            String eventDecription,
            int recordStatusResId,
            int eventGenreResId,
            boolean isFavorGenre,
            float[] projectionMatrix){

        mpX = x;
        mpY = y;
        mpXdp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    mpX,
                    mContext.getResources().getDisplayMetrics());
        mpYdp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    mpY,
                    mContext.getResources().getDisplayMetrics());
        mProjectionMatrix = projectionMatrix;
        minute = eventMinute;
        title = eventTitle;
        description = eventDecription;
        recordResId = recordStatusResId;
        genreResId = eventGenreResId;
        favorGenre = isFavorGenre;
        mergeCount = merge;

//        mEventBlock = new EventBlock(mContext,
//                merge, 200f,
//                projectionMatrix);
//        mEventBlock.setPosition(mpXdp, mpYdp);
//
//        EventComponent.EventRec blockRec = mEventBlock.new EventRec();
//
//        mEventMinute = new EventMinute(mContext, eventMinute,
//                FontHandle.getInstance().getFontTypeface(),
//                mEventBlock.getTopVerticeYPoint(),
//                mEventBlock.getBottomVerticeYPoint(),
//                projectionMatrix);
//        mEventMinute.set(mpXdp, mpYdp);
//        if(isFavorGenre){
//            mEventGenre = new EventGenre(mContext,
//                    eventGenreResId,
//                    mEventMinute.getRightVerticeXPoint(),
//                    0,
//                    mEventBlock.getTopVerticeYPoint(),
//                    mEventBlock.getBottomVerticeYPoint(),
//                    projectionMatrix);
//            mEventGenre.set(mpXdp, mpYdp);
//            ((EventGenre)mEventGenre).setMarginLeft(mEventMinute.getRightVerticeXPoint());
//        }
//        mEventRecord = new EventRecord(mContext,
//                recordStatusResId,
//                blockRec.getTopBound(),
//                blockRec.getBottomBound(),
//                mEventBlock.getRightVerticeXPoint(),
//                projectionMatrix);
//        mEventRecord.set(mpXdp, mpYdp);
//        mEventTitle = new EventTitle(mContext,
//                eventTitle,
//                FontHandle.getInstance().getFontTypeface(),
//                merge,
//                mEventMinute.getBottomVerticeYPoint(),
//                mEventBlock.getBottomVerticeYPoint(),
//                mEventBlock.getRightVerticeXPoint(),
//                projectionMatrix);
//        mEventTitle.set(mpXdp, mpYdp);
//        mEventDescription = new EventDescription(
//                mContext,
//                eventDecription,
//                FontHandle.getInstance().getFontTypeface(),
//                merge,
//                mEventTitle.getBottomVerticeYPoint(),
//                mEventBlock.getBottomVerticeYPoint(),
//                mEventBlock.getRightVerticeXPoint(),
//                projectionMatrix);
//        mEventDescription.set(mpXdp, mpYdp);
    }

    public void bind(
            float x,
            float y,
            int merge,
            float height,
            String eventMinute,
            String eventTitle,
            String eventDecription,
            int recordStatusResId,
            int eventGenreResId,
            boolean isFavorGenre,
            float[] projectionMatrix){
        this.height = height;
        bind(x, y, merge, eventMinute, eventTitle, eventDecription, recordStatusResId, eventGenreResId, isFavorGenre, projectionMatrix);
    }

    public float getX(){return mpXdp;}
    public float getY(){return mpYdp;}

    public void draw(){
        if(null == mEventBlock){
            mEventBlock = new EventBlock(mContext,
                    mergeCount, height,
                    mProjectionMatrix);
            mEventBlock.set(mpXdp, mpYdp);
        }

        if(null != mEventBlock){
            mEventBlock.bindData();
            mEventBlock.draw();
        }

        if(null == mEventMinute){
            mEventMinute = new EventMinute(mContext, minute,
                    FontHandle.getInstance().getFontTypeface(),
                    mEventBlock.getTopVerticeYPoint(),
                    mEventBlock.getBottomVerticeYPoint(),
                    mProjectionMatrix);
            mEventMinute.set(mpXdp, mpYdp);
        }

        if(null != mEventMinute){
//            mEventMinute.set(mpXdp, mpYdp);
            mEventMinute.bindData();
            mEventMinute.draw();
        }

        if(null == mEventGenre){
            if(favorGenre){
                mEventGenre = new EventGenre(mContext,
                        genreResId,
                        mEventMinute.getRightVerticeXPoint(),
                        0,
                        mEventBlock.getTopVerticeYPoint(),
                        mEventBlock.getBottomVerticeYPoint(),
                        mProjectionMatrix);
                mEventGenre.set(mpXdp, mpYdp);
//                ((EventGenre)mEventGenre).setMarginLeft(mEventMinute.getRightVerticeXPoint());
            }
        }
        if(null != mEventGenre){
//            mEventGenre.set(mpXdp, mpYdp);
            mEventGenre.bindData();
            mEventGenre.draw();
        }

        if(null == mEventRecord){
            mEventRecord = new EventRecord(mContext,
                recordResId,
                mEventBlock.getTopVerticeYPoint(),
                mEventBlock.getBottomVerticeYPoint(),
                mEventBlock.getRightVerticeXPoint(),
                mProjectionMatrix);
            mEventRecord.set(mpXdp, mpYdp);
        }
        if(null !=  mEventRecord){
//            mEventRecord.set(mpXdp, mpYdp);
            mEventRecord.bindData();
            mEventRecord.draw();
        }

        if(null == mEventTitle){
            mEventTitle = new EventTitle(mContext,
                title,
                FontHandle.getInstance().getFontTypeface(),
                mergeCount,
                mEventMinute.getBottomVerticeYPoint(),
                mEventBlock.getBottomVerticeYPoint(),
                mEventBlock.getRightVerticeXPoint(),
                mProjectionMatrix);
            mEventTitle.set(mpXdp, mpYdp);
        }
        if(null != mEventTitle){
//            mEventTitle.set(mpXdp, mpYdp);
            mEventTitle.bindData();
            mEventTitle.draw();
        }

        if(null == mEventDescription){
            mEventDescription = new EventDescription(
                mContext,
                description,
                FontHandle.getInstance().getFontTypeface(),
                mergeCount,
                mEventTitle.getBottomVerticeYPoint(),
                mEventBlock.getBottomVerticeYPoint(),
                mEventBlock.getRightVerticeXPoint(),
                mProjectionMatrix);
            mEventDescription.set(mpXdp, mpYdp);
        }
        if(null != mEventDescription){
//            mEventDescription.set(mpXdp, mpYdp);
            mEventDescription.bindData();
            mEventDescription.draw();
        }
    }

//    private float timeToHeight(Time startTime, Time endTime)
}
