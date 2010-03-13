package com.prunicki.suzuki.twinkle;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChangePlayerDialog extends TwinkleDialog {
    private Cursor cursor;
    private ListView mlistView;

    public ChangePlayerDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeplayer);
        
        setTitle("Choose Player");
//        Display display = getWindow().getWindowManager().getDefaultDisplay();
//        int windowHeight = display.getHeight();
        
        mlistView = (ListView) findViewById(android.R.id.list);
        mlistView.setOnItemClickListener(itemListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        
        SuzukiApplication appCtx = (SuzukiApplication) getContext().getApplicationContext();
        
        ScoreDAO dao = appCtx.getDAO();
        cursor = dao.fetchAllPlayers();
        
        ChangePlayerListViewAdapter cursorAdapter = new ChangePlayerListViewAdapter(appCtx, cursor);
        mlistView.setAdapter(cursorAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cursor.close();
    }
    
    private OnItemClickListener itemListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> listView, View view, int arg2, long arg3) {
        }
    };
}
