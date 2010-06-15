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
package com.prunicki.twinkle.game;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.prunicki.twinkle.GameButtonListener;
import com.prunicki.twinkle.GameRoundCallback;
import com.prunicki.twinkle.R;
import com.prunicki.twinkle.SoundPlayer;

public class HearRhythm extends GameRound {
    int mRhythm;
    
    public HearRhythm(ViewGroup viewGroup, GameRoundCallback callback) {
        super(R.layout.hearrhythm, viewGroup, true, 4, callback);
        
        mRhythm = -1;
        prepareNext();
    }

    @Override
    public void onCreate(Activity activity) {
        super.onCreate(activity);
        
        View[] buttons = mButtons;
        GameButtonListener[] listeners = mButtonListeners;
        GameRoundCallback callback = mCallback;
        
        int x = 0;
        buttons[x] = activity.findViewById(R.id.HearMissStopStop);
        listeners[x++] = new RhythmListener(SoundPlayer.MISSISSIPPI_STOP_STOP_RHYTHM, callback);
        buttons[x] = activity.findViewById(R.id.HearMissAlligator);
        listeners[x++] = new RhythmListener(SoundPlayer.MISSISSIPPI_ALLIGATOR_RHYTHM, callback);
        buttons[x] = activity.findViewById(R.id.HearDownPony);
        listeners[x++] = new RhythmListener(SoundPlayer.DOWN_PONY_UP_PONY_RHYTHM, callback);
        buttons[x] = activity.findViewById(R.id.HearIceCream);
        listeners[x++] = new RhythmListener(SoundPlayer.ICE_CREAM_SH_CONE_RHYTHM, callback);
        
        setListenersIntoButtons(buttons, listeners);
    }

    @Override
    protected void playNotes(SoundPlayer.PlayerCallback soundPlayerCallback) {
        mApp.getSoundPlayer().playRhythm(mRhythm, soundPlayerCallback);
    }

    @Override
    protected void prepareNext() {
        mRhythm = nextRandom(mRhythm);
    }
    
    private class RhythmListener extends GameButtonListener {
        private int mRhythm;
        
        private RhythmListener (int rhythm, GameRoundCallback callback) {
            super(callback);
            mRhythm = rhythm;
        }

        @Override
        protected boolean checkSuccess() {
            return mRhythm == HearRhythm.this.mRhythm;
        }
    }
}
