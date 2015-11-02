package com.joedsantiago.stratpoint.androidassesment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.joedsantiago.stratpoint.androidassesment.data.Movie;
import com.joedsantiago.stratpoint.androidassesment.image.ImageLoader;


public class MovieDetailFragment extends Fragment {

    public static final String ARG_MOVIE = "movie";
    private Movie mMovie;
    private ImageView backDropImage;
    private ImageView coverPhoto;
    private TextView rating;
    private TextView title;
    private TextView yearReleased;
    private TextView overview;

    public MovieDetailFragment() {
    }

    public static MovieDetailFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE)) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        backDropImage = (ImageView) rootView.findViewById(R.id.backdrop_iv);
        coverPhoto = (ImageView) rootView.findViewById(R.id.cover_photo_iv);
        title = (TextView) rootView.findViewById(R.id.title_tv);
        yearReleased = (TextView) rootView.findViewById(R.id.year_release_tv);
        rating = (TextView) rootView.findViewById(R.id.rating_tv);
        overview = (TextView) rootView.findViewById(R.id.overview_tv);


        if (mMovie != null) {
            ImageLoader.load(mMovie.getBackdropPhotoUrl()).to(backDropImage).start();
            ImageLoader.load(mMovie.getCoverPhotoUrl()).to(coverPhoto).resetViewBeforeLoading(false).start();
            title.setText(mMovie.getTitle());
            yearReleased.setText(mMovie.getReleaseDate());
            rating.setText("Rating: " + mMovie.getRating());
            overview.setText(mMovie.getOverView());
        }

        return rootView;
    }
}
