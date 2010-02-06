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

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Pitch extends GameActivity {
    private View[] mButtons;
    private GameButtonListener[] mButtonListeners;
    
    private View mNextButton;
    private View mReplayButton;
    
    int[] mNotes;
    boolean mSame;
    boolean mHigher;
    boolean mFirstRun;
    
    public Pitch() {
        super(7);
        mFirstRun = true;
        mButtons = new View[3];
        mButtonListeners = new GameButtonListener[3];
        mNotes = new int[2];
        generateNotes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pitch);
        
        View[] buttons = mButtons;
        GameButtonListener[] listeners = mButtonListeners;

        int x = 0;
        buttons[x] = findViewById(R.id.Higher);
        listeners[x++] = new HigherListener();
        buttons[x] = findViewById(R.id.Lower);
        listeners[x++] = new LowerListener();
        buttons[x] = findViewById(R.id.Same);
        listeners[x++] = new SameListener();
        
        setListenersIntoButtons(buttons, listeners);
        
        mNextButton = findViewById(R.id.PitchNext);
        mNextButton.setOnClickListener(mNextListener);
        
        mReplayButton = findViewById(R.id.PitchReplay);
        mReplayButton.setOnClickListener(mReplayListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        GameButtonListener.setPlayerIntoListeners(mButtonListeners, mPlayer);
        
        if (mFirstRun) {
            mPlayer.playNote(mNotes);
        }
        
        mFirstRun = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        GameButtonListener.setPlayerIntoListeners(mButtonListeners, null);
    }
    
    private void generateNotes() {
        mNotes[0] = nextRandom(mNotes[0]);
        mNotes[1] = nextRandom(mNotes[1]);
        
        mSame = false;
        if (mNotes[1] < mNotes[0]) {
            mHigher = true;
        } else if (mNotes[0] == mNotes[1]) {
            mSame = true;
        } else {
            mHigher = false;
        }
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
    
    private class HigherListener extends GameButtonListener {
        
        private HigherListener() {
            super(Pitch.this);
        }

        @Override
        public boolean checkSuccess() {
            return !mSame && mHigher;
        }
    }
    
    private class LowerListener extends GameButtonListener {
        
        private LowerListener() {
            super(Pitch.this);
        }

        @Override
        public boolean checkSuccess() {
            return !mSame && !mHigher;
        }
    }
    
    private class SameListener extends GameButtonListener {
        
        private SameListener() {
            super(Pitch.this);
        }

        @Override
        public boolean checkSuccess() {
            return mSame;
        }
    }
}
