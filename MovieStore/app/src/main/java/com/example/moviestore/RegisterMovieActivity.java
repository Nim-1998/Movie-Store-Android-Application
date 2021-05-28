package com.example.moviestore;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import static android.provider.BaseColumns._ID;
import static com.example.moviestore.Constants.DIRECTOR;
import static com.example.moviestore.Constants.FAVOURITE;
import static com.example.moviestore.Constants.LIST_OF_ACTORS;
import static com.example.moviestore.Constants.RATING;
import static com.example.moviestore.Constants.REVIEW;
import static com.example.moviestore.Constants.TABLE_NAME;
import static com.example.moviestore.Constants.TITLE;
import static com.example.moviestore.Constants.YEAR;

public class RegisterMovieActivity extends AppCompatActivity {
    private static String[] FROM = {_ID, TITLE, YEAR, DIRECTOR, LIST_OF_ACTORS, RATING,REVIEW,FAVOURITE,};
    private MovieStoreData movieStoreData;

    private static final String LOG_TAG = RegisterMovieActivity.class.getSimpleName();
    private EditText title,year,director,listOfActors,rating,review;
    private String titleValue,directorValue,actorsValue,reviewValue;
    private int errorOccurredTime,yearValue,ratingsValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);

        // Get views reference
        title = findViewById(R.id.text_title);
        year = findViewById(R.id.text_year);
        director = findViewById(R.id.text_director);
        listOfActors = findViewById(R.id.text_actors);
        rating = findViewById(R.id.text_rating);
        review = findViewById(R.id.text_review);

        // Crate an instance from MovieStoreData
        movieStoreData = new MovieStoreData(this);
    }

    public void registerMovie(View view) {
        if (getData()){
            // Set database favourite column value
            int favouriteColumn = 0;

            // Add Movie details into the database
            try {
                insertData(titleValue,yearValue,directorValue,actorsValue,ratingsValue,
                        reviewValue,favouriteColumn);
            } finally {
                movieStoreData.close();
            }
            Log.d(LOG_TAG,"User entered data : "+titleValue+" | "+yearValue+" | "+
                    directorValue+" | "+actorsValue+" | "+ratingsValue+" | "+reviewValue);

            // Display success message
            Toast.makeText(getApplicationContext(),"Data successfully added to" +
                    " the database ! ", Toast.LENGTH_SHORT).show();

            // Set EditViews empty
            title.setText("");
            year.setText("");
            director.setText("");
            listOfActors.setText("");
            rating.setText("");
            review.setText("");


        } else {
            errorOccurredTime = 0;
        }

    }

    /*
    * Read EditText filed data.
    */
    private boolean getData(){
        errorOccurredTime = 0;
        if (isEmpty(title)){
            errorOccurredTime +=1;
            title.setError("Required !");
        } else {
            titleValue = title.getText().toString();
        }

        if (isEmpty(year)){
            year.setError("Required !");
            errorOccurredTime +=1;
        }else {
            yearValue = Integer.parseInt(year.getText().toString());
            if (yearValue <= 1895){
                year.setError("Need to be greater than 1895 !");
                errorOccurredTime +=1;
            }
        }

        if (isEmpty(director)){
            director.setError("Required !");
            errorOccurredTime +=1;
        }else{
            directorValue = director.getText().toString();
        }

        if (isEmpty(listOfActors)){
            listOfActors.setError("Required !");
            errorOccurredTime +=1;
        } else {
            actorsValue = listOfActors.getText().toString();
        }

        if (isEmpty(rating)){
            rating.setError("Required !");
            errorOccurredTime +=1;
        } else{
            ratingsValue = Integer.parseInt(rating.getText().toString());
            if (ratingsValue < 0 || ratingsValue > 10){
                rating.setError("Need to be in range 1 - 10 !");
                errorOccurredTime +=1;
            }
        }

        if (isEmpty(review)){
            review.setError("Required !");
            errorOccurredTime +=1;
        } else {
            reviewValue = review.getText().toString();
        }

        Log.d(LOG_TAG,titleValue+" | "+yearValue+" | "+directorValue+" | "+actorsValue
                +" | "+ratingsValue+" | "+reviewValue);

        if (errorOccurredTime == 0){
            return true;
        } else {
            return false;
        }
    }

    /*
    * Check Text fields are empty or not
    */
    private boolean isEmpty(EditText editText) {
        String value = editText.getText().toString();
        return value.isEmpty();
    }

    /*
    * Create new row in movieStore table.
    * Add user enter data into the database.
    */
    private void insertData(String title,int year,String director,
                            String actors,int rating,String review,int favourite) {

        SQLiteDatabase database = movieStoreData.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE,title);
        values.put(YEAR,year);
        values.put(DIRECTOR,director);
        values.put(LIST_OF_ACTORS,actors);
        values.put(RATING,rating);
        values.put(REVIEW,review);
        values.put(FAVOURITE,favourite);

        database.insertOrThrow(TABLE_NAME,null,values);
    }
}