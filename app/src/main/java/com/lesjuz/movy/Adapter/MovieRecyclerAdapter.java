package com.lesjuz.movy.Adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lesjuz.movy.Model.Movies;
import com.lesjuz.movy.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Lesjuz on 3/6/2017.
 */

public class MovieRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Movies item);
    }
    private  List<Movies> movies;
    private Context mContext;
    private final OnItemClickListener listener;

    final static int PopularMovies = 0;
    final static int NotPopularMovies    = 1;

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView title;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.poster) ImageView poster;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public class PopularMovieViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.ivMovie)
        public FrameLayout ivMovie;
        @BindView(R.id.tvTitle)
        public TextView tvTitle;
        @BindView(R.id.ivPlay)
        public ImageView ivPlay;
        @BindView(R.id.ivMovieImage)
        public ImageView ivImage;

        public PopularMovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
        public ImageView getImageView() {
            return ivImage;
        }

        public void setImageView(ImageView ivExample) {
            this.ivImage = ivExample;
        }
        public TextView getTitle() {
            return tvTitle;
        }

        public void setTitle(TextView title) {
            this.tvTitle = title;
        }

    }
    private class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder {
        public RecyclerViewSimpleTextViewHolder(View v) {
            super(v);
        }}

    public MovieRecyclerAdapter(Context context, List<Movies> movies, OnItemClickListener listener){
        this.movies = movies;
        this.mContext=context;
        this.listener = listener;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public int getItemViewType(int position) {
        Movies mv=movies.get(position);
        if (mv.getVoteAverage() >= 5) {
            return PopularMovies;
        }
        return NotPopularMovies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case PopularMovies:
                View v1 = inflater.inflate(R.layout.popular_movie_item, viewGroup, false);
                viewHolder = new PopularMovieViewHolder(v1);
                break;
            case NotPopularMovies:
                View v2 = inflater.inflate(R.layout.movie_item, viewGroup, false);
                viewHolder = new MovieViewHolder(v2);
                break;
            default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                break;
        }
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case PopularMovies:
                PopularMovieViewHolder vh1 = (PopularMovieViewHolder) viewHolder;
                configureViewHolder1(vh1, position,listener);
                break;
            case NotPopularMovies:
                MovieViewHolder vh2 = (MovieViewHolder) viewHolder;
                configureViewHolder2(vh2, position,listener);
                break;
            default:
                break;
        }

    }

    private void configureViewHolder2(MovieViewHolder vh2, int position, final OnItemClickListener listener) {
        final Movies mv=movies.get(position);

        TextView tv1 = vh2.title;
        tv1.setText(mv.getTitle());
        TextView tv2 = vh2.description;
        tv2.setText(mv.getOverview());

        ImageView iv=vh2.poster;
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            Picasso.with(getContext()).load(mv.getPosterUrl()).transform(new RoundedCornersTransformation(10, 10)).placeholder(R.mipmap.movie_placeholder).into(iv);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.with(getContext()).load(mv.getBackDropUrl()).transform(new RoundedCornersTransformation(10, 10)).placeholder(R.mipmap.movie_placeholder).into(iv);
        }
        vh2.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(mv);
            }
        });
    }

    private void configureViewHolder1(PopularMovieViewHolder vh1, int position, final OnItemClickListener listener) {
        final Movies mv=movies.get(position);
        vh1.getImageView().setImageResource(0);
        vh1.getTitle().setText(mv.getTitle());
        Picasso.with(getContext())
                .load(mv.getBackDropUrl())
                .placeholder(R.mipmap.movie_placeholder)
                .transform(new RoundedCornersTransformation(10, 10))
                .into(vh1.getImageView());
        vh1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(mv);
            }
        });
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


}


