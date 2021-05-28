package com.example.moviestore;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
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

public class SearchActivity extends AppCompatActivity {
    //Take class name as tag
    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    private static String[] FROM = {_ID, TITLE, YEAR, DIRECTOR, LIST_OF_ACTORS, RATING,REVIEW,FAVOURITE,};
    private MovieStoreData movieStoreData;
    private EditText searchText;
    private LinearLayout linearLayout;
    private String searchTextValue;

    private ArrayList<String> searchTitleResult =  new ArrayList<>();
    private ArrayList<String> searchDirectorResult =  new ArrayList<>();
    private ArrayList<String> searchActorResult =  new ArrayList<>();

    private int buttonCount =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Get an instance from MovieStoreData class
        movieStoreData = new MovieStoreData(this);

        // Get views references
        searchText = findViewById(R.id.search_text_box);
        linearLayout = findViewById(R.id.linearLayout);
    }

    /*
    * Get search text value
    */
    public void searchText(View view) {
        buttonCount++;
        String value = searchText.getText().toString();
        if (value.isEmpty()) {
            searchText.setError("Enter text !");
        } else {
            searchTextValue = value;
        }

        Cursor cursor = getAllMovie();
        searchResult(cursor,searchTextValue);
        setResult();

        // After search one result clear all ArrayList
        searchTitleResult.clear();
        searchDirectorResult.clear();
        searchActorResult.clear();
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
    *  Get search result
    */
    private void searchResult(Cursor cursor, String text) {
        while (cursor.moveToNext()) {
            if (cursor.getString(1).toLowerCase().contains(text.toLowerCase())) {
                searchTitleResult.add(cursor.getString(1));
            }

            if (cursor.getString(3).contains(text)) {
                searchDirectorResult.add(cursor.getString(3));
            }
            if (cursor.getString(4).contains(text)) {
                searchActorResult.add(cursor.getString(4));
            }
        }
        cursor.close();
    }

    /*
    * Set Search result
    */
    private void setResult() {

        // Remove previous views from Linear layout
        if (linearLayout.getChildCount()>0) {
            linearLayout.removeAllViews();
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(24, 20, 24, 20);

        if (!searchTitleResult.isEmpty()) {
            LinearLayout line1 = new LinearLayout(this);
            line1.setOrientation(LinearLayout.VERTICAL);
            TextView titleTextView = new TextView(this);
            titleTextView.setText(R.string.search_title_result);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,32);
            titleTextView.setTextColor(Color.parseColor("#0A1172"));
            titleTextView.setLayoutParams(layoutParams);
            line1.addView(titleTextView);


            for (int i = 0;i<searchTitleResult.size(); i++) {
                TextView titleResult = new TextView(this);
                titleResult.setText(searchTitleResult.get(i));
                // Set font size as Device Independent Pixels
                titleResult.setTextSize(TypedValue.COMPLEX_UNIT_DIP,28);
                titleResult.setTextColor(Color.parseColor("#ffa602"));
                titleResult.setLayoutParams(layoutParams);
                line1.addView(titleResult);
            }
            linearLayout.addView(line1);
        } else {
            LinearLayout line1 = new LinearLayout(this);
            line1.setOrientation(LinearLayout.VERTICAL);
            TextView titleTextView = new TextView(this);
            titleTextView.setText(R.string.no_title_search_result);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
            titleTextView.setTextColor(Color.parseColor("#BF0000"));
            titleTextView.setLayoutParams(layoutParams);
            line1.addView(titleTextView);
            linearLayout.addView(line1);
        }

        if (!searchDirectorResult.isEmpty()) {
            LinearLayout line1 = new LinearLayout(this);
            line1.setOrientation(LinearLayout.VERTICAL);
            TextView titleTextView = new TextView(this);
            titleTextView.setText(R.string.search_director_result);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,32);
            titleTextView.setTextColor(Color.parseColor("#0A1172"));
            titleTextView.setLayoutParams(layoutParams);
            line1.addView(titleTextView);


            for (int i = 0;i<searchDirectorResult.size(); i++) {
                TextView titleResult = new TextView(this);
                titleResult.setText(searchDirectorResult.get(i));
                // Set font size as Device Independent Pixels
                titleResult.setTextSize(TypedValue.COMPLEX_UNIT_DIP,28);
                titleResult.setTextColor(Color.parseColor("#ffa602"));
                titleResult.setLayoutParams(layoutParams);
                line1.addView(titleResult);
            }
            linearLayout.addView(line1);
        } else {
            LinearLayout line1 = new LinearLayout(this);
            line1.setOrientation(LinearLayout.VERTICAL);
            TextView titleTextView = new TextView(this);
            titleTextView.setText(R.string.no_director_search_result);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
            titleTextView.setTextColor(Color.parseColor("#BF0000"));
            titleTextView.setLayoutParams(layoutParams);
            line1.addView(titleTextView);
            linearLayout.addView(line1);
        }

        if (!searchActorResult.isEmpty()) {
            LinearLayout line1 = new LinearLayout(this);
            line1.setOrientation(LinearLayout.VERTICAL);
            TextView titleTextView = new TextView(this);
            titleTextView.setText(R.string.search_actor_result);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,32);
            titleTextView.setTextColor(Color.parseColor("#0A1172"));
            titleTextView.setLayoutParams(layoutParams);
            line1.addView(titleTextView);


            for (int i = 0;i<searchActorResult.size(); i++) {
                TextView titleResult = new TextView(this);
                titleResult.setText(searchActorResult.get(i));
                // Set font size as Device Independent Pixels
                titleResult.setTextSize(TypedValue.COMPLEX_UNIT_DIP,28);
                titleResult.setTextColor(Color.parseColor("#ffa602"));
                titleResult.setLayoutParams(layoutParams);
                line1.addView(titleResult);
            }
            linearLayout.addView(line1);
        } else {
            LinearLayout line1 = new LinearLayout(this);
            line1.setOrientation(LinearLayout.VERTICAL);
            TextView titleTextView = new TextView(this);
            titleTextView.setText(R.string.no_actor_search_result);
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
            titleTextView.setTextColor(Color.parseColor("#BF0000"));
            titleTextView.setLayoutParams(layoutParams);
            line1.addView(titleTextView);
            linearLayout.addView(line1);
        }
    }
}

/*
* References :
* android - Add & delete view from Layout. (no date). Stack Overflow. Available from https://stackoverflow.com/questions/3805599/add-delete-view-from-layout [Accessed 10 April 2021].
* android - How to check if a view has children. (no date). Stack Overflow. Available from https://stackoverflow.com/questions/39847334/how-to-check-if-a-view-has-children [Accessed 11 April 2021].
*/