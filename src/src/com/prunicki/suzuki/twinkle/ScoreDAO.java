package com.prunicki.suzuki.twinkle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ScoreDAO {
    private static final String DATABASE_NAME = "score";
    private static final int DATABASE_VERSION = 1;
    
    private static final String CREATE_PLAYER_TABLE_SQL =
        "CREATE TABLE \"player\" (" +
        "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
        "\"name\" TEXT NOT NULL," +
        "\"difficulty\" INTEGER NOT NULL," +
        "\"hi_score\" INTEGER NOT NULL," +
        "\"last_score\" INTEGER NOT NULL," +
        "\"total_score\" INTEGER NOT NULL," +
        "\"total_played\" INTEGER NOT NULL" +
        ");";
    
    public static final int TABLE_KEY_INDEX = 0;
    public static final String TABLE_KEY_NAME = "_id";
    
    private static final String PLAYER_TABLE_NAME = "player";
    
    public static final int PLAYER_NAME_COLUMN_INDEX = 1;
    public static final int PLAYER_DIFFICULTY_COLUMN_INDEX = 2;
    public static final int PLAYER_HI_SCORE_COLUMN_INDEX = 3;
    public static final int PLAYER_LAST_SCORE_COLUMN_INDEX = 4;
    public static final int PLAYER_TOTAL_SCORE_COLUMN_INDEX = 5;
    public static final int PLAYER_TOTAL_PLAYED_COLUMN_INDEX = 6;
    
    private static final String PLAYER_NAME_COLUMN = "name";
    private static final String PLAYER_DIFFICULTY_COLUMN = "difficulty";
    private static final String PLAYER_HI_SCORE_COLUMN = "hi_score";
    private static final String PLAYER_LAST_SCORE_COLUMN = "last_score";
    private static final String PLAYER_TOTAL_SCORE_COLUMN = "total_score";
    private static final String PLAYER_TOTAL_PLAYED_COLUMN = "total_played";
    private static final String[] PLAYER_TABLE_COLUMNS = {TABLE_KEY_NAME, PLAYER_NAME_COLUMN, PLAYER_DIFFICULTY_COLUMN,
        PLAYER_HI_SCORE_COLUMN, PLAYER_LAST_SCORE_COLUMN, PLAYER_TOTAL_SCORE_COLUMN, PLAYER_TOTAL_PLAYED_COLUMN};
    
    private static final String[] TABLE_NAMES = {PLAYER_TABLE_NAME};
    
    private final Context mContext;
    private ScoreDBHelper mDBHelper;
    private SQLiteDatabase mDb;
    
    public ScoreDAO(Context context) {
        mContext = context;
    }
    
    public void open() throws SQLException {
        mDBHelper = new ScoreDBHelper(mContext);
        mDb = mDBHelper.getWritableDatabase();
    }
    
    public void release() {
        mDBHelper.close();
        mDBHelper = null;
        mDb = null;
    }
    
    public int playerCount() {
        Cursor cursor = mDb.query(PLAYER_TABLE_NAME, new String[] {"count(_id) cnt"}, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            return count;
        } finally {
            cursor.close();
        }
        
    }
    
    public Cursor fetchPlayer(long id) {
        Cursor cursor = mDb.query(PLAYER_TABLE_NAME, PLAYER_TABLE_COLUMNS,
                createKeyCriteria(id), null, null, null, null);
        
        int count = cursor.getCount();
        if (count > 1) {
            throw new IllegalStateException("Only 1 row should ever be returned.");
        } else if (count == 0) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        
        return cursor;
    }
    
    public Cursor fetchAllPlayers() {
        Cursor cursor = mDb.query(PLAYER_TABLE_NAME, PLAYER_TABLE_COLUMNS,
                null, null, null, null, PLAYER_NAME_COLUMN);
        
        if (cursor != null) {
            cursor.moveToFirst();
        }
        
        return cursor;
    }
    
    public int createPlayer(String name) {
        ContentValues values = createPlayerContentValues(name, 0, 0, 0, 0, 0);
        
        return (int) mDb.insert(PLAYER_TABLE_NAME, null, values);
    }

    public void savePlayer(long id, String name, int difficulty, int hiScore, int lastScore, int totalScore, int totalPlayed) {
        ContentValues values = createPlayerContentValues(name, difficulty, hiScore, lastScore, totalScore, totalPlayed);
        
        mDb.update(PLAYER_TABLE_NAME, values, createKeyCriteria(id), null);
    }
    
    private String createKeyCriteria(long id) {
        return TABLE_KEY_NAME + "=" + id;
    }

    private ContentValues createPlayerContentValues(String name, int difficulty, int hiScore,
            int lastScore, int totalScore, int totalPlayed) {
        ContentValues values = new ContentValues();
        values.put(PLAYER_NAME_COLUMN, name);
        values.put(PLAYER_DIFFICULTY_COLUMN, difficulty);
        values.put(PLAYER_HI_SCORE_COLUMN, hiScore);
        values.put(PLAYER_LAST_SCORE_COLUMN, lastScore);
        values.put(PLAYER_TOTAL_SCORE_COLUMN, totalScore);
        values.put(PLAYER_TOTAL_PLAYED_COLUMN, totalPlayed);
        return values;
    }
    
    private class ScoreDBHelper extends SQLiteOpenHelper {

        public ScoreDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_PLAYER_TABLE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //FIXME Should not be deleting all old data on upgrade
            Log.w(Main.TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            dropTables(db);
            onCreate(db);
        }

        public void dropTables(SQLiteDatabase db) {
            for (String tableName : TABLE_NAMES) {
                db.execSQL("DROP TABLE IF EXISTS " + tableName);
            }
        }
    }
}
