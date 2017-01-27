package com.example.stilefano.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.util.ArrayList;
import android.view.View.OnClickListener;
import java.util.HashMap;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    private ArrayList<HashMap<String, String>> arrayMovieList;
    private String moviePath = "https://image.tmdb.org/t/p/w300_and_h450_bestv2/";
    private final MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(HashMap<String, Object> movieDetail);
    }


    public MoviesAdapter(ArrayList<HashMap<String, String>> movieList, MoviesAdapterOnClickHandler clickHandler){
        arrayMovieList = movieList;
        mClickHandler = clickHandler;
    }


    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        public TextView title;
        public ImageView poster;


        public MoviesAdapterViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.movie_title);
            poster = (ImageView) view.findViewById(R.id.movie_poster);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            HashMap<String, Object> movieDetail = new HashMap<>();
            movieDetail.put("Title",arrayMovieList.get(adapterPosition).get("title"));
            movieDetail.put("Release",arrayMovieList.get(adapterPosition).get("release_date"));
            movieDetail.put("Popularity",arrayMovieList.get(adapterPosition).get("popularity"));
            movieDetail.put("Overview",arrayMovieList.get(adapterPosition).get("overview"));
            movieDetail.put("Vote",arrayMovieList.get(adapterPosition).get("vote_average"));
            Bitmap image=((BitmapDrawable) poster.getDrawable()).getBitmap();
            Bitmap imageCompressed = Bitmap.createScaledBitmap(image, 200, 300, false);
            movieDetail.put("Img",imageCompressed);
            mClickHandler.onClick(movieDetail);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        Context context = viewGroup.getContext();
        int LayoutIdForListItem = R.layout.movie_list_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(LayoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MoviesAdapterViewHolder viewholder = new MoviesAdapterViewHolder(view);

        return viewholder;
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
        holder.title.setText(arrayMovieList.get(position).get("title"));
        new DownloadImageTask(holder.poster)
                .execute(moviePath+arrayMovieList.get(position).get("poster"));


    }



    @Override
    public int getItemCount() {
        return arrayMovieList.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}