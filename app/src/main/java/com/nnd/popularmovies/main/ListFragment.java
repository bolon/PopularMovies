package com.nnd.popularmovies.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nnd.popularmovies.App;
import com.nnd.popularmovies.BuildConfig;
import com.nnd.popularmovies.R;
import com.nnd.popularmovies.dependency.MovieDbAPI;
import com.nnd.popularmovies.main.movies.Movie;
import com.nnd.popularmovies.main.movies.ResponseMovieAPI;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    @Inject MovieDbAPI movieDbAPI;
    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    private int mColumnCount = 2;
    private boolean isPopularMoviesShown = true;
    private OnListFragmentInteractionListener mListener;
    private Callback<ResponseMovieAPI> callBack = new Callback<ResponseMovieAPI>() {
        @Override
        public void onResponse(Call<ResponseMovieAPI> call, Response<ResponseMovieAPI> response) {
            try {
                Timber.i("Fetch success... " + response.body().getMovieList().size());

                recyclerView.setAdapter(new MyListRecyclerViewAdapter(response.body()
                                                                              .getMovieList(), mListener));
            } catch (NullPointerException ex) {
                progressBar.setVisibility(View.INVISIBLE);
                Timber.i(call.request().url().toString());
            }
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onFailure(Call<ResponseMovieAPI> call, Throwable t) {
            Timber.e("Fetch failed... " + t.getMessage());
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), R.string.toast_text_check_con, Toast.LENGTH_LONG).show();
        }
    };

    public ListFragment() {
    }

    public static ListFragment newInstance(int columnCount) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getAppComponent().inject(this);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_list, container, false);

        ButterKnife.bind(this, view);

        // Set the adapter
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), mColumnCount));
        }

        if (recyclerView.getAdapter() == null) fetchData(true);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isPopularShown", isPopularMoviesShown);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            fetchData(savedInstanceState.getBoolean("isPopularShown", true));
        }
    }

    private void fetchData(boolean isByPopularity) {
        isPopularMoviesShown = isByPopularity;
        recyclerView.setAdapter(null);
        progressBar.setVisibility(View.VISIBLE);

        if (isByPopularity)
            movieDbAPI.fetchPopularMovie(BuildConfig.API_KEY_MOVDB, 1).enqueue(callBack);
        else movieDbAPI.fetchTopMovie(BuildConfig.API_KEY_MOVDB, 1).enqueue(callBack);

    }

    public void sortBy(boolean isByPopularity) {
        if (isByPopularity == isPopularMoviesShown) return;

        fetchData(isByPopularity);
    }

    public interface OnListFragmentInteractionListener {
        void onItemClicked(Movie item);
    }
}
