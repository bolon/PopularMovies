package com.nnd.popularmovies.main;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nnd.popularmovies.R;
import com.nnd.popularmovies.main.ListFragment.OnListFragmentInteractionListener;
import com.nnd.popularmovies.main.movies.Movie;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class MyListRecyclerViewAdapter extends RecyclerView.Adapter<MyListRecyclerViewAdapter.MovieViewHolder> {
    private static final String PARAM_PATH_IMAGE_SIZE = "w185";
    public static String IMG_BASE_URL = "http://image.tmdb.org/t/p/";

    private final List<Movie> movies;
    private final OnListFragmentInteractionListener mListener;

    @Inject Uri imgUri;

    public MyListRecyclerViewAdapter(List<Movie> items, OnListFragmentInteractionListener listener) {
        movies = items;
        mListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        holder.movie = movies.get(position);
        holder.mIdView.setText(String.valueOf(movies.get(position).getId()));
        holder.mContentView.setText(movies.get(position).getTitle());

        String imgPath = movies.get(position).getImg();
        Glide.with(holder.layoutView.getContext())
                .load(providesURIImage(imgPath))
                .asBitmap()
                .centerCrop()
                .into(holder.imgMovie);

        holder.layoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.movie);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    Uri providesURIImage(String posterPath) {
        return Uri.parse(IMG_BASE_URL)
                .buildUpon()
                .appendPath(PARAM_PATH_IMAGE_SIZE)
                .appendEncodedPath(posterPath)
                .build();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public Movie movie;
        @BindView(R.id.container_item) View layoutView;
        @BindView(R.id.id) TextView mIdView;
        @BindView(R.id.poster) ImageView imgMovie;
        @BindView(R.id.content) TextView mContentView;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
