package com.joedsantiago.stratpoint.androidassesment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.joedsantiago.stratpoint.androidassesment.data.Movie;
import com.joedsantiago.stratpoint.androidassesment.network.MovieRequest;
import com.joedsantiago.stratpoint.androidassesment.network.RequestCallback;

import java.util.List;

public class MovieListFragment extends ListFragment {


    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Callbacks mCallbacks = onMovieSelected;
    private int mActivatedPosition = ListView.INVALID_POSITION;

    public interface Callbacks {
        void onItemSelected(Movie movie);
    }

    private String movieUrl = "https://dl.dropboxusercontent.com/u/5624850/movielist/list_movies_page1.json";

    private static Callbacks onMovieSelected = new Callbacks() {

        @Override
        public void onItemSelected(Movie movie) {

        }
    };

    public MovieListFragment() {
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        new MovieRequest().get(movieUrl,
                new RequestCallback<List<Movie>>() {
                    @Override
                    public void onCompleted(List<Movie> movies) {
                        setListAdapter(new MovieAdapter(getActivity(), R.layout.movie_list_item, movies));
                        if(((MovieListActivity)getActivity()).isTwoPane() && savedInstanceState == null){
                            mCallbacks.onItemSelected((Movie) getListAdapter().getItem(0));
                        }
                    }

                    @Override
                    public void onError(String errMsg) {
                        Toast.makeText(getActivity(), "Unable to connect", Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        mCallbacks.onItemSelected((Movie) getListAdapter().getItem(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
}
