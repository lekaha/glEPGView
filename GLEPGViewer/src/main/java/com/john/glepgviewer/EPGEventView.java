package com.john.glepgviewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.john.glepgviewer.util.FontHandle;

import java.io.InputStream;
import java.util.Vector;

public class EPGEventView extends View {
	private final static String TAG = EPGEventView.class.getSimpleName();
//	private final int HOUR_HEIGHT = 
//	private final int MIN_EPG_EVENT_WIDTH = 145;
	
	private final float LAYOUT_PADDING_TOP = 5f;
	private final float LAYOUT_PADDING_LEFT = 9f;
	private final float LAYOUT_PADDING_BOTTOM = 9f;
	
	private final float TIME_WIDTH = 19f;
	private final float TIME_HEIGHT = 19f;
	private final float TITLE_TIME_TEXT_SIZE = 16f;
	private final float TITLE_TIME_TEXT_LINESPACING = 1f;

    private float mTimeWidth;
    private float mTimeMarginTop;
    private float mGenreMarginTop;
    private float mTitleMarginTop;
    private float mDescriptionMarginTop;
	
//	private final float TITLE_HEIGHT = TITLE_TIME_TEXT_SIZE + TITLE_TIME_TEXT_LINESPACING + TITLE_TIME_TEXT_LINESPACING;
	
	private final int MINIMUM_MOVE_DISTANCE = 30;
	
	private final float GENRE_WIDTH = 18f;
	private final float GENRE_HEIGHT = 18f;
	
	private final float DECRIPTION_TEXT_SIZE = 14f;
	private final float DECRIPTION_HEIGHT = DECRIPTION_TEXT_SIZE + TITLE_TIME_TEXT_LINESPACING + TITLE_TIME_TEXT_LINESPACING + 2f;
	 
	private Context mContext = null;
	private Typeface mTypeface = null;
	private final int NORMAL_BG_COLOR = 0xff262626;
	private final int MINI_BG_COLOR = 0xff69a5d7;
	private final int PRESS_BG_COLOR = 0xffc44908;
	private final int CURRENT_BG_COLOR = 0xff551e1e;
	private final int PASSED_EVENT_COLOR = 0xff828282;
	private final int FUTURE_EVENT_COLOR = 0xffffffff;
	private int mCurrentEventBackgroundColor;

	private boolean mIsCurrentEvent = false;
	private boolean mIsMiniEvent = false;
	private String mEventMinute = null;
	private String mEventTitle = null;
	private String mEventDecription = null;
	private int mEventStatusResId = -1;
	private int mEventGenreResId = -1;
	private int mEventStatus = 0;
	private int mEventGenre = 0;
	
//	private int mEventHeight;
//	private int mEventWidth;
	
	public EPGEventView(Context context) {
		super(context);
		mContext = context;
		if (!isInEditMode()) {
        }
		setLayerType(View.LAYER_TYPE_HARDWARE, null);
		
		initDraw();
//		mEventHeight = mContext.getResources().getDimensionPixelSize(R.dimen.epg_page_content_time_hour_height);
//		mEventWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MIN_EPG_EVENT_WIDTH, mContext.getResources().getDisplayMetrics());
//		Log.d(TAG, "[ EPGEventView ] EventHeight = " + mEventHeight + " EventWidth = " + mEventWidth);
	}
	
	public EPGEventView(Context context, Typeface typeface) {
		super(context);
		mContext = context;
		if (!isInEditMode()) {
        }
		setLayerType(View.LAYER_TYPE_HARDWARE, null);
		
		mTypeface = typeface;
		initDraw();
//		mEventHeight = mContext.getResources().getDimensionPixelSize(R.dimen.epg_page_content_time_hour_height);
//		mEventWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MIN_EPG_EVENT_WIDTH, mContext.getResources().getDisplayMetrics());
//		Log.d(TAG, "[ EPGEventView ] EventHeight = " + mEventHeight + " EventWidth = " + mEventWidth);
	}
	
	public EPGEventView(Context context, AttributeSet attrs){
		super(context, attrs);
		mContext = context;
		if (!isInEditMode()) {
        }
		setLayerType(View.LAYER_TYPE_HARDWARE, null);
		
		initDraw();
//		mEventHeight = mContext.getResources().getDimensionPixelSize(R.dimen.epg_page_content_time_hour_height);
//		mEventWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MIN_EPG_EVENT_WIDTH, mContext.getResources().getDisplayMetrics());
//		Log.d(TAG, "[ EPGEventView ] EventHeight = " + mEventHeight + " EventWidth = " + mEventWidth);
	}
	
	public EPGEventView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		mContext = context;
		if (!isInEditMode()) {
        }
		setLayerType(View.LAYER_TYPE_HARDWARE, null);
		
		initDraw();
//		mEventHeight = mContext.getResources().getDimensionPixelSize(R.dimen.epg_page_content_time_hour_height);
//		mEventWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MIN_EPG_EVENT_WIDTH, mContext.getResources().getDisplayMetrics());
//		Log.d(TAG, "[ EPGEventView ] EventHeight = " + mEventHeight + " EventWidth = " + mEventWidth);
	}
		
//	public void bindData(
//			boolean isCurrentProgram,
//			boolean isMiniEvent,
//			String eventMinute,
//			String eventTitle,
//			String eventDecription,
//			int recordStatus,
//			int eventGenre,
//			boolean isFavorGenre,
//			OnClickListener listener){
////		mEventMinute = eventMinute;
////		mEventTitle = eventTitle;
////		mEventDecription = eventDecription;
////		mEventStatus = recordStatus;
//
//		int genreResId = getGenreIconResId(eventGenre);
//		int statusResId = getScheduleStatusResId(recordStatus);
//		bindData(isCurrentProgram,
//				isMiniEvent,
//				eventMinute,
//				eventTitle,
//				eventDecription,
//				statusResId,
//				genreResId,
//				isFavorGenre,
//				listener
//				);
//	}
	
//	public void bindData(
//			boolean isCurrentProgram,
//			boolean isMiniEvent,
//			String eventMinute,
//			String eventTitle,
//			String eventDecription,
//			int recordStatusResId,
//			int eventGenre,
//			boolean isFavorGenre,
//			OnClickListener listener){
////		mEventMinute = eventMinute;
////		mEventTitle = eventTitle;
////		mEventDecription = eventDecription;
////		mEventStatus = recordStatus;
//
//		int genreResId = -1;
//		mEventGenre = eventGenre;
////		int statusResId = getScheduleStatusResId(recordStatus);
//		bindData(isCurrentProgram,
//				isMiniEvent,
//				eventMinute,
//				eventTitle,
//				eventDecription,
//				recordStatusResId,
//				genreResId,
//				isFavorGenre,
//				listener
//				);
//	}
	
//	private Vector<String> mEventTitleVector = null;
//	private Vector<String> mEventDecriptionVector = null;
	public void bindData(
			boolean isCurrentProgram,
			boolean isMiniEvent,
			String eventMinute,
			String eventTitle,
			String eventDecription,
			int recordStatusResId,
			int eventGenreResId,
			boolean isFavorGenre,
			OnClickListener listener){
		mEventMinute = eventMinute;
		mEventTitle = eventTitle;
		mEventDecription = eventDecription;
		mEventStatusResId = recordStatusResId;
		mIsCurrentEvent = isCurrentProgram;
		mIsMiniEvent = isMiniEvent;
		
//		mEventTitleVector = GetTextIfon(mEventTitle, mEventTitleVector, mTimeAndTitlePaint, (LAYOUT_PADDING_LEFT * 2) + 2f);
//		mEventDecriptionVector = GetTextIfon(mEventDecription, mEventDecriptionVector, mDecriptionPaint, (LAYOUT_PADDING_LEFT * 2));
				
		if(isFavorGenre){
			mEventGenreResId = eventGenreResId;
			if(-1 != mEventGenreResId)
				mEventGenreBitmap = getLocalBitmap(getContext(), mEventGenreResId);
//			mGenreBitmapInputStream = getContext().getResources().openRawResource(mEventGenreResId);
		}
		else{
			mEventGenreResId = -1;
			mEventGenreBitmap = null;
		}
		
		if(-1 != mEventStatusResId){
			mEventRecordStatusBitmap = getLocalBitmap(getContext(), mEventStatusResId);
//			mRecordStatusBitmapInputStream = getContext().getResources().openRawResource(mEventStatusResId);
		}		
		
//		selectBackgroundColor();
		
		if(null != listener)
			setOnClickListener(listener);
		
		//invalidate();
	}
	
	private EPGEventViewControl mEPGEventInfo;
//	public void bindData(
//			EPGEventViewControl info){
//
//		mEventMinute = info.getEventTimeMinute();
//		mEventTitle = info.getEventInfo().getEventTitle();
//		mEventDecription = info.getEventInfo().getEventDescription();
//		mEventStatusResId = info.getEventRecordStatusResId();
//		mIsCurrentEvent = info.isCurrentEvent();
//		mIsMiniEvent = info.isMiniEvent();
//
//		mEPGEventInfo = info;
//
//		if(-1 != mEventStatusResId){
//			mEventRecordStatusBitmap = getLocalBitmap(getContext(), mEventStatusResId);
//		}
//
////		selectBackgroundColor();
//
//		if(null != info.getEventOnClickListner())
//			setOnClickListener(info.getEventOnClickListner());
//	}
	
	private boolean mIsPassed = false;
	public void setEventPassed(boolean isPassed){
		mIsPassed = isPassed;
	}

	private Paint mLeftBorderPaint;
	private Paint mTopBorderPaint;
	private Paint mRightAndBottomBorderPaint;
	private Paint mBackgroundPaint;
//	private RectF mEventRect;
	
	private Paint mTimeAndTitlePaint;
	private Paint mDecriptionPaint;
	
	private Paint mEventGenrePaint;
//	private InputStream mGenreBitmapInputStream;
	private Bitmap mEventGenreBitmap;
	private Matrix mEventGenreMatrix;
	
	private Paint mEventRecordStatusPaint;
//	private InputStream mRecordStatusBitmapInputStream;
	private Bitmap mEventRecordStatusBitmap;
	private Matrix mEventRecordStatusMatrix;
	private final int MINI_TOP_BORDER_COLOR = 0xffa5bef7;
	private final int NORMAL_TOP_BORDER_COLOR = 0xff6e6e6e;

	private void initDraw(){
//		mEventTitleVector = new Vector<String>();
//		mEventDecriptionVector = new Vector<String>();
		
		mLeftBorderPaint = new Paint();
		mLeftBorderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mLeftBorderPaint.setColor(0xff505050);
		
		mTopBorderPaint = new Paint();
		mTopBorderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mTopBorderPaint.setColor(NORMAL_TOP_BORDER_COLOR);
		
		mRightAndBottomBorderPaint = new Paint();
		mRightAndBottomBorderPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mRightAndBottomBorderPaint.setColor(0xff000000);
		
		mBackgroundPaint = new Paint();
		mCurrentEventBackgroundColor = NORMAL_BG_COLOR;
		mBackgroundPaint.setColor(mCurrentEventBackgroundColor);
//		mEventRect = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
		
		
		mEventGenrePaint = new Paint();
		mEventGenrePaint.setFilterBitmap(true);
//		mEventGenreBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.epg_dropdown_menu_genre_icon_0_all);
		mEventGenreMatrix = new Matrix();
//		mEventGenreMatrix.setTranslate((9f + TIME_WIDTH + 4f), (9f));
//		mEventGenreMatrix.postScale((GENRE_WIDTH / mEventGenreBitmap.getWidth()), (GENRE_HEIGHT / mEventGenreBitmap.getHeight()));
        mTimeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TIME_WIDTH, mContext.getResources().getDisplayMetrics());
        mTimeMarginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (LAYOUT_PADDING_TOP + TITLE_TIME_TEXT_SIZE + TITLE_TIME_TEXT_LINESPACING), mContext.getResources().getDisplayMetrics());
        mGenreMarginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (LAYOUT_PADDING_TOP + TITLE_TIME_TEXT_LINESPACING + TITLE_TIME_TEXT_LINESPACING + 1), mContext.getResources().getDisplayMetrics());
		
		mEventRecordStatusPaint = new Paint();
		mEventRecordStatusPaint.setFilterBitmap(true);
//		mEventRecordStatusBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.epg_icon_recording_status_period_failed);
		mEventRecordStatusMatrix = new Matrix();
//		mEventRecordStatusMatrix.setTranslate((), (0f));
//		mEventRecordStatusMatrix.postScale((GENRE_WIDTH / mEventGenreBitmap.getWidth()), (GENRE_HEIGHT / mEventGenreBitmap.getHeight()));
		
		mTimeAndTitlePaint = new Paint();
		mTimeAndTitlePaint.setAntiAlias(true);
        mTypeface = FontHandle.getInstance().getFontTypeface();
		if(null != mTypeface)
			mTimeAndTitlePaint.setTypeface(mTypeface);
		else
			mTimeAndTitlePaint.setTypeface(Typeface.DEFAULT);
		mTimeAndTitlePaint.setTextAlign(Paint.Align.LEFT);
		float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, TITLE_TIME_TEXT_SIZE, mContext.getResources().getDisplayMetrics());
		mTimeAndTitlePaint.setTextSize(textSize);
		mTimeAndTitlePaint.setColor(0xffffffff);
        mTitleMarginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (LAYOUT_PADDING_TOP + TITLE_TIME_TEXT_SIZE + TIME_HEIGHT + TITLE_TIME_TEXT_LINESPACING), mContext.getResources().getDisplayMetrics());
		
		mDecriptionPaint = new Paint();
		mDecriptionPaint.setAntiAlias(true);
		if(null != mTypeface)
			mDecriptionPaint.setTypeface(mTypeface);
		else
			mDecriptionPaint.setTypeface(Typeface.DEFAULT);
		mDecriptionPaint.setTextAlign(Paint.Align.LEFT);
		textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DECRIPTION_TEXT_SIZE, mContext.getResources().getDisplayMetrics());
		mDecriptionPaint.setTextSize(textSize);
		mDecriptionPaint.setColor(PASSED_EVENT_COLOR);
        mDescriptionMarginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (LAYOUT_PADDING_TOP + DECRIPTION_HEIGHT + TIME_HEIGHT + TITLE_TIME_TEXT_LINESPACING), mContext.getResources().getDisplayMetrics());


		setOnTouchListener(new OnTouchListener() {
			private float mTouchDownX;
			private float mTouchDownY; 
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(MotionEvent.ACTION_DOWN == event.getAction()){
					mTouchDownX = event.getX();
					mTouchDownY = event.getY();
					Log.d(TAG, "[onTouch] Motion Action = " + event.getAction());
					mCurrentEventBackgroundColor = PRESS_BG_COLOR;
					mBackgroundPaint.setColor(mCurrentEventBackgroundColor);
					v.invalidate();
					System.gc();
				}
				else if(MotionEvent.ACTION_MOVE == event.getAction()){	
					if((Math.abs(event.getX() - mTouchDownX) > MINIMUM_MOVE_DISTANCE) || ((Math.abs(event.getY() - mTouchDownY) > MINIMUM_MOVE_DISTANCE))){
						event.getX();
						mCurrentEventBackgroundColor = NORMAL_BG_COLOR;
						mCurrentEventBackgroundColor = selectBackgroundColor(mEPGEventInfo);
						mBackgroundPaint.setColor(mCurrentEventBackgroundColor);
						v.postInvalidate();
					}
				}
				else if(MotionEvent.ACTION_UP == event.getAction()){
//					Log.d(TAG, "[onTouch] MotionEvent Action_UP, call System.gc()");
					mBackgroundPaint.setColor(mCurrentEventBackgroundColor);
					System.gc();
				}else{
					mCurrentEventBackgroundColor = NORMAL_BG_COLOR;
					mCurrentEventBackgroundColor = selectBackgroundColor(mEPGEventInfo);
					mBackgroundPaint.setColor(mCurrentEventBackgroundColor);
					v.postInvalidate();
				}
				return false;
			}
		});
	}
	
//	private void selectBackgroundColor(){
//			
//		if(mIsMiniEvent){
//			if(!mIsPassed){
//				mCurrentEventBackgroundColor = MINI_BG_COLOR;
//			}
//		}
//		else{
//			if(mIsCurrentEvent){
//				mCurrentEventBackgroundColor = CURRENT_BG_COLOR;
//			}
//			else{
//				mCurrentEventBackgroundColor = NORMAL_BG_COLOR;
//			}
//		}
//	}
	
	private int selectBackgroundColor(EPGEventViewControl info){
		if(PRESS_BG_COLOR == mCurrentEventBackgroundColor){
			if(!mIsPassed)
				mDecriptionPaint.setColor(FUTURE_EVENT_COLOR);
			return PRESS_BG_COLOR;
		}
		if(null != info){
            if(info.isMiniEvent()){
                if(!mIsPassed){
                    mCurrentEventBackgroundColor = MINI_BG_COLOR;
                }
            }
            else{
                if(info.isCurrentEvent()){
                    mCurrentEventBackgroundColor = CURRENT_BG_COLOR;
                }
                else{
                    mCurrentEventBackgroundColor = NORMAL_BG_COLOR;
                }
            }
        }
		mDecriptionPaint.setColor(PASSED_EVENT_COLOR);
//		mCurrentEventBackgroundColor = NORMAL_BG_COLOR;
		return mCurrentEventBackgroundColor;
	}
	
	private int getGenreIconResId(int genreEnum){
		int resId = R.drawable.epg_dropdown_menu_genre_icon_0_all;

		mEventGenreResId = resId;
		return mEventGenreResId;
	}
	
	
	private int getScheduleStatusResId(int status){
		int resId = R.drawable.epg_icon_recording_status_period;

		mEventStatusResId = resId;
		
		return mEventStatusResId;
	}
	
	
	private Bitmap getLocalBitmap(Context con, int resourceId){
	    InputStream inputStream = con.getResources().openRawResource(resourceId);
	    return BitmapFactory.decodeStream(inputStream, null, getBitmapOptions());
	}
	
	private BitmapFactory.Options getBitmapOptions(){
//		int sample = 1;
	    BitmapFactory.Options options = new BitmapFactory.Options();
//	    try {
//			BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options, true);
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		}
	    
	    options.inPurgeable = true;
	    options.inInputShareable = true;
//	    options.inSampleSize = sample;
	    return options;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		Log.d(TAG, "[ onDraw ] Entry");
		mCurrentEventBackgroundColor = selectBackgroundColor(mEPGEventInfo); 
		drawBackground(canvas);	
		if(null != mEPGEventInfo){
			if(-1 != mEPGEventInfo.getEventRecordStatusResId()){
				drawEventRecordStatus(canvas, mEPGEventInfo.getEventRecordStatusResId());
			}
		}else{
			if(-1 != mEventStatus)
				drawEventRecordStatus(canvas, getScheduleStatusResId(mEventStatus));
			else{
				if(mEventStatusResId != -1)
					drawEventRecordStatus(canvas, mEventStatusResId);
			}
		}
		
		canvas.clipRect(0, 0, getWidth() - 1f, getHeight() - LAYOUT_PADDING_BOTTOM);
		
		if(null != mEPGEventInfo){
//            if(mEPGEventInfo.isNeedCheckParental() && (mEPGEventInfo.getEventInfo().getParentalControlAge() > mEPGEventInfo.getParentalControlAge())){
//                drawTime(canvas, mEPGEventInfo.getEventTimeMinute());
//                String title = mEPGEventInfo.getEventInfo().getEventTitle().replaceAll(".", "●");
//                drawTitle(canvas, title);
//
//                String decription = mEPGEventInfo.getEventInfo().getEventDescription().replaceAll(".", "●");
//                drawDecription(canvas, decription);
//            }
//            else{
//
//                if(mEPGEventInfo.isFavorEventGenre())
//                    drawEventGenre(canvas, mEPGEventInfo.getEventGenre());
//
//                drawTime(canvas, mEPGEventInfo.getEventTimeMinute());
//                drawTitle(canvas, mEPGEventInfo.getEventInfo().getEventTitle());
//                drawDecription(canvas, mEPGEventInfo.getEventInfo().getEventDescription());
//            }
		}
		else{
			
			if(null != mEventMinute){
				drawTime(canvas, mEventMinute);
			}
			if(null != mEventTitle){
				drawTitle(canvas, mEventTitle);
			}
			if(null != mEventDecription){
				drawDecription(canvas, mEventDecription);
			}
            if(null != mEventGenreBitmap){
                drawEventGenre(canvas, 0);
            }
		}
	}
	
	@Override
	protected void onDetachedFromWindow(){
//		Log.d(TAG, "[ onDetachedFromWindow ] Entry");
		if(null != mEventGenreBitmap){
			mEventGenreBitmap.recycle();
			mEventGenreBitmap = null;
		}
		if(null != mEventRecordStatusBitmap){
			mEventRecordStatusBitmap.recycle();
			mEventRecordStatusBitmap = null;
		}
		
		if(null != mBackground){
			mBackground.recycle();
			mBackground = null;
		}
		
//		System.gc();
//		Log.d(TAG, "[ onDetachedFromWindow ] Exit");
	}
	
	private void drawTime(Canvas canvas, String minute) {
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		if(mIsPassed){
			mTimeAndTitlePaint.setColor(PASSED_EVENT_COLOR);
		}
		else{
			mTimeAndTitlePaint.setColor(FUTURE_EVENT_COLOR);
		}
		canvas.drawText(minute, LAYOUT_PADDING_LEFT, mTimeMarginTop, mTimeAndTitlePaint);
		canvas.restore();		
	}
	
//	private void drawTime(Canvas canvas, int minute) {
//		canvas.save(Canvas.MATRIX_SAVE_FLAG);		
//		canvas.drawText(String.format("%02d", mEventMinute), 9f, (5f + TITLE_TIME_TEXT_SIZE + TITLE_TIME_TEXT_LINESPACING), mTimeAndTitlePaint);
//		canvas.restore();		
//	}
	
	private void drawEventGenre(Canvas canvas, int genre){
//		AVMLog.d(TAG, "drawEventGenre", "EPG Title = " + mEventTitle + ", Genre = " + genre.name());
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		mEventGenreBitmap = getLocalBitmap(getContext(), getGenreIconResId(0));
		if(null != mEventGenreBitmap){
			mEventGenreMatrix.setScale((GENRE_WIDTH / mEventGenreBitmap.getWidth()), (GENRE_HEIGHT / mEventGenreBitmap.getHeight()));
//            mEventGenreMatrix.postTranslate((LAYOUT_PADDING_LEFT + mTimeWidth + 4f), (LAYOUT_PADDING_TOP + TITLE_TIME_TEXT_LINESPACING + TITLE_TIME_TEXT_LINESPACING));
			mEventGenreMatrix.postTranslate((LAYOUT_PADDING_LEFT + mTimeWidth + 4f), mGenreMarginTop);
			if(mIsPassed){
				ColorMatrix cm = new ColorMatrix();
			    cm.setSaturation(0);
			    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
			    mEventGenrePaint.setColorFilter(f);
			}
			else{
				mEventGenrePaint.setColorFilter(null);
			}
			canvas.drawBitmap(mEventGenreBitmap, mEventGenreMatrix, mEventGenrePaint);
		}
		canvas.restore();
	}
//	
//	private void drawEventRecordStatus(Canvas canvas, AVMRecordStatus status){
//		canvas.save(Canvas.MATRIX_SAVE_FLAG);	
//		mEventRecordStatusBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.epg_icon_recording_status_period_failed);
//		mEventRecordStatusMatrix.setTranslate((getWidth() - mEventRecordStatusBitmap.getWidth()), (0f));
//		canvas.drawBitmap(mEventRecordStatusBitmap, mEventRecordStatusMatrix, mEventRecordStatusPaint);
//		canvas.restore();
//	}
	
//	private void drawEventGenre(Canvas canvas, int genreResId){
////		AVMLog.d(TAG, "drawEventGenre", "EPG Title = " + mEventTitle + ", Genre drawable id = " + genreResId);
//		canvas.save(Canvas.MATRIX_SAVE_FLAG);		
////		mEventGenreBitmap = getLocalBitmap(getContext(), genreResId);
//		if(null != mEventGenreBitmap){
//			mEventGenreMatrix.setScale((GENRE_WIDTH / mEventGenreBitmap.getWidth()), (GENRE_HEIGHT / mEventGenreBitmap.getHeight()));
//			mEventGenreMatrix.postTranslate((LAYOUT_PADDING_LEFT + TIME_WIDTH + 4f), (LAYOUT_PADDING_TOP + TITLE_TIME_TEXT_LINESPACING + TITLE_TIME_TEXT_LINESPACING));
//			if(mIsPassed){
//				ColorMatrix cm = new ColorMatrix();
//			    cm.setSaturation(0);
//			    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//			    mEventGenrePaint.setColorFilter(f);
//			}
//			else{
//				mEventGenrePaint.setColorFilter(null);
//			}
//			canvas.drawBitmap(mEventGenreBitmap, mEventGenreMatrix, mEventGenrePaint);
//		}
//		canvas.restore();
//	}
	
	private void drawEventRecordStatus(Canvas canvas, int statusResId){
		canvas.save(Canvas.MATRIX_SAVE_FLAG);	
		mEventRecordStatusBitmap = getLocalBitmap(getContext(), statusResId);
		if(null != mEventRecordStatusBitmap){
			mEventRecordStatusMatrix.setTranslate((getWidth() - mEventRecordStatusBitmap.getWidth()), (0f));
			canvas.drawBitmap(mEventRecordStatusBitmap, mEventRecordStatusMatrix, mEventRecordStatusPaint);
		}
		canvas.restore();
	}
	
//	private void drawEventRecordStatus(Canvas canvas, AVMScheduleStatus status){
//		canvas.save(Canvas.MATRIX_SAVE_FLAG);	
////		mEventRecordStatusBitmap = getLocalBitmap(getContext(), statusResId);
//		if(null != mEventRecordStatusBitmap){
//			mEventRecordStatusMatrix.setTranslate((getWidth() - mEventRecordStatusBitmap.getWidth()), (0f));
//			canvas.drawBitmap(mEventRecordStatusBitmap, mEventRecordStatusMatrix, mEventRecordStatusPaint);
//		}
//		canvas.restore();
//	}
	
	private void drawTitle(Canvas canvas, String title){
		canvas.save(Canvas.MATRIX_SAVE_FLAG);	
		if(mIsPassed){
			mTimeAndTitlePaint.setColor(PASSED_EVENT_COLOR);
		}
		else{
			mTimeAndTitlePaint.setColor(FUTURE_EVENT_COLOR);
		}
		mDecriptionFontHeight = 0;
		mDecriptionRealLine = 0;
		FontMetrics fm = mTimeAndTitlePaint.getFontMetrics();

		int istart = 0;
		int w = 0;
		int fontHeight = (int) (Math.ceil(fm.descent - fm.top) + 2);
//		float marginTop = (LAYOUT_PADDING_TOP + TITLE_TIME_TEXT_SIZE + TIME_HEIGHT + TITLE_TIME_TEXT_LINESPACING);
        float marginTop = mTitleMarginTop;
		int nextLine = 0;
		int count = title.length();
        float maxWidth = 0f;
		float[] widths = new float[1];
		char ch;
		for (int i = 0; i < count; i++) {
			ch = title.charAt(i);
			widths[0] = 0f;
			String str = String.valueOf(ch);
			mTimeAndTitlePaint.getTextWidths(str, widths);
            if(widths[0] > maxWidth){
                maxWidth = widths[0];
            }
			if (ch == '\n') {
				nextLine++;
				istart = i + 1;
				w = 0;
			}
			else{
				w += (int) Math.ceil(widths[0]);
				if (w > (getWidth() - (LAYOUT_PADDING_LEFT * 2))) {
					 canvas.drawText((String) (title.substring(istart, i)), (LAYOUT_PADDING_LEFT),
							 marginTop + fontHeight * nextLine, mTimeAndTitlePaint);
					 nextLine++;
					 istart = i;
					 i--;
					 w = 0;
				}
				else{
					if (i == (count - 1)) {
						canvas.drawText((String) (title.substring(istart, count)), (LAYOUT_PADDING_LEFT),
								marginTop + fontHeight * nextLine, mTimeAndTitlePaint);
						nextLine++;
					}
				}
			}
			
		}
		mDecriptionFontHeight = fontHeight;
		mDecriptionRealLine = nextLine;
        Log.d(TAG, "init title font size = " + mTimeAndTitlePaint.getTextSize() + " height = " + mDecriptionFontHeight + " font width = " + maxWidth);
//		canvas.drawText(title, (9f), (5f + TITLE_TIME_TEXT_SIZE + TIME_HEIGHT + TITLE_TIME_TEXT_LINESPACING), mTimeAndTitlePaint);
		canvas.restore();
	}
	
	private void drawTitle(Canvas canvas, Vector<String> title){
		canvas.save(Canvas.MATRIX_SAVE_FLAG);	
		if(mIsPassed){
			mTimeAndTitlePaint.setColor(PASSED_EVENT_COLOR);
		}
		else{
			mTimeAndTitlePaint.setColor(FUTURE_EVENT_COLOR);
		}
		
		float marginTop = (LAYOUT_PADDING_TOP + TITLE_TIME_TEXT_SIZE + TIME_HEIGHT + TITLE_TIME_TEXT_LINESPACING);
		for (int i = mCurrentLine, j = 0; i < mDecriptionRealLine; i++, j++) {
           if (j > mDecriptionLineNum) {
               break;
           }
           canvas.drawText((String) (title.elementAt(i)), (LAYOUT_PADDING_LEFT),
        		   marginTop + mDecriptionFontHeight * j, mTimeAndTitlePaint);
		}
		canvas.restore();
	}
	
	
	private void drawDecription(Canvas canvas, String decription){
		canvas.save(Canvas.MATRIX_SAVE_FLAG);	
		float titleHeight = TIME_HEIGHT + mDecriptionFontHeight * (mDecriptionRealLine - 1);
		FontMetrics fm = mDecriptionPaint.getFontMetrics();
		int istart = 0;
		int w = 0;
		int fontHeight = (int) (Math.ceil(fm.descent - fm.top) + 2);
//		float marginTop = (LAYOUT_PADDING_TOP + DECRIPTION_HEIGHT + TIME_HEIGHT + TITLE_TIME_TEXT_LINESPACING + titleHeight);
        float marginTop = (mDescriptionMarginTop + titleHeight);
		int nextLine = 0;
		int count = decription.length();
        float maxWidth = 0f;
		float[] widths = new float[1];
		char ch;
		for (int i = 0; i < count; i++) {
			ch = decription.charAt(i);
			widths[0] = 0f;
			String str = String.valueOf(ch);
			mDecriptionPaint.getTextWidths(str, widths);
            if(widths[0] > maxWidth){
                maxWidth = widths[0];
            }
			if (ch == '\n') {
				nextLine++;
				istart = i + 1;
				w = 0;
			}
			else{
				w += (int) Math.ceil(widths[0]);
				if (w > (getWidth() - (LAYOUT_PADDING_LEFT * 2))) {
					 canvas.drawText((String) (decription.substring(istart, i)), (LAYOUT_PADDING_LEFT),
							 marginTop + fontHeight * nextLine, mDecriptionPaint);
					 nextLine++;
					 istart = i;
					 i--;
					 w = 0;
				}
				else{
					if (i == (count - 1)) {
						canvas.drawText((String) (decription.substring(istart, count)), (LAYOUT_PADDING_LEFT),
								marginTop + fontHeight * nextLine, mDecriptionPaint);
						nextLine++;
					}
				}
			}
			
		}
        Log.d(TAG, "init description font size = " + mDecriptionPaint.getTextSize() + " height = " + fontHeight + " font width = " + maxWidth);
//		canvas.drawText(decription, (9f), (5f + DECRIPTION_HEIGHT + TIME_HEIGHT + titleHeight), mDecriptionPaint);
		canvas.restore();
	}
	
	private void drawDecription(Canvas canvas, Vector<String> decription){
		canvas.save(Canvas.MATRIX_SAVE_FLAG);	
		float titleHeight = TIME_HEIGHT + mDecriptionFontHeight * (mDecriptionRealLine - 1);
		for (int i = mCurrentLine, j = 0; i < mDecriptionRealLine; i++, j++) {
           if (j > mDecriptionLineNum) {
               break;
           }
           canvas.drawText((String) (decription.elementAt(i)), (LAYOUT_PADDING_LEFT),
        		   (LAYOUT_PADDING_TOP + DECRIPTION_HEIGHT + TIME_HEIGHT + titleHeight) + mDecriptionFontHeight * j, mDecriptionPaint);
	    }
		canvas.restore();
	}
	
	private int mCurrentLine;
	private int mDecriptionFontHeight;
	private int mDecriptionLineNum;
	private int mDecriptionRealLine;
//	private final int HEIGHT = 200;
	public Vector<String> GetTextIfon(String text, Vector<String> destination, Paint paint, float marginRight) {
		   Vector<String> mString = destination;
	       char ch;
	       int w = 0;
	       int istart = 0;
	       mCurrentLine = 0;
	       mDecriptionRealLine = 0;
	       FontMetrics fm = paint.getFontMetrics();
	       mDecriptionFontHeight = (int) (Math.ceil(fm.descent - fm.top) + 2);
	       mDecriptionLineNum = (int) (getHeight() / mDecriptionFontHeight);
	       
	       int count = text.length();
	       for (int i = 0; i < count; i++) {
	           ch = text.charAt(i);
	           float[] widths = new float[1];
	           String str = String.valueOf(ch);
	           paint.getTextWidths(str, widths);
	           if (ch == '\n') {
	        	   mDecriptionRealLine++;
	               mString.addElement(text.substring(istart, i));
	               istart = i + 1;
	               w = 0;
	           } else {
	               w += (int) Math.ceil(widths[0]);
	               if (w > (getWidth() - marginRight)) {
	            	   mDecriptionRealLine++;
	                   mString.addElement(text.substring(istart, i));
	                   istart = i;
	                   i--;
	                   w = 0;
	               } else {
	                   if (i == count - 1) {
	                	   mDecriptionRealLine++;
	                       mString.addElement(text.substring(istart,
	                               count));
	                   }
	               }
	           }
	       }
	       
	       return mString;
	   }
	
	private void drawBackground(Canvas canvas) {
//		if (mBackground == null) {
//			Log.w(TAG, "Background not created");
//			regenerateBackground();	
//		}
		mBackgroundPaint.setColor(mCurrentEventBackgroundColor);
//		canvas.drawBitmap(mBackground, 0.0f, 0.0f, mBackgroundPaint);
//		if(mIsCurrentEvent){
//			mBackgroundPaint.setColor(0xff551e1e);
//		}
//		else{
//			mBackgroundPaint.setColor(mCurrentEventBackgroundColor);
//		}
		
		drawEventRim(canvas);
	}
	
	private void drawEventRim(Canvas canvas){
		// first, draw the metallic body
//		canvas.drawRect(mEventRect, mBackgroundPaint);
		// now the outer rim
//		float height = mEventRect.bottom;
//		canvas.drawLine(0.0f, 0.0f, 0.0f, height, mLeftBorderPaint);
//		canvas.drawLine(0.0f, 0.0f, mEventRect.width(), 0.0f, mTopBorderPaint);
//		canvas.drawLine(mEventRect.width(), 0.0f, mEventRect.width(), height, mRightAndBottomBorderPaint);
//		canvas.drawLine(0.0f, height, mEventRect.width(), height, mRightAndBottomBorderPaint);
		if(null != mEPGEventInfo){
			if(mEPGEventInfo.isMiniEvent() && !mIsPassed){
				mTopBorderPaint.setColor(MINI_TOP_BORDER_COLOR);
			}
			else{
				mTopBorderPaint.setColor(NORMAL_TOP_BORDER_COLOR);
			}
		}
		else{
			if(mIsMiniEvent && !mIsPassed){
				
				mTopBorderPaint.setColor(MINI_TOP_BORDER_COLOR);
			}
			else{
				mTopBorderPaint.setColor(NORMAL_TOP_BORDER_COLOR);
			}
		}
		canvas.drawRect(0.0f, 0.0f, getWidth(), getHeight() - 1, mBackgroundPaint);
		canvas.drawLine(0.0f, 0.0f, 0.0f, getHeight(), mLeftBorderPaint);
		canvas.drawLine(0.0f, 0.0f, getWidth(), 0.0f, mTopBorderPaint);
		canvas.drawLine(getWidth() - 1, 0.0f, getWidth() - 1, getHeight(), mRightAndBottomBorderPaint);
		canvas.drawLine(0.0f, getHeight() - 1, getWidth(), getHeight() - 1, mRightAndBottomBorderPaint);
	}
	
	private Bitmap mBackground;
	
	
//	private void regenerateBackground() {
//		// free the old bitmap
//		if (mBackground != null) {
//			mBackground.recycle();
////			System.gc();
//		}
//		
//		int height = getHeight() - 1;
//		mBackground = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_4444);
//		Canvas backgroundCanvas = new Canvas(mBackground);
//		float scaleW = (float) getWidth();		
//		float scaleH = (float) height;
//		backgroundCanvas.scale(scaleW, scaleH);
//		drawEventRim(backgroundCanvas);
//	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//		Log.d(TAG, "[onSizeChanged] Size changed to " + w + "x" + h);
		
//		regenerateBackground();
	}
	
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
//		Log.d(TAG, "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
//		Log.d(TAG, "Height spec: " + MeasureSpec.toString(heightMeasureSpec));
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		int chosenWidth = chooseDimension(widthMode, widthSize);
		int chosenHeight = chooseDimension(heightMode, heightSize);
		
//		int chosenDimension = Math.min(chosenWidth, chosenHeight);
		setMeasuredDimension(chosenWidth, chosenHeight);
	}
	
	private int chooseDimension(int mode, int size) {
		if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
			return size;
		} else { // (mode == MeasureSpec.UNSPECIFIED)
			return getPreferredSize();
		} 
	}
	
	// in case there is no size specified
	private int getPreferredSize() {
		return 300;
	}
}
