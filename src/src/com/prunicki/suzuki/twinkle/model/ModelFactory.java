package com.prunicki.suzuki.twinkle.model;

import com.prunicki.suzuki.twinkle.ScoreDAO;

import android.database.Cursor;

public final class ModelFactory {
    
    private ModelFactory() {}
    
    /**
     * Creates a player.  Does not advance the cursor.
     * 
     * @param cursor
     * @return a Player
     */
    public static Player createPlayer(Cursor cursor) {
        int id = cursor.getInt(ScoreDAO.TABLE_KEY_INDEX);
        String name = cursor.getString(1);
        
        return new Player(id, name);
    }
}
