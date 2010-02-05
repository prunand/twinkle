package com.prunicki.suzuki.twinkle;

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
            case Player.MISSISSIPPI_STOP_STOP_RHYTHM:
                rhythm = MISSISSIPPI_STOP_STOP;
                break;
            case Player.MISSISSIPPI_ALLIGATOR_RHYTHM:
                rhythm = MISSISSIPPI_ALLIGATOR;
                break;
            case Player.DOWN_PONY_UP_PONY_RHYTHM:
                rhythm = DOWN_PONY_UP_PONY;
                break;
            case Player.ICE_CREAM_SH_CONE_RHYTHM:
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
