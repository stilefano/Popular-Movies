package com.example.stilefano.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;


public class MovieDetailsActivity extends AppCompatActivity {

    private TextView mMovieTitle,mMovieReleaseDate,mMoviePopularity,mMovieVote,mMovieOverview;
    private ImageView mMovieImage;
    private String movieTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_movie_details);

        mMovieTitle         = (TextView) findViewById(R.id.title);
        mMovieImage         = (ImageView) findViewById(R.id.movie_poster);
        mMovieReleaseDate   = (TextView) findViewById(R.id.release_date);
        mMoviePopularity    = (TextView) findViewById(R.id.popularity);
        mMovieOverview      = (TextView) findViewById(R.id.overview);
        mMovieVote          = (TextView) findViewById(R.id.vote);

        Intent intent = getIntent();
        HashMap<String, Object> hashMap = (HashMap<String, Object>)intent.getSerializableExtra("map");
        String title = hashMap.get("Title").toString();
        String release = getResources().getString(R.string.released)+hashMap.get("Release").toString();
        String popularity = getResources().getString(R.string.popularity)+hashMap.get("Popularity").toString();
        String overview = hashMap.get("Overview").toString();
        String vote = getResources().getString(R.string.vote)+hashMap.get("Vote").toString();



        if(intent != null){
            mMovieTitle.setText(title);
            mMovieImage.setImageBitmap((Bitmap)hashMap.get("Img"));
            mMovieReleaseDate.setText(release);
            mMoviePopularity.setText(popularity);
            mMovieOverview.setText(overview);
            mMovieVote.setText(vote);
        }

    }


}
