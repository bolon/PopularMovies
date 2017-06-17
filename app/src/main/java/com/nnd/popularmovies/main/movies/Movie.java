package com.nnd.popularmovies.main.movies;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Android dev on 6/17/17.
 */

public class Movie {
    @SerializedName("id") int id;
    @SerializedName("poster_path") String img;
    @SerializedName("vote_average") float voteAverage;
    @SerializedName("original_title") String originalTitle;
    @SerializedName("released_date") String releasedDate;
    @SerializedName("overview") String synopsis;

    String title;
    float popularity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate) {
        this.releasedDate = releasedDate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }
}
