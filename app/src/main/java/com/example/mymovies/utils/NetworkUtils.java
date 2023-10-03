package com.example.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {
    private static final String API_KYE = "72410524684efbc91f10addf555fa743";
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos";
    private static final String BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews";
    private static final String BASE_URL_PREVIEW_VIDEOS = "https://img.youtube.com/vi/%s/0.jpg";

    private static final String PARAMS_API_KEY = "api_key";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_SORT_BY = "sort_by";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAMS_MIN_VOTE_COUNT = "vote_count.gte";
    private static final String MIN_VOTE_COUNT_VALUE = "1000";

    private static final String LANGUAGE_VALUE = "en-EN";

    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    public static final String SMALL_POSTER_SIZE = "w500";
    public static final String BIG_POSTER_SIZE = "w780";

    public static final String SORT_BY_POPULARITY = "popularity";
    public static final String SORT_BY_TOP_RATED = "vote_average";
    public static final String SORT_BY_RELEASE_DATE = "release_date";
    public static final String SORT_BY_REVENUE = "revenue";
    public static final String SORT_BY_ORIGINAL_TITLE = "original_title";
    public static final String DESC = ".desc";
    public static final String ASC = ".asc";

    public static URL buildUrlToMovie(int id) {
        Uri uri = Uri.parse(String.format(BASE_URL_VIDEOS, id))
                .buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KYE)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildUrlToReviews(int id) {
        Uri uri = Uri.parse(String.format(BASE_URL_REVIEWS, id))
                .buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KYE)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String buildPathToPreview(String id) {
        Uri uri = Uri.parse(String.format(BASE_URL_PREVIEW_VIDEOS, id))
                .buildUpon()
                .build();
        return uri.toString();
    }

    public static URL buildUrl(String sortBy, int page) {
        URL result = null;

        try {
            result = new URL(Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(PARAMS_API_KEY, API_KYE)
                    .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                    .appendQueryParameter(PARAMS_SORT_BY, sortBy)
                    .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
                    .appendQueryParameter(PARAMS_PAGE, String.valueOf(page))
                    .build()
                    .toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONForTrailer(int id) {
        JSONObject result = null;
        URL url = buildUrlToMovie(id);

        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONForReviews(int id) {
        JSONObject result = null;
        URL url = buildUrlToReviews(id);

        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static JSONObject getJSONFromNetwork(String sortBy, int page) {
        JSONObject result = null;
        URL url = buildUrl(sortBy, page);

        try {
            result = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;

    }

    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject result = null;
            if (urls == null || urls.length == 0) {
                return null;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                BufferedReader bufferedReader =
                        new BufferedReader(
                                new InputStreamReader(
                                        connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
                result = new JSONObject(stringBuilder.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }
            return result;

        }
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject> {
        private Bundle bundle;

        private OnStartLoadingListener onStartLoadingListener;

        public  interface OnStartLoadingListener{
            void onStartLoading();
        }

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (onStartLoadingListener!=null)
                onStartLoadingListener.onStartLoading();
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if (bundle == null)
                return null;
            URL url = null;
            try {
                url = new URL(bundle.getString("url"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            JSONObject result = null;
            if (url == null) {
                return null;
            }
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader =
                        new BufferedReader(
                                new InputStreamReader(
                                        connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
                result = new JSONObject(stringBuilder.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
            }
            return result;
        }
    }

}