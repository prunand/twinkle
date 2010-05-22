package com.prunicki.suzuki.twinkle;

import static com.prunicki.suzuki.twinkle.Constants.PLAYER_ID_KEY;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.prunicki.suzuki.twinkle.db.ScoreDAO;
import com.prunicki.suzuki.twinkle.model.ModelHelper;
import com.prunicki.suzuki.twinkle.model.Player;

public class PlayerInfoScreen extends TwinkleActivity {
    Player mPlayer;
    
    private EditText mNameField;
    private View mDeleteBtn;
    private View mRenameBtn;
    private View mCancelBtn;

    TwinkleApplication mApp;
    long mCurrentPlayerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerinfo);
        setTitle("Player Info");
        
        mApp =  (TwinkleApplication) getApplication();
        
        mNameField = (EditText) findViewById(R.id.Name);
        TextView hiScoreView = (TextView) findViewById(R.id.HiScore);
        TextView avgScoreView = (TextView) findViewById(R.id.AvgScore);
        TextView lastScoreView = (TextView) findViewById(R.id.LastScore);
        mDeleteBtn = findViewById(R.id.Delete);
        mRenameBtn = findViewById(R.id.Rename);
        mCancelBtn = findViewById(R.id.Cancel);
        
        Player currentPlayer = mApp.getCurrentPlayer();
        long playerId = getIntent().getLongExtra(PLAYER_ID_KEY, -1);
        Player player = findPlayer(currentPlayer, playerId, mApp.getDAO());
        
        hiScoreView.setText("Hi Score: " + player.getHiScore());
        avgScoreView.setText("Average Score: " + Math.round(player.getAverage() * 10) / 10f);
        lastScoreView.setText("Last Score: " + player.getLastScore());
        
        mNameField.setText(player.getName());
        mDeleteBtn.setOnClickListener(mDeleteListener);
        mRenameBtn.setOnClickListener(mRenameListener);
        mCancelBtn.setOnClickListener(mCancelListener);
        
        mPlayer = player;
        mCurrentPlayerid = currentPlayer == null ? -1 : currentPlayer.getId();
    }

    private static Player findPlayer(Player currentPlayer, long playerId, ScoreDAO dao) {
        if (playerId < 0) {
            throw new IllegalStateException("Invalid player id: " + playerId);
        }
        
        Player player = currentPlayer;
        if (player == null || player.getId() != playerId) {
            player = ModelHelper.fetchPlayer(playerId, dao);
        }
        if (player == null) {
            throw new IllegalStateException("No player found for id: " + playerId);
        }
        return player;
    }
    
    private OnClickListener mDeleteListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final Player player = mPlayer;
            
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PlayerInfoScreen.this);
            dialogBuilder.setTitle("Delete Player?");
            dialogBuilder.setMessage("Are you sure you want to delete " +  mPlayer.getName() + "?");
            dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    long playerId = player.getId();
                    ModelHelper.deletePlayer(player, mApp.getDAO());
                    if (playerId == mCurrentPlayerid) {
                        mApp.setCurrentPlayer(null);
                    }
                    finish();
                }
            });
            dialogBuilder.setNegativeButton("Keep", null);
            
            AlertDialog dlg = dialogBuilder.create();
            dlg.show();
        }
    };
    
    private OnClickListener mRenameListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            String newName = mNameField.getText().toString();
            ScoreDAO dao = mApp.getDAO();
            Cursor cursor = dao.fetchPlayer(newName);
            if (cursor == null) {
                mPlayer.setName(newName);
                ModelHelper.savePlayer(mPlayer, dao);
            } else {
                cursor.close();
            }
            finish();
        }
    };
    
    private OnClickListener mCancelListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
