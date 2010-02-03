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

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class GameButtonListener implements OnClickListener {
    
    private final Context mContext;
    private Player mPlayer;
    
    public GameButtonListener(Context context) {
        mContext = context;
    }
    
    public final void setPlayer(Player player) {
        mPlayer = player;
    }

    @Override
    public void onClick(View arg0) {
        if (checkSuccess()) {
            SuccessDialog dialog = new SuccessDialog(mContext);
            dialog.show();
        } else {
            mPlayer.playThump();
        }
    }
    
    protected abstract boolean checkSuccess();
}
