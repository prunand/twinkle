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
import com.prunicki.twinkle.widget.NoteView;

public class SeeNote extends GameRound {
    private NoteView mStaffView;
    
    int mNote;
    
    public SeeNote(ViewGroup viewGroup, GameRoundCallback callback) {
        super(R.layout.seenote, viewGroup, false, 7, callback);
        
        mNote = -1;
        prepareNext();
    }

    @Override
    public void onCreate(Activity activity) {
        super.onCreate(activity);
        
        View[] buttons = mButtons;
        GameButtonListener[] listeners = mButtonListeners;
        GameRoundCallback callback = mCallback;
        
        int x = 0;
        buttons[x] = activity.findViewById(R.id.SeeNoteA);
        listeners[x++] = new NoteListener(SoundPlayer.A_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.SeeNoteB);
        listeners[x++] = new NoteListener(SoundPlayer.B_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.SeeNoteC);
        listeners[x++] = new NoteListener(SoundPlayer.C_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.SeeNoteD);
        listeners[x++] = new NoteListener(SoundPlayer.D_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.SeeNoteE);
        listeners[x++] = new NoteListener(SoundPlayer.E_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.SeeNoteF);
        listeners[x++] = new NoteListener(SoundPlayer.F_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.SeeNoteG);
        listeners[x] = new NoteListener(SoundPlayer.G_NOTE, callback);
        
        setListenersIntoButtons(buttons, listeners);
        
        mStaffView = (NoteView) activity.findViewById(R.id.NoteView);
        showNextView();
    }
    
    @Override
    protected void showNextView() {
        mStaffView.setNote(mNote);
    }

    @Override
    protected void prepareNext() {
        mNote = nextRandom(mNote);
    }
    
    private class NoteListener extends GameButtonListener {
        private int mNote;
        
        private NoteListener (int note, GameRoundCallback callback) {
            super(callback);
            mNote = note;
        }

        @Override
        protected boolean checkSuccess() {
            return mNote == SeeNote.this.mNote;
        }
    }
}
