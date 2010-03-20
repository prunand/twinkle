package com.prunicki.suzuki.twinkle;

import com.prunicki.suzuki.twinkle.db.ScoreDAO;
import com.prunicki.suzuki.twinkle.model.ModelHelper;
import com.prunicki.suzuki.twinkle.model.Player;
import com.prunicki.suzuki.twinkle.model.Score;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class NewPlayerDialog extends TwinkleDialog {
    private TwinkleApplication mAppCtx;
    private EditText mName;
    private View mAdd;
    private View mCancel;

    public NewPlayerDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newplayer);
        
        mAppCtx = (TwinkleApplication) getContext().getApplicationContext();
        
        setTitle("New Player");
        
        mName = (EditText) findViewById(R.id.Name);
        mAdd = findViewById(R.id.Add);
        mCancel = findViewById(R.id.Cancel);
        
        mAdd.setOnClickListener(mAddListener);
        
        mCancel.setOnClickListener(mCancelListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    
    private View.OnClickListener mAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = mName.getText().toString();
            
            TwinkleApplication appCtx = mAppCtx;
            ScoreDAO dao = appCtx.getDAO();
            long id = dao.createPlayer(name, Score.DIFFICULTY_LEVEL_EASY);
            long easyId = dao.createScore(id, Score.DIFFICULTY_LEVEL_EASY);
            long hardId = dao.createScore(id, Score.DIFFICULTY_LEVEL_HARD);
            if (id >= 0 && easyId >= 0 && hardId >= 0) {
                Player player = ModelHelper.fetchPlayer(id, dao);
                
                appCtx.setCurrentPlayer(player);
                dismiss();
            }
        }
    };
    
    private View.OnClickListener mCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
        }
    };
}
