package com.prunicki.suzuki.twinkle.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.prunicki.suzuki.twinkle.util.PropertyChangeUtil;

public class Player {
    public static final String PROP_CHG_NAME = "propChgName";
    public static final String PROP_CHG_LAST_SCORE = "propChgLastScore";
    private static final int MAX_NAME_LENGTH = 15;
    
    private final long mId;
    private String mName;
    private int mHiScore;
    private int mLastScore;
    private int mTotalScore;
    private int mTotalPlayed;
    ArrayList<WeakReference<PropertyChangeListener>> mListeners;
    
    public Player(long id, String name, int hiScore, int lastScore, int totalScore, int totalPlayed) {
        this.mId = id;
        this.mName = clipName(name);
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
    
    public void setName(String name) {
        String oldName = this.mName;
        this.mName = clipName(name);
        PropertyChangeUtil.firePropertyChangeEvent(mListeners, new PropertyChangeEvent(this, PROP_CHG_NAME, oldName, name));
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
    
    private static String clipName(String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            name = name.substring(0, MAX_NAME_LENGTH);
        }
        
        return name;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        PropertyChangeUtil.addPropertyChangeListener(mListeners, listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        PropertyChangeUtil.removePropertyChangeListener(mListeners, listener);
    }
}
