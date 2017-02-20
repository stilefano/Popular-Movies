package com.example.stilefano.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


class MoviesContract{

    public static final class  MoviesEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorites_movies";
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String POSTER = "poster";
        public static final String OVERVIEW = "overview";
        public static final String VOTE = "vote";
        public static final String DATE = "date";
    }


}