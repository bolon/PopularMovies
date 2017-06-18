package com.nnd.popularmovies.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nnd.popularmovies.App;
import com.nnd.popularmovies.R;
import com.nnd.popularmovies.main.movies.Movie;

import org.parceler.Parcels;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

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
        Glide.with(this).load(buildImgPath()).asBitmap().asIs().into(imgMovie);
        textTitle.setText(currentMovie.getTitle());
        textSynopsis.setText(currentMovie.getSynopsis());
        Timber.i(currentMovie.getReleasedDate() + " released date");
        textReleasedYear.setText(currentMovie.getReleasedDate());
        textRating.setText(String.valueOf(currentMovie.getPopularity()));
    }

    String buildImgPath() {
        return imgUri.buildUpon().appendEncodedPath(currentMovie.getImg()).build().toString();
    }
}
