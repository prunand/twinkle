package com.prunicki.suzuki.twinkle.model;

import static com.prunicki.suzuki.twinkle.model.Score.DIFFICULTY_LEVEL_HARD;
import static com.prunicki.suzuki.twinkle.model.Score.PROP_CHG_LAST_SCORE;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.prunicki.suzuki.twinkle.Utils;

public class Player {
    public static final String PROP_CHG_DIFFICULTY = "propChgDifficulty";
    
    private final long mId;
    private final String mName;
    private int mDifficulty;
    private Score mEasyScore;
    private Score mHardScore;
    private ScoreChangeListener scoreChangeListener;
    ArrayList<WeakReference<PropertyChangeListener>> mListeners;
    
    public Player(long id, String name, int difficulty, Score easyScore, Score hardScore) {
        this.mId = id;
        this.mName = name;
        this.mDifficulty = difficulty;
        mEasyScore = easyScore;
        mHardScore = hardScore;
        
        scoreChangeListener = new ScoreChangeListener();
        mListeners = new ArrayList<WeakReference<PropertyChangeListener>>();
        
        mEasyScore.addPropertyChangeListener(scoreChangeListener);
        mHardScore.addPropertyChangeListener(scoreChangeListener);
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
        return getCurrentScore().getHiScore();
    }

    public int getLastScore() {
        return getCurrentScore().getLastScore();
    }

    public void setLastScore(int lastScore) {
        getCurrentScore().setLastScore(lastScore);
    }

    public int getTotalScore() {
        return getCurrentScore().getTotalScore();
    }
    
    public int getTotalPlayed() {
        return getCurrentScore().getTotalPlayed();
    }
    
    public float getAverage() {
        return getCurrentScore().getAverage();
    }
    
    public Score getEasyScore() {
        return mEasyScore;
    }
    
    public Score getHardScore() {
        return mHardScore;
    }
    
    private Score getCurrentScore() {
        Score score = mEasyScore;
        
        if (mDifficulty == DIFFICULTY_LEVEL_HARD) {
            score = mHardScore;
        }
        
        return score;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        Utils.addPropertyChangeListener(mListeners, listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        Utils.removePropertyChangeListener(mListeners, listener);
    }
    
    private class ScoreChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (PROP_CHG_LAST_SCORE.equals(event.getPropertyName())) {
                Utils.firePropertyChangeEvent(mListeners, new PropertyChangeEvent(Player.this, PROP_CHG_DIFFICULTY, event.getOldValue(), event.getNewValue()));
            }
        }
    }
}
