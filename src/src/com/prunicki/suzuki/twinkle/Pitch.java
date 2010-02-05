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

public class Pitch extends Activity {
    
    private View mHigherButton;
    private View mLowerButton;
    private View mSameButton;
    
    private View mReplayButton;
    private View mNextButton;
    private Player mPlayer;
    private Random mRandom;
    private int[] mNotes;
    private boolean mSame;
    private boolean mHigher;
    private boolean mFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pitch);
        
        mRandom = new Random();
        mNotes = new int[2];
        generateNotes();
        mFirstRun = true;
        
        mReplayButton = (View) findViewById(R.id.PitchReplay);
        mNextButton = (View) findViewById(R.id.PitchNext);

        mHigherButton = (View) findViewById(R.id.Higher);
        mLowerButton = (View) findViewById(R.id.Lower);
        mSameButton = (View) findViewById(R.id.Same);
        
        mReplayButton.setOnClickListener(mReplayListener);
        mNextButton.setOnClickListener(mNextListener);
        
        mHigherButton.setOnClickListener(mHigherListener);
        mLowerButton.setOnClickListener(mLowerListener);
        mSameButton.setOnClickListener(mSameListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        mPlayer = ((SuzukiApplication) getApplication()).getPlayer();
        
        if (mFirstRun) {
            mPlayer.playNote(mNotes);
        }
        
        mFirstRun = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        mPlayer.pause();
        mPlayer = null;
    }
    
    private void generateNotes() {
        mNotes[0] = nextNote();
        mNotes[1] = nextNote();
        
        mSame = false;
        if (mNotes[1] < mNotes[0]) {
            mHigher = true;
        } else if (mNotes[0] == mNotes[1]) {
            mSame = true;
        } else {
            mHigher = false;
        }
    }
    
    private int nextNote() {
        return mRandom.nextInt(7);
    }
    
    private OnClickListener mReplayListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            mPlayer.playNote(mNotes);
        }
    };
    
    private OnClickListener mNextListener = new OnClickListener() {
        
        @Override
        public void onClick(View arg0) {
            generateNotes();
            mPlayer.playNote(mNotes);
        }
    };

    private OnClickListener mHigherListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!mSame && mHigher) {
                SuccessDialog dialog = new SuccessDialog(Pitch.this);
                dialog.show();
            } else {
                mPlayer.playThump();
            }
        }
    };
    
    private OnClickListener mLowerListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!mSame && !mHigher) {
                SuccessDialog dialog = new SuccessDialog(Pitch.this);
                dialog.show();
            } else {
                mPlayer.playThump();
            }
        }
    };
    
    private OnClickListener mSameListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mSame) {
                SuccessDialog dialog = new SuccessDialog(Pitch.this);
                dialog.show();
            } else {
                mPlayer.playThump();
            }
        }
    };
}
