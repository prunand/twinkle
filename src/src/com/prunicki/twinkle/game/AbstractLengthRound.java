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

import static com.prunicki.twinkle.Constants.TAG;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prunicki.twinkle.GameButtonListener;
import com.prunicki.twinkle.GameRoundCallback;
import com.prunicki.twinkle.R;
import com.prunicki.twinkle.widget.SimpleNoteView;

public class AbstractLengthRound extends GameRound {
    protected static final int WHOLE_NOTE = 0;
    protected static final int HALF_NOTE = 1;
    protected static final int QUARTER_NOTE = 2;
    protected static final int EIGHTH_NOTE = 3;
    protected static final int SIXTEENTH_NOTE = 4;
    
    private final String descTxt;
    protected SimpleNoteView mStaffView;
    int mNote;
    
    public AbstractLengthRound(ViewGroup viewGroup, GameRoundCallback callback, String descTxt) {
        super(R.layout.notelength, viewGroup, false, 5, callback);
        
        this.descTxt = descTxt;
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
        
        String lengthText = mActivity.getResources().getString(R.string.length_desc);
        lengthText = String.format(lengthText, descTxt);
        TextView txtView = (TextView) activity.findViewById(R.id.DescText);
        txtView.setText(lengthText);
        
        showNextView();
    }

    @Override
    protected void prepareNext() {
        mNote = nextRandom(mNote);
        Log.d(TAG, "Next Random " + mNote);
    }
    
    private class NotelengthListener extends GameButtonListener {
        private int mNote;

        private NotelengthListener (int note, GameRoundCallback callback) {
            super(callback);
            mNote = note;
        }

        @Override
        protected boolean checkSuccess() {
            return mNote == AbstractLengthRound.this.mNote;
        }
    }
}
