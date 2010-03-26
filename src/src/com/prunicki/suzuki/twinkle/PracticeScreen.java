package com.prunicki.suzuki.twinkle;

import android.os.Bundle;
import android.view.ViewGroup;

import com.prunicki.suzuki.twinkle.game.GameRound;
import com.prunicki.suzuki.twinkle.game.HearNote;
import com.prunicki.suzuki.twinkle.game.HearRhythm;
import com.prunicki.suzuki.twinkle.game.Pitch;
import com.prunicki.suzuki.twinkle.game.SeeNote;
import com.prunicki.suzuki.twinkle.game.SeeRhythm;

public class PracticeScreen extends TwinkleActivity implements GameRoundCallback {
    public static final String PRACTICE_TYPE_KEY = "practiceType";
    public static final int PITCH_PRACTICE_TYPE = 0;
    public static final int SEE_NOTE_PRACTICE_TYPE = 1;
    public static final int HEAR_NOTE_PRACTICE_TYPE = 2;
    public static final int SEE_RHYTHM_PRACTICE_TYPE = 3;
    public static final int HEAR_RHYTHM_PRACTICE_TYPE = 4;
    
    public static final String DIFFICULTY_LEVEL_KEY = "difficultyLevel";
    
    private ViewGroup mGameView;
    private GameRound mGameRound;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        int practiceType = getIntent().getIntExtra(PRACTICE_TYPE_KEY, -1);
        
        GameRound gameRound = null;
        switch(practiceType) {
            case PITCH_PRACTICE_TYPE:
                gameRound = new Pitch(this);
                break;
            case SEE_NOTE_PRACTICE_TYPE:
                gameRound = new SeeNote(this);
                break;
            case HEAR_NOTE_PRACTICE_TYPE:
                gameRound = new HearNote(this);
                break;
            case SEE_RHYTHM_PRACTICE_TYPE:
                gameRound = new SeeRhythm(this);
                break;
            case HEAR_RHYTHM_PRACTICE_TYPE:
                gameRound = new HearRhythm(this);
                break;
            default:
                throw new IllegalArgumentException("Unkown practice type " + practiceType);
        }
        
        mGameRound = gameRound;
        mGameView = (ViewGroup) findViewById(R.id.GameView);
        initCurrentGameRound(gameRound);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        mGameRound.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        mGameRound.onPause();
    }

    @Override
    public void roundComplete() {
        SuccessDialog dlg = new SuccessDialog(this, -1);
        dlg.show();
    }
    
    private void initCurrentGameRound(GameRound gameRound) {
        this.getLayoutInflater().inflate(gameRound.mResourceId, mGameView);
        this.getLayoutInflater().inflate(R.layout.replay_button, mGameView);
        gameRound.onCreate(PracticeScreen.this);
    }
}
