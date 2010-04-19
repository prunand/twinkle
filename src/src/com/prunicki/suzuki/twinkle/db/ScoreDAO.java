package com.prunicki.suzuki.twinkle.db;

import static com.prunicki.suzuki.twinkle.db.DdlBuilder.INTEGER_TYPE;
import static com.prunicki.suzuki.twinkle.db.DdlBuilder.TABLE_KEY_NAME;
import static com.prunicki.suzuki.twinkle.db.DdlBuilder.TEXT_TYPE;
import static com.prunicki.suzuki.twinkle.db.DdlBuilder.createColumnDef;
import static com.prunicki.suzuki.twinkle.db.DdlBuilder.createDDL;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.prunicki.suzuki.twinkle.Main;
import com.prunicki.suzuki.twinkle.db.DdlBuilder.ColumnDef;

public class ScoreDAO {
    private static final String DATABASE_NAME = "score";
    private static final int DATABASE_VERSION = 1;
    
    private static final String PLAYER_TABLE_NAME = "player";
    public static final int PLAYER_NAME_COLUMN_INDEX = 1;
    
    private static final ColumnDef[] PLAYER_COLUMN_DEF = {
        createColumnDef("name", TEXT_TYPE, false),
    };
    private static final String PLAYER_NAME_COLUMN = PLAYER_COLUMN_DEF[PLAYER_NAME_COLUMN_INDEX - 1].name;
    private static final String[] PLAYER_TABLE_COLUMNS = {TABLE_KEY_NAME, PLAYER_NAME_COLUMN};
    
    private static final String SCORE_TABLE_NAME = "score";
    public static final int SCORE_PLAYER_ID_COLUMN_INDEX = 1;
    public static final int SCORE_HI_SCORE_COLUMN_INDEX = 2;
    public static final int SCORE_LAST_SCORE_COLUMN_INDEX = 3;
    public static final int SCORE_TOTAL_SCORE_COLUMN_INDEX = 4;
    public static final int SCORE_TOTAL_PLAYED_COLUMN_INDEX = 5;
    
    private static final ColumnDef[] SCORE_COLUMN_DEF = {
        createColumnDef("player_id", INTEGER_TYPE, false),
        createColumnDef("hi_score", INTEGER_TYPE, false),
        createColumnDef("last_score", INTEGER_TYPE, false),
        createColumnDef("total_score", INTEGER_TYPE, false),
        createColumnDef("total_played", INTEGER_TYPE, false),
    };
    private static final String SCORE_PLAYER_ID_COLUMN = SCORE_COLUMN_DEF[SCORE_PLAYER_ID_COLUMN_INDEX - 1].name;
    private static final String SCORE_HI_SCORE_COLUMN = SCORE_COLUMN_DEF[SCORE_HI_SCORE_COLUMN_INDEX - 1].name;
    private static final String SCORE_LAST_SCORE_COLUMN = SCORE_COLUMN_DEF[SCORE_LAST_SCORE_COLUMN_INDEX - 1].name;
    private static final String SCORE_TOTAL_SCORE_COLUMN = SCORE_COLUMN_DEF[SCORE_TOTAL_SCORE_COLUMN_INDEX - 1].name;
    private static final String SCORE_TOTAL_PLAYED_COLUMN = SCORE_COLUMN_DEF[SCORE_TOTAL_PLAYED_COLUMN_INDEX - 1].name;
    private static final String[] SCORE_TABLE_COLUMNS = {TABLE_KEY_NAME, SCORE_PLAYER_ID_COLUMN,
        SCORE_HI_SCORE_COLUMN, SCORE_LAST_SCORE_COLUMN, SCORE_TOTAL_SCORE_COLUMN, SCORE_TOTAL_PLAYED_COLUMN};
    
    private static final String[] TABLE_NAMES = {PLAYER_TABLE_NAME, SCORE_TABLE_NAME};
    
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
    
    public Cursor fetchPlayer(String name) {
        Cursor cursor = mDb.query(PLAYER_TABLE_NAME, PLAYER_TABLE_COLUMNS,
                PLAYER_NAME_COLUMN + "='" + name + "'", null, null, null, null);
        
        int count = cursor.getCount();
        if (count == 0) {
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
    
    public long createPlayer(String name) {
        ContentValues values = createPlayerContentValues(name);
        
        return mDb.insert(PLAYER_TABLE_NAME, null, values);
    }

    public void savePlayer(long id, String name) {
        ContentValues values = createPlayerContentValues(name);
        
        mDb.update(PLAYER_TABLE_NAME, values, createKeyCriteria(id), null);
    }

    public int deletePlayer(long id) {
        return mDb.delete(PLAYER_TABLE_NAME, createKeyCriteria(id), null);
    }
    
    public Cursor fetchScoresForPlayer(long playerId) {
        Cursor cursor = mDb.query(SCORE_TABLE_NAME, SCORE_TABLE_COLUMNS,
                SCORE_PLAYER_ID_COLUMN + '=' + playerId, null, null, null, null);
        
        int count = cursor.getCount();
        if (count == 0) {
            //TODO Should the DAo relaly be closing the cursor?  Probably not.
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        
        return cursor;
    }
    
    public int deleteScoresForPlayer(long playerId) {
        return mDb.delete(SCORE_TABLE_NAME, SCORE_PLAYER_ID_COLUMN + '=' + playerId, null);
    }
    
    public long createScore(long playerId) {
        ContentValues values = createScoreContentValues(playerId, 0, 0, 0, 0);
        return mDb.insert(SCORE_TABLE_NAME, null, values);
    }

    public void saveScore(long id, long playerId, int hiScore,
            int lastScore, int totalScore, int totalPlayed) {
        ContentValues values = createScoreContentValues(playerId, hiScore,
                lastScore, totalScore, totalPlayed);
        
        mDb.update(SCORE_TABLE_NAME, values, createKeyCriteria(id), null);
    }
    
    private static String createKeyCriteria(long id) {
        return TABLE_KEY_NAME + '=' + id;
    }

    private ContentValues createPlayerContentValues(String name) {
        ContentValues values = new ContentValues();
        values.put(PLAYER_NAME_COLUMN, name);
        return values;
    }

    private ContentValues createScoreContentValues(long playerId, int hiScore,
            int lastScore, int totalScore, int totalPlayed) {
        ContentValues values = new ContentValues();
        values.put(SCORE_PLAYER_ID_COLUMN, playerId);
        values.put(SCORE_HI_SCORE_COLUMN, hiScore);
        values.put(SCORE_LAST_SCORE_COLUMN, lastScore);
        values.put(SCORE_TOTAL_SCORE_COLUMN, totalScore);
        values.put(SCORE_TOTAL_PLAYED_COLUMN, totalPlayed);
        return values;
    }
    
    private class ScoreDBHelper extends SQLiteOpenHelper {

        public ScoreDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createDDL(PLAYER_TABLE_NAME, PLAYER_COLUMN_DEF));
            db.execSQL(createDDL(SCORE_TABLE_NAME, SCORE_COLUMN_DEF));
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
