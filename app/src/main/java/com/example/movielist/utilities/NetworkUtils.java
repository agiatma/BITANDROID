package com.example.movielist.utilities;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    //final static String MOVIEDB_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=bdd497cb1f3fadbdace5b3d6becea086";
    final static String REVIEW_MOVIEDB = "https://api.themoviedb.org/3/movie/";
    final static String REVIEW_MOVIEDB2 = "/reviews?api_key=bdd497cb1f3fadbdace5b3d6becea086";

    final static String TRAILER_MOVIEDB = "https://api.themoviedb.org/3/movie/";
    final static String TRAILER_MOVIEDB2 = "/videos?api_key=f2b1ee93b23ec622ee804999aff4567e";

    public static URL buildUrl(String movieurl) throws MalformedURLException{
        String movieUrl = movieurl;

        URL url = new URL(movieUrl);

        return url;
    }

    public static URL buildReview(String id) throws MalformedURLException {
        String idnow = id;
        URL url = new URL(REVIEW_MOVIEDB+idnow+REVIEW_MOVIEDB2);
        return url;
    }

    public static URL buildTrailer(String id) throws  MalformedURLException{
        String idnow = id;
        URL url = new URL(TRAILER_MOVIEDB+idnow+TRAILER_MOVIEDB2);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
