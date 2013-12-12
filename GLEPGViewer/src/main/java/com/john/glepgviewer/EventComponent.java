package com.john.glepgviewer;

/**
 * Created by john on 11/12/13.
 */
public abstract class EventComponent {
    protected final String TAG = getClass().getSimpleName();

    protected final static int X = 0, Y = 1;
    protected final static String MIN_COLOR = "#69a5d7";
    protected final static String BG_COLOR = "#262626";
    protected final static int FONT_PADDING = 2;

    protected final static float LAYOUT_PADDING_TOP = 5f;
    protected final static float LAYOUT_PADDING_LEFT = 9f;
    protected final static float LAYOUT_PADDING_RIGHT = 9f;
    protected final static float LAYOUT_PADDING_BOTTOM = 9f;

    protected final static float MIN_EVENT_HEIGHT = 15f;

    /* Need to initialize */
    protected int DIMENSION;
    protected int STRIDE;
    protected float[] VERTEX_DATA;
    protected VertexArray vertexArray;
    protected float[] projectionMatrix;

    protected float mHeight;
    protected float mWidth;
    protected float pstX;
    protected float pstY;
//    protected Context mContext

    public EventComponent(){
//        Log.d(TAG, "ctor: EventComponent");
        init();
    }

//    public EventComponent(String title, Typeface typeface){}
//    public EventComponent(String title, Typeface typeface, float textSize, float width, float height, float[] projectionMatrix, float upperBound, float lowBound){}
//    public EventComponent(Context context, Typeface typeface, float[] projectionMatrix){}

    public abstract float getCenterVerticeYPoint();
    public abstract float getTopVerticeYPoint();
    public abstract float getBottomVerticeYPoint();
    public float getLeftVerticeXPoint(){return 0f;}
    public float getRightVerticeXPoint(){return 0f;}

    public void set(float px, float py){
//        for(int i = 0; i<(VERTEX_DATA.length/DIMENSION); i++){
//            VERTEX_DATA[i * DIMENSION + X] += px;
//            VERTEX_DATA[i * DIMENSION + Y] += (-py);
//        }
//        vertexArray.commit();
        pstX = px;
        pstY = py;
    }

    public void move(float x, float y){
        for(int i = 0; i<(VERTEX_DATA.length/DIMENSION); i++){
            VERTEX_DATA[i * DIMENSION + X] += x;
            VERTEX_DATA[i * DIMENSION + Y] += (-y);
        }
        vertexArray.commit();
    }

    public abstract void init();
    public abstract void bindData();
    public abstract void draw();

    public class EventRec{
        public float getTopBound(){
            return getTopVerticeYPoint();
        }

        public float getRightBound(){
            return getTopVerticeYPoint();
        }

        public float getBottomBound(){
            return getBottomVerticeYPoint();
        }

        public float getLeftBound(){
            return getTopVerticeYPoint();
        }
    }
}
