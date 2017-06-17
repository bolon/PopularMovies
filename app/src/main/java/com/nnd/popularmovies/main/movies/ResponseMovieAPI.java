package com.nnd.popularmovies.main.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android dev on 6/17/17.
 */

public class ResponseMovieAPI {
    @SerializedName("results") List<Movie> movieList;
    int page;

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
