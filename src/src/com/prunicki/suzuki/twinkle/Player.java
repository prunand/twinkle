package com.prunicki.suzuki.twinkle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Player {
    public static final String PROP_CHG_LAST_SCORE = "propChgLastScore";
    public static final String PROP_CHG_DIFFICULTY = "propChgDifficulty";
    
    private final long mId;
    private final String mName;
    private int mDifficulty;
    private int mHiScore;
    private int mLastScore;
    private int mTotalScore;
    private int mTotalPlayed;
    private ArrayList<WeakReference<PropertyChangeListener>> mListeners;
    
    public Player(long id, String name, int difficulty, int hiScore, int lastScore,
            int totalScore, int totalPlayed) {
        this.mId = id;
        this.mName = name;
        this.mDifficulty = difficulty;
        this.mHiScore = hiScore;
        this.mLastScore = lastScore;
        this.mTotalScore = totalScore;
        this.mTotalPlayed = totalPlayed;
        
        mListeners = new ArrayList<WeakReference<PropertyChangeListener>>();
    }
    
    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(int difficulty) {
        int oldDifficulty = mDifficulty;
        this.mDifficulty = difficulty;
        Utils.firePropertyChangeEvent(mListeners, new PropertyChangeEvent(this, PROP_CHG_DIFFICULTY, oldDifficulty, difficulty));
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
        
        Utils.firePropertyChangeEvent(mListeners, new PropertyChangeEvent(this, PROP_CHG_LAST_SCORE, oldLastScore, lastScore));
    }

    public int getTotalScore() {
        return mTotalScore;
    }
    
    public int getTotalPlayed() {
        return mTotalPlayed;
    }
    
    public float getAverage() {
        return (float) mTotalScore / mTotalPlayed;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        Utils.addPropertyChangeListener(mListeners, listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        Utils.removePropertyChangeListener(mListeners, listener);
    }
}
