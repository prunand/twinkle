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