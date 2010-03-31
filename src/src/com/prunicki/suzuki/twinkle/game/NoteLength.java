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
package com.prunicki.suzuki.twinkle.game;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.prunicki.suzuki.twinkle.GameButtonListener;
import com.prunicki.suzuki.twinkle.GameRoundCallback;
import com.prunicki.suzuki.twinkle.Main;
import com.prunicki.suzuki.twinkle.R;
import com.prunicki.suzuki.twinkle.widget.SimpleNoteView;

public class NoteLength extends GameRound {
    private static final int WHOLE_NOTE = 0;
    private static final int HALF_NOTE = 1;
    private static final int QUARTER_NOTE = 2;
    private static final int EIGHTH_NOTE = 3;
    private static final int SIXTEENTH_NOTE = 4;
    
    private SimpleNoteView mStaffView;
    int mNote;
    
    public NoteLength(GameRoundCallback callback) {
        super(R.layout.notelength, false, 5, callback);
        
        mNote = -1;
        nextNote();
    }

    @Override
    public void onCreate(Activity activity) {
        super.onCreate(activity);
        
        View[] buttons = mButtons;
        GameButtonListener[] listeners = mButtonListeners;
        GameRoundCallback callback = mCallback;

        int x = 0;
        buttons[x] = activity.findViewById(R.id.WholeNote);
        listeners[x++] = new NotelengthListener(WHOLE_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.HalfNote);
        listeners[x++] = new NotelengthListener(HALF_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.QuarterNote);
        listeners[x++] = new NotelengthListener(QUARTER_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.EighthNote);
        listeners[x++] = new NotelengthListener(EIGHTH_NOTE, callback);
        buttons[x] = activity.findViewById(R.id.SixteenthNote);
        listeners[x++] = new NotelengthListener(SIXTEENTH_NOTE, callback);

        setListenersIntoButtons(buttons, listeners);

        mStaffView = (SimpleNoteView) activity.findViewById(R.id.StaffView);
        changeNoteInView();
    }
    
    void changeNoteInView() {
        char noteChar;
        
        switch(mNote) {
            case WHOLE_NOTE:
                noteChar = SimpleNoteView.WHOLE_NOTE;
                break;
            case HALF_NOTE:
                noteChar = SimpleNoteView.HALF_NOTE;
                break;
            case QUARTER_NOTE:
                noteChar = SimpleNoteView.QUARTER_NOTE;
                break;
            case EIGHTH_NOTE:
                noteChar = SimpleNoteView.EIGHTH_NOTE;
                break;
            case SIXTEENTH_NOTE:
                noteChar = SimpleNoteView.SIXTEENTH_NOTE;
                break;
            default:
                throw new IllegalStateException("Invalid note " + mNote);
        }
        
        mStaffView.setNote(noteChar);
    }

    void nextNote() {
        mNote = nextRandom(mNote);
        Log.d(Main.TAG, "Next Random " + mNote);
    }
    
    private class NotelengthListener extends GameButtonListener {
        private int mNote;

        private NotelengthListener (int note, GameRoundCallback callback) {
            super(callback);
            mNote = note;
        }

        @Override
        protected boolean checkSuccess() {
            return mNote == NoteLength.this.mNote;
        }
    }
}
