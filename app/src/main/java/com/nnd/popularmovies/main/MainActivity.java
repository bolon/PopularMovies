package com.nnd.popularmovies.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.nnd.popularmovies.App;
import com.nnd.popularmovies.R;
import com.nnd.popularmovies.dependency.MovieDbAPI;
import com.nnd.popularmovies.main.movies.Movie;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListFragmentInteractionListener, ConnectionCheckListener {
    private static final String FRAGMENT_LIST_TAG = "fragment_list";
    @Inject MovieDbAPI movieDbAPI;
    @BindView(R.id.container) FrameLayout f;
    @BindView(R.id.layout_retry) View layoutRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.getAppComponent().inject(this);
        ButterKnife.bind(this);

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
                f.sortBy(true);
                break;
            }
            case R.id.action_sort_rating: {
                f.sortBy(false);
                break;
            }
        }
        return true;
    }

    @Override
    public void onItemClicked(Movie selectedMovie) {
        startActivity(DetailsActivity.createIntent(this, selectedMovie));
    }

    /**
     * Check connection by try to connect to Google DNS
     */
    void checkConnection() {
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
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, ListFragment.newInstance(2), FRAGMENT_LIST_TAG)
                .commit();
    }

    @Override
    public void onFailConnect() {
        layoutRetry.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.text_connection_retry)
    void onClickRetry() {
        checkConnection();
    }
}
