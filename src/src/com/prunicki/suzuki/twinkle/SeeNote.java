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
import android.widget.Button;

public class SeeNote extends Activity {
    
    private StaffView mStaffView;
    
    private View[] mNoteButtons;
    private NoteListener[] mNoteListeners;
    
    private View mNextButton;
    
    private Random mRandom;
    int mNote;  //default to prevent accessor method from being created at compile.
    
    public SeeNote() {
        mNoteButtons = new Button[7];
        mNoteListeners = new NoteListener[7];
        mRandom = new Random();
        nextNote();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seenote);
        
        View[] noteButtons = mNoteButtons;
        NoteListener[] noteListeners = mNoteListeners;
        
        int x = 0;
        noteButtons[x] = findViewById(R.id.SeeNoteA);
        noteListeners[x++] = new NoteListener(Player.A_NOTE);
        noteButtons[x] = findViewById(R.id.SeeNoteB);
        noteListeners[x++] = new NoteListener(Player.B_NOTE);
        noteButtons[x] = findViewById(R.id.SeeNoteC);
        noteListeners[x++] = new NoteListener(Player.C_NOTE);
        noteButtons[x] = findViewById(R.id.SeeNoteD);
        noteListeners[x++] = new NoteListener(Player.D_NOTE);
        noteButtons[x] = findViewById(R.id.SeeNoteE);
        noteListeners[x++] = new NoteListener(Player.E_NOTE);
        noteButtons[x] = findViewById(R.id.SeeNoteF);
        noteListeners[x++] = new NoteListener(Player.F_NOTE);
        noteButtons[x] = findViewById(R.id.SeeNoteG);
        noteListeners[x] = new NoteListener(Player.G_NOTE);
        
        int count = noteButtons.length;
        for (int i = 0; i < count; i++) {
            noteButtons[i].setOnClickListener(noteListeners[i]);
        }
        
        mStaffView = (StaffView) findViewById(R.id.StaffView);
        
        mNextButton = (View) findViewById(R.id.SeeNoteNext);
        mNextButton.setOnClickListener(mNextListener);
        
        changeNoteInView();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        Player player = ((SuzukiApplication) getApplication()).getPlayer();
        
        setPlayerIntoListeners(player);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        setPlayerIntoListeners(null);
    }

    void changeNoteInView() {
        mStaffView.setNote(mNote);
    }

    void nextNote() {
        Random random = mRandom;
        
        while (true) {
            int nextInt = random.nextInt(7);
            if (mNote != nextInt) {
                mNote = nextInt;
                break;
            }
        }
    }

    private void setPlayerIntoListeners(Player player) {
        NoteListener[] noteListeners = mNoteListeners;
        
        int count = noteListeners.length;
        for (int i = 0; i < count; i++) {
            noteListeners[i].setPlayer(player);
        }
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
