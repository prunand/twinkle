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

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.prunicki.suzuki.twinkle.game.GameRound;
import com.prunicki.suzuki.twinkle.game.HearNote;
import com.prunicki.suzuki.twinkle.game.HearRhythm;
import com.prunicki.suzuki.twinkle.game.NoteLength;
import com.prunicki.suzuki.twinkle.game.Pitch;
import com.prunicki.suzuki.twinkle.game.RestLength;
import com.prunicki.suzuki.twinkle.game.SeeNote;
import com.prunicki.suzuki.twinkle.game.SeeRhythm;

public class PracticeScreen extends TwinkleActivity implements GameRoundCallback {
    public static final String PRACTICE_TYPE_KEY = "practiceType";
    public static final int PITCH_PRACTICE_TYPE = 0;
    public static final int PITCH_NOTE_PRACTICE_TYPE = 1;
    public static final int READ_NOTE_PRACTICE_TYPE = 2;
    public static final int READ_REST_PRACTICE_TYPE = 3;
    public static final int READ_LENGTH_PRACTICE_TYPE = 4;
    public static final int SEE_RHYTHM_PRACTICE_TYPE = 5;
    public static final int HEAR_RHYTHM_PRACTICE_TYPE = 6;
    
    private ViewGroup mGameView;
    private GameRound mGameRound;
    private View mNextButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        mGameView = (ViewGroup) findViewById(R.id.GameView);
        
        int practiceType = getIntent().getIntExtra(PRACTICE_TYPE_KEY, -1);
        GameRound gameRound = null;
        switch(practiceType) {
            case PITCH_PRACTICE_TYPE:
                gameRound = new Pitch(mGameView, this);
                break;
            case PITCH_NOTE_PRACTICE_TYPE:
                gameRound = new HearNote(mGameView, this);
                break;
            case READ_NOTE_PRACTICE_TYPE:
                gameRound = new SeeNote(mGameView, this);
                break;
            case READ_REST_PRACTICE_TYPE:
                gameRound = new RestLength(mGameView, this);
                break;
            case READ_LENGTH_PRACTICE_TYPE:
                gameRound = new NoteLength(mGameView, this);
                break;
            case SEE_RHYTHM_PRACTICE_TYPE:
                gameRound = new SeeRhythm(mGameView, this);
                break;
            case HEAR_RHYTHM_PRACTICE_TYPE:
                gameRound = new HearRhythm(mGameView, this);
                break;
            default:
                throw new IllegalArgumentException("Unkown practice type " + practiceType);
        }
        
        mGameRound = gameRound;
        initCurrentGameRound(gameRound);
        mNextButton = mGameView.findViewById(R.id.Next);
        
        mNextButton.setOnClickListener(mNextListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        mGameRound.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        mGameRound.onPause();
    }

    @Override
    public void roundComplete() {
        SuccessDialog dlg = new SuccessDialog(this, -1);
        dlg.show();
    }
    
    private void initCurrentGameRound(GameRound gameRound) {
        this.getLayoutInflater().inflate(gameRound.mResourceId, mGameView);
        this.getLayoutInflater().inflate(R.layout.replay_next, mGameView);
        gameRound.onCreate(PracticeScreen.this);
    }
    
    private OnClickListener mNextListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mGameRound.next();
        }
    };
}
