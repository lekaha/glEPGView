package com.john.glepgviewer;

import android.content.Context;

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
        merge = 1;
        mEventBlock = new EventBlock(mContext,
                merge, 220f,
                projectionMatrix);
        EventComponent.EventRec blockRec = mEventBlock.new EventRec();

        mEventMinute = new EventMinute(mContext, eventMinute,
                FontHandle.getInstance().getFontTypeface(),
                mEventBlock.getTopVerticeYPoint(),
                mEventBlock.getBottomVerticeYPoint(),
                projectionMatrix);
        if(isFavorGenre){
            mEventGenre = new EventGenre(mContext,
                    eventGenreResId,
                    mEventMinute.getRightVerticeXPoint(),
                    0,
                    mEventBlock.getTopVerticeYPoint(),
                    mEventBlock.getBottomVerticeYPoint(),
                    projectionMatrix);
            ((EventGenre)mEventGenre).setMarginLeft(mEventMinute.getRightVerticeXPoint());
        }
        mEventRecord = new EventRecord(mContext,
                recordStatusResId,
                blockRec.getTopBound(),
                blockRec.getBottomBound(),
                mEventBlock.getRightVerticeXPoint(),
                projectionMatrix);
        mEventTitle = new EventTitle(mContext,
                eventTitle,
                FontHandle.getInstance().getFontTypeface(),
                merge,
                mEventMinute.getBottomVerticeYPoint(),
                mEventBlock.getBottomVerticeYPoint(),
                mEventBlock.getRightVerticeXPoint(),
                projectionMatrix);
        mEventDescription = new EventDescription(
                mContext,
                eventDecription,
                FontHandle.getInstance().getFontTypeface(),
                merge,
                mEventTitle.getBottomVerticeYPoint(),
                mEventBlock.getBottomVerticeYPoint(),
                mEventBlock.getRightVerticeXPoint(),
                projectionMatrix);
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
}
