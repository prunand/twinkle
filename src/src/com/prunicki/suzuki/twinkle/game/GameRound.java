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
package com.prunicki.suzuki.twinkle.game;

import static com.prunicki.suzuki.twinkle.model.Score.DIFFICULTY_LEVEL_HARD;

import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.prunicki.suzuki.twinkle.GameButtonListener;
import com.prunicki.suzuki.twinkle.GameRoundCallback;
import com.prunicki.suzuki.twinkle.Main;
import com.prunicki.suzuki.twinkle.R;
import com.prunicki.suzuki.twinkle.SoundPlayer;
import com.prunicki.suzuki.twinkle.TwinkleApplication;

public abstract class GameRound {
    public final int mResourceId;
    private final boolean mSound;

    private final Random mRandom;
    private final int mMaxRandom;

    protected Activity mActivity;
    protected SoundPlayer mSoundPlayer;
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

    public void onCreate(Activity activity) {
        mActivity = activity;

        mReplayButton = activity.findViewById(R.id.Replay);
        if (mSound) {
            mReplayButton.setVisibility(View.VISIBLE);
            mReplayButton.setOnClickListener(mReplayListener);
        } else {
            mReplayButton.setVisibility(View.GONE);
            mReplayButton.setOnClickListener(null);
        }
    }

    public void onResume() {
        mFirstRun++;
        mSoundPlayer = ((TwinkleApplication) mActivity.getApplication()).getSoundPlayer();
        GameButtonListener.setSoundPlayerIntoListeners(mButtonListeners, mSoundPlayer);

        if (isFirstRun()) {
            playNotes();
        }
    }

    public void onPause() {
        GameButtonListener.setSoundPlayerIntoListeners(mButtonListeners, null);
    }

    public final void next() {
        prepareNext();
        
        if (mSound) {
            playNotes();
        } else {
            showNextView();
        }
    }
    
    protected abstract void prepareNext();

    protected void playNotes(SoundPlayer.PlayerCallback soundPlayerCallback) {
    }
    
    protected void showNextView() {
    }

    protected final boolean isFirstRun() {
        return mFirstRun <= 1;
    }

    protected int nextRandom(int prev) {
        Random random = mRandom;
        int maxRandom = mMaxRandom;

        while (true) {
            int nextInt = random.nextInt(maxRandom);
            if (prev != nextInt) {
                return nextInt;
            }
        }
    }

    protected int getMaxScore() {
        return mMaxRandom;
    }

    public int getScore(int mDifficultyLevel) {
        int score = getMaxScore();

        if (mDifficultyLevel == DIFFICULTY_LEVEL_HARD) {
            for (int i = 0; i < mMaxRandom; i++) {
                if (mButtonListeners[i].isClickedWrong()) {
                    score--;
                }
            }
        }

        return score;
    }

    void playNotes() {
        if (mSound) {
            final ProgressDialog dialog = new ProgressDialog(mActivity);
            dialog.setMessage("Listen...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Log.d(Main.TAG, "Dialog cancelled.");
                    mSoundPlayer.pause();
                }
            });
            dialog.show();

            playNotes(new SoundPlayer.PlayerCallback() {
                @Override
                public void playbackComplete() {
                    Log.d(Main.TAG, "Dismissing playback dialog.");
                    dialog.dismiss();
                }
            });
        }
    }

    protected static void setListenersIntoButtons(View[] buttons, OnClickListener[] listeners) {
        int count = buttons.length;
        for (int i = 0; i < count; i++) {
            buttons[i].setOnClickListener(listeners[i]);
        }
    }

    private OnClickListener mReplayListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            playNotes();
        }
    };
}
