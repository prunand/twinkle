package com.prunicki.suzuki.twinkle.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.prunicki.suzuki.twinkle.util.PropertyChangeUtil;

public class Score {
    public static final String PROP_CHG_LAST_SCORE = "propChgLastScore";
    
    private final long mId;
    private final long mPlayerId;
    private int mHiScore;
    private int mLastScore;
    private int mTotalScore;
    private int mTotalPlayed;
    private ArrayList<WeakReference<PropertyChangeListener>> mListeners;
    
    public Score(long id, long playerId, int hiScore, int lastScore, int totalScore, int totalPlayed) {
        mId = id;
        mPlayerId = playerId;
        mHiScore = hiScore;
        mLastScore = lastScore;
        mTotalScore = totalScore;
        mTotalPlayed = totalPlayed;
        
        mListeners = new ArrayList<WeakReference<PropertyChangeListener>>();
    }
    
    public long getId() {
        return mId;
    }
    
    public long getPlayerId() {
        return mPlayerId;
    }

    public int getHiScore() {
        return mHiScore;
    }

    public int getLastScore() {
        return mLastScore;
    }

    public void setLastScore(int lastScore) {
        int oldLastScore = lastScore;
        this.mLastScore = lastScore;
        if (lastScore > mHiScore) {
            mHiScore = lastScore;
        }
        mTotalScore += lastScore;
        mTotalPlayed++;
        
        PropertyChangeUtil.firePropertyChangeEvent(mListeners, new PropertyChangeEvent(this, PROP_CHG_LAST_SCORE, oldLastScore, lastScore));
    }

    public int getTotalScore() {
        return mTotalScore;
    }
    
    public int getTotalPlayed() {
        return mTotalPlayed;
    }
    
    public float getAverage() {
        return mTotalPlayed == 0 ? 0f :(float) mTotalScore / mTotalPlayed;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        PropertyChangeUtil.addPropertyChangeListener(mListeners, listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        PropertyChangeUtil.removePropertyChangeListener(mListeners, listener);
    }
}
