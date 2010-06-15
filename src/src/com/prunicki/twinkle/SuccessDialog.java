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

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.prunicki.twinkle.SoundPlayer.PlayerCallback;

public class SuccessDialog {
    private TwinkleApplication mApp;
    private View mSuccessImage;
    private SuccessPlayerCallback mPlayerCallback;
    private TextView mSuccessText;
    private Activity mActivity;
    AtomicBoolean stopped = new AtomicBoolean(false);
    AlertDialog mdialog;
    
    public SuccessDialog(Context context, int score) {
        mActivity = (Activity) context;
        mApp = (TwinkleApplication) mActivity.getApplication();
        mPlayerCallback = new SuccessPlayerCallback();
        
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View successView = inflater.inflate(R.layout.success, null);
        
        mSuccessImage = successView.findViewById(R.id.SuccessImage);
        mSuccessText = (TextView) successView.findViewById(R.id.SuccessText);
        
        mSuccessImage.setOnClickListener(mSuccessListener);
        
        String text;
        if (score < 0) {
            text = mActivity.getResources().getString(R.string.great_job);
        } else {
            text = mActivity.getResources().getString(R.string.you_won_stars);
            text = String.format(text, score);
        }
        mSuccessText.setText(text);
        
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(successView);
        
        mdialog = dialogBuilder.create();
        mdialog.setCanceledOnTouchOutside(true);
        mdialog.setOnCancelListener(mCancelListener);
        mdialog.setOnDismissListener(mOnDismissListener);
    }
    
    protected void show() {
        mdialog.show();
        mApp.getSoundPlayer().playSuccess(mPlayerCallback);
    }
    
    void pausePlayer() {
        stopped.set(true);
        mApp.getSoundPlayer().pause();
    }

    private OnCancelListener mCancelListener = new OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            pausePlayer();
            mActivity.finish();
        }
    };
    
    private View.OnClickListener mSuccessListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mdialog.cancel();
        }
    };
    
    private DialogInterface.OnDismissListener mOnDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            pausePlayer();
        }
    };
    
    private class SuccessPlayerCallback implements PlayerCallback {
        @Override
        public void playbackComplete() {
            if (!stopped.get()) {
                pausePlayer();
                mdialog.cancel();
            }
        }
    }
}
