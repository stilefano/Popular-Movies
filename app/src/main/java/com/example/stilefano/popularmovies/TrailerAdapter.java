package com.example.stilefano.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private LayoutInflater inflater;
    private Context theContext;
    private ArrayList<HashMap<String, String>> arrayList;
    private TextView tv_trailer_list_name;
    private TextView tv_author;
    private TextView tv_content;
    private TextView trailers_title;
    private TextView reviews_title;
    private String youtube_id;
    private Button btn_trailer;
    private int reviewsCounter = 0;


    public TrailerAdapter(ArrayList<HashMap<String, String>> data,Context context) {
        theContext = context;
        arrayList = data;
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TrailerAdapterViewHolder(View view) {
            super(view);
            tv_trailer_list_name = (TextView) view.findViewById(R.id.trailer_tv);
            tv_author = (TextView) view.findViewById(R.id.author);
            tv_content = (TextView) view.findViewById(R.id.content);
            btn_trailer = (Button) view.findViewById(R.id.trailer_btn);
            trailers_title = (TextView) view.findViewById(R.id.trailer_title);
            reviews_title = (TextView) view.findViewById(R.id.reviews_title);
//            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            int adapterPosition = getAdapterPosition();
            openYoutube();
        }
    }

    public void openYoutube(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youtube_id));
        intent.putExtra("VIDEO_ID", youtube_id);
        theContext.startActivity(intent);
    }

    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int LayoutIdForListItem = R.layout.trailer_list_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(LayoutIdForListItem, parent, shouldAttachToParentImmediately);
        TrailerAdapterViewHolder viewholder = new TrailerAdapterViewHolder(view);
        Button button = (Button) view.findViewById(R.id.trailer_btn);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openYoutube();
            }
        });
        return viewholder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder holder, int position) {

        if(position==0){
            trailers_title.setVisibility(View.VISIBLE);
        }

        if(arrayList.get(position).get("site") != null && arrayList.get(position).get("site").equals("YouTube")){
            tv_trailer_list_name.setVisibility(View.VISIBLE);
            tv_trailer_list_name.setText(arrayList.get(position).get("name"));
            youtube_id = arrayList.get(position).get("key");
            btn_trailer.setVisibility(View.VISIBLE);
        }else{
            if(reviewsCounter==0){
                reviews_title.setVisibility(View.VISIBLE);
            }
            ++reviewsCounter;
            tv_author.setVisibility(View.VISIBLE);
            tv_author.setText(arrayList.get(position).get("author")+":");
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(arrayList.get(position).get("content"));
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}