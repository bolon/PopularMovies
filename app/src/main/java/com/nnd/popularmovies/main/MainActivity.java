package com.nnd.popularmovies.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.nnd.popularmovies.App;
import com.nnd.popularmovies.R;
import com.nnd.popularmovies.dependency.MovieDbAPI;
import com.nnd.popularmovies.main.movies.Movie;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListFragmentInteractionListener {
    @Inject MovieDbAPI movieDbAPI;
    @BindView(R.id.container) FrameLayout f;

    //TODO : (1) Check if no connection
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.getAppComponent().inject(this);
        ButterKnife.bind(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, ListFragment.newInstance(2), "test")
                .commit();
    }

    @Override
    public void onListFragmentInteraction(Movie item) {
        //stub
    }
}
