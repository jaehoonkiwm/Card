package com.hoon.card;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Hoon on 2015-07-05.
 */
public class Database {
    private static final String DATABASE_NAME = "cardDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABE_NAME = "score_table";
    private static final String CREATE_SQL = "create table " + TABE_NAME
            + "( " + ScoreTable._ID + " integer primary key autoincrement, "
            + ScoreTable.NAME + " text, "
            + ScoreTable.SCORE + " integer "
            + ");";

    private static final String DROP_TABLE_SQL = "drop table if exists " + TABE_NAME;

    public interface ScoreTable extends BaseColumns {
        public static final String NAME = "name";
        public static final String SCORE = "score";
    }

    private DatabaseHelper mDbhelper;
    private SQLiteDatabase db;

    public Database(Context context) {
        mDbhelper = new DatabaseHelper(context);
    }

    public long insertData(ContentValues values) {
        db = mDbhelper.getWritableDatabase();
        long newRowId;
        newRowId = db.insert(TABE_NAME, null, values);

        return newRowId;
    }

    public ContentValues[] readDate() {
        ContentValues[] values = null;
        db = mDbhelper.getReadableDatabase();
        String[] projection = {
                ScoreTable._ID,
                ScoreTable.NAME,
                ScoreTable.SCORE,
        };

        String sortOrder = ScoreTable.SCORE + " DESC";
        Cursor c = db.query(TABE_NAME, projection, null, null, null, null, sortOrder);
        int rows = c.getCount();
        if (rows > 0) {
            values = new ContentValues[rows];
            c.moveToFirst();

            for (int i = 0; i != rows; ++i) {
                values[i] = new ContentValues();
                values[i].put(ScoreTable.NAME, c.getString(1));
                values[i].put(ScoreTable.SCORE, c.getString(2));

                if (i == 9)
                    break;
            }
        }

        c.close();
        return values;
    }

    private class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper (Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion)
                db.execSQL(DROP_TABLE_SQL);
        }
    }
}
