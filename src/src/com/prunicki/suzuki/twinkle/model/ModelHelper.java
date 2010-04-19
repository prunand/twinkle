package com.prunicki.suzuki.twinkle.model;

import com.prunicki.suzuki.twinkle.db.DdlBuilder;
import com.prunicki.suzuki.twinkle.db.ScoreDAO;

import android.database.Cursor;

public class ModelHelper {

    public static Player fetchPlayer(long id, ScoreDAO dao) {
        Cursor cursor = dao.fetchPlayer(id);
        if (cursor == null) {
            return null;
        }

        try {
            String name = cursor.getString(ScoreDAO.PLAYER_NAME_COLUMN_INDEX);
            Score score = fetchScore(id, dao);
            if (score == null) {
                return null;
            }
            return new Player(id, name, score);
        } finally {
            cursor.close();
        }
    }

    public static void savePlayer(Player player, ScoreDAO dao) {
        dao.savePlayer(player.getId(), player.getName());
        Score score = player.getScore();
        dao.saveScore(score.getId(), score.getPlayerId(), score.getHiScore(),
                score.getLastScore(), score.getTotalScore(), score.getTotalPlayed());
    }

    public static void deletePlayer(Player player, ScoreDAO dao) {
        long playerId = player.getId();
        dao.deleteScoresForPlayer(playerId);
        dao.deletePlayer(playerId);
    }
    
    private static Score fetchScore(long id, ScoreDAO dao) {
        Cursor cursor = dao.fetchScoresForPlayer(id);
        if (cursor == null) {
            return null;
        }
        
        try {
            return mapScore(cursor);
        } finally {
            cursor.close();
        }
    }
    
    private static Score mapScore(Cursor cursor) {
        long scoreId = cursor.getLong(DdlBuilder.TABLE_KEY_INDEX);
        int playerId = cursor.getInt(ScoreDAO.SCORE_PLAYER_ID_COLUMN_INDEX);
        int hiScore = cursor.getInt(ScoreDAO.SCORE_HI_SCORE_COLUMN_INDEX);
        int lastScore = cursor.getInt(ScoreDAO.SCORE_LAST_SCORE_COLUMN_INDEX);
        int totalScore = cursor.getInt(ScoreDAO.SCORE_TOTAL_SCORE_COLUMN_INDEX);
        int totalPlayed = cursor.getInt(ScoreDAO.SCORE_TOTAL_PLAYED_COLUMN_INDEX);
        
        return new Score(scoreId, playerId, hiScore, lastScore, totalScore, totalPlayed);
    }
}