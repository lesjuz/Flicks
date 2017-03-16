package com.lesjuz.movy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lesjuz.movy.Model.Movies;
import com.lesjuz.movy.R;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import okhttp3.OkHttpClient;

/**
 * Created by Lesjuz on 3/11/2017.
 */

public class MovieDetailActivity extends AppCompatActivity {
    @BindView(R.id.ivPoster)
    ImageView ivPoster;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rbMovieRating)
    RatingBar rbRating;
    @BindView(R.id.tvReleaseDate)
    TextView    tvReleaseDate;
    @BindView(R.id.tvOverView)
    TextView    tvOverView;
    @BindView(R.id.ivVideoPreviewPlayButton)
    ImageView   ivPlayButton;

    Movies movie;


    private static final int     SHOW_MOVIE_TRAILER_REQUEST = 2;
    private static final String  SELECTED_MOVIE  = "SELECTED_MOVIE";
    private static final String  SELECTED_MOVIE_TRAILER_URL = "SELECTED_MOVIE_TRAILER_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        int orientation = getApplicationContext().getResources().getConfiguration().orientation;

        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        movie = (Movies) Parcels.unwrap(getIntent().getParcelableExtra(SELECTED_MOVIE));

        setupViews();
    }

    private void setupViews() {

        OkHttpClient client = new OkHttpClient();
        Picasso picasso = new Picasso.Builder(getApplicationContext()).downloader(new OkHttp3Downloader(client)).build();
        // populate the views
        picasso.load(movie.getBackDropUrl())
                .transform(new RoundedCornersTransformation(10, 10)).placeholder(R.mipmap.movie_placeholder)
                .into(ivPoster);
        tvTitle.setText(movie.getTitle());
        tvOverView.setText(movie.getOverview());
        tvReleaseDate.setText("Release Date: " + movie.getRelease_date());
        rbRating.setRating(movie.getVoteAverage() / 2);

        if(movie.getTrailerURL()!=null){
            ivPlayButton.setVisibility(View.VISIBLE);
            // set up listerner for handling clicks on the play button.
            ivPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MovieDetailActivity.this, QuickPlayActivity.class);
                    intent.putExtra(SELECTED_MOVIE_TRAILER_URL, movie.getTrailerURL());
                    startActivityForResult(intent, SHOW_MOVIE_TRAILER_REQUEST);
                }
            });
}
}


}
