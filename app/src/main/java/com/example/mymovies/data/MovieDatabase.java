package com.example.mymovies.data;


import androidx.room.RoomDatabase;
import androidx.room.*;

import android.content.Context;


@Database(entities = {Movie.class, FavouriteMovie.class}, version = 3, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String DB_NAME = "movie.db";
    private static MovieDatabase database;
    private static final Object LOCK = new Object();

    public static MovieDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (database == null)
                database = Room.databaseBuilder(context, MovieDatabase.class, DB_NAME)
                        .fallbackToDestructiveMigration().build();
            return database;
        }
    }

    public abstract MovieDao movieDao();
}
