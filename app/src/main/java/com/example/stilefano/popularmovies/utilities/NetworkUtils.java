package com.example.stilefano.popularmovies.utilities;



import android.content.Context;
import android.net.Uri;

import com.example.stilefano.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {


    public static URL UriBuildUrl(Context context,String param){

        String API_URL = "https://api.themoviedb.org/3/movie/"+param+"?";
        String PQ = "api_key";
        String LANG = "language";
        String LANG_VALUE = "en-US";
        String PAGE = "page";
        String PAGE_VALUE = "1";
        String API = context.getResources().getString(R.string.api);


            Uri buildUri = Uri.parse(API_URL).buildUpon()
                    .appendQueryParameter(PQ,API)
                    .appendQueryParameter(LANG,LANG_VALUE)
                    .appendQueryParameter(PAGE,PAGE_VALUE)
                    .build();

            URL url = null;

            try{
                url = new URL(buildUri.toString());
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            return url;
    }

    public static String getResponseFromHttpUrl (URL url) throws IOException{
        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = httpUrlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }

        }finally {
            httpUrlConnection.disconnect();
        }
    }



}
