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
package com.prunicki.suzuki.twinkle.widget;

import com.prunicki.suzuki.twinkle.SoundPlayer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class RhythmView extends StaffView {
    
    private static final String MISSISSIPPI = SIXTEENTH_START_NOTE + SIXTEENTH_MIDDLE_NOTE +
        SIXTEENTH_MIDDLE_NOTE + SIXTEENTH_END_NOTE;
    private static final String PONY = EIGHTH_START_NOTE + EIGHTH_TO_SIXTEENTH_MIDDLE_NOTE +
        SIXTEENTH_END_NOTE;
    private static final String STOP_STOP = EIGHTH_START_NOTE + EIGHTH_END_NOTE;
    
    private static final String MISSISSIPPI_STOP_STOP = MISSISSIPPI + " " + STOP_STOP;
    private static final String MISSISSIPPI_ALLIGATOR = MISSISSIPPI + " " + MISSISSIPPI;
    private static final String DOWN_PONY_UP_PONY = PONY + " " + PONY;
    private static final String ICE_CREAM_SH_CONE = STOP_STOP + " " + EIGHTH_REST + " " + EIGHTH_NOTE;
    
    private int mRhythm;

    public RhythmView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void drawCustom(Canvas canvas) {
        float fontSize = (float) (mLineHeight * 1.2);
        mFontPaint.setTextSize(fontSize);
        
        String rhythm = null;
        switch (mRhythm) {
            case SoundPlayer.MISSISSIPPI_STOP_STOP_RHYTHM:
                rhythm = MISSISSIPPI_STOP_STOP;
                break;
            case SoundPlayer.MISSISSIPPI_ALLIGATOR_RHYTHM:
                rhythm = MISSISSIPPI_ALLIGATOR;
                break;
            case SoundPlayer.DOWN_PONY_UP_PONY_RHYTHM:
                rhythm = DOWN_PONY_UP_PONY;
                break;
            case SoundPlayer.ICE_CREAM_SH_CONE_RHYTHM:
                rhythm = ICE_CREAM_SH_CONE;
                break;
        }
        
        canvas.drawText(rhythm, mStartNoteX, mLineY[3], mFontPaint);
    }

    public void setRhythm(int rhythm) {
        if (rhythm != mRhythm) {
            mRhythm = rhythm;
            postInvalidate();
        }
    }
}
