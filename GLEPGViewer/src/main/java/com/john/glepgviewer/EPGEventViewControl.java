package com.john.glepgviewer;

import android.content.Context;
import android.text.format.Time;
import android.view.View.OnClickListener;

public class EPGEventViewControl {
//	private final String TAG = "EPGEventViewControl";
	private Context mContext;
	
	
	private final int MIN_EPG_EVENT_WIDTH = 145;
	private int mMinEPGEventViewWidth = MIN_EPG_EVENT_WIDTH;
	private final int mMinEPGEventViewHeight = 15;
	private int mEPGEventViewHeight;
	private int mHourHeight;
	private final int MILLISECOND = 60000;
	private final int FIRST_TIME = 5;
	private final int END_TIME = 5;
	private float mIntervalRatioFromFirstTime;
//	private Typeface mTypeface = null;

	private Time mFirstTime;
	private Time mEndTime;
	private boolean mIsRecording;
	private int mRecordType;
	private int mRecordStatus;
	private boolean mIsFavorGenre;
	private int mEventMinute;
	
//	private final int MINIMUM_MOVE_DISTANCE = 30;
//	private int mCurrentEventBackgroundResId;
	private OnClickListener mOnClickListener;
	
	private int mProgramIndex = 0;
	private int mCombineProgramCount = 1;
	private boolean mIsCurrentProgram = false;

    private boolean mIsNeedCheckParental = false;
    private int mParentalControlAge = 0;
//	public EPGEventViewControl(Context context,
//                               Calendar day,
//                               EPGEventInfo info,
//                               int programIndex,
//                               boolean isCurrentProgram,
//                               int combineProgramCounty,
//                               boolean isRecording,
//                               AVMScheduleType recordType,
//                               AVMScheduleStatus recordStatus,
//                               boolean isFavorGenre,
//                               boolean isNeedcheckParental,
//                               int parentalControlAge,
//                               Typeface typeface,
//                               OnClickListener listener) {
//
//		mEventInfo = info;
//		Calendar today = (Calendar) day.clone();
//		mFirstTime = new Time();
//		mFirstTime.set(0, 0, FIRST_TIME, today.get(Calendar.DAY_OF_MONTH),
//				today.get(Calendar.MONTH), today.get(Calendar.YEAR));
//		today.add(Calendar.DATE, 1);
//		mEndTime = new Time();
//		mEndTime.set(0, 0, END_TIME, today.get(Calendar.DAY_OF_MONTH),
//				today.get(Calendar.MONTH), today.get(Calendar.YEAR));
//
//		mProgramIndex = programIndex;
//		mIsCurrentProgram = isCurrentProgram;
//		mCombineProgramCount = combineProgramCounty;
//		mIsRecording = isRecording;
//		mRecordType = recordType;
//		mRecordStatus = recordStatus;
//		mIsFavorGenre = isFavorGenre;
//        mIsNeedCheckParental = isNeedcheckParental;
//        mParentalControlAge = parentalControlAge;
////		mTypeface = typeface;
//		mContext = context;
//		Resources r = mContext.getResources();
//		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MIN_EPG_EVENT_WIDTH, r.getDisplayMetrics());
//
//		px = r.getDimension(R.dimen.epg_page_event_width);
//		mMinEPGEventViewWidth = (int)(px);
////		Log.d(TAG, "EPGEventViewControl: MinEPGEventViewWidth = " + mMinEPGEventViewWidth);
//		mHourHeight = mContext.getResources().getDimensionPixelSize(R.dimen.epg_page_content_time_hour_height);
//		mOnClickListener = listener;
////		initEPGEventView(context);
//		computeEventContent();
//	}
	
//	public void computeEventContent(float heightTimes){
//		float h = mContext.getResources().getDimensionPixelSize(R.dimen.epg_page_content_time_hour_height);
//		mHourHeight = (int)( h * heightTimes);
//		computeEventContent();
//	}
		
	private void computeEventContent(){	
		Time startTime = null;//mEventInfo.getEventStartTime();
		int minute = startTime.minute;
		Time endTime = null;//mEventInfo.getEventEndTime();
		mEventMinute = minute;
		
		long interval = startTime.toMillis(false) - mFirstTime.toMillis(false);
		if(interval < 0){
			startTime.hour = mFirstTime.hour;
			interval = 0;
		}
		long overtime = mEndTime.toMillis(false) - endTime.toMillis(false);
		if(overtime < 0){
			endTime = mEndTime;
		}
		long d = endTime.toMillis(false) - startTime.toMillis(false);
		
		float minInterval = interval / (float) MILLISECOND;
		mIntervalRatioFromFirstTime = (minInterval)/(60f);
		if( 0 > mIntervalRatioFromFirstTime){
			mIntervalRatioFromFirstTime = mIntervalRatioFromFirstTime + 24;
		}
		int min = (int) ((d % MILLISECOND) != 0 ? ((d / MILLISECOND) + 1) : (d / MILLISECOND));
		float durationRatioByHour = ((float)min)/(60f);
		int hourHeight = mHourHeight;
		if(0 == mHourHeight){
//			hourHeight = mContext.getResources().getDimensionPixelSize(R.dimen.epg_page_content_time_hour_height);
		}
		int epgEventLayoutHeight = Math.round (hourHeight * durationRatioByHour);
//		if( 0 == minute){
//			epgEventLayoutHeight += ( 1 * Math.ceil(durationRatioByHour));
//		}
		mEPGEventViewHeight = epgEventLayoutHeight;
		int marginTop = (int) (Math.round(mIntervalRatioFromFirstTime * 
				(mHourHeight)));
		mTopMargin = marginTop;
		mLeftMargin = mProgramIndex * mMinEPGEventViewWidth;
		
//		Log.d(TAG, "[initEPGEventView] Title = " + mEventInfo.getEventTitle());
//		Log.d(TAG, "[initEPGEventView] Description = " + mEventInfo.getEventDescription());
//		Log.d(TAG, "[initEPGEventView] Genre = " + mEventInfo.getGenre().name());
//		Log.d(TAG, "[initEPGEventView] StartTime = " + startTime.month + "/" + startTime.monthDay + " " + startTime.hour + ":" + startTime.minute);
//		Log.d(TAG, "[initEPGEventView] EndTime = " + endTime.month + "/" + endTime.monthDay + " " + endTime.hour + ":" + endTime.minute);
//		Log.d(TAG, "[initEPGEventView] Duration(min) = " + min);
//		Log.d(TAG, "[initEPGEventView] Ratio(min/hour) = " + durationRatioByHour);
//		Log.d(TAG, "[initEPGEventView] FirstTime of Day = " + mFirstTime.hour + ":" + mFirstTime.minute);
//		Log.d(TAG, "[initEPGEventView] intervalRatio(min) = " + mIntervalRatioFromFirstTime);
//		Log.d(TAG, "[initEPGEventView] (X, Y) = (" + mLeftMargin + ", " + mTopMargin + ")");
//		Log.d(TAG, "[initEPGEventView] ============================================================ ");
		
	}

	
	int mWidth = 0;
	int mHeight = 0;
	
//	public void setScale(float scaleFactor){
//		onScaling(scaleFactor);
//	}
	
//	protected void onScaling(float scaleFactor){
//		int hourHeight = (int) (mEPGEventViewHeight * scaleFactor);
////		int marginTop = (int) (Math.ceil(mIntervalRatioFromFirstTime * 
////										(hourHeight + 1)));
//		
//		LayoutParams lp = (LayoutParams) mEPGEventRootLayout.getLayoutParams();
//		if(hourHeight != mEPGEventViewHeight){
//			lp.height = hourHeight;
//			mEPGEventRootLayout.setLayoutParams(lp);
//		}
//	}
	
	public int getEventWidth(){
		return mMinEPGEventViewWidth * mCombineProgramCount;
	}
	
	public int getEventHeight(){
		return mEPGEventViewHeight;
	}
	
	private int mLeftMargin = 0;	
	public int getEventX(){
//		return mProgramIndex * MIN_EPG_EVENT_WIDTH;
		return mLeftMargin;
	}
	
	private int mTopMargin = 0;
	public int getEventY(){
//		int marginTop = (int) (FloatMath.ceil(mIntervalRatioFromFirstTime * 
//				(mHourHeight + 1)));
//		return marginTop;
		return mTopMargin;
	}
	
//	public EPGEventInfo getEventInfo(){
//		return mEventInfo;
//	}
	
	public int getProgramIndex(){
		return mProgramIndex;
	}

	public String getEventTimeMinute(){
		return String.format("%02d", mEventMinute);
	}
	
	public boolean isMiniEvent(){
		if(mEPGEventViewHeight < mMinEPGEventViewHeight)
			return true;
		return false;
	}
	
	public boolean isCurrentEvent(){
		return mIsCurrentProgram;
	}
	
	public boolean isFavorEventGenre(){
		return mIsFavorGenre;
	}
	
	public boolean isRecording(){
		return mIsRecording;
	}
	
//	public AVMScheduleType getEventRecordType(){
//		return mRecordType;
//	}
//
//	public AVMScheduleStatus getEventRecordStatus(){
//		return mRecordStatus;
//	}
	
	public int getEventRecordStatusResId(){
		return 0;
	}
	
//	public AVMEPGGenreEnum getEventGenre(){
//		return mEventInfo.getGenre();
//	}

    public boolean isNeedCheckParental(){
        return mIsNeedCheckParental;
    }

    public int getParentalControlAge(){
        return mParentalControlAge;
    }
	
//	public int getEventGenreResId(){
//		return getGenreIconResId(mEventInfo.getGenre());
//	}
	
	public OnClickListener getEventOnClickListner(){
		return mOnClickListener;
	}
	
	@Override
	public String toString(){
		return "EPGEvent view";
	}
}
