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

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class NoteView extends StaffView {
    private int mNote;

    public NoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        mNote = -1;
    }

    @Override
    protected void drawCustom(Canvas canvas) {
        if (mNote >= 0) {
            drawNote(canvas);
        }
    }
    
    public void setNote(int note) {
        if (note != mNote) {
            mNote = note;
            postInvalidate();
        }
    }

    private void drawNote(Canvas canvas) {
        mFontPaint.setTextSize(mLineHeight);
        
        float[] lineYArray = mLineY;
        
        float y = 0;
        switch (mNote) {
            case 0:
                y = lineYArray[1];
                break;
            case 1:
                y = (lineYArray[1] + lineYArray[2]) / 2;
                break;
            case 2:
                y = lineYArray[2];
                break;
            case 3:
                y = (lineYArray[2] + lineYArray[3]) / 2;
                break;
            case 4:
                y = lineYArray[3];
                break;
            case 5:
                y = (lineYArray[3] + lineYArray[4]) / 2;
                break;
            case 6:
                y = lineYArray[4];
                break;
        }
        
        canvas.drawText(WHOLE_NOTE, mStartNoteX, y, mFontPaint);
    }

}
