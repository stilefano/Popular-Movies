package com.example.stilefano.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;


public class MoviesDbContract{

    public static final class  MoviesEntry implements BaseColumns{

        public static final String TABLE_NAME = "favorites_movies";
        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String POSTER = "poster";
        public static final String OVERVIEW = "overview";
        public static final String VOTE = "vote";
        public static final String DATE = "date";
        public static final String CONTENT_AUTHORITY = "com.example.stilefano.popularmovies.data";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_TERMS = TABLE_NAME;
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TERMS).build();
    }


}