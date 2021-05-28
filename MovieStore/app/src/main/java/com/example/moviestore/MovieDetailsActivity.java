package com.example.moviestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

public class MovieDetailsActivity extends AppCompatActivity {
    //Take class name as tag
    private static final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();
    private String selectedMovieTitle;

    private static String[] FROM = {_ID, TITLE, YEAR, DIRECTOR, LIST_OF_ACTORS, RATING,REVIEW,FAVOURITE,};
    private MovieStoreData movieStoreData;

    // Create String and integer variables to save input field values
    private String titleValue,directorValue,actorsValue,reviewValue;
    private int errorOccurredTime,yearValue,ratingsValue,favouriteValue;
    private String updateTitle,updateDirector,updateActor,updateReview;
    private int updateYear,updateRating,updateFavourite,currentRating;

    private Boolean radioButtonStatus = false;

    private EditText title,year,director,listOfActors,review;
    private RadioButton favouriteButton, notFavouriteButton;
    private ImageView rating_1,rating_2,rating_3,rating_4,rating_5,rating_6,rating_7,rating_8,rating_9,rating_10;
    private ArrayList<ImageView> imageViewList =  new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Get an instance from MovieStoreData class
        movieStoreData = new MovieStoreData(this);

        // Get reference for EditText fields
        title = findViewById(R.id.edit_text_title);
        year = findViewById(R.id.edit_text_year);
        director = findViewById(R.id.edit_text_director);
        listOfActors = findViewById(R.id.edit_text_actors);
        review = findViewById(R.id.edit_text_review);
        favouriteButton = findViewById(R.id.favouriteButton);
        notFavouriteButton = findViewById(R.id.notFavouriteButton);

        rating_1 = findViewById(R.id.rating_1);
        rating_2 = findViewById(R.id.rating_2);
        rating_3 = findViewById(R.id.rating_3);
        rating_4 = findViewById(R.id.rating_4);
        rating_5 = findViewById(R.id.rating_5);
        rating_6 = findViewById(R.id.rating_6);
        rating_7 = findViewById(R.id.rating_7);
        rating_8 = findViewById(R.id.rating_8);
        rating_9 = findViewById(R.id.rating_9);
        rating_10 = findViewById(R.id.rating_10);

        // Add rating imageView into the Array List
        imageViewList.add(rating_1);
        imageViewList.add(rating_2);
        imageViewList.add(rating_3);
        imageViewList.add(rating_4);
        imageViewList.add(rating_5);
        imageViewList.add(rating_6);
        imageViewList.add(rating_7);
        imageViewList.add(rating_8);
        imageViewList.add(rating_9);
        imageViewList.add(rating_10);

        //Get the Intent
        Intent intent = getIntent();
        //Get string message from the intent Extras
        selectedMovieTitle = intent.getStringExtra(EditMoviesActivity.EXTRA_MESSAGE);

        Log.i(LOG_TAG,"Selected movie title : "+selectedMovieTitle);

        // Load selected movie details
        Cursor cursor = getAllMovie();
        getMovieDetail(cursor);

        // Set movie details to the layout
        setMovieDetail();
        Log.i(LOG_TAG,"Load details : "+titleValue+" | "+yearValue+" | "+
                directorValue+" | "+actorsValue+" | "+ratingsValue+" | "+ reviewValue+" | "+favouriteValue);
        currentRating = ratingsValue;
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
     * Get selected movie details
     */
    private void getMovieDetail(Cursor cursor) {

        while (cursor.moveToNext()) {
           String title = cursor.getString(1);
           if (title.equals(selectedMovieTitle)) {
               titleValue = cursor.getString(1);
               yearValue = cursor.getInt(2);
               directorValue = cursor.getString(3);
               actorsValue = cursor.getString(4);
               ratingsValue = cursor.getInt(5);
               reviewValue = cursor.getString(6);
               favouriteValue = cursor.getInt(7);
           }
        }
        cursor.close();
    }

    /*
    *   Set Movie details into the layout
    */
    private void setMovieDetail () {
        String yearInString = String.valueOf(yearValue);
        title.setText(titleValue);
        year.setText(yearInString);
        director.setText(directorValue);
        listOfActors.setText(actorsValue);
        review.setText(reviewValue);
        if (favouriteValue == 1) {
            favouriteButton.setChecked(true);
            notFavouriteButton.setChecked(false);
        } else {
            notFavouriteButton.setChecked(true);
            favouriteButton.setChecked(false);
        }
        for (int i =0; i<ratingsValue; i++) {
            imageViewList.get(i).setColorFilter(Color.parseColor("#f9a602"));

        }
    }

    /*
    * Updated movie details in database
    */
    public void updateMovieDetails(View view) {
        if (getUpdateValue()){

            // Add updated Movie details into the database
            updateDetails();

            Log.d(LOG_TAG,"Updated details : "+updateTitle+" | "+updateYear+
                    " | "+updateDirector+" | "+updateActor+" | "+updateRating+" | "+updateReview+" | "+updateFavourite);

            // Display success message
            Toast.makeText(getApplicationContext(),"Database successfully updated ! ", Toast.LENGTH_SHORT).show();

        } else {
            errorOccurredTime = 0;
        }
    }

    /*
    * Get update values from layout
    */
    public boolean getUpdateValue () {

        errorOccurredTime = 0;
        if (isEmpty(title)){
            errorOccurredTime +=1;
            title.setError("Required !");
        } else {
            updateTitle = title.getText().toString();
        }

        if (isEmpty(year)){
            year.setError("Required !");
            errorOccurredTime +=1;
        }else {
            updateYear = Integer.parseInt(year.getText().toString());
            if (updateYear <= 1895){
                year.setError("Need to be greater than 1895 !");
                errorOccurredTime +=1;
            }
        }

        if (isEmpty(director)){
            director.setError("Required !");
            errorOccurredTime +=1;
        }else{
            updateDirector = director.getText().toString();
        }

        if (isEmpty(listOfActors)){
            listOfActors.setError("Required !");
            errorOccurredTime +=1;
        } else {
            updateActor = listOfActors.getText().toString();
        }

        if (isEmpty(review)){
            review.setError("Required !");
            errorOccurredTime +=1;
        } else {
            updateReview = review.getText().toString();
        }

        if (errorOccurredTime == 0){
            return true;
        } else {
            return false;
        }
    }

    /*
     * Check Text fields are empty or not
     */
    private boolean isEmpty(EditText editText){
        String value = editText.getText().toString();
        return value.isEmpty();
    }

    /*
    * Set updated ratings ratings
    */
    public void setRating(View view) {
        int reduceRating = 0;
        int increaseRating = 0;
        for (int i = 0;i<imageViewList.size();i++) {
            if (imageViewList.get(i).isPressed()) {
                if (imageViewList.get(i).getColorFilter() != null) {
                    imageViewList.get(i).clearColorFilter();
                    reduceRating+=1;
                } else {
                    imageViewList.get(i).setColorFilter(Color.parseColor("#f9a602"));
                    increaseRating+=1;
                }
            }
        }

        updateRating = currentRating + (increaseRating - reduceRating);
        currentRating = updateRating;
    }

    /*
    * Handle radio button action
    */
    public void onRadioButtonClicked(View view) {
        if (favouriteButton.isChecked()) {
            updateFavourite = 1;
            radioButtonStatus = true;
        } else if (notFavouriteButton.isChecked()){
            updateFavourite = 0;
            radioButtonStatus = true;
        } else {
            updateFavourite = favouriteValue;
        }
    }

    /*
    *  Update Database
    */
    private void updateDetails() {
        if (!radioButtonStatus) {
            updateFavourite = favouriteValue;
        }

        SQLiteDatabase database = movieStoreData.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(TITLE,updateTitle);
        values.put(YEAR,updateYear);
        values.put(DIRECTOR,updateDirector);
        values.put(LIST_OF_ACTORS,updateActor);
        values.put(RATING,updateRating);
        values.put(REVIEW,updateReview);
        values.put(FAVOURITE,updateFavourite);

        database.update(TABLE_NAME,values,TITLE+" =?",new String[]{selectedMovieTitle});
    }
}

/*
* Reference :
*  ColorFilter. (no date). Android Developers. Available from https://developer.android.com/reference/android/graphics/ColorFilter [Accessed 10 April 2021].
*/