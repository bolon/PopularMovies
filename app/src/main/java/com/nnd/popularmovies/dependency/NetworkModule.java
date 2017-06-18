package com.nnd.popularmovies.dependency;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Android dev on 6/17/17.
 */

@Module
public class NetworkModule {
    private static final String PARAM_PATH_IMAGE_SIZE = "w185";
    private static String IMG_BASE_URL = "http://image.tmdb.org/t/p/";
    private static String API_VERSION = "3";
    public static String API_BASE_URL = "https://api.themoviedb.org/" + API_VERSION + "/";

    public NetworkModule() {
    }

    @Singleton
    @Provides
    Call.Factory providesCallFactory() {
        return new OkHttpClient.Builder().build();
    }

    @Singleton
    @Provides
    Gson providesGson() {
        return new GsonBuilder().create();
    }

    @Provides
    Retrofit providesRetrofit(Call.Factory callFactory, Gson gson) {
        return new Retrofit.Builder().baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(callFactory)
                .build();
    }

    @Provides
    MovieDbAPI providesMovieAPI(Retrofit retrofit) {
        return retrofit.create(MovieDbAPI.class);
    }

    @Provides
    @Named("ImgUri")
    Uri providesURIImage() {
        return Uri.parse(IMG_BASE_URL).buildUpon().appendPath(PARAM_PATH_IMAGE_SIZE).build();
    }
}
