package com.example.moviestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class RatingResultActivity extends AppCompatActivity {
    private static final String LOG_TAG = RatingResultActivity.class.getSimpleName();
    String selectedMovieTitle;

    private final String API_KEY = "k_sips0r6f";
    private static ArrayList<String> idList = new ArrayList<>();
    private static ArrayList<String> titleList = new ArrayList<>();
    private static ArrayList<String> ratingsList = new ArrayList<>();
    private static ArrayList<String> imageUrlList = new ArrayList<>();

    private ListView listView;
    private LinearLayout linearLayout;
    String selectedImageUrl;
    private ImageView imageView;
    private static Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_result);

        idList.clear();
        titleList.clear();
        ratingsList.clear();
        imageUrlList.clear();
        //Get the Intent
        Intent intent = getIntent();
        //Get string message from the intent Extras
        selectedMovieTitle = intent.getStringExtra(EditMoviesActivity.EXTRA_MESSAGE);

        Log.i(LOG_TAG, "Selected movie title : " + selectedMovieTitle);

        linearLayout = findViewById(R.id.linearLayout);

        listView = findViewById(R.id.find_list_view);
        imageView = findViewById(R.id.image_view);

        // Get an instance from GetMovieTitle class to reguest Movie title data
        new GetMovieTitle().execute();
        Log.d(LOG_TAG,"Title List After execute : "+titleList);
    }

    public void viewResult(View view) {

        if (!titleList.isEmpty() && !ratingsList.isEmpty()) {
            ArrayList<String> finalList = new ArrayList<>();
            String rating ;
            for (int i = 0 ;i<ratingsList.size(); i++) {
                rating = ratingsList.get(i);
                if (rating.equals("") || rating == null) {
                    rating = "Not Rated ! ";
                }
                String result = titleList.get(i) +" | Ratings : "+ rating;
                finalList.add(result);

            }

            // Set ListView actions handler
            ListAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_single_choice,finalList){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    TextView textView = (TextView) super.getView(position, convertView, parent);
                    // Set the color here
                    textView.setTextColor(Color.parseColor("#0C8503"));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                    return textView;
                }
            };
            listView.setAdapter(arrayAdapter);

            // Set ListView onClick action
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedImageUrl = "";
                    if (listView.isItemChecked(position)) {
                        selectedImageUrl = imageUrlList.get(position);
                        Toast.makeText(RatingResultActivity.this,"You selected check box "+imageUrlList.get(position), Toast.LENGTH_LONG).show();
                    } else {
                        selectedImageUrl = "";
                    }
                }
            });


            Log.i(LOG_TAG,"Image url : "+selectedImageUrl);
        } else {
            Toast.makeText(RatingResultActivity.this, "Unable to load movie details !", Toast.LENGTH_LONG).show();
        }
    }

    /*
    * Inside method create an instance from  GetMovieRating class to get movie total ratings
    */
    public void requestData(View view) {
        for (int i = 0; i < titleList.size(); i++) {
            Log.d(LOG_TAG,"i : "+i+" id : "+idList.get(i));

            new GetMovieRating(idList.get(i)).execute();
        }
        Log.d(LOG_TAG,"Rating List after execute : "+ratingsList);
    }

    /*
    * Inside method send request to get movie image
    */
    public void viewMovieImage(View view) {
        Log.d(LOG_TAG,"Get image Button clicked !");
        GetImageThread runnable = new GetImageThread();
        new Thread(runnable).start();
    }

    /*
     * Request movie data from API
     */
    private class GetMovieTitle extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String value = null;
            Log.e(LOG_TAG, "title !" + selectedMovieTitle);
            if (selectedMovieTitle != null) {

                String url = "https://imdb-api.com/en/API/SearchTitle/" + API_KEY + "/" + selectedMovieTitle;

                try {
                    URL pageUrl = new URL(url);
                    // Open connection
                    HttpURLConnection connection = (HttpURLConnection) pageUrl.openConnection();
                    // Set GET method
                    connection.setRequestMethod("GET");
                    connection.connect();
                    // Get data
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    value = bufferedReader.readLine();
                    Log.e(LOG_TAG, "Title list extract !" + value);


                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, "Error occurred !" + e);
                } catch (IOException ex) {
                    Log.e(LOG_TAG, "Error occurred !" + ex);
                }
            }
            return value;
        }

        @Override
        protected void onPostExecute(String value) {
            if (value != null) {
                try {
                    JSONObject mainResult = (JSONObject) new JSONObject(value);
                    JSONArray movieArray =  mainResult.getJSONArray("results");
                    int length = movieArray.length();

                    for (int i = 0; i < length; i++) {
                        JSONObject movie = (JSONObject) movieArray.get(i);
                        String movieTitle = movie.getString("title");
                        String imageId = (String) movie.getString("image");
                        String id = (String) movie.getString("id");
                        titleList.add(movieTitle);
                        idList.add(id);
                        imageUrlList.add(imageId);

                    }

                    Log.i(LOG_TAG, "Title list  after load : " +titleList);

                } catch (JSONException e) {
                    Toast.makeText(RatingResultActivity.this, "Unable to load movie details !", Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, "Error occurred !" + e);
                }
            } else {
                Toast.makeText(RatingResultActivity.this, "Unable to load movie details !", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
    * Get movie ratings from API
    */
    private class GetMovieRating extends AsyncTask<String, String, String> {
        String id;

        public GetMovieRating(String id) {
            this.id = id;
        }

        @Override
        protected String doInBackground(String... strings) {

            String movieImageUrl = "https://imdb-api.com/en/API/UserRatings/" + API_KEY + "/" + id;

            String rating = null;
            try {
                URL ratingUrl = new URL(movieImageUrl);
                HttpURLConnection ratingConnection = (HttpURLConnection) ratingUrl.openConnection();
                ratingConnection.connect();
                InputStream inputStream = ratingConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                rating = bufferedReader.readLine();
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error occurred !" + e);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error occurred !" + e);
            }
            return rating;
        }

        @Override
        protected void onPostExecute(String value) {
            if (value != null) {
                try {
                    JSONObject mainResult = (JSONObject) new JSONObject(value);
                    String rating = mainResult.getString("totalRating");
                    ratingsList.add(rating);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.e(LOG_TAG, "Rating list  !" + ratingsList);

        }
    }

    /*
    * Create GET request to get selected movie image
    */
    private class GetImageThread implements Runnable {

        // Log.d(LOG_TAG,"Get image Button clicked !");
        @Override
        public void run() {
            Log.d(LOG_TAG,"Image URL : "+selectedImageUrl);
            try {
                URL imageUrl = new URL(selectedImageUrl);
                Log.i(LOG_TAG,"Image url : "+selectedImageUrl);

                HttpURLConnection imageConnection = (HttpURLConnection) imageUrl.openConnection();
                imageConnection.connect();
                InputStream inputStream = imageConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
