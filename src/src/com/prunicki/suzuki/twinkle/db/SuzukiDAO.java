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
package com.prunicki.suzuki.twinkle.db;

import static com.prunicki.suzuki.twinkle.Main.TAG;
import static com.prunicki.suzuki.twinkle.db.DdlBuilder.INTEGER_TYPE;
import static com.prunicki.suzuki.twinkle.db.DdlBuilder.TABLE_KEY_NAME;
import static com.prunicki.suzuki.twinkle.db.DdlBuilder.createColumnDef;
import static com.prunicki.suzuki.twinkle.db.DdlBuilder.createDDL;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.prunicki.suzuki.twinkle.db.DdlBuilder.ColumnDef;

public class SuzukiDAO {
    private static final String DATABASE_NAME = "suzukitwinkle";
    private static final int DATABASE_VERSION = 1;
    
    private static final String STATUS_TABLE_NAME = "status";
    
    private static final ColumnDef[] STATUS_COLUMN_DEF = {
        createColumnDef("upgrade_dismiss", INTEGER_TYPE, false),
    };
    public static final int UPGRADE_DISMISS_COLUMN_INDEX = 1;
    
    private static final String UPGRADE_DISMISS_COLUMN = STATUS_COLUMN_DEF[UPGRADE_DISMISS_COLUMN_INDEX - 1].name;
    private static final String[] STATUS_TABLE_COLUMNS = {TABLE_KEY_NAME, UPGRADE_DISMISS_COLUMN};
    
    private static final String[] TABLE_NAMES = {STATUS_TABLE_NAME};
    
    private final Context mContext;
    private ScoreDBHelper mDBHelper;
    private SQLiteDatabase mDb;
    
    public SuzukiDAO(Context context) {
        mContext = context;
    }
    
    public void open() throws SQLException {
        mDBHelper = new ScoreDBHelper(mContext);
        mDb = mDBHelper.getWritableDatabase();
        int dismissCount = getDismissCount();
        if (dismissCount == 0) {
            insertStatus();
        }
//        mDBHelper.dropTables(mDb);
//        mDBHelper.onCreate(mDb);
    }
    
    public void release() {
        mDBHelper.close();
        mDBHelper = null;
        mDb = null;
    }
    
    public int queryNumberOfDismisses() {
        Cursor cursor = mDb.query(STATUS_TABLE_NAME, STATUS_TABLE_COLUMNS, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            int dismisses = cursor.getInt(1);
            return dismisses;
        } finally {
            cursor.close();
        }
    }
    
    public void updateNumberOfDismisses(int number) {
        ContentValues values = createContentValues(number);
        
        mDb.update(STATUS_TABLE_NAME, values, null, null);
    }
    
    private int getDismissCount() {
        Cursor cursor = mDb.query(STATUS_TABLE_NAME, new String[] {"count(_id) cnt"}, null, null, null, null, null);
        try {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            return count;
        } finally {
            cursor.close();
        }
    }
    
    private void insertStatus() {
        ContentValues values = createContentValues(0);
        
        mDb.insert(STATUS_TABLE_NAME, null, values);
    }
    
    private ContentValues createContentValues(int numberDismisses) {
        ContentValues values = new ContentValues();
        values.put(UPGRADE_DISMISS_COLUMN, numberDismisses);
        return values;
    }

    private class ScoreDBHelper extends SQLiteOpenHelper {
        public ScoreDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createDDL(STATUS_TABLE_NAME, STATUS_COLUMN_DEF));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //FIXME Should not be deleting all old data on upgrade
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
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