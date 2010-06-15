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

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.prunicki.twinkle.db.ScoreDAO;

public class NewPlayerDialog extends TwinkleDialog {
    private TwinkleApplication mAppCtx;
    private EditText mName;
    private View mAdd;
    private View mCancel;
    long mNewPlayerId;

    public NewPlayerDialog(Context context) {
        super(context);
        
        mNewPlayerId = -1;
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
    
    public long getNewPlayerId() {
        return mNewPlayerId;
    }
    
    private View.OnClickListener mAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = mName.getText().toString();
            
            if (name.length() > 0) {
                ScoreDAO dao = mAppCtx.getDAO();
                mNewPlayerId = dao.createPlayer(name);
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
