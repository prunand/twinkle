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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SeeNote extends Activity {
    
    private ImageView mRhythmImage;
    
    private Button mANote;
    private Button mBNote;
    private Button mCNote;
    private Button mDNote;
    private Button mENote;
    private Button mFNote;
    private Button mGNote;
    
    private Button mNextButton;
    private Button mBackButton;
    
    private Random mRandom;
    private int mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seenote);
        
        mRandom = new Random();
        nextRhythm();
        
        mRhythmImage = (ImageView) findViewById(R.id.NoteImage);
        
        mNextButton = (Button) findViewById(R.id.SeeNoteNext);
        mBackButton = (Button) findViewById(R.id.SeeNoteBack);

        mANote = (Button) findViewById(R.id.SeeNoteA);
        mBNote = (Button) findViewById(R.id.SeeNoteB);
        mCNote = (Button) findViewById(R.id.SeeNoteC);
        mDNote = (Button) findViewById(R.id.SeeNoteD);
        mENote = (Button) findViewById(R.id.SeeNoteE);
        mFNote = (Button) findViewById(R.id.SeeNoteF);
        mGNote = (Button) findViewById(R.id.SeeNoteG);
        
        mNextButton.setOnClickListener(mNextListener);
        mBackButton.setOnClickListener(mBackListener);
        
        mANote.setOnClickListener(new NoteListener(Player.A_NOTE));
        mBNote.setOnClickListener(new NoteListener(Player.B_NOTE));
        mCNote.setOnClickListener(new NoteListener(Player.C_NOTE));
        mDNote.setOnClickListener(new NoteListener(Player.D_NOTE));
        mENote.setOnClickListener(new NoteListener(Player.E_NOTE));
        mFNote.setOnClickListener(new NoteListener(Player.F_NOTE));
        mGNote.setOnClickListener(new NoteListener(Player.G_NOTE));
        
        placeImage();
    }
    
    private void placeImage() {
        switch (mNote) {
            case Player.A_NOTE:
                Log.d(Main.TAG, "placing a");
                mRhythmImage.setImageResource(R.drawable.note_a);
                break;
            case Player.B_NOTE:
                Log.d(Main.TAG, "placing b");
                mRhythmImage.setImageResource(R.drawable.note_b);
                break;
            case Player.C_NOTE:
                Log.d(Main.TAG, "placing c");
                mRhythmImage.setImageResource(R.drawable.note_c);
                break;
            case Player.D_NOTE:
                Log.d(Main.TAG, "placing d");
                mRhythmImage.setImageResource(R.drawable.note_d);
                break;
            case Player.E_NOTE:
                Log.d(Main.TAG, "placing e");
                mRhythmImage.setImageResource(R.drawable.note_e);
                break;
            case Player.F_NOTE:
                Log.d(Main.TAG, "placing f");
                mRhythmImage.setImageResource(R.drawable.note_f);
                break;
            case Player.G_NOTE:
                Log.d(Main.TAG, "placing g");
                mRhythmImage.setImageResource(R.drawable.note_g);
                break;
        }
    }

    private void nextRhythm() {
        boolean done = false;
        while (!done) {
            int nextInt = mRandom.nextInt(7);
            if (mNote != nextInt) {
                mNote = nextInt;
                done = true;
            }
        }
    }
    
    private OnClickListener mNextListener = new OnClickListener() {
        
        @Override
        public void onClick(View arg0) {
            Log.d(Main.TAG, "Note was " + mNote);
            nextRhythm();
            Log.d(Main.TAG, "Note is " + mNote);
            placeImage();
        }
    };
    
    private OnClickListener mBackListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
           finish();
        }
    };
    
    private class NoteListener implements OnClickListener {
        
        private int mSelectedNote;
        
        private NoteListener (int rhythm) {
            mSelectedNote = rhythm;
        }

        @Override
        public void onClick(View v) {
            if (mNote == mSelectedNote) {
                SuccessDialog dialog = new SuccessDialog(SeeNote.this);
                dialog.show();
            }
        }
    }
}
