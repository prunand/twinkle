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
package com.prunicki.twinkle.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.prunicki.twinkle.util.PropertyChangeUtil;

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
