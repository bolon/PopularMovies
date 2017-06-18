package com.nnd.popularmovies.dependency;

import com.nnd.popularmovies.main.DetailsActivity;
import com.nnd.popularmovies.main.ListFragment;
import com.nnd.popularmovies.main.MainActivity;
import com.nnd.popularmovies.main.MyListRecyclerViewAdapter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Android dev on 6/17/17.
 */

@Singleton
@Component(modules = {NetworkModule.class})
public interface AppComponent {
    void inject(MainActivity a);

    void inject(ListFragment f);

    void inject(MyListRecyclerViewAdapter adapter);

    void inject(DetailsActivity a);
}
