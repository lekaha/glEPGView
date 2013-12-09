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

        if(hasGLES20()){
            setContentView(R.layout.activity_main);
        }

        EPGEventView epg = (EPGEventView)findViewById(R.id.view2);
        epg.bindData(false, false, "00", "にっぽん再発見！瀬戸内物語　私のとっておきの１枚　写真募集「山口」",
                "「にっぽん再発見　瀬戸内物語」私のとっておきの一枚に投稿された写真を紹介する１分ミニ番組。今回は、山口県。更なる投稿も呼びかける。",
                R.drawable.epg_icon_recording_status_period,
                R.drawable.epg_dropdown_menu_genre_icon_0_all, true, null);
        epg.setVisibility(View.INVISIBLE);
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
