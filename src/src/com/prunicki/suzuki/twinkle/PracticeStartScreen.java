package com.prunicki.suzuki.twinkle;

import static com.prunicki.suzuki.twinkle.PracticeScreen.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PracticeStartScreen extends TwinkleActivity {
    
    private View pitchBtn;
    private View seeNoteBtn;
    private View hearNoteBtn;
    private View seeRhythmBtn;
    private View hearRhythmBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);
        
        pitchBtn = findViewById(R.id.Pitch);
        seeNoteBtn = findViewById(R.id.SeeNote);
        hearNoteBtn = findViewById(R.id.HearNote);
        seeRhythmBtn = findViewById(R.id.SeeRhythm);
        hearRhythmBtn = findViewById(R.id.HearRhythm);
        
        pitchBtn.setOnClickListener(new PracticeStartListener(PITCH_PRACTICE_TYPE));
        seeNoteBtn.setOnClickListener(new PracticeStartListener(SEE_NOTE_PRACTICE_TYPE));
        hearNoteBtn.setOnClickListener(new PracticeStartListener(HEAR_NOTE_PRACTICE_TYPE));
        seeRhythmBtn.setOnClickListener(new PracticeStartListener(SEE_RHYTHM_PRACTICE_TYPE));
        hearRhythmBtn.setOnClickListener(new PracticeStartListener(HEAR_RHYTHM_PRACTICE_TYPE));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    
    private class PracticeStartListener implements OnClickListener {
        private int practiceType;
        
        public PracticeStartListener(int practiceType) {
            this.practiceType = practiceType;
        }
        
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PracticeStartScreen.this, PracticeScreen.class);
            intent.putExtra(PracticeScreen.PRACTICE_TYPE_KEY, practiceType);

            startActivity(intent);
        }
    };
}
