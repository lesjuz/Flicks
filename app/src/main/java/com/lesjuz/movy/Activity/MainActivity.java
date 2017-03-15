package com.lesjuz.movy.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.lesjuz.movy.Adapter.MovieRecyclerAdapter;
import com.lesjuz.movy.Model.Movies;
import com.lesjuz.movy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movies> moviesArrayList;
    MovieRecyclerAdapter adapter;
    OkHttpClient client = new OkHttpClient();
    private String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private static final int     SHOW_MOVIE_DETAIL_REQUEST = 1;
    private static final int     SHOW_MOVIE_TRAILER_REQUEST = 2;
    private static final String  SELECTED_MOVIE  = "SELECTED_MOVIE";
    private static final String  SELECTED_MOVIE_TRAILER_URL = "SELECTED_MOVIE_TRAILER_URL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.recyclerview);

        // Initialize default movies array
        moviesArrayList = new ArrayList<Movies>();

        // Create adapter passing in the sample user data
        adapter = new MovieRecyclerAdapter(this,moviesArrayList, new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movies movie) {
                taskSelected(movie);
            }
        });
        rvMovies.setAdapter(adapter);
        // Set layout manager to position the items
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        Request request = new Request.Builder()
                .url(url)
                .build();
        moviesArrayList.clear();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                JSONArray moviesJson = null;
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    moviesJson = json.getJSONArray("results");
                    moviesArrayList.addAll(Movies.fromJson(moviesJson)); // add new items
                    notifyArrayAdapterDataSetChangedOnUIThread(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    response.body().close();
                }
            }
        });


    }
    private void notifyArrayAdapterDataSetChangedOnUIThread(final MovieRecyclerAdapter adapter) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    private void taskSelected(Movies movie) {

        if (movie.getVoteAverage() >= 5 && movie.getTrailerURL() != null) {
            Intent intent = new Intent(MainActivity.this, QuickPlayActivity.class);
            intent.putExtra(SELECTED_MOVIE_TRAILER_URL, movie.getTrailerURL());
            startActivityForResult(intent, SHOW_MOVIE_TRAILER_REQUEST);
        } else {
            Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
            intent.putExtra(SELECTED_MOVIE, Parcels.wrap(movie));
            startActivityForResult(intent, SHOW_MOVIE_DETAIL_REQUEST);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
