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

public class HearNote extends GameRound {
    int mNote;
    
    public HearNote(GameRoundCallback callback) {
        super(R.layout.hearnote, true, 7, callback);
        nextNote();
    }

    @Override
    protected void onCreate(Activity activity) {
        super.onCreate(activity);
        
        View[] buttons = mButtons;
        GameButtonListener[] listeners = mButtonListeners;
        GameRoundCallback callback = mCallback;
        
        int x = 0;
        buttons[x] = activity.findViewById(R.id.HearNoteA);
        listeners[x++] = new NoteListener(Player.A_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.HearNoteB);
        listeners[x++] = new NoteListener(Player.B_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.HearNoteC);
        listeners[x++] = new NoteListener(Player.C_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.HearNoteD);
        listeners[x++] = new NoteListener(Player.D_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.HearNoteE);
        listeners[x++] = new NoteListener(Player.E_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.HearNoteF);
        listeners[x++] = new NoteListener(Player.F_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.HearNoteG);
        listeners[x++] = new NoteListener(Player.G_NOTE, callback);
        
        setListenersIntoButtons(buttons, listeners);
    }

    @Override
    protected void playNotes(Player.PlayerCallback playerCallback) {
        mPlayer.playNote(mNote, playerCallback);
    }

    void nextNote() {
        mNote = nextRandom(mNote);
    }
    
    private class NoteListener extends GameButtonListener {
        private int mNote;
        
        private NoteListener(int note, GameRoundCallback callback) {
            super(callback);
            mNote = note;
        }

        @Override
        public boolean checkSuccess() {
            return mNote == HearNote.this.mNote;
        }
    }
}
