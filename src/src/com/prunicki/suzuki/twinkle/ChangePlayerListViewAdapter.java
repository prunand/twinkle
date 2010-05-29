package com.prunicki.suzuki.twinkle;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.prunicki.suzuki.twinkle.db.ScoreDAO;

public class ChangePlayerListViewAdapter extends CursorAdapter {
    
    public ChangePlayerListViewAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView textView = (TextView) inflater.inflate(R.layout.changeplayeritem, parent, false);
        
        bindView(textView, context, cursor);
        return textView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = (TextView) view;
        textView.setText(cursor.getString(ScoreDAO.PLAYER_NAME_COLUMN_INDEX));
    }
}
