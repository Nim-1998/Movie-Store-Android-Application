package com.example.moviestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class EditMoviesActivity extends AppCompatActivity {
    private static String[] FROM = {_ID, TITLE, YEAR, DIRECTOR, LIST_OF_ACTORS, RATING,REVIEW,FAVOURITE,};
    private MovieStoreData movieStoreData;

    private static final String LOG_TAG = EditMoviesActivity.class.getSimpleName();
    ArrayList<String> allMovieTitle = new ArrayList<>();
    private ListView movie_title_view;

    //Define ket for the Intent extras
    public static final String EXTRA_MESSAGE = "com.example.android.moviestore.extra.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies);

        movieStoreData = new MovieStoreData(this);
        movie_title_view = findViewById(R.id.movie_title_list_view);

        Cursor cursor = getAllMovie();
        allMovieTitle = getAllMovieTitle(cursor);
        Log.i(LOG_TAG,"Load movie list : "+allMovieTitle);

        // Set ListView adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.all_movie_title_list,R.id.movie_title_text_view,allMovieTitle);
        movie_title_view.setAdapter(adapter);

        // Set onClick Listener
        movie_title_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = adapter.getItem(position);
                Toast.makeText(getApplicationContext(),"You selected name is "+value,Toast.LENGTH_SHORT).show();
                Log.d("Main Activity","You selected name is "+value);
                // Call new Page
                Intent newIntent = new Intent(EditMoviesActivity.this,MovieDetailsActivity.class);
                newIntent.putExtra(EXTRA_MESSAGE,value);
                startActivity(newIntent);

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
}