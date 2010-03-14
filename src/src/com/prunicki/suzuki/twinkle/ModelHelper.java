package com.prunicki.suzuki.twinkle;

import android.database.Cursor;

public class ModelHelper {

    public static Player fetchPlayer(long id, ScoreDAO dao) {
        Cursor cursor = dao.fetchPlayer(id);
        if (cursor == null) {
            return null;
        }

        try {
            String name = cursor.getString(ScoreDAO.PLAYER_NAME_COLUMN_INDEX);
            int hiScore = cursor.getInt(ScoreDAO.PLAYER_HI_SCORE_COLUMN_INDEX);
            int lastScore = cursor.getInt(ScoreDAO.PLAYER_LAST_SCORE_COLUMN_INDEX);
            int totalScore = cursor.getInt(ScoreDAO.PLAYER_TOTAL_SCORE_COLUMN_INDEX);
            int totalPlayed = cursor.getInt(ScoreDAO.PLAYER_TOTAL_PLAYED_COLUMN_INDEX);
            return new Player(id, name, hiScore, lastScore, totalScore, totalPlayed);
        } finally {
            cursor.close();
        }
    }

    public static void savePlayer(Player player, ScoreDAO dao) {
        dao.savePlayer(player.getId(), player.getName(), player.getHiScore(), player.getLastScore(),
                player.getTotalScore(), player.getTotalPlayed());
    }
}