package com.john.glepgviewer;

/**
 * Created by john on 12/12/13.
 */
public class EventInfo {
    private long mId;
    private float mX;
    private float mY;
    private float mHeight;
    private int mMerge;
    private String mMin;
    private String mTitle;
    private String mDescription;
    private boolean mIsFavoriteGenre;
    private int mGenreResId;
    private int mRecordResId;

    public EventInfo(
            long id,
            float x, float y, int merge, float height,
            String min, String title, String description,
            boolean isFavor, int genre, int record
    ){
        mId = id;
        mX = x;
        mY = y;
        mHeight = height;
        mMerge = merge;
        mMin = min;
        mTitle = title;
        mDescription = description;
        mIsFavoriteGenre = isFavor;
        mGenreResId = genre;
        mRecordResId = record;
    }

    public float getX() {
        return mX;
    }

    public float getY() {
        return mY;
    }

    public int getMerge() {
        return mMerge;
    }

    public float getWidth() { return mMerge * 145f; }

    public float getHeight() {
        return mHeight;
    }

    public String getMin() {
        return mMin;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean isFavoriteGenre() {
        return mIsFavoriteGenre;
    }

    public int getGenreResId() {
        return mGenreResId;
    }

    public int getmRecordResId() {
        return mRecordResId;
    }

    @Override
    public boolean equals(Object o) {
//        Log.d("EventInfo", "[equals]");

        if (this == o) return true;
        if (!(o instanceof EventInfo)) return false;

        EventInfo eventInfo = (EventInfo) o;

        if (mGenreResId != eventInfo.mGenreResId) return false;
        if (Float.compare(eventInfo.mHeight, mHeight) != 0) return false;
        if (mId != eventInfo.mId) return false;
        if (mIsFavoriteGenre != eventInfo.mIsFavoriteGenre) return false;
        if (mMerge != eventInfo.mMerge) return false;
        if (mRecordResId != eventInfo.mRecordResId) return false;
        if (Float.compare(eventInfo.mX, mX) != 0) return false;
        if (Float.compare(eventInfo.mY, mY) != 0) return false;
        if (mDescription != null ? !mDescription.equals(eventInfo.mDescription) : eventInfo.mDescription != null)
            return false;
        if (mMin != null ? !mMin.equals(eventInfo.mMin) : eventInfo.mMin != null) return false;
        if (mTitle != null ? !mTitle.equals(eventInfo.mTitle) : eventInfo.mTitle != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
//        Log.d("EventInfo", "[hashCode]");
        int result = (int) (mId ^ (mId >>> 32));
//        result = 31 * result + (mX != +0.0f ? Float.floatToIntBits(mX) : 0);
//        result = 31 * result + (mY != +0.0f ? Float.floatToIntBits(mY) : 0);
//        result = 31 * result + (mHeight != +0.0f ? Float.floatToIntBits(mHeight) : 0);
//        result = 31 * result + mMerge;
//        result = 31 * result + (mMin != null ? mMin.hashCode() : 0);
//        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
//        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
//        result = 31 * result + (mIsFavoriteGenre ? 1 : 0);
//        result = 31 * result + mGenreResId;
//        result = 31 * result + mRecordResId;
        return result;
    }
}
