package com.nnd.popularmovies.main;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nnd.popularmovies.App;
import com.nnd.popularmovies.R;
import com.nnd.popularmovies.main.ListFragment.OnListFragmentInteractionListener;
import com.nnd.popularmovies.main.movies.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class MyListRecyclerViewAdapter extends RecyclerView.Adapter<MyListRecyclerViewAdapter.MovieViewHolder> {


    private final List<Movie> movies;
    private final OnListFragmentInteractionListener mListener;

    @Inject @Named("ImgUri") Uri imgUri;

    public MyListRecyclerViewAdapter(List<Movie> items, OnListFragmentInteractionListener listener) {
        App.getAppComponent().inject(this);
        movies = new ArrayList<>();
        movies.addAll(items);
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

        String imgPath = movies.get(position).getImg();
        imgPath = imgUri.buildUpon().appendEncodedPath(imgPath).build().toString();

        Context context = holder.layoutView.getContext();
        int imgWidth = (int) context.getResources().getDimension(R.dimen.movie_poster_list_width);
        int imgHeight = (int) context.getResources().getDimension(R.dimen.movie_poster_list_height);
        Picasso.with(context)
                .load(imgPath)
                .resize(imgWidth, imgHeight)
                .centerCrop()
                .noPlaceholder()
                .into(holder.imgMovie);

        holder.layoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onItemClicked(holder.movie);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void replaceData(List<Movie> movieList) {
        movies.clear();
        movies.addAll(movieList);
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public Movie movie;
        @BindView(R.id.container_item) View layoutView;
        @BindView(R.id.poster) ImageView imgMovie;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
