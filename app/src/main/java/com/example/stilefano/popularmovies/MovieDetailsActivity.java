package com.example.stilefano.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public class MovieDetailsActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> arrayList;
    private TextView mMovieTitle,mMovieReleaseDate,mMoviePopularity,mMovieVote,mMovieOverview;
    private ImageView mMovieImage;
    private HashMap<String, Object> hashMap;
    private Button mFavButton;
    private RecyclerView recyclerView;

    private TrailerAdapter trailerAdapter;

    private Cursor cursor;

    public SQLiteDatabase mDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MoviesDbHelper movieHelper = new MoviesDbHelper(this);
        mDB = movieHelper.getReadableDatabase();

        setContentView(R.layout.activity_display_movie_details);

        mMovieTitle         = (TextView) findViewById(R.id.title);
        mMovieImage         = (ImageView) findViewById(R.id.movie_poster);
        mMovieReleaseDate   = (TextView) findViewById(R.id.release_date);

        mMovieOverview      = (TextView) findViewById(R.id.overview);
        mMovieVote          = (TextView) findViewById(R.id.vote);

        mFavButton          = (Button) findViewById(R.id.fav_button);

        arrayList = new ArrayList<>();

        Intent intent = getIntent();
        hashMap = (HashMap<String, Object>)intent.getSerializableExtra("map");
        String title = hashMap.get("Title").toString();
        String release = hashMap.get("Release").toString();

        String overview = hashMap.get("Overview").toString();
        String vote = hashMap.get("Vote").toString()+getResources().getString(R.string.vote);
        URL buildUrl = NetworkUtils.UriBuildUrl(getApplicationContext(),hashMap.get("Id").toString()+"/videos");
        URL buildUrlReviews = NetworkUtils.UriBuildUrl(getApplicationContext(),hashMap.get("Id").toString()+"/reviews");

        recyclerView = (RecyclerView) findViewById(R.id.trailer_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new FetchExtraArr().execute(buildUrl);
        new FetchExtraArr().execute(buildUrlReviews);

        ContentResolver resolver = getContentResolver();


        cursor = resolver.query(
                MoviesDbContract.MoviesEntry.CONTENT_URI,
                null,
                MoviesDbContract.MoviesEntry.TITLE + " = ?" ,
                new String[] {hashMap.get("Title").toString()},
                null);

        if(cursor.moveToFirst()){
            mFavButton.setText(R.string.btn_fav_remove);
            mFavButton.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        }else{
            mFavButton.setText(R.string.btn_fav);
            mFavButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

            mMovieTitle.setText(title);
            mMovieImage.setImageBitmap((Bitmap)hashMap.get("Img"));
            mMovieReleaseDate.setText(release);

            mMovieOverview.setText(overview);
            mMovieVote.setText(vote);

    }



    public class FetchExtraArr extends AsyncTask<URL,Void,String>{

        protected void onPreExecute(){
            super.onPreExecute();
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

            JSONObject obj = null;

            try {
                obj = new JSONObject(searchResult);
                JSONArray items = null;

                items = obj.getJSONArray("results");
                arrayList = populateTrailerHashMap(items);

                trailerAdapter = new TrailerAdapter(arrayList, MovieDetailsActivity.this);
                recyclerView.setAdapter(trailerAdapter);

            } catch (Throwable t) {
                t.printStackTrace();
            }

        }
    }

    public void toggleAsFavorite(View view) {
        if(cursor.moveToFirst()) {
            removeFavMovie(hashMap);
        }else{
            addFavMovie(hashMap);
        }
    }

    private int removeFavMovie(HashMap<String, Object> hashMap){
        mFavButton.setText(R.string.btn_fav);
        mFavButton.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        String stringId = hashMap.get("Id").toString();
        Uri uri = MoviesDbContract.MoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        Toast.makeText(getBaseContext(),"Removed from favorite list",Toast.LENGTH_SHORT).show();

        return getContentResolver().delete(uri, null, null);
    }

    private Uri addFavMovie(HashMap<String, Object> hashMap) {

        ContentValues cv = new ContentValues();

        mFavButton.setText(R.string.btn_fav_remove);
        mFavButton.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        cv.put(MoviesDbContract.MoviesEntry.TITLE, hashMap.get("Title").toString());
        cv.put(MoviesDbContract.MoviesEntry.DATE, hashMap.get("Release").toString());
        cv.put(MoviesDbContract.MoviesEntry.VOTE, hashMap.get("Vote").toString());
        cv.put(MoviesDbContract.MoviesEntry.OVERVIEW, hashMap.get("Overview").toString());
        cv.put(MoviesDbContract.MoviesEntry.POSTER, hashMap.get("Poster").toString());
        cv.put(MoviesDbContract.MoviesEntry.ID, hashMap.get("Id").toString());
        Toast.makeText(getBaseContext(),"Added as favorite",Toast.LENGTH_SHORT).show();
        Uri uri = getContentResolver().insert(MoviesDbContract.MoviesEntry.CONTENT_URI, cv);

        return uri;

    }

    private ArrayList<HashMap<String, String>> populateTrailerHashMap(JSONArray items) throws JSONException {
        for(int i = 0; i < items.length(); i++){
            JSONObject c = items.getJSONObject(i);

            if(c.has("name")){
                String name = c.getString("name");
                String site = c.getString("site");
                String key = c.getString("key");

                HashMap<String, String> trailer = new HashMap<>();

                trailer.put("name",name);
                trailer.put("site",site);
                trailer.put("key",key);

                arrayList.add(trailer);
            }else if(c.has("author")){
                String author = c.getString("author");
                String content = c.getString("content");

                HashMap<String, String> review = new HashMap<>();

                review.put("author",author);
                review.put("content",content);

                arrayList.add(review);
            }
        }
        return arrayList;
    }


}
