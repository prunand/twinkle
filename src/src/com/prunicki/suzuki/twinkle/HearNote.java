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
import android.widget.Button;

public class HearNote extends GameActivity {
    private View[] mButtons;
    private NoteListener[] mButtonListeners;
    
    private View mNextButton;
    private View mReplayButton;
    
    int mNote;
    private boolean mFirstRun;
    
    public HearNote() {
        super(7);
        mFirstRun = true;
        mButtons = new Button[7];
        mButtonListeners = new NoteListener[7];
        nextNote();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hearnote);
        
        View[] buttons = mButtons;
        NoteListener[] listeners = mButtonListeners;
        
        
        int x = 0;
        buttons[x] = findViewById(R.id.HearNoteA);
        listeners[x++] = new NoteListener(Player.A_NOTE);
        buttons[x] = findViewById(R.id.HearNoteB);
        listeners[x++] = new NoteListener(Player.B_NOTE);
        buttons[x] = findViewById(R.id.HearNoteC);
        listeners[x++] = new NoteListener(Player.C_NOTE);
        buttons[x] = findViewById(R.id.HearNoteD);
        listeners[x++] = new NoteListener(Player.D_NOTE);
        buttons[x] = findViewById(R.id.HearNoteE);
        listeners[x++] = new NoteListener(Player.E_NOTE);
        buttons[x] = findViewById(R.id.HearNoteF);
        listeners[x++] = new NoteListener(Player.F_NOTE);
        buttons[x] = findViewById(R.id.HearNoteG);
        listeners[x++] = new NoteListener(Player.G_NOTE);
        
        setListenersIntoButtons(buttons, listeners);
        
        mNextButton = findViewById(R.id.HearNoteNext);
        mNextButton.setOnClickListener(mNextListener);
        
        mReplayButton = findViewById(R.id.HearNoteReplay);
        mReplayButton.setOnClickListener(mReplayListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        GameButtonListener.setPlayerIntoListeners(mButtonListeners, mPlayer);
        
        if (mFirstRun) {
            playNote();
        }
        
        mFirstRun = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        GameButtonListener.setPlayerIntoListeners(mButtonListeners, null);
    }
    
    void nextNote() {
        mNote = nextRandom(mNote);
    }
    
    void playNote() {
        mPlayer.playNote(mNote);
    }

    private OnClickListener mReplayListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            playNote();
        }
    };
    
    private OnClickListener mNextListener = new OnClickListener() {
        
        @Override
        public void onClick(View arg0) {
            nextNote();
            playNote();
        }
    };
    
    private class NoteListener extends GameButtonListener {
        private int mNote;
        
        private NoteListener(int note) {
            super(HearNote.this);
            mNote = note;
        }

        @Override
        public boolean checkSuccess() {
            return mNote == HearNote.this.mNote;
        }
    }
}
