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

import android.app.Application;

public class SuzukiApplication extends Application {
    private Player mPlayer;
    private ScoreDAO mDAO;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        
        releasePlayer();
        releaseDAO();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        
        releasePlayer();
    }
    
    public Player getPlayer() {
        if (mPlayer == null) {
            mPlayer = new Player();
            mPlayer.initialize(this);
        }
        
        return mPlayer;
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
    
    public ScoreDAO getDAO() {
        if (mDAO == null) {
            mDAO = new ScoreDAO(this);
            mDAO.open();
            mDAO.createPlayer("Anna");
            mDAO.createPlayer("Emily");
            mDAO.createPlayer("John");
            mDAO.createPlayer("Levi");
            mDAO.createPlayer("Rita");
            mDAO.createPlayer("Fred");
            mDAO.createPlayer("Francis");
            mDAO.createPlayer("Frederick");
            mDAO.createPlayer("Johanson");
        }
        
        return mDAO;
    }
    
    private void releaseDAO() {
        if (mDAO != null) {
            mDAO.release();
            mDAO = null;
        }
    }
}
