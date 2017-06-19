package com.nnd.popularmovies.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.nnd.popularmovies.App;
import com.nnd.popularmovies.R;
import com.nnd.popularmovies.main.movies.Movie;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {
    private static String KEY_EXTRA_MOVIE = "key_movie";
    @BindView(R.id.text_movie_title) TextView textTitle;
    @BindView(R.id.text_movie_duration) TextView textDuration;
    @BindView(R.id.text_movie_year) TextView textReleasedYear;
    @BindView(R.id.text_movie_synopsis) TextView textSynopsis;
    @BindView(R.id.text_movie_rating) TextView textRating;
    @BindView(R.id.img_movie_poster) ImageView imgMovie;

    @Inject @Named("ImgUri") Uri imgUri;

    private Movie currentMovie;

    public static Intent createIntent(Context context, Movie movie) {
        Parcelable parcelableMovie = Parcels.wrap(movie);
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(KEY_EXTRA_MOVIE, parcelableMovie);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        App.getAppComponent().inject(this);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            Parcelable p = getIntent().getParcelableExtra(KEY_EXTRA_MOVIE);
            currentMovie = Parcels.unwrap(p);

            setData();
        }
    }

    private void setData() {
        Picasso.with(this).load(buildImgPath()).placeholder(R.mipmap.ic_placeholder).into(imgMovie);
        textTitle.setText(currentMovie.getTitle());
        textSynopsis.setText(currentMovie.getSynopsis());
        textReleasedYear.setText(formatDate(currentMovie.getReleasedDate()));
        textRating.setText(formatRating(currentMovie.getVoteAverage()));
    }

    private String formatDate(String date) {
        String[] splittedString = date.split("-");
        return splittedString[0];
    }

    private String formatRating(float rating) {
        String formattedRating = String.valueOf(rating + File.separator + getString(R.string.vote_scale));
        return formattedRating;
    }

    String buildImgPath() {
        return imgUri.buildUpon().appendEncodedPath(currentMovie.getImg()).build().toString();
    }
}
