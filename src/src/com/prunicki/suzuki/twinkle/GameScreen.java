package com.prunicki.suzuki.twinkle;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.prunicki.suzuki.twinkle.db.ScoreDAO;
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

public class GameScreen extends TwinkleActivity implements GameRoundCallback {
    private static final int END_ROUND_ANIMATION_TIME = 1000;
    private Player mPlayer;
    private ScoreDAO mDao;
    private ViewGroup mGameView;
    private GameRound[] gameRounds;
    private GameRound mCurrentGameRound;
    private int mCurrentRoundIndex;
    
    public GameScreen() {
        int i = 0;
        gameRounds = new GameRound[7];
        gameRounds[i++] = new RestLength(this);
        gameRounds[i++] = new NoteLength(this);
        gameRounds[i++] = new Pitch(this);
        gameRounds[i++] = new SeeNote(this);
        gameRounds[i++] = new HearNote(this);
        gameRounds[i++] = new SeeRhythm(this);
        gameRounds[i++] = new HearRhythm(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        TwinkleApplication app = (TwinkleApplication) getApplication();
        mPlayer = app.getCurrentPlayer();
        mDao = app.getDAO();
        
        mGameView = (ViewGroup) findViewById(R.id.GameView);
        
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
                if (mCurrentRoundIndex < gameRounds.length) {
                    Animation anim = AnimationUtils.loadAnimation(GameScreen.this, R.anim.fadeout);
                    anim.setDuration(END_ROUND_ANIMATION_TIME);
                    mGameView.startAnimation(anim);
                }
            }

            @Override
            protected void onPostExecute(Void result) {
                mGameView.removeAllViews();
                mCurrentGameRound.onPause();
                
                if (mCurrentRoundIndex < gameRounds.length) {
                    mCurrentGameRound = gameRounds[mCurrentRoundIndex++];
                    
                    initCurrentGameRound();
                    mCurrentGameRound.onResume();
                } else {
                    int score = 0;
                    
                    for (int i = 0; i < gameRounds.length; i++) {
                        score += gameRounds[i].getScore();
                    }
                    mPlayer.setLastScore(score);
                    ModelHelper.savePlayer(mPlayer, mDao);
                    
                    SuccessDialog dlg = new SuccessDialog(GameScreen.this, score);
                    dlg.show();
                    
                    Log.d(Main.TAG, "Score = " + score);
                }
            }
            
            @Override
            protected Void doInBackground(Void... params) {
                if (mCurrentRoundIndex < gameRounds.length) {
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
    
    private void initCurrentGameRound() {
        GameRound currentGameRound = mCurrentGameRound;
        this.getLayoutInflater().inflate(currentGameRound.mResourceId, mGameView);
        this.getLayoutInflater().inflate(R.layout.replay_button, mGameView);
        currentGameRound.onCreate(GameScreen.this);
    }
}
