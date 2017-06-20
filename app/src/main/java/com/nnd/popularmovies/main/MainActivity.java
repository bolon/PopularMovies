package com.nnd.popularmovies.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nnd.popularmovies.App;
import com.nnd.popularmovies.BuildConfig;
import com.nnd.popularmovies.R;
import com.nnd.popularmovies.dependency.MovieDbAPI;
import com.nnd.popularmovies.main.movies.Movie;
import com.nnd.popularmovies.main.movies.ResponseMovieAPI;

import org.parceler.Parcels;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListFragmentInteractionListener, ConnectionCheckListener {
    private static final String FRAGMENT_LIST_TAG = "fragment_list";
    @Inject MovieDbAPI movieDbAPI;
    @BindView(R.id.container) FrameLayout f;
    @BindView(R.id.layout_retry) View layoutRetry;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    private boolean isPopularMoviesShown = true;
    private Bundle savedInstance = null;
    private List<Movie> movieList = new ArrayList<>();
    private Callback<ResponseMovieAPI> callBack = new Callback<ResponseMovieAPI>() {
        @Override
        public void onResponse(Call<ResponseMovieAPI> call, Response<ResponseMovieAPI> response) {
            progressBar.setVisibility(View.INVISIBLE);

            try {
                Timber.i("Fetch success... " + response.body().getMovieList().size());
                movieList = response.body().getMovieList();
                notifyItemChanged(movieList);
            } catch (NullPointerException ex) {
                Timber.i(call.request().url().toString());
                movieList = Collections.emptyList();
            }
        }

        @Override
        public void onFailure(Call<ResponseMovieAPI> call, Throwable t) {
            progressBar.setVisibility(View.INVISIBLE);

            Timber.e("Fetch failed... " + t.getMessage());
            Toast.makeText(getApplicationContext(), R.string.toast_text_check_con, Toast.LENGTH_LONG)
                    .show();
        }
    };

    private void notifyItemChanged(List<Movie> movieList) {
        getListMovieFragment().insertItem(movieList);
    }

    ListFragment getListMovieFragment() {
        return (ListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_TAG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.getAppComponent().inject(this);
        ButterKnife.bind(this);

        savedInstance = savedInstanceState;

        checkConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ListFragment f = (ListFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_TAG);

        if (f == null) return true;

        switch (item.getItemId()) {
            case R.id.action_sort_popularity: {
                if (!isPopularMoviesShown) fetchData(true);
                break;
            }
            case R.id.action_sort_rating: {
                if (isPopularMoviesShown) fetchData(false);
                break;
            }
        }
        return true;
    }

    @Override
    public void onItemClicked(Movie selectedMovie) {
        startActivity(DetailsActivity.createIntent(this, selectedMovie));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Parcelable p = Parcels.wrap(movieList);
        outState.putParcelable("data", p);

        super.onSaveInstanceState(outState);
    }

    /**
     * Check connection by try to connect to Google DNS
     */
    void checkConnection() {
        progressBar.setVisibility(View.VISIBLE);
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected void onPreExecute() {
                layoutRetry.setVisibility(View.INVISIBLE);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    int timeoutMs = 1500; //ms
                    Socket sock = new Socket();
                    SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

                    sock.connect(sockaddr, timeoutMs);
                    sock.close();

                    return true;
                } catch (IOException e) { return false; }
            }

            @Override
            protected void onPostExecute(Boolean isConnect) {
                if (isConnect) onSuccessConnect();
                else onFailConnect();
            }
        }.execute();
    }

    @Override
    public void onSuccessConnect() {
        layoutRetry.setVisibility(View.INVISIBLE);

        if (savedInstance != null) {
            progressBar.setVisibility(View.INVISIBLE);

            Parcelable p = savedInstance.getParcelable("data");
            movieList = Parcels.unwrap(p);
        } else fetchData(true);

        ListFragment f = ListFragment.newInstance(2, movieList);
        startFragment(f);
    }

    private void startFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, f, FRAGMENT_LIST_TAG)
                .commit();
    }

    private void fetchData(boolean isByPopularity) {
        isPopularMoviesShown = isByPopularity;

        if (isByPopularity)
            movieDbAPI.fetchPopularMovie(BuildConfig.API_KEY_MOVDB, 1).enqueue(callBack);
        else movieDbAPI.fetchTopMovie(BuildConfig.API_KEY_MOVDB, 1).enqueue(callBack);

    }

    @Override
    public void onFailConnect() {
        progressBar.setVisibility(View.INVISIBLE);
        layoutRetry.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.text_connection_retry)
    void onClickRetry() {
        checkConnection();
    }
}
