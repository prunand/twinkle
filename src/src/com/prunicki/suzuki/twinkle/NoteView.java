package com.prunicki.suzuki.twinkle;

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
