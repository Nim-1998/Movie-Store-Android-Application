package com.example.moviestore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static com.example.moviestore.Constants.DIRECTOR;
import static com.example.moviestore.Constants.FAVOURITE;
import static com.example.moviestore.Constants.LIST_OF_ACTORS;
import static com.example.moviestore.Constants.RATING;
import static com.example.moviestore.Constants.REVIEW;
import static com.example.moviestore.Constants.TABLE_NAME;
import static com.example.moviestore.Constants.TITLE;
import static com.example.moviestore.Constants.YEAR;

public class MovieStoreData extends SQLiteOpenHelper {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String DATABASE_NAME = "movieStore.db";
    private static final int DATABASE_VERSION = 1;

    public MovieStoreData(Context ctx) {
        super(ctx,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("
                    +_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TITLE + " TEXT NOT NULL,"
                    + YEAR + " INTEGER,"
                    + DIRECTOR + " TEXT NOT NULL,"
                    + LIST_OF_ACTORS + " TEXT NOT NULL,"
                    + RATING +" INTEGER,"
                    + REVIEW + " TEXT NOT NULL,"
                    + FAVOURITE + " INTEGER);");

        Log.i(LOG_TAG,"Table is created !");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }
}
