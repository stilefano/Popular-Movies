package com.example.stilefano.popularmovies.data;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieContentProvider extends ContentProvider {

    private MoviesDbHelper moviesDbHelper;
    public static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    public static UriMatcher buildUriMatcher() {

        
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MoviesDbContract.MoviesEntry.CONTENT_AUTHORITY, MoviesDbContract.MoviesEntry.PATH_TERMS, MOVIES);
        uriMatcher.addURI(MoviesDbContract.MoviesEntry.CONTENT_AUTHORITY, MoviesDbContract.MoviesEntry.PATH_TERMS+"/#", MOVIES_WITH_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        moviesDbHelper = new MoviesDbHelper(context);
        return true;
    }

    
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        
        final SQLiteDatabase db = moviesDbHelper.getReadableDatabase();

        
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        
        switch (match) {
            
            case MOVIES:
                retCursor =  db.query(MoviesDbContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();

        
        int match = sUriMatcher.match(uri);
        Uri returnUri; 

        switch (match) {
            case MOVIES:
                
                
                long id = db.insert(MoviesDbContract.MoviesEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MoviesDbContract.MoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            
            
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        
        getContext().getContentResolver().notifyChange(uri, null);

        
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        
        final SQLiteDatabase db = moviesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        
        int tasksDeleted; 

        
        switch (match) {
            
            case MOVIES_WITH_ID:
                
                String id = uri.getPathSegments().get(1);
                
                tasksDeleted = db.delete(MoviesDbContract.MoviesEntry.TABLE_NAME, "id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        
        if (tasksDeleted != 0) {
            
            getContext().getContentResolver().notifyChange(uri, null);
        }

        
        return tasksDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
