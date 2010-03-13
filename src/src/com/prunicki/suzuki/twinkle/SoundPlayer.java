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

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.JetPlayer;
import android.media.SoundPool;
import android.media.JetPlayer.OnJetEventListener;
import android.util.Log;

public class SoundPlayer {
    
    public static final int E_NOTE = 0;
    public static final int D_NOTE = 1;
    public static final int C_NOTE = 2;
    public static final int B_NOTE = 3;
    public static final int A_NOTE = 4;
    public static final int G_NOTE = 5;
    public static final int F_NOTE = 6;
    
    public static final int MISSISSIPPI_STOP_STOP_RHYTHM = 0;
    public static final int MISSISSIPPI_ALLIGATOR_RHYTHM = 1;
    public static final int DOWN_PONY_UP_PONY_RHYTHM = 2;
    public static final int ICE_CREAM_SH_CONE_RHYTHM = 3;
    
    private static final int MUTE_MASK = 0xFFFF;
    private static final int STARTING_TRACK = 0x02;
    
    private static final int QUARTER_NOTE_SEGMENT = 0;
    private static final int THUMP_SEGMENT = 1;
    private static final int NOTES_SEGMENT = 2;
    private static final int FIRST_RHYTHM_SEGMENT = 3;
    
    private boolean mInitialized;
    private JetPlayer mSuzukiJetPlayer;
    private SoundPool mSoundPool;
    private int[] mSingleInt;
    private int mSuccessSoundID;
    private int mSoundStream;
    private Timer mTimer;
    private PlayerCallback mCallback;
    
    public synchronized void initialize(Context context) {
        if (!mInitialized) {
            mSuzukiJetPlayer = JetPlayer.getJetPlayer();
            mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            mSuccessSoundID = mSoundPool.load(context, R.raw.good_job, 1);
            
            AssetFileDescriptor piano = context.getResources().openRawResourceFd(R.raw.suzuki);
            mSuzukiJetPlayer.loadJetFile(piano);
            mSuzukiJetPlayer.setEventListener(new NoteMidiEventListener());
            
            mInitialized = true;
            mSingleInt = new int[1];
            
            mTimer = new Timer(SoundPlayer.class.getSimpleName(), true);
        }
    }

    public synchronized void release() {
        if (mInitialized) {
            pause();
            doRelease();
        }
    }

    public synchronized void playNote(int note, PlayerCallback callback) {
        mSingleInt[0] = note;
        playSuzukiJet(NOTES_SEGMENT, mSingleInt, callback);
    }

    public synchronized void playNote(int notes[], PlayerCallback callback) {
        playSuzukiJet(NOTES_SEGMENT, notes, callback);
    }

    public synchronized void playThump() {
        mSingleInt[0] = 0;
        playSuzukiJet(THUMP_SEGMENT, mSingleInt, null);
    }

    public synchronized void playRhythm(int rhythm, PlayerCallback callback) {
        mSingleInt[0] = 0;
        playSuzukiJet(rhythm + FIRST_RHYTHM_SEGMENT, mSingleInt, callback);
    }
    
    public synchronized void playSuccess(TimerTask timerTask) {
        if (mInitialized) {
            pause();
            
            mSoundStream = mSoundPool.play(mSuccessSoundID, 0.2f, 0.2f, 0, 0, 1.0f);
            mTimer.schedule(timerTask, 3000);
        }
    }
    
    public synchronized void pause() {
        if (mInitialized) {
            if (mSuzukiJetPlayer.pause()) {
                mSuzukiJetPlayer.clearQueue();
            } else {
                Log.w(Main.TAG, "Could not pause the jet player.");
            }
            mSoundPool.stop(mSoundStream);
            mCallback = null;
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            doRelease();
        } catch (Throwable t) {
            super.finalize();
        }
    }

    private void playSuzukiJet(int segment, int[] tracks, PlayerCallback callback) {
        if (mInitialized) {
            pause();
            
            int trackBit = 0;
            int trackMask = 0;
            boolean queued = false;
            for (int i = 0; i < tracks.length; i++) {
                trackBit = STARTING_TRACK << tracks[i];
                trackMask = MUTE_MASK ^ trackBit;
                queued = mSuzukiJetPlayer.queueJetSegment(segment, -1, 0, 0, trackMask, (byte) 0);
                if (queued) {
                    if (i < tracks.length - 1) {
                        mSuzukiJetPlayer.queueJetSegment(QUARTER_NOTE_SEGMENT, -1, 0, 0, MUTE_MASK, (byte) 0);
                    }
                } else {
                    break;
                }
            }
            
            if (queued) {
                if (Log.isLoggable(Main.TAG, Log.DEBUG)) {
                    Log.d(Main.TAG, "Segment " + segment + " queued.");
                }
                mSuzukiJetPlayer.play();
            } else {
                if (Log.isLoggable(Main.TAG, Log.DEBUG)) {
                    Log.d(Main.TAG, "Segment " + segment + " _NOT_ queued.");
                }
            }
            mCallback = callback;
        } else {
            if (Log.isLoggable(Main.TAG, Log.WARN)) {
                Log.w(Main.TAG, "Attempting to play Segment " + segment + " when not initialized.");
            }
        }
    }
    
    private void doRelease() {
        try {
            if (mSuzukiJetPlayer != null) {
                mSuzukiJetPlayer.closeJetFile();
                mSuzukiJetPlayer = null;
            }
        } catch (Exception e) {}
        
        try {
            if (mSoundPool != null) {
                mSoundPool.release();
                mSoundPool = null;
            }
        } catch (Exception e) {}

        try {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
        } catch (Exception e) {}
        
        mInitialized = false;
    }
    
    private class NoteMidiEventListener implements OnJetEventListener {

        @Override
        public void onJetEvent(JetPlayer player, short segment, byte track,
                byte channel, byte controller, byte value) {
            Log.d(Main.TAG, "Received Jet event: " + value);
        }

        @Override
        public void onJetNumQueuedSegmentUpdate(JetPlayer player, int nbSegments) {
            Log.d(Main.TAG, "Number of queued segments updated: " + nbSegments);
            if (mCallback != null && nbSegments == 0) {
                mCallback.playbackComplete();
            }
        }

        @Override
        public void onJetPauseUpdate(JetPlayer player, int paused) {
            Log.d(Main.TAG, "Jet paused: " + paused);
        }

        @Override
        public void onJetUserIdUpdate(JetPlayer player, int userId,
                int repeatCount) {
            Log.d(Main.TAG, "User id updated: " + userId);
        }
    }
    
    public static interface PlayerCallback {
        void playbackComplete();
    }
}