package com.example.stilefano.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.stilefano.popularmovies.data.MoviesDbContract;


public class MoviesDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favorites.db";

    private static final int DATABASE_VERSION = 6;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_CREATE_TABLE = "CREATE TABLE "+ MoviesDbContract.MoviesEntry.TABLE_NAME + " ("
                + MoviesDbContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoviesDbContract.MoviesEntry.TITLE + " TEXT NOT NULL, "
                + MoviesDbContract.MoviesEntry.VOTE + " TEXT NOT NULL, "
                + MoviesDbContract.MoviesEntry.OVERVIEW + " TEXT NOT NULL, "
                + MoviesDbContract.MoviesEntry.DATE + " TEXT NOT NULL, "
                + MoviesDbContract.MoviesEntry.POSTER + " TEXT NOT NULL, "
                + MoviesDbContract.MoviesEntry.ID + " INTEGER NOT NULL"+")";


        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ MoviesDbContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}