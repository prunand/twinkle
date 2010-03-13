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
        "\"name\" TEXT NOT NULL" +
        ");";
    
    private static final String CREATE_SCORE_TABLE_SQL =
        "CREATE TABLE \"score\" (" +
        "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
        "\"player_id\" INTEGER NOT NULL," +
        "\"game_id\" INTEGER NOT NULL," +
        "\"total_played\" INTEGER NOT NULL," +
        "\"total_score\" INTEGER NOT NULL," +
        "\"last_score\" INTEGER NOT NULL," +
        "\"hi_score\" INTEGER NOT NULL" +
        ");";
    
    public static final int TABLE_KEY_INDEX = 0;
    public static final String TABLE_KEY_NAME = "_id";
    
    private static final String PLAYER_TABLE_NAME = "player";
    private static final String PLAYER_NAME_COLUMN = "name";
    private static final String[] PLAYER_TABLE_COLUMNS = {TABLE_KEY_NAME, PLAYER_NAME_COLUMN};
    
    public static final int PLAYER_NAME_COLUMN_INDEX = 1;
    
    private static final String SCORE_TABLE_NAME = "score";
    private static final String[] SCORE_TABLE_COLUMNS = {TABLE_KEY_NAME, "player_id"};
    
    private static final String[] TABLE_NAMES = {PLAYER_TABLE_NAME, SCORE_TABLE_NAME};
    
    private static final String COUNT_PLAYER_SQL = "select count(" + TABLE_KEY_NAME + ") from " + PLAYER_TABLE_NAME;
    
    private final Context mContext;
    private ScoreDBHelper mDBHelper;
    private SQLiteDatabase mDb;
    
    public ScoreDAO(Context context) {
        mContext = context;
    }
    
    public void open() throws SQLException {
        mDBHelper = new ScoreDBHelper(mContext);
        mDb = mDBHelper.getWritableDatabase();
        mDBHelper.dropTables(mDb);
        mDBHelper.onCreate(mDb);
    }
    
    public void release() {
        mDBHelper.close();
        mDBHelper = null;
        mDb = null;
    }
    
    public Cursor fetchPlayer(int id) {
        Cursor cursor = mDb.query(PLAYER_TABLE_NAME, PLAYER_TABLE_COLUMNS,
                createKeyCriteria(id), null, null, null, null);
        
        int count = cursor.getCount();
        if (count > 1) {
            throw new IllegalStateException("Only 1 row should ever be returned.");
        } else if (count == 1) {
            return null;
        }
        
        return cursor;
    }
    
    public Cursor fetchAllPlayers() {
        Cursor cursor = mDb.query(PLAYER_TABLE_NAME, PLAYER_TABLE_COLUMNS,
                null, null, null, null, null);
        
        if (cursor != null) {
            cursor.moveToFirst();
        }
        
        return cursor;
    }
    
    public int createPlayer(String name) {
        ContentValues values = new ContentValues();
        values.put(PLAYER_NAME_COLUMN, name);
        
        return (int) mDb.insert(PLAYER_TABLE_NAME, null, values);
    }
    
    private String createKeyCriteria(final long id) {
        return TABLE_KEY_NAME + "=" + id;
    }
    
    private class ScoreDBHelper extends SQLiteOpenHelper {

        public ScoreDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_PLAYER_TABLE_SQL);
            db.execSQL(CREATE_SCORE_TABLE_SQL);
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
