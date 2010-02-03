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
package com.prunicki.suzuki.twinkle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SightSoundDialog extends Dialog {
    
    private View mSightButton;
    private View mSoundButton;
    private Class<? extends Activity> mSeeClass;
    private Class<? extends Activity> mHearClass;

    public SightSoundDialog(Context context, Class<? extends Activity> seeClass,
            Class<? extends Activity> hearClass) {
        super(context);
        
        mSeeClass = seeClass;
        mHearClass = hearClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sightsound);
        
        setTitle(R.string.dialog_sight_sound);
        setCanceledOnTouchOutside(true);
        
        mSightButton = (View) findViewById(R.id.DlgSight);
        mSoundButton = (View) findViewById(R.id.DlgSound);
        
        try {
            mSightButton.setOnClickListener(mSightListener);
            mSoundButton.setOnClickListener(mSoundListener);
        } catch (Exception e) {
            Log.d(Main.TAG, "caught exception: " + e.getMessage(), e);
        }
    }

    private void launch(Class<? extends Activity> activity) {
        cancel();
        Context context = getContext();
        Intent intent = new Intent(context, activity);
        
        context.startActivity(intent);
    }
    
    private View.OnClickListener mSightListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            launch(mSeeClass);
        }
    };
    
    private View.OnClickListener mSoundListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            launch(mHearClass);
        }
    };
}
