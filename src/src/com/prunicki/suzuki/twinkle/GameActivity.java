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

import android.view.View;
import android.view.View.OnClickListener;

public abstract class GameActivity extends TwinkleActivity {
    private final Random mRandom;
    private final int mMaxRandom;
    protected Player mPlayer;
    
    public GameActivity(int maxRandom) {
        mMaxRandom = maxRandom;
        mRandom = new Random();
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        mPlayer = ((SuzukiApplication) getApplication()).getPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        mPlayer.pause();
        mPlayer = null;
    }
    
    protected int nextRandom(int prev) {
        Random random = mRandom;
        
        while (true) {
            int nextInt = random.nextInt(mMaxRandom);
            if (prev != nextInt) {
                return nextInt;
            }
        }
    }

    protected static void setListenersIntoButtons(View[] buttons, OnClickListener[] listeners) {
        int count = buttons.length;
        for (int i = 0; i < count; i++) {
            buttons[i].setOnClickListener(listeners[i]);
        }
    }
}
