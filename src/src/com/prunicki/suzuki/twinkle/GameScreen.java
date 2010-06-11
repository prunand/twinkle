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

import static com.prunicki.suzuki.twinkle.Constants.TAG;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.prunicki.suzuki.twinkle.game.GameRound;
import com.prunicki.suzuki.twinkle.game.HearNote;
import com.prunicki.suzuki.twinkle.game.HearRhythm;
import com.prunicki.suzuki.twinkle.game.NoteLength;
import com.prunicki.suzuki.twinkle.game.Pitch;
import com.prunicki.suzuki.twinkle.game.RestLength;
import com.prunicki.suzuki.twinkle.game.SeeNote;
import com.prunicki.suzuki.twinkle.game.SeeRhythm;
import com.prunicki.suzuki.twinkle.model.ModelHelper;
import com.prunicki.suzuki.twinkle.model.Player;
import com.prunicki.suzuki.twinkle.util.WidgetUtil;

public class GameScreen extends TwinkleActivity implements GameRoundCallback {
    private static final int END_ROUND_ANIMATION_TIME = 1000;
    private TwinkleApplication mApp;
    private Player mPlayer;
    private ViewGroup mGameView;
    private GameRound[] gameRounds;
    private GameRound mCurrentGameRound;
    private int mCurrentRoundIndex;
    
    public GameScreen() {
        gameRounds = new GameRound[7];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        mApp = (TwinkleApplication) getApplication();
        mPlayer = mApp.getCurrentPlayer();
        
        mGameView = (ViewGroup) findViewById(R.id.GameView);
        
        int i = 0;
        gameRounds[i++] = new RestLength(mGameView, this);
        gameRounds[i++] = new NoteLength(mGameView, this);
        gameRounds[i++] = new Pitch(mGameView, this);
        gameRounds[i++] = new SeeNote(mGameView, this);
        gameRounds[i++] = new HearNote(mGameView, this);
        gameRounds[i++] = new SeeRhythm(mGameView, this);
        gameRounds[i++] = new HearRhythm(mGameView, this);
        
        mCurrentGameRound = gameRounds[mCurrentRoundIndex++];
        initCurrentGameRound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        mCurrentGameRound.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        mCurrentGameRound.onPause();
    }

    @Override
    public void roundComplete() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                if (!isGameOver()) {
                    startAnimation();
                }
            }

            @Override
            protected void onPostExecute(Void result) {
                processEndGame();
            }
            
            @Override
            protected Void doInBackground(Void... params) {
                if (!isGameOver()) {
                    try {
                        Thread.sleep(END_ROUND_ANIMATION_TIME);
                    } catch (InterruptedException e) {
                        //Nothing to do really.
                    }
                }
                
                return null;
            }
        };
        
        task.execute((Void) null);
    }

    boolean isGameOver() {
        return mCurrentRoundIndex >= gameRounds.length;
    }

    void startAnimation() {
        WidgetUtil.setChildWidgetsEnabled(mGameView, false);
        Animation anim = AnimationUtils.loadAnimation(GameScreen.this, R.anim.fade);
        anim.setDuration(END_ROUND_ANIMATION_TIME);
        mGameView.startAnimation(anim);
    }

    void processEndGame() {
        mGameView.removeAllViews();
        mCurrentGameRound.onPause();
        
        GameRound[] games = gameRounds;
        int length = games.length;
        
        if (mCurrentRoundIndex < length) {
            mCurrentGameRound = games[mCurrentRoundIndex++];
            
            initCurrentGameRound();
            mCurrentGameRound.onResume();
        } else {
            int score = 0;
            
            for (int i = 0; i < length; i++) {
                score += games[i].getScore();
            }
            mPlayer.setLastScore(score);
            ModelHelper.savePlayer(mPlayer, mApp.getDAO());
            
            SuccessDialog dlg = new SuccessDialog(GameScreen.this, score);
            dlg.show();
            
            Log.d(TAG, "Score = " + score);
        }
    }
    
    private void initCurrentGameRound() {
        GameRound currentGameRound = mCurrentGameRound;
        this.getLayoutInflater().inflate(currentGameRound.mResourceId, mGameView);
        this.getLayoutInflater().inflate(R.layout.replay_button, mGameView);
        currentGameRound.onCreate(GameScreen.this);
    }
}
