package com.example.stilefano.popularmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.stilefano.popularmovies.data.MoviesDbContract;
import com.example.stilefano.popularmovies.data.MoviesDbHelper;
import com.example.stilefano.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.stilefano.popularmovies.MoviesAdapter.MoviesAdapterOnClickHandler;

public class MainActivity extends AppCompatActivity implements MoviesAdapterOnClickHandler {

    ArrayList<HashMap<String, String>> movieList;
    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private ProgressBar mLoadingIndicator;
    private SQLiteDatabase mDb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        URL buildUrl = NetworkUtils.UriBuildUrl(getApplicationContext(),"top_rated");
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        GridLayoutManager gridLayout = new GridLayoutManager(this,calculateNoOfColumns(getBaseContext()));
        recyclerView.setLayoutManager(gridLayout);

        if (savedInstanceState != null) {
            savedInstanceState.getSerializable("key");
            movieList = (ArrayList<HashMap<String,String>>) savedInstanceState.getSerializable("key");
            moviesAdapter = new MoviesAdapter(movieList, MainActivity.this);
            recyclerView.setAdapter(moviesAdapter);
        }else{
            new FetchData().execute(buildUrl);
            movieList = new ArrayList<>();
        }

    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    @Override
    public void onClick(HashMap<String, Object> movieDetail) {
        Context context = this;
        Class destinationClass = MovieDetailsActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("map", movieDetail);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        final int itemId = item.getItemId();
        switch (itemId){

            case R.id.sort_by_popularity:
                URL popUrl = NetworkUtils.UriBuildUrl(getApplicationContext(),"popular");
                new FetchData().execute(popUrl);
                return true;

            case R.id.sort_by_rating:
                URL ratedUrl = NetworkUtils.UriBuildUrl(getApplicationContext(),"top_rated");
                new FetchData().execute(ratedUrl);
                return true;

            case R.id.display_favorites:
                Cursor cursor = getFavFromDB();
                movieList.clear();

                while(cursor.moveToNext()){
                    HashMap<String, String> movie = new HashMap<>();
                    movie.put("poster", cursor.getString(5));
                    movie.put("title", cursor.getString(1));
                    movie.put("release_date", cursor.getString(4));
                    movie.put("overview", cursor.getString(3));
                    movie.put("vote_average", cursor.getString(2));
                    movie.put("id", cursor.getString(6));
                    movieList.add(movie);
                }

                moviesAdapter = new MoviesAdapter(movieList, MainActivity.this);
                recyclerView.setAdapter(moviesAdapter);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class FetchData extends AsyncTask<URL,Void,String>{

        protected void onPreExecute(){
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String searchResults = null;
            try{
                searchResults = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return searchResults;
        }

        @Override
        protected void onPostExecute(String searchResult){

            mLoadingIndicator.setVisibility(View.INVISIBLE);

            //empty array list if there's already a query
            movieList.clear();

            JSONObject obj = null;

            try {
                obj = new JSONObject(searchResult);
                JSONArray items = null;

                items = obj.getJSONArray("results");
                movieList = populateMovieHashMap(items);

            } catch (Throwable t) {
                t.printStackTrace();
            }

            moviesAdapter = new MoviesAdapter(movieList, MainActivity.this);
            recyclerView.setAdapter(moviesAdapter);

        }


    }

    private ArrayList<HashMap<String, String>> populateMovieHashMap(JSONArray items) throws JSONException {
        for(int i = 0; i < items.length(); i++){
            JSONObject c = items.getJSONObject(i);
            String title = c.getString("title");
            String id = c.getString("id");
            String poster = c.getString("poster_path");
            String overview = c.getString("overview");
            String popularity = c.getString("popularity");
            String release_date = c.getString("release_date");
            String release_date_arr[] = release_date.split("\\-");
            String vote_average = c.getString("vote_average");

            HashMap<String, String> movie = new HashMap<>();

            movie.put("title",title);
            movie.put("poster",poster);
            movie.put("overview",overview);
            movie.put("popularity",popularity);
            movie.put("release_date",release_date_arr[0]);
            movie.put("vote_average",vote_average);
            movie.put("id",id);

            movieList.add(movie);
        }
        return movieList;
    }

    private Cursor getFavFromDB(){

        ContentResolver resolver = getContentResolver();


        return resolver.query(
                MoviesDbContract.MoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("key", movieList);
        super.onSaveInstanceState(outState);
    }


}
