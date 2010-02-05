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

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.app.Application;

public class SuzukiApplication extends Application {
    private static final Long PLAYER = new Long(1);
    
    private HashMap<Long, SoftReference<Object>> map;

    @Override
    public void onCreate() {
        super.onCreate();
        
        map = new HashMap<Long, SoftReference<Object>>();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        
        releasePlayer();
        map.clear();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        
        releasePlayer();
    }
    
    public Player getPlayer() {
        SoftReference<Object> playerRef = map.get(PLAYER);
        Player player = (Player) (playerRef == null ? null : playerRef.get());
        
        if (player == null) {
            player = new Player();
            player.initialize(this);
            map.put(PLAYER, new SoftReference<Object>(player));
        }
        
        return (Player) player;
    }

    private void releasePlayer() {
        SoftReference<Object> playerRef = map.get(PLAYER);
        Player player = (Player) (playerRef == null ? null : playerRef.get());
        
        if (player != null) {
            player.release();
        }
    }
}
