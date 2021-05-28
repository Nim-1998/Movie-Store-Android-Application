package com.example.moviestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //  Take class name as tag
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    Button main_button_1,main_button_2,main_button_3,main_button_4,main_button_5,main_button_6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get view reference
        main_button_1 = findViewById(R.id.main_button_1);
        main_button_2 = findViewById(R.id.main_button_2);
        main_button_3 = findViewById(R.id.main_button_3);
        main_button_4 = findViewById(R.id.main_button_4);
        main_button_5 = findViewById(R.id.main_button_5);
        main_button_6 = findViewById(R.id.main_button_6);

        // Set button 1 onClick action
        main_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG,"Register Movie Button clicked ! ");
                Intent firstIntent = new Intent(MainActivity.this,RegisterMovieActivity.class);
                startActivity(firstIntent);

            }
        });

        // Set button 2 onClick action
        main_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG,"Display Movies Button clicked ! ");
                Intent secondIntent = new Intent(MainActivity.this,DisplayMoviesActivity.class);
                startActivity(secondIntent);

            }
        });

        // Set button 3 onClick action
        main_button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG,"Display Favourite Movies Button clicked ! ");
                Intent thirdIntent = new Intent(MainActivity.this,FavouriteActivity.class);
                startActivity(thirdIntent);

            }
        });

        // Set button 4 onClick action
        main_button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG,"Edit Movies Button clicked ! ");
                Intent fourthIntent = new Intent(MainActivity.this,EditMoviesActivity.class);
                startActivity(fourthIntent);

            }
        });

        // Set button 5 onClick action
        main_button_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG,"Search Movie details Button clicked ! ");
                Intent fifthIntent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(fifthIntent);

            }
        });

        // Set button 6 onClick action
        main_button_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG,"Search Ratings Button clicked ! ");
                Intent sixthIntent = new Intent(MainActivity.this,RatingsActivity.class);
                startActivity(sixthIntent);

            }
        });

    }
}