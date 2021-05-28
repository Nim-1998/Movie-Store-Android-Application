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

public class FavouriteActivity extends AppCompatActivity {
    private static String[] FROM = {_ID, TITLE, YEAR, DIRECTOR, LIST_OF_ACTORS, RATING,REVIEW,FAVOURITE,};
    private MovieStoreData movieStoreData;

    private static final String LOG_TAG = FavouriteActivity.class.getSimpleName();

    private ListView listView;

    ArrayList<String> favouriteMovieList = new ArrayList<>();
    ArrayList<String> uncheckedMovieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        // Create an instance from MovieStoreData class
        movieStoreData = new MovieStoreData(this);
        listView = findViewById(R.id.favourite_movie_list_view);

        // Load favourite movie list from database
        Cursor cursor = getAllMovie();
        favouriteMovieList = getAllFavouriteMovie(cursor);

        // Set Array Adapter
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_multiple_choice,favouriteMovieList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                // Set the color here
                textView.setTextColor(Color.parseColor("#00008B"));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                return textView;
            }
        };

        // Set all list view items as checked
        listView.setAdapter(arrayAdapter);
        for (int i =0;i<favouriteMovieList.size(); i++) {
            listView.setItemChecked(i,true);
        }

        // Set ListView actions handler
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!listView.isItemChecked(position)) {
                    Toast.makeText(getApplicationContext(),"You un-checked check box "
                            +favouriteMovieList.get(position), Toast.LENGTH_LONG).show();

                    Log.d("Main Activity ","You clicked  "+favouriteMovieList.get(position));
                    uncheckedMovieList.add(favouriteMovieList.get(position));
                } else {
                    String title = favouriteMovieList.get(position);
                    for (int i = 0; i<uncheckedMovieList.size(); i++) {
                        if (uncheckedMovieList.get(i).equals(title)) {
                            uncheckedMovieList.remove(i);
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
     * Get all favourite movie titles in Alphabetical order
     */
    private ArrayList<String> getAllFavouriteMovie(Cursor cursor) {
        ArrayList<String> movieList = new ArrayList<>();

        while (cursor.moveToNext()) {
            int favouriteStatus = cursor.getInt(7);
            if (favouriteStatus == 1) {
                String title = cursor.getString(1);
                movieList.add(title);
            }
        }
        cursor.close();
        Log.i(LOG_TAG,"Favourite Movie list : "+movieList);
        return movieList;

    }

    /*
     * Button Save onClick action handler
     */
    public void updateFavouriteMovie(View view) {
        Log.i(LOG_TAG,"Unchecked  movie list : "+uncheckedMovieList);
        for (int i = 0; i<uncheckedMovieList.size(); i++) {
            removeFavouriteMovies(uncheckedMovieList.get(i));
        }
    }

    /*
     * Update favourite column in database table (unchecked result)
     */
    private void removeFavouriteMovies(String movieTitle) {

        SQLiteDatabase database = movieStoreData.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAVOURITE,0);

        database.update(TABLE_NAME,values,TITLE+" =?",new String[]{movieTitle});
    }
}