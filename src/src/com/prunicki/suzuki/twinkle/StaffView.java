package com.prunicki.suzuki.twinkle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class StaffView extends View {
    private static final int STROKE_WIDTH = 2;
    private static final int RATIO = 3;
    private static final String TIME_4 = "#";
    private static final String TREBLE_CLEF = "$";
    private static final String WHOLE_NOTE = "1";
    
    private final float mDensity;
    private final float mStrokeWidth;
    private final float mPadding;
    private final Paint mpaint;
    
    private final Typeface mTypeface;
    private final Paint mTypefacePaint;
    
    private int mNote;
    private int mWidth;
    private int mHeight;
    private float mStartX;
    private float mDrawWidth;
    private float mDrawHeight;
    private float mStaffStartY;
    private float mStaffHeight;
    private float[] mLineY;
    
    public StaffView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        mNote = -1;
        mLineY = new float[5];
        mTypeface = Typeface.createFromAsset(context.getAssets(), "staff.ttf");
        
        mTypefacePaint = new Paint();
        mTypefacePaint.setTypeface(mTypeface);
        
        mDensity = context.getResources().getDisplayMetrics().density;
        mStrokeWidth = STROKE_WIDTH * mDensity;
        mPadding = 4 * mDensity;
        
        mpaint = new Paint();
        mpaint.setColor(Color.BLACK);
        mpaint.setStrokeWidth(mStrokeWidth);
        mpaint.setStyle(Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        if (mWidth != width && mHeight != height) {
            recomputeCoordinates(width, height);
        }
        
        drawBorder(canvas);
        drawStaffLines(canvas);
        
        float clefHeight = (float) (mDrawHeight * 1);
        mTypefacePaint.setTextSize(clefHeight);
        canvas.drawText(TREBLE_CLEF, mStartX + 2 * (mDrawWidth / 100), mStaffStartY + mStaffHeight + 2, mTypefacePaint);
        
        if (mNote >= 0) {
            drawNote(canvas);
        }
        
        super.onDraw(canvas);
    }

    public void setNote(int note) {
        if (note != mNote) {
            mNote = note;
            postInvalidate();
        }
    }
    
    private void drawNote(Canvas canvas) {
        mTypefacePaint.setTextSize(mStaffHeight);
        
        float y = mLineY[4];
        switch (mNote) {
            case 0:
                y = mLineY[1];
                break;
            case 1:
                y = (mLineY[1] + mLineY[2]) / 2;
                break;
            case 2:
                y = mLineY[2];
                break;
            case 3:
                y = (mLineY[2] + mLineY[3]) / 2;
                break;
            case 4:
                y = mLineY[3];
                break;
            case 5:
                y = (mLineY[3] + mLineY[4]) / 2;
                break;
            case 6:
                y = mLineY[4];
                break;
        }
        
        canvas.drawText(WHOLE_NOTE, mStartX + 80, y, mTypefacePaint);
    }

    private void recomputeCoordinates(int width, int height) {
        mWidth = width;
        mHeight = height;
        float paddedWidth = width - mPadding * 2;
        float paddedHeight = height - mPadding * 2;
        
        mStartX = mPadding;
        float startY = mPadding;
        mDrawWidth = paddedWidth;
        mDrawHeight = paddedWidth / RATIO;
        if (mDrawHeight > paddedHeight) {
            mDrawWidth = paddedHeight * RATIO;
            mDrawHeight = paddedHeight;
            mStartX = (paddedWidth - mDrawWidth) / 2;
        } else {
            startY = (paddedHeight - mDrawHeight) / 2;
        }
        
        mStaffHeight = (3 * mDrawHeight) / 5;
        float staffDifference = (mDrawHeight - mStaffHeight) / 2;
        mStaffStartY = startY + staffDifference;
    }

    private void drawBorder(Canvas canvas) {
        Drawable border = getContext().getResources().getDrawable(R.drawable.border);
        Rect borderBounds = new Rect(0, 0, mWidth, mHeight);
        border.setBounds(borderBounds);
        border.draw(canvas);
    }
    
    private void drawStaffLines(Canvas canvas) {
        float lineHeight = mStaffHeight / 4;
        float endX = mStartX + mDrawWidth;
        for (int i = 0; i < 5; i++) {
            float lineY = mStaffStartY + lineHeight * i;
            mLineY[i] = lineY;
            canvas.drawLine(mStartX, lineY, endX, lineY, mpaint);
        }
        drawFour(canvas, lineHeight, mLineY[2]);
        drawFour(canvas, lineHeight, mLineY[4]);
        
        float vertLine1X = mStartX + (mDrawWidth / 100);
        canvas.drawLine(vertLine1X, mStaffStartY, vertLine1X, mStaffStartY + mStaffHeight, mpaint);
        float vertLine2X = mDrawWidth - 2 * (mDrawWidth / 100);
        canvas.drawLine(vertLine2X, mStaffStartY, vertLine2X, mStaffStartY + mStaffHeight, mpaint);
        canvas.drawLine(mDrawWidth, mStaffStartY, mDrawWidth, mStaffStartY + mStaffHeight, mpaint);
    }
    
    private void drawFour(Canvas canvas, float lineHeight, float lineY) {
        mTypefacePaint.setTextSize(lineHeight * 2);
        canvas.drawText(TIME_4, mStartX + 40, lineY, mTypefacePaint);
    }
}
