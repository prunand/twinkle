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
package com.prunicki.suzuki.twinkle.model;

import android.database.Cursor;

import com.prunicki.suzuki.twinkle.db.ScoreDAO;

public class ModelHelper {

    public static Player fetchPlayer(long id, ScoreDAO dao) {
        Cursor cursor = dao.fetchPlayer(id);
        if (cursor == null) {
            return null;
        }

        try {
            String name = cursor.getString(ScoreDAO.PLAYER_NAME_COLUMN_INDEX);
            int hiScore = cursor.getInt(ScoreDAO.HI_SCORE_COLUMN_INDEX);
            int lastScore = cursor.getInt(ScoreDAO.LAST_SCORE_COLUMN_INDEX);
            int totalScore = cursor.getInt(ScoreDAO.TOTAL_SCORE_COLUMN_INDEX);
            int totalPlayed = cursor.getInt(ScoreDAO.TOTAL_PLAYED_COLUMN_INDEX);
            return new Player(id, name, hiScore, lastScore, totalScore, totalPlayed);
        } finally {
            cursor.close();
        }
    }

    public static void savePlayer(Player player, ScoreDAO dao) {
        dao.savePlayer(player.getId(), player.getName(), player.getHiScore(), player.getLastScore(),
                player.getTotalScore(), player.getTotalPlayed());
    }

    public static void deletePlayer(Player player, ScoreDAO dao) {
        long playerId = player.getId();
        dao.deletePlayer(playerId);
    }
}