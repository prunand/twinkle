package com.prunicki.suzuki.twinkle;

import static com.prunicki.suzuki.twinkle.PracticeScreen.HEAR_RHYTHM_PRACTICE_TYPE;
import static com.prunicki.suzuki.twinkle.PracticeScreen.PITCH_NOTE_PRACTICE_TYPE;
import static com.prunicki.suzuki.twinkle.PracticeScreen.PITCH_PRACTICE_TYPE;
import static com.prunicki.suzuki.twinkle.PracticeScreen.READ_LENGTH_PRACTICE_TYPE;
import static com.prunicki.suzuki.twinkle.PracticeScreen.READ_NOTE_PRACTICE_TYPE;
import static com.prunicki.suzuki.twinkle.PracticeScreen.READ_REST_PRACTICE_TYPE;
import static com.prunicki.suzuki.twinkle.PracticeScreen.SEE_RHYTHM_PRACTICE_TYPE;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PracticeStartScreen extends TwinkleActivity {
    
    private View pitchRelativeBtn;
    private View pitchNoteBtn;
    private View readNoteBtn;
    private View readRestBtn;
    private View readLengthBtn;
    private View seeRhythmBtn;
    private View hearRhythmBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice);
        
        pitchRelativeBtn = findViewById(R.id.PitchRelative);
        pitchNoteBtn = findViewById(R.id.PitchNote);
        readNoteBtn = findViewById(R.id.ReadNote);
        readRestBtn = findViewById(R.id.ReadRest);
        readLengthBtn = findViewById(R.id.ReadLength);
        seeRhythmBtn = findViewById(R.id.SeeRhythm);
        hearRhythmBtn = findViewById(R.id.HearRhythm);
        
        pitchRelativeBtn.setOnClickListener(new PracticeStartListener(PITCH_PRACTICE_TYPE));
        pitchNoteBtn.setOnClickListener(new PracticeStartListener(PITCH_NOTE_PRACTICE_TYPE));
        readNoteBtn.setOnClickListener(new PracticeStartListener(READ_NOTE_PRACTICE_TYPE));
        readRestBtn.setOnClickListener(new PracticeStartListener(READ_REST_PRACTICE_TYPE));
        readLengthBtn.setOnClickListener(new PracticeStartListener(READ_LENGTH_PRACTICE_TYPE));
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
