package com.example.stilefano.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MoviesDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 2;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_CREATE_TABLE = "CREATE TABLE "+ MoviesContract.MoviesEntry.TABLE_NAME + " ("
                + MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoviesContract.MoviesEntry.TITLE + " TEXT NOT NULL, "
                + MoviesContract.MoviesEntry.VOTE + " TEXT NOT NULL, "
                + MoviesContract.MoviesEntry.OVERVIEW + " TEXT NOT NULL, "
                + MoviesContract.MoviesEntry.DATE + " TEXT NOT NULL, "
                + MoviesContract.MoviesEntry.POSTER + " TEXT NOT NULL, "
                + MoviesContract.MoviesEntry.ID + " INTEGER NOT NULL"+")";


        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}