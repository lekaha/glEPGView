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

        float pXdp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        x,
                        mContext.getResources().getDisplayMetrics());
        float pYdp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        y,
                        mContext.getResources().getDisplayMetrics());

        mEventBlock = new EventBlock(mContext,
                merge, 200f,
                projectionMatrix);
        mEventBlock.setPosition(pXdp, y);
        EventComponent.EventRec blockRec = mEventBlock.new EventRec();

        mEventMinute = new EventMinute(mContext, eventMinute,
                FontHandle.getInstance().getFontTypeface(),
                mEventBlock.getTopVerticeYPoint(),
                mEventBlock.getBottomVerticeYPoint(),
                projectionMatrix);
        mEventMinute.set(pXdp, pYdp);
        if(isFavorGenre){
            mEventGenre = new EventGenre(mContext,
                    eventGenreResId,
                    mEventMinute.getRightVerticeXPoint(),
                    0,
                    mEventBlock.getTopVerticeYPoint(),
                    mEventBlock.getBottomVerticeYPoint(),
                    projectionMatrix);
            mEventGenre.set(pXdp, pYdp);
            ((EventGenre)mEventGenre).setMarginLeft(mEventMinute.getRightVerticeXPoint());
        }
        mEventRecord = new EventRecord(mContext,
                recordStatusResId,
                blockRec.getTopBound(),
                blockRec.getBottomBound(),
                mEventBlock.getRightVerticeXPoint(),
                projectionMatrix);
        mEventRecord.set(pXdp, pYdp);
        mEventTitle = new EventTitle(mContext,
                eventTitle,
                FontHandle.getInstance().getFontTypeface(),
                merge,
                mEventMinute.getBottomVerticeYPoint(),
                mEventBlock.getBottomVerticeYPoint(),
                mEventBlock.getRightVerticeXPoint(),
                projectionMatrix);
        mEventTitle.set(pXdp, pYdp);
        mEventDescription = new EventDescription(
                mContext,
                eventDecription,
                FontHandle.getInstance().getFontTypeface(),
                merge,
                mEventTitle.getBottomVerticeYPoint(),
                mEventBlock.getBottomVerticeYPoint(),
                mEventBlock.getRightVerticeXPoint(),
                projectionMatrix);
        mEventDescription.set(pXdp, pYdp);
    }

    public void draw(){
        if(null != mEventBlock){
            mEventBlock.bindData();
            mEventBlock.draw();
        }

        if(null != mEventMinute){
            mEventMinute.bindData();
            mEventMinute.draw();
        }

        if(null != mEventGenre){
            mEventGenre.bindData();
            mEventGenre.draw();
        }

        if(null != mEventTitle){
            mEventTitle.bindData();
            mEventTitle.draw();
        }

        if(null !=  mEventRecord){
            mEventRecord.bindData();
            mEventRecord.draw();
        }

        if(null != mEventDescription){
            mEventDescription.bindData();
            mEventDescription.draw();
        }
    }

//    private float timeToHeight(Time startTime, Time endTime)
}
