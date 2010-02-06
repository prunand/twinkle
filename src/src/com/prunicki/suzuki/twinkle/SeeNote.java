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

public class SeeNote extends GameActivity {
    private NoteView mStaffView;
    private View[] mButtons;
    private NoteListener[] mButtonListeners;
    
    private View mNextButton;
    
    int mNote;
    
    public SeeNote() {
        super(7);
        mButtons = new Button[7];
        mButtonListeners = new NoteListener[7];
        nextNote();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seenote);
        
        View[] buttons = mButtons;
        NoteListener[] listeners = mButtonListeners;
        
        int x = 0;
        buttons[x] = findViewById(R.id.SeeNoteA);
        listeners[x++] = new NoteListener(Player.A_NOTE);
        buttons[x] = findViewById(R.id.SeeNoteB);
        listeners[x++] = new NoteListener(Player.B_NOTE);
        buttons[x] = findViewById(R.id.SeeNoteC);
        listeners[x++] = new NoteListener(Player.C_NOTE);
        buttons[x] = findViewById(R.id.SeeNoteD);
        listeners[x++] = new NoteListener(Player.D_NOTE);
        buttons[x] = findViewById(R.id.SeeNoteE);
        listeners[x++] = new NoteListener(Player.E_NOTE);
        buttons[x] = findViewById(R.id.SeeNoteF);
        listeners[x++] = new NoteListener(Player.F_NOTE);
        buttons[x] = findViewById(R.id.SeeNoteG);
        listeners[x] = new NoteListener(Player.G_NOTE);
        
        setListenersIntoButtons(buttons, listeners);
        
        mNextButton = (View) findViewById(R.id.SeeNoteNext);
        mNextButton.setOnClickListener(mNextListener);
        
        mStaffView = (NoteView) findViewById(R.id.NoteView);
        changeNoteInView();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        GameButtonListener.setPlayerIntoListeners(mButtonListeners, mPlayer);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        GameButtonListener.setPlayerIntoListeners(mButtonListeners, null);
    }

    void changeNoteInView() {
        mStaffView.setNote(mNote);
    }

    void nextNote() {
        mNote = nextRandom(mNote);
    }
    
    private OnClickListener mNextListener = new OnClickListener() {
        
        @Override
        public void onClick(View arg0) {
            nextNote();
            changeNoteInView();
        }
    };
    
    private class NoteListener extends GameButtonListener {
        
        private int mNote;
        
        private NoteListener (int note) {
            super(SeeNote.this);
            mNote = note;
        }

        @Override
        protected boolean checkSuccess() {
            return mNote == SeeNote.this.mNote;
        }
    }
}
