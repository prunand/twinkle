package com.prunicki.suzuki.twinkle.model;

import static com.prunicki.suzuki.twinkle.model.Score.PROP_CHG_LAST_SCORE;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.prunicki.suzuki.twinkle.util.PropertyChangeUtil;

public class Player {
    public static final String PROP_CHG_NAME = "propChgName";
    private static final int MAX_NAME_LENGTH = 15;
    
    private final long mId;
    private String mName;
    private Score mScore;
    private ScoreChangeListener scoreChangeListener;
    ArrayList<WeakReference<PropertyChangeListener>> mListeners;
    
    public Player(long id, String name, Score score) {
        this.mId = id;
        this.mName = clipName(name);
        mScore = score;
        
        scoreChangeListener = new ScoreChangeListener();
        mListeners = new ArrayList<WeakReference<PropertyChangeListener>>();
        
        mScore.addPropertyChangeListener(scoreChangeListener);
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
        return mScore.getHiScore();
    }

    public int getLastScore() {
        return mScore.getLastScore();
    }

    public void setLastScore(int lastScore) {
        mScore.setLastScore(lastScore);
    }

    public int getTotalScore() {
        return mScore.getTotalScore();
    }
    
    public int getTotalPlayed() {
        return mScore.getTotalPlayed();
    }
    
    public float getAverage() {
        return mScore.getAverage();
    }
    
    public Score getScore() {
        return mScore;
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
    
    private class ScoreChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if (PROP_CHG_LAST_SCORE.equals(event.getPropertyName())) {
                PropertyChangeUtil.firePropertyChangeEvent(mListeners, new PropertyChangeEvent(Player.this, PROP_CHG_LAST_SCORE, event.getOldValue(), event.getNewValue()));
            }
        }
    }
}
