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
package com.prunicki.twinkle.game;

import static com.prunicki.twinkle.Constants.TAG;

import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.prunicki.twinkle.GameButtonListener;
import com.prunicki.twinkle.GameRoundCallback;
import com.prunicki.twinkle.R;
import com.prunicki.twinkle.SoundPlayer;
import com.prunicki.twinkle.TwinkleApplication;
import com.prunicki.twinkle.util.WidgetUtil;

public abstract class GameRound {
    public final int mResourceId;
    private ViewGroup viewGroup;
    private final boolean mSound;

    private final Random mRandom;
    private final int mMaxRandom;
    
    protected Activity mActivity;
    protected TwinkleApplication mApp;
    protected View[] mButtons;
    protected GameButtonListener[] mButtonListeners;
    protected int mFirstRun;
    protected GameRoundCallback mCallback;

    private View mReplayButton;

    public GameRound(int resourceId, ViewGroup viewGroup, boolean sound, int numGameButtons, GameRoundCallback callback) {
        this.mResourceId = resourceId;
        this.viewGroup = viewGroup;
        mSound = sound;
        mMaxRandom = numGameButtons;
        mCallback = callback;

        mButtons = new View[numGameButtons];
        mButtonListeners = new GameButtonListener[numGameButtons];
        mRandom = new Random();
    }

    public void onCreate(Activity activity) {
        mActivity = activity;
        mApp = (TwinkleApplication) mActivity.getApplication();

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
        GameButtonListener.setSoundPlayerIntoListeners(mButtonListeners, mApp.getSoundPlayer());

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

    public int getScore() {
        int score = getMaxScore();

        for (int i = 0; i < mMaxRandom; i++) {
            if (mButtonListeners[i].isClickedWrong()) {
                score--;
            }
        }

        return score;
    }

    void playNotes() {
        if (mSound) {
            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    WidgetUtil.setChildWidgetsEnabled(viewGroup, false);
                }

                @Override
                protected void onPostExecute(Void result) {
                    final ProgressDialog dialog = new ProgressDialog(mActivity);
                    dialog.setMessage("Listen...");
                    dialog.setIndeterminate(true);
                    dialog.setCancelable(true);
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            WidgetUtil.setChildWidgetsEnabled(viewGroup, true);
                        }
                    });
                    dialog.setOnCancelListener(new OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Log.d(TAG, "Dialog cancelled.");
                            mApp.getSoundPlayer().pause();
                        }
                    });
                    dialog.show();
                    
                    playNotes(new SoundPlayer.PlayerCallback() {
                        @Override
                        public void playbackComplete() {
                            Log.d(TAG, "Dismissing playback dialog.");
                            dialog.dismiss();
                        }
                    });
                }

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        //Nothing to do really.
                    }
                    
                    return null;
                }
            };
            
            task.execute((Void) null);
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
