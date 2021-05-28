package com.example.moviestore;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
    public static final String TABLE_NAME = "movieStore";
    //Columns in the table
    public static final String TITLE = "title";
    public static final String YEAR = "year";
    public static final String DIRECTOR = "director";
    public static final String LIST_OF_ACTORS = "listOfActors";
    public static final String RATING = "rating";
    public static final String REVIEW = "review";
    public static final String FAVOURITE = "favourite";
}
