package com.nnd.popularmovies.dependency;

import com.nnd.popularmovies.main.movies.ResponseMovieAPI;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Android dev on 6/17/17.
 */

public interface MovieDbAPI {
    @GET("test")
    Call<ResponseBody> callTest(@Query("test") String s);

    @GET("movie/popular")
    Call<ResponseMovieAPI> fetchPopularMovie(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/top_rated")
    Call<ResponseMovieAPI> fetchTopMovie(@Query("api_key") String apiKey, @Query("page") int page);
}
