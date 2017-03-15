package com.lesjuz.movy.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lesjuz on 3/6/2017.
 */
@Parcel
public class Movies {
     int id;
     String title;
     String overview;
     String posterUrl;
     String backDropUrl;
     String release_date;
     float popularity;
     float voteAverage;
     int voteCount;
     String trailerURL;

    public  Movies(){}

    public int getId() {
        return id;
    }

    public String getBackDropUrl() {
        return String.format("https://image.tmdb.org/t/p/w500/%s", backDropUrl);
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterUrl() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterUrl);
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTitle() {
        return title;
    }

    public float getPopularity() {
        return popularity;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public String getTrailerURL() {

        return trailerURL;
    }

    public int getVoteCount() {
        return voteCount;
    }


    public static Movies fromJson(JSONObject jsonObject) throws JSONException {
        Movies m = new Movies();
        // Deserialize json into object fields
            m.id = jsonObject.getInt("id");
            m.title = jsonObject.getString("original_title");
            m.overview = jsonObject.getString("overview");
            m.posterUrl = jsonObject.getString("poster_path");
            m.backDropUrl=jsonObject.getString("backdrop_path");
            m.release_date=jsonObject.getString("release_date");
            m.popularity = (float) jsonObject.getDouble("popularity");
            m.voteAverage =(float) jsonObject.getDouble("vote_average");
            m.voteCount = jsonObject.getInt("vote_count");
           m.fetchMovieTrailerURL();

        // Return new object
        return m;
    }
    public static ArrayList<Movies> fromJson(JSONArray jsonArray) throws JSONException {
        JSONObject moviesJson;
        ArrayList<Movies> moviesArrayList = new ArrayList<Movies>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {

                moviesJson= jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Movies movies = Movies.fromJson(moviesJson);
            if (movies != null) {
                moviesArrayList.add(movies);
            }
        }

        return moviesArrayList;
    }

    private void fetchMovieTrailerURL() {

        String url = "https://api.themoviedb.org/3/movie/" + id + "/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    JSONArray movieJSONTrailers =  json.getJSONArray("youtube");
                    if (movieJSONTrailers.length() > 0) {
                        trailerURL = movieJSONTrailers.getJSONObject(0).getString("source");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }
}
