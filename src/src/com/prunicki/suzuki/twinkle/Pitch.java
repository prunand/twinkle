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
import android.view.View;

public class Pitch extends GameRound {
    int[] mNotes;
    boolean mSame;
    boolean mHigher;

    public Pitch(GameRoundCallback callback) {
        super(R.layout.pitch, true, 3, callback);
        
        mNotes = new int[2];
        generateNotes();
    }

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        
        View[] buttons = mButtons;
        GameButtonListener[] listeners = mButtonListeners;
        GameRoundCallback callback = mCallback;

        int x = 0;
        buttons[x] = activity.findViewById(R.id.Higher);
        listeners[x++] = new HigherListener(callback);
        buttons[x] = activity.findViewById(R.id.Lower);
        listeners[x++] = new LowerListener(callback);
        buttons[x] = activity.findViewById(R.id.Same);
        listeners[x++] = new SameListener(callback);
        
        setListenersIntoButtons(buttons, listeners);
    }
    
    @Override
    protected void playNotes(Player.PlayerCallback playerCallback) {
        mPlayer.playNote(mNotes, playerCallback);
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
    
    private class HigherListener extends GameButtonListener {
        private boolean pressed;
        
        private HigherListener(GameRoundCallback callback) {
            super(callback);
        }

        @Override
        public boolean checkSuccess() {
            boolean success = !mSame && mHigher;
            if (!success && !pressed) {
                pressed = true;
            }
            return success;
        }
    }
    
    private class LowerListener extends GameButtonListener {
        private boolean pressed;
        
        private LowerListener(GameRoundCallback callback) {
            super(callback);
        }

        @Override
        public boolean checkSuccess() {
            boolean success = !mSame && !mHigher;
            if (!success && !pressed) {
                pressed = true;
            }
            return success;
        }
    }
    
    private class SameListener extends GameButtonListener {
        private boolean pressed;
        
        private SameListener(GameRoundCallback callback) {
            super(callback);
        }

        @Override
        public boolean checkSuccess() {
            if (!mSame && !pressed) {
                pressed = true;
            }
            return mSame;
        }
    }
}
