package com.example.moviestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.moviestore.Constants.DIRECTOR;
import static com.example.moviestore.Constants.FAVOURITE;
import static com.example.moviestore.Constants.LIST_OF_ACTORS;
import static com.example.moviestore.Constants.RATING;
import static com.example.moviestore.Constants.REVIEW;
import static com.example.moviestore.Constants.TABLE_NAME;
import static com.example.moviestore.Constants.TITLE;
import static com.example.moviestore.Constants.YEAR;

public class DisplayMoviesActivity extends AppCompatActivity {
    private static String[] FROM = {_ID, TITLE, YEAR, DIRECTOR, LIST_OF_ACTORS, RATING,REVIEW,FAVOURITE,};
    private MovieStoreData movieStoreData;

    private static final String LOG_TAG = DisplayMoviesActivity.class.getSimpleName();

    ArrayList<String> movieList = new ArrayList<>();
    ArrayList<String> favouriteMovieList = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies);

        // Get an instance from MovieStoreData class
        movieStoreData = new MovieStoreData(this);

        listView = findViewById(R.id.all_movie_list_view);

        // Get all movie titles
        Cursor cursor = getAllMovie();
        movieList = getAllMovieTitle(cursor);


        // Set Array Adapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_multiple_choice,movieList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                // Set the color here
                textView.setTextColor(Color.parseColor("#006400"));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                return textView;
            }
        };
        listView.setAdapter(arrayAdapter);

        // Set ListView action handler
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listView.isItemChecked(position)) {
                    Toast.makeText(getApplicationContext(),"You selected check box "+movieList.get(position), Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG,"You selected  "+movieList.get(position));
                    favouriteMovieList.add(movieList.get(position));
                } else {
                    String title = movieList.get(position);
                    for (int i = 0;i<favouriteMovieList.size(); i++) {
                        if (favouriteMovieList.get(i).equals(title)) {
                            favouriteMovieList.remove(i);
                            Log.d(LOG_TAG, "Your un check action is completed !");
                        }
                    }
                }
            }
        });
    }

    /*
    * Create cursor object to read database
    */
    private Cursor getAllMovie() {
        SQLiteDatabase database = movieStoreData.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,FROM,null,null,null,null,TITLE + " ASC");
        return cursor;
    }

    /*
    * Get all movie titles in Alphabetical order
    */
    private ArrayList<String> getAllMovieTitle(Cursor cursor) {
        ArrayList<String> movieTitleList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            movieTitleList.add(title);
        }
        cursor.close();
        Log.i(LOG_TAG,"Movie list :"+movieTitleList);
        return movieTitleList;
    }

    /*
    * Button Add to favourites onClick action handler
    */
    public void saveFavouriteMovie(View view) {
        Log.i(LOG_TAG,"Favourite movie list : "+favouriteMovieList);
        for (int i = 0; i<favouriteMovieList.size(); i++) {
            addFavouriteMovies(favouriteMovieList.get(i));
        }
    }

    /*
    * Update favourite column in database table
    */
    private void addFavouriteMovies(String movieTitle) {

        SQLiteDatabase database = movieStoreData.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAVOURITE,1);

        database.update(TABLE_NAME,values,TITLE+" =?",new String[]{movieTitle});

    }

}