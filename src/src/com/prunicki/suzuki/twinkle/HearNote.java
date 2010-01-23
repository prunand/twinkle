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

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HearNote extends Activity {
    
    private Button mNextButton;
    private Button mReplayButton;
    private Button mBackButton;
    
    private Button mABtn;
    private Button mBBtn;
    private Button mCBtn;
    private Button mDBtn;
    private Button mEBtn;
    private Button mFBtn;
    private Button mGBtn;
    
    private Player mPlayer;
    private Random mRandom;
    private int mNote;
    private boolean mFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hearnote);
        
        mRandom = new Random();
        mNote = nextNote();
        mFirstRun = true;
        
        mReplayButton = (Button) findViewById(R.id.HearNoteReplay);
        mNextButton = (Button) findViewById(R.id.HearNoteNext);
        mBackButton = (Button) findViewById(R.id.HearNoteBack);
        
        mABtn = (Button) findViewById(R.id.HearNoteA);
        mBBtn = (Button) findViewById(R.id.HearNoteB);
        mCBtn = (Button) findViewById(R.id.HearNoteC);
        mDBtn = (Button) findViewById(R.id.HearNoteD);
        mEBtn = (Button) findViewById(R.id.HearNoteE);
        mFBtn = (Button) findViewById(R.id.HearNoteF);
        mGBtn = (Button) findViewById(R.id.HearNoteG);
        
        mReplayButton.setOnClickListener(mReplayListener);
        mNextButton.setOnClickListener(mNextListener);
        mBackButton.setOnClickListener(mBackListener);
        
        mABtn.setOnClickListener(new NoteButtonListener(Player.A_NOTE));
        mBBtn.setOnClickListener(new NoteButtonListener(Player.B_NOTE));
        mCBtn.setOnClickListener(new NoteButtonListener(Player.C_NOTE));
        mDBtn.setOnClickListener(new NoteButtonListener(Player.D_NOTE));
        mEBtn.setOnClickListener(new NoteButtonListener(Player.E_NOTE));
        mFBtn.setOnClickListener(new NoteButtonListener(Player.F_NOTE));
        mGBtn.setOnClickListener(new NoteButtonListener(Player.G_NOTE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        mPlayer = ((SuzukiApplication) getApplication()).getPlayer();
        
        if (mFirstRun) {
            mPlayer.playNote(mNote);
        }
        
        mFirstRun = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        mPlayer.pause();
        mPlayer = null;
    }
    
    private int nextNote() {
        return mRandom.nextInt(7);
    }
    
    private OnClickListener mReplayListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            mPlayer.playNote(mNote);
        }
    };
    
    private OnClickListener mNextListener = new OnClickListener() {
        
        @Override
        public void onClick(View arg0) {
            mNote = nextNote();
            mPlayer.playNote(mNote);
        }
    };
    
    private OnClickListener mBackListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            finish();
        }
    };
    
    private class NoteButtonListener implements OnClickListener {
        
        private int mButtonNote;
        
        private NoteButtonListener(int note) {
            mButtonNote = note;
        }

        @Override
        public void onClick(View v) {
            if (mButtonNote == mNote) {
                SuccessDialog dialog = new SuccessDialog(HearNote.this);
                dialog.show();
            } else {
                mPlayer.playNote(mButtonNote);
            }
        }
    }
}
