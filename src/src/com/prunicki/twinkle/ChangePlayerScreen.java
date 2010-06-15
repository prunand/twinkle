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
package com.prunicki.twinkle;

import static com.prunicki.twinkle.Constants.PLAYER_ID_KEY;
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

import com.prunicki.twinkle.db.ScoreDAO;
import com.prunicki.twinkle.model.ModelHelper;
import com.prunicki.twinkle.model.Player;

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
        setTitle("Choose Player");
        
        TwinkleApplication appCtx = (TwinkleApplication) getApplicationContext();
        mAppCtx = appCtx;
        
        mlistView = (ListView) findViewById(android.R.id.list);
        mCancel = findViewById(R.id.Cancel);
        mAddPlayer = findViewById(R.id.AddPlayer);
        
        mlistView.setOnItemClickListener(mItemListener);
        mlistView.setOnItemLongClickListener(mItemLongClickListener);
        
        mAddPlayer.setOnClickListener(mAddPlayerListener);
        mCancel.setOnClickListener(mCancelListener);
        
        ScoreDAO dao = appCtx.getDAO();
        mCursor = dao.fetchAllPlayers();
        
        ChangePlayerListViewAdapter cursorAdapter = new ChangePlayerListViewAdapter(appCtx, mCursor);
        mlistView.setAdapter(cursorAdapter);
        
        startManagingCursor(mCursor);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mCursor.requery();
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
