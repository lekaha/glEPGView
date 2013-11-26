package com.john.glepgviewer;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.john.glepgviewer.util.FontHandle;

public class MainActivity extends Activity {

    private TestGLSurfaceView mTestGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        FontHandle.getInstance().setupFont(this, FontHandle.FONT_COUNTRY_ID.JP, "aribfont", 3, 1);


//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
//        Matrix m = new Matrix();
//        m.postScale(2,2);
//        Bitmap bitmap = Bitmap.createBitmap( 100, 100, Bitmap.Config.ARGB_4444);  // Create Bitmap
//        Canvas canvas = new Canvas( bitmap );           // Create Canvas for Rendering to Bitmap
//        bitmap.eraseColor(Color.parseColor("#FF1A1A1A"));                // Set Transparent Background (ARGB)
//        canvas.setMatrix(m);
//        Paint paint = new Paint();                      // Create Android Paint Instance
//
//        paint.setAntiAlias( true );                     // Enable Anti Alias
//        paint.setTextSize( 16 );                      // Set Text Size
//        paint.setColor( Color.WHITE);                     // Set ARGB (White, Opaque)
//        paint.setTypeface(Typeface.DEFAULT );
//        paint.clearShadowLayer();
//        canvas.drawText( "にっぽん再発見！瀬戸内物語", 5, 22, paint );                               // Draw Character
//        Log.d("MainActivity", "Density: " + bitmap.getDensity());

        if(hasGLES20()){
//            ImageView iv = new ImageView(this);
//            iv.setImageBitmap(bitmap);
//            setContentView(iv, new ViewGroup.LayoutParams(100, 100));
//            RelativeLayout fl = (RelativeLayout) findViewById(R.id.container);
//            mTestGLSurfaceView = new TestGLSurfaceView(this);
//            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//            rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
//            rlp.addRule(RelativeLayout.CENTER_VERTICAL);
//            fl.addView(mTestGLSurfaceView);
//            setContentView(mTestGLSurfaceView);
            setContentView(R.layout.activity_main);
        }
        else{
            setContentView(R.layout.activity_main);
        }

        EPGEventView epg = (EPGEventView)findViewById(R.id.view2);
        epg.bindData(false, false, "00", "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
                "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
                R.drawable.epg_icon_recording_status_period,
                R.drawable.epg_dropdown_menu_genre_icon_0_all, true, null);
    }

    private boolean hasGLES20() {
        ActivityManager am = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x20000;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
