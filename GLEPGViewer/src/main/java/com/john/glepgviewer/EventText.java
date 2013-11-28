package com.john.glepgviewer;

import android.content.Context;

/**
 * Created by john on 11/12/13.
 */
public abstract class EventText extends EventComponent {
    protected final static int POSITION_COMPONENT_COUNT = 2;
    protected final static int TEXTURE_COMPONENT_COUNT = 2;
    protected final static int U = 2, V = 3;
    protected final static String BIG_TEXT_COLOR = "#ffffffff";
    protected final static String SMALL_TEXT_COLOR = "#ff828282";
    protected final static String PASSED_TEXT_COLOR = "#ff828282";

    protected GLTextView mTextView;
    protected Context mContext;

    protected abstract void init(boolean nothing);

    @Override
    public void init(){
        DIMENSION = (POSITION_COMPONENT_COUNT + TEXTURE_COMPONENT_COUNT);
        STRIDE = DIMENSION * Constants.BYTES_PER_FLOAT;
        init(false);
    }

    @Override
    public float getCenterVerticeYPoint() {
        if(null != VERTEX_DATA){
            return VERTEX_DATA[0 * DIMENSION + Y];
        }
        return 0;
    }

    @Override
    public float getTopVerticeYPoint() {
        if(null != VERTEX_DATA){
            return VERTEX_DATA[3 * DIMENSION + Y];
        }
        return 0;
    }

    @Override
    public float getBottomVerticeYPoint() {
        if(null != VERTEX_DATA){
            return VERTEX_DATA[1 * DIMENSION + Y];
        }
        return 0;
    }
}
