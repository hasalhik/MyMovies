package com.example.mymovies.utils;

import android.util.Log;

import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {
    private static final String KEY_RESULTS = "results";

    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";

    private static final String KEY_KEY_OF_VIDEO = "key";
    private static final String KEY_NAME = "name";
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    private static final String KEY_VOTE_CONTE = "vote_count";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_RELEASE_DATE = "release_date";


    public static ArrayList<Movie> getMoviesFromJson(JSONObject jsonObject) {
        ArrayList<Movie> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);

                result.add(new Movie(
                        objectMovie.getInt(KEY_ID),
                        objectMovie.getInt(KEY_VOTE_CONTE),
                        objectMovie.getString(KEY_TITLE),
                        objectMovie.getString(KEY_ORIGINAL_TITLE),
                        objectMovie.getString(KEY_OVERVIEW),
                        NetworkUtils.BASE_POSTER_URL + NetworkUtils.SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH),
                        NetworkUtils.BASE_POSTER_URL + NetworkUtils.BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH),
                        objectMovie.getString(KEY_BACKDROP_PATH),
                        objectMovie.getDouble(KEY_VOTE_AVERAGE),
                        objectMovie.getString(KEY_RELEASE_DATE)
                ));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Review> getReviewsFromJson(JSONObject jsonObject) {
        ArrayList<Review> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            Log.d("MyLog", "jsonarr: "+jsonArray);
            Log.d("MyLog", "jsonarrLen: "+jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);

                result.add(new Review(
                        objectMovie.getString(KEY_AUTHOR),
                        objectMovie.getString(KEY_CONTENT)
                ));
                Log.d("MyLog", "res: "+result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Trailer> getTrailerFromJson(JSONObject jsonObject) {
        ArrayList<Trailer> result = new ArrayList<>();
        if (jsonObject == null) {
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objectMovie = jsonArray.getJSONObject(i);

                result.add(new Trailer(
                        BASE_YOUTUBE_URL + objectMovie.getString(KEY_KEY_OF_VIDEO),
                        objectMovie.getString(KEY_NAME),
                        objectMovie.getString(KEY_KEY_OF_VIDEO)

                ));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


}
