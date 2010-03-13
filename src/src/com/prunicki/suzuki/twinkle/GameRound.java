package com.prunicki.suzuki.twinkle;

import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class GameRound {
    public final int mResourceId;
    private final boolean mSound;
    
    private final Random mRandom;
    private final int mMaxRandom;
    
    protected Activity mActivity;
    protected Player mPlayer;
    protected View[] mButtons;
    protected GameButtonListener[] mButtonListeners;
    protected int mFirstRun;
    protected GameRoundCallback mCallback;

    private View mReplayButton;
    
    public GameRound(int resourceId, boolean sound, int numGameButtons, GameRoundCallback callback) {
        this.mResourceId = resourceId;
        mSound = sound;
        mMaxRandom = numGameButtons;
        mCallback = callback;
        
        mButtons = new View[numGameButtons];
        mButtonListeners = new GameButtonListener[numGameButtons];
        mRandom = new Random();
    }
    
    public int getScore(int mDifficultyLevel) {
        int score = getMaxScore();
        
        if (mDifficultyLevel == GameScreen.DIFFICULTY_LEVEL_HARD) {
            for (int i = 0; i < mMaxRandom; i++) {
                if (mButtonListeners[i].isClickedWrong()) {
                    score--;
                }
            }
        }
        
        return score;
    }
    
    protected void onCreate(Activity activity) {
        mActivity = activity;
        
        mReplayButton = activity.findViewById(R.id.Replay);
        if (mSound) {
            mReplayButton.setVisibility(View.VISIBLE);
            mReplayButton.setOnClickListener(mReplayListener);
        } else {
            mReplayButton.setVisibility(View.INVISIBLE);
            mReplayButton.setOnClickListener(null);
        }
    }
    
    protected void onResume() {
        mFirstRun++;
        mPlayer = ((SuzukiApplication) mActivity.getApplication()).getPlayer();
        GameButtonListener.setPlayerIntoListeners(mButtonListeners, mPlayer);
        
        if (isFirstRun()) {
            performPlayNotes();
        }
    }
    
    protected void onPause() {
        GameButtonListener.setPlayerIntoListeners(mButtonListeners, null);
    }
    
    protected void playNotes(Player.PlayerCallback playerCallback) {
    }
    
    protected final boolean isFirstRun() {
        return mFirstRun <= 1;
    }
    
    protected int nextRandom(int prev) {
        Random random = mRandom;
        
        while (true) {
            int nextInt = random.nextInt(mMaxRandom);
            if (prev != nextInt) {
                return nextInt;
            }
        }
    }
    
    protected int getMaxScore() {
        return mMaxRandom;
    }
    
    protected static void setListenersIntoButtons(View[] buttons, OnClickListener[] listeners) {
        int count = buttons.length;
        for (int i = 0; i < count; i++) {
            buttons[i].setOnClickListener(listeners[i]);
        }
    }
    
    void performPlayNotes() {
        if (mSound) {
            final ProgressDialog dialog = new ProgressDialog(mActivity);
            dialog.setMessage("Listen...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            
            playNotes(new Player.PlayerCallback() {
                
                @Override
                public void playbackComplete() {
                    dialog.cancel();
                }
            });
        }
    }
    
    private OnClickListener mReplayListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            performPlayNotes();
        }
    };
}
