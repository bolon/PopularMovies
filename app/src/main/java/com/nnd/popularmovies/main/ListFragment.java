package com.nnd.popularmovies.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nnd.popularmovies.App;
import com.nnd.popularmovies.R;
import com.nnd.popularmovies.dependency.MovieDbAPI;
import com.nnd.popularmovies.main.movies.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_DATA = "movie-data";
    @Inject MovieDbAPI movieDbAPI;
    @BindView(R.id.list) RecyclerView recyclerView;
    private int mColumnCount = 2;
    private List<Movie> movieList = new ArrayList<>();
    private OnListFragmentInteractionListener mListener;
    private MyListRecyclerViewAdapter adapter;

    public ListFragment() {
    }

    public static ListFragment newInstance(int columnCount, List<Movie> data) {
        ListFragment fragment = new ListFragment();
        Parcelable p = Parcels.wrap(data);
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putParcelable(ARG_DATA, p);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getAppComponent().inject(this);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            Parcelable p = getArguments().getParcelable(ARG_DATA);
            movieList = Parcels.unwrap(p);
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

        if (!movieList.isEmpty()) {
            adapter = new MyListRecyclerViewAdapter(movieList, mListener);
        } else adapter = new MyListRecyclerViewAdapter(Collections.<Movie>emptyList(), mListener);

        recyclerView.setAdapter(adapter);
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
        outState.putInt("data_2", 3);
        super.onSaveInstanceState(outState);
    }

    void insertItem(List<Movie> movie) {
        adapter.replaceData(movie);
    }

    public interface OnListFragmentInteractionListener {
        void onItemClicked(Movie item);
    }
}
