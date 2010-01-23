/*
 * Copyright 2010 Andrew Prunicki
 * 
 * This file is part of Twinkle.
 * 
 * Twinkle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Twinkle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Twinkle.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.prunicki.suzuki.twinkle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Main extends Activity {
    static final String TAG = "SuzukiTwinkle";
    
    private Button mNameButton;
    private Button mPitchButton;
    private Button mRhythmButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mPitchButton = (Button) findViewById(R.id.Pitch);
        mRhythmButton = (Button) findViewById(R.id.Rhythm);
        mNameButton = (Button) findViewById(R.id.Name);
        
        mPitchButton.setOnClickListener(mPitchListener);
        mRhythmButton.setOnClickListener(mRhythmListener);
        mNameButton.setOnClickListener(mNameListener);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.about:
                Dialog d = new Dialog(this);
                d.setContentView(R.layout.about);
                d.setCanceledOnTouchOutside(true);
                
                LinearLayout layout = (LinearLayout) d.findViewById(R.id.AboutLayout);
                layout.setOnClickListener(new AboutListener(d));
                
                String appName = Main.this.getResources().getString(R.string.app_name);
                d.setTitle("About " + appName);
                d.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private OnClickListener mPitchListener = new OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(Main.this, Pitch.class);
            
            Main.this.startActivity(intent);
        }
    };

    private OnClickListener mRhythmListener = new OnClickListener() {
        public void onClick(View v) {
            SightSoundDialog dialog = new SightSoundDialog(Main.this, SeeRhythm.class, HearRhythm.class);
            dialog.show();
        }
    };

    private OnClickListener mNameListener = new OnClickListener() {
        public void onClick(View v) {
            SightSoundDialog dialog = new SightSoundDialog(Main.this, SeeNote.class, HearNote.class);
            dialog.show();
        }
    };

    private class AboutListener implements OnClickListener {
        
        private Dialog mDialog;
        
        public AboutListener(Dialog dialog) {
            mDialog = dialog;
        }
        
        public void onClick(View v) {
            mDialog.cancel();
        }
    };
}