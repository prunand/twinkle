package com.prunicki.suzuki.twinkle;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChangePlayerDialog extends TwinkleDialog {
    private final boolean mAllowCancel;
    
    private Cursor mCursor;
    private ListView mlistView;
    private SuzukiApplication mAppCtx;
    private View mAddPlayer;

    public ChangePlayerDialog(Context context, boolean allowCancel) {
        super(context);
        mAllowCancel = allowCancel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeplayer);
        
        mAppCtx = (SuzukiApplication) getContext().getApplicationContext();
        
        setTitle("Choose Player");
        setCancelable(mAllowCancel);
        
        mlistView = (ListView) findViewById(android.R.id.list);
        mAddPlayer = findViewById(R.id.AddPlayer);
        
        mlistView.setOnItemClickListener(itemListener);
        
        if (mAllowCancel) {
            mAddPlayer.setOnClickListener(mAddPlayerListener);
        } else {
            mAddPlayer.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        SuzukiApplication appCtx = mAppCtx;
        
        ScoreDAO dao = appCtx.getDAO();
        mCursor = dao.fetchAllPlayers();
        
        ChangePlayerListViewAdapter cursorAdapter = new ChangePlayerListViewAdapter(appCtx, mCursor);
        mlistView.setAdapter(cursorAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCursor.close();
    }
    
    private View.OnClickListener mAddPlayerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            NewPlayerDialog dlg = new NewPlayerDialog(getContext(), mAllowCancel);
            dlg.show();
        }
    };
    
    private OnItemClickListener itemListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
            SuzukiApplication appCtx = mAppCtx;
            
            ScoreDAO dao = appCtx.getDAO();
            Player player = ModelHelper.fetchPlayer(id, dao);
            appCtx.setCurrentPlayer(player);
            dismiss();
        }
    };
}
