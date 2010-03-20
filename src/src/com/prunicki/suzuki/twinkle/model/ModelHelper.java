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
            int difficulty = cursor.getInt(ScoreDAO.PLAYER_DIFFICULTY_COLUMN_INDEX);
            Score[] scores = fetchScores(id, dao);
            if (scores == null) {
                return null;
            }
            return new Player(id, name, difficulty, scores[Score.DIFFICULTY_LEVEL_EASY],
                    scores[Score.DIFFICULTY_LEVEL_HARD]);
        } finally {
            cursor.close();
        }
    }
    
    private static Score[] fetchScores(long id, ScoreDAO dao) {
        Cursor cursor = dao.fetchScoresForPlayer(id);
        if (cursor == null) {
            return null;
        }
        
        Score[] scores = new Score[2];
        
        try {
            Score tmpScore = mapScore(cursor);
            scores[tmpScore.getDifficulty()] = tmpScore;
            cursor.moveToNext();
            tmpScore = mapScore(cursor);
            scores[tmpScore.getDifficulty()] = tmpScore;
            return scores;
        } finally {
            cursor.close();
        }
    }
    
    private static Score mapScore(Cursor cursor) {
        long scoreId = cursor.getLong(DdlBuilder.TABLE_KEY_INDEX);
        int playerId = cursor.getInt(ScoreDAO.SCORE_PLAYER_ID_COLUMN_INDEX);
        int difficulty = cursor.getInt(ScoreDAO.PLAYER_DIFFICULTY_COLUMN_INDEX);
        int hiScore = cursor.getInt(ScoreDAO.SCORE_HI_SCORE_COLUMN_INDEX);
        int lastScore = cursor.getInt(ScoreDAO.SCORE_LAST_SCORE_COLUMN_INDEX);
        int totalScore = cursor.getInt(ScoreDAO.SCORE_TOTAL_SCORE_COLUMN_INDEX);
        int totalPlayed = cursor.getInt(ScoreDAO.SCORE_TOTAL_PLAYED_COLUMN_INDEX);
        
        return new Score(scoreId, playerId, difficulty, hiScore, lastScore, totalScore, totalPlayed);
    }

    public static void savePlayer(Player player, ScoreDAO dao) {
        dao.savePlayer(player.getId(), player.getName(), player.getDifficulty());
        Score easyScore = player.getEasyScore();
        dao.saveScore(easyScore.getId(), easyScore.getPlayerId(), easyScore.getDifficulty(),
                easyScore.getHiScore(), easyScore.getLastScore(), easyScore.getTotalScore(),
                easyScore.getTotalPlayed());
        Score hardScore = player.getHardScore();
        dao.saveScore(hardScore.getId(), hardScore.getPlayerId(), hardScore.getDifficulty(),
                hardScore.getHiScore(), hardScore.getLastScore(), hardScore.getTotalScore(),
                hardScore.getTotalPlayed());
    }
}