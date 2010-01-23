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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SeeRhythm extends Activity {
    
    private ImageView mRhythmImage;
    
    private Button mMissStopStop;
    private Button mMissAlligator;
    private Button mHearDownPony;
    private Button mHearIceCream;
    
    private Button mNextButton;
    private Button mBackButton;
    
    private Random mRandom;
    private int mRhythm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seerhythm);
        
        mRandom = new Random();
        nextRhythm();
        
        mRhythmImage = (ImageView) findViewById(R.id.RhythmImage);
        
        mNextButton = (Button) findViewById(R.id.SeeRhythmNext);
        mBackButton = (Button) findViewById(R.id.SeeRhythmBack);

        mMissStopStop = (Button) findViewById(R.id.SeeMissStopStop);
        mMissAlligator = (Button) findViewById(R.id.SeeMissAlligator);
        mHearDownPony = (Button) findViewById(R.id.SeeDownPony);
        mHearIceCream = (Button) findViewById(R.id.SeeIceCream);
        
        mNextButton.setOnClickListener(mNextListener);
        mBackButton.setOnClickListener(mBackListener);
        
        mMissStopStop.setOnClickListener(new RhythmListener(Player.MISSISSIPPI_STOP_STOP_RHYTHM));
        mMissAlligator.setOnClickListener(new RhythmListener(Player.MISSISSIPPI_ALLIGATOR_RHYTHM));
        mHearDownPony.setOnClickListener(new RhythmListener(Player.DOWN_PONY_UP_PONY_RHYTHM));
        mHearIceCream.setOnClickListener(new RhythmListener(Player.ICE_CREAM_SH_CONE_RHYTHM));
        
        placeImage();
    }
    
    private void placeImage() {
        switch (mRhythm) {
            case Player.MISSISSIPPI_STOP_STOP_RHYTHM:
                Log.d(Main.TAG, "placing mss");
                mRhythmImage.setImageResource(R.drawable.miss_stop_stop);
                break;
            case Player.MISSISSIPPI_ALLIGATOR_RHYTHM:
                Log.d(Main.TAG, "placing ma");
                mRhythmImage.setImageResource(R.drawable.miss_alligator);
                break;
            case Player.DOWN_PONY_UP_PONY_RHYTHM:
                Log.d(Main.TAG, "placing dpup");
                mRhythmImage.setImageResource(R.drawable.down_pony_up_pony);
                break;
            case Player.ICE_CREAM_SH_CONE_RHYTHM:
                Log.d(Main.TAG, "placing icsc");
                mRhythmImage.setImageResource(R.drawable.ice_cream_sh_cone);
                break;
        }
    }

    private void nextRhythm() {
        boolean done = false;
        while (!done) {
            int nextInt = mRandom.nextInt(4);
            if (mRhythm != nextInt) {
                mRhythm = nextInt;
                done = true;
            }
        }
    }
    
    private OnClickListener mNextListener = new OnClickListener() {
        
        @Override
        public void onClick(View arg0) {
            Log.d(Main.TAG, "Rhythm was " + mRhythm);
            nextRhythm();
            Log.d(Main.TAG, "Rhythm is " + mRhythm);
            placeImage();
        }
    };
    
    private OnClickListener mBackListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
           finish();
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
                SuccessDialog dialog = new SuccessDialog(SeeRhythm.this);
                dialog.show();
            }
        }
    }
}
