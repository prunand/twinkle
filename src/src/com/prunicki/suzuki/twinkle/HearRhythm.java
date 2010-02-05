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

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HearRhythm extends TwinkleActivity {
    
    private Button mMissStopStop;
    private Button mMissAlligator;
    private Button mHearDownPony;
    private Button mHearIceCream;
    
    private View mReplayButton;
    private View mNextButton;
    
    private Player mPlayer;
    private Random mRandom;
    private int mRhythm;
    private boolean mFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hearrhythm);
        
        mRandom = new Random();
        mRhythm = nextRhythm();
        mFirstRun = true;
        
        mReplayButton = (View) findViewById(R.id.HearRhythmReplay);
        mNextButton = (View) findViewById(R.id.HearRhythmNext);

        mMissStopStop = (Button) findViewById(R.id.HearMissStopStop);
        mMissAlligator = (Button) findViewById(R.id.HearMissAlligator);
        mHearDownPony = (Button) findViewById(R.id.HearDownPony);
        mHearIceCream = (Button) findViewById(R.id.HearIceCream);
        
        mReplayButton.setOnClickListener(mReplayListener);
        mNextButton.setOnClickListener(mNextListener);
        
        mMissStopStop.setOnClickListener(new RhythmListener(Player.MISSISSIPPI_STOP_STOP_RHYTHM));
        mMissAlligator.setOnClickListener(new RhythmListener(Player.MISSISSIPPI_ALLIGATOR_RHYTHM));
        mHearDownPony.setOnClickListener(new RhythmListener(Player.DOWN_PONY_UP_PONY_RHYTHM));
        mHearIceCream.setOnClickListener(new RhythmListener(Player.ICE_CREAM_SH_CONE_RHYTHM));
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        mPlayer = ((SuzukiApplication) getApplication()).getPlayer();
        
        if (mFirstRun) {
            mPlayer.playRhythm(mRhythm);
        }
        
        mFirstRun = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        mPlayer.pause();
        mPlayer = null;
    }
    
    private int nextRhythm() {
        return mRandom.nextInt(4);
    }
    
    private OnClickListener mReplayListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            mPlayer.playRhythm(mRhythm);
        }
    };
    
    private OnClickListener mNextListener = new OnClickListener() {
        
        @Override
        public void onClick(View arg0) {
            mRhythm = nextRhythm();
            mPlayer.playRhythm(mRhythm);
        }
    };
    
    private class RhythmListener implements OnClickListener {
        
        private int mSelectedRhythm;
        
        private RhythmListener (int rhythm) {
            mSelectedRhythm = rhythm;
        }

        @Override
        public void onClick(View v) {
            if (mRhythm == mSelectedRhythm) {
                SuccessDialog dialog = new SuccessDialog(HearRhythm.this);
                dialog.show();
            } else {
                mPlayer.playRhythm(mSelectedRhythm);
            }
        }
    }
}
