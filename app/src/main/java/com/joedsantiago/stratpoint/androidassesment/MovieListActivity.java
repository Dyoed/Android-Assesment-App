package com.joedsantiago.stratpoint.androidassesment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.joedsantiago.stratpoint.androidassesment.data.Movie;
import com.joedsantiago.stratpoint.androidassesment.image.ImageLoader;


public class MovieListActivity extends AppCompatActivity
        implements MovieListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        ImageLoader.init(getApplicationContext(), 5);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            ((MovieListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.movie_list))
                    .setActivateOnItemClick(true);
        }
    }

    public boolean isTwoPane() {
        return mTwoPane;
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(movie.getTitle());
            if (currentFragment == null || !currentFragment.isAdded()) {
                MovieDetailFragment fragment = MovieDetailFragment.newInstance(movie);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment, movie.getTitle())
                        .commit();
            }
        } else {
            Intent detailIntent = new Intent(this, MovieDetailActivity.class);
            detailIntent.putExtra(MovieDetailFragment.ARG_MOVIE, movie);
            startActivity(detailIntent);
        }
    }
}
