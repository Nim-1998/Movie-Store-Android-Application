package com.example.moviestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class RatingsActivity extends AppCompatActivity {
    private static String[] FROM = {_ID, TITLE, YEAR, DIRECTOR, LIST_OF_ACTORS, RATING,REVIEW,FAVOURITE,};
    private MovieStoreData movieStoreData;

    private static final String LOG_TAG = RatingsActivity.class.getSimpleName();

    private ArrayList<String> allMovie = new ArrayList<>();
    private ListView listView;

    private String selectedMovieTitle;
    //Define key for the Intent extras
    public static final String EXTRA_MESSAGE = "com.example.android.moviestore.extra.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        // Get an instance from MovieStoreData class
        movieStoreData = new MovieStoreData(this);
        listView = findViewById(R.id.movie_list_view);

        // Get all movie details
        Cursor cursor = getAllMovie();
        allMovie = getAllMovieTitle(cursor);

        // Set ListView ArrayAdapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,android.R.id.text1,allMovie){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                // Set the color here
                textView.setTextColor(Color.parseColor("#0C8503"));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                return textView;
            }
        };
        listView.setAdapter(arrayAdapter);

        // Set ListView onClick action
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMovieTitle = "";
                if (listView.isItemChecked(position)) {
                    selectedMovieTitle = allMovie.get(position);
                } else {
                    selectedMovieTitle = "";
                }
            }
        });
    }

    /*
     * Create cursor object to read database
     */
    private Cursor getAllMovie() {
        SQLiteDatabase database = movieStoreData.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,FROM,null,null,null,null,null);
        return cursor;
    }


    /*
     * Get all movie titles
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

    public void findMovieRatings(View view) {
        Intent newIntent = new Intent(RatingsActivity.this,RatingResultActivity.class);
        newIntent.putExtra(EXTRA_MESSAGE,selectedMovieTitle);
        startActivity(newIntent);
    }
}