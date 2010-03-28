package com.prunicki.suzuki.twinkle;

import static com.prunicki.suzuki.twinkle.Constants.PLAYER_ID_KEY;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.prunicki.suzuki.twinkle.db.ScoreDAO;
import com.prunicki.suzuki.twinkle.model.ModelHelper;
import com.prunicki.suzuki.twinkle.model.Player;

public class ChangePlayerScreen extends TwinkleActivity {
    private Cursor mCursor;
    private ListView mlistView;
    private TwinkleApplication mAppCtx;
    private View mCancel;
    private View mAddPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeplayer);
        
        mAppCtx = (TwinkleApplication) getApplicationContext();
        
        setTitle("Choose Player");
        
        mlistView = (ListView) findViewById(android.R.id.list);
        mCancel = findViewById(R.id.Cancel);
        mAddPlayer = findViewById(R.id.AddPlayer);
        
        mlistView.setOnItemClickListener(mItemListener);
        mlistView.setOnItemLongClickListener(mItemLongClickListener);
        
        mAddPlayer.setOnClickListener(mAddPlayerListener);
        mCancel.setOnClickListener(mCancelListener);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        TwinkleApplication appCtx = mAppCtx;
        
        ScoreDAO dao = appCtx.getDAO();
        mCursor = dao.fetchAllPlayers();
        
        ChangePlayerListViewAdapter cursorAdapter = new ChangePlayerListViewAdapter(appCtx, mCursor);
        mlistView.setAdapter(cursorAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCursor.close();
    }
    
    void setCurrentPlayer(long id) {
        TwinkleApplication appCtx = mAppCtx;
        
        ScoreDAO dao = appCtx.getDAO();
        Player player = ModelHelper.fetchPlayer(id, dao);
        appCtx.setCurrentPlayer(player);
    }
    
    private View.OnClickListener mCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private View.OnClickListener mAddPlayerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NewPlayerDialog dlg = new NewPlayerDialog(ChangePlayerScreen.this);
            dlg.setOnDismissListener(mAddPlayerDismissListener);
            dlg.show();
        }
    };
    
    private OnDismissListener mAddPlayerDismissListener = new OnDismissListener() {
        
        @Override
        public void onDismiss(DialogInterface dialog) {
            NewPlayerDialog dlg = (NewPlayerDialog) dialog;
            long newPlayerId = dlg.getNewPlayerId();
            if (newPlayerId >= 0) {
                setCurrentPlayer(newPlayerId);
                finish();
            } else {
                mCursor.requery();
            }
        }
    };
    
    private OnItemClickListener mItemListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
            setCurrentPlayer(id);
            finish();
        }
    };
    
    private OnItemLongClickListener mItemLongClickListener = new OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> listView, View view, int position, long id) {
            Intent intent = new Intent(ChangePlayerScreen.this, PlayerInfoScreen.class);
            intent.putExtra(PLAYER_ID_KEY, id);
            startActivity(intent);
            
            return true;
        }
    };
}
