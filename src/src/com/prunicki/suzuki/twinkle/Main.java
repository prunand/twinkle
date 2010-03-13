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
import android.widget.ToggleButton;

public class Main extends TwinkleActivity {
    static final String TAG = "SuzukiTwinkle";
    
    private Button mPlayButton;
    private Button mPracticeButton;
    private Button mSwitchPlayerButton;
    private ToggleButton mDifficultyButton;
    ScoreDAO mDAO;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mPlayButton = (Button) findViewById(R.id.MainPlay);
        mSwitchPlayerButton = (Button) findViewById(R.id.SwitchPlayer);
        mPracticeButton = (Button) findViewById(R.id.MainPractice);
        mDifficultyButton = (ToggleButton) findViewById(R.id.MainDifficultyLevel);
        
        mPlayButton.setOnClickListener(mPlayListener);
        mSwitchPlayerButton.setOnClickListener(mSwitchPlayerListener);
        mPracticeButton.setOnClickListener(mPracticeListener);
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
        Dialog d = null;
        LinearLayout layout = null;
        switch(item.getItemId()) {
            case R.id.about:
                d  = new Dialog(this);
                d.setContentView(R.layout.about);
                d.setCanceledOnTouchOutside(true);
                
                layout  = (LinearLayout) d.findViewById(R.id.AboutLayout);
                layout.setOnClickListener(new AboutListener(d));
                
                String appName = Main.this.getResources().getString(R.string.app_name);
                d.setTitle("About " + appName);
                d.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private OnClickListener mPlayListener = new OnClickListener() {
        public void onClick(View v) {
            boolean hard = mDifficultyButton.isChecked();
            int level = hard ? GameScreen.DIFFICULTY_LEVEL_HARD : GameScreen.DIFFICULTY_LEVEL_EASY;
            
            Intent intent = new Intent(Main.this, GameScreen.class);
            intent.putExtra(GameScreen.DIFFICULTY_LEVEL_KEY, level);
            startActivity(intent);
        }
    };
    
    private OnClickListener mSwitchPlayerListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ChangePlayerDialog dlg = new ChangePlayerDialog(Main.this);
            dlg.show();
        }
    };
    
    private OnClickListener mPracticeListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            SuccessDialog dlg = new SuccessDialog(Main.this, 22);
            dlg.show();
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