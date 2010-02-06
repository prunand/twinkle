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

public class SeeRhythm extends GameActivity {
    private RhythmView mStaffView;
    private View[] mButtons;
    private RhythmListener[] mButtonListeners;
    
    private View mNextButton;
    
    int mRhythm;
    
    public SeeRhythm() {
        super(4);
        mButtons = new View[4];
        mButtonListeners = new RhythmListener[4];
        nextRhythm();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seerhythm);
        
        View[] buttons = mButtons;
        RhythmListener[] listeners = mButtonListeners;

        int x = 0;
        buttons[x] = findViewById(R.id.SeeMissStopStop);
        listeners[x++] = new RhythmListener(Player.MISSISSIPPI_STOP_STOP_RHYTHM);
        buttons[x] = findViewById(R.id.SeeMissAlligator);
        listeners[x++] = new RhythmListener(Player.MISSISSIPPI_ALLIGATOR_RHYTHM);
        buttons[x] = findViewById(R.id.SeeDownPony);
        listeners[x++] = new RhythmListener(Player.DOWN_PONY_UP_PONY_RHYTHM);
        buttons[x] = findViewById(R.id.SeeIceCream);
        listeners[x++] = new RhythmListener(Player.ICE_CREAM_SH_CONE_RHYTHM);
        
        setListenersIntoButtons(buttons, listeners);
        
        mNextButton = (View) findViewById(R.id.SeeRhythmNext);
        mNextButton.setOnClickListener(mNextListener);
        
        mStaffView = (RhythmView) findViewById(R.id.RhythmView);
        changeRhythmInView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        Player player = ((SuzukiApplication) getApplication()).getPlayer();
        
        GameButtonListener.setPlayerIntoListeners(mButtonListeners, player);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        GameButtonListener.setPlayerIntoListeners(mButtonListeners, null);
    }

    void nextRhythm() {
        mRhythm = nextRandom(mRhythm);
    }
    
    void changeRhythmInView() {
        mStaffView.setRhythm(mRhythm);
    }
    
    private OnClickListener mNextListener = new OnClickListener() {
        
        @Override
        public void onClick(View arg0) {
            nextRhythm();
            changeRhythmInView();
        }
    };
    
    private class RhythmListener extends GameButtonListener {
        private int mRhythm;
        
        private RhythmListener (int rhythm) {
            super(SeeRhythm.this);
            mRhythm = rhythm;
        }

        @Override
        protected boolean checkSuccess() {
            return mRhythm == SeeRhythm.this.mRhythm;
        }
    }
}
