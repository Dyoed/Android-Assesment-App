package com.joedsantiago.stratpoint.androidassesment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.joedsantiago.stratpoint.androidassesment.data.Movie;
import com.joedsantiago.stratpoint.androidassesment.image.ImageLoader;

import java.util.List;

/**
 * Created by dyoed on 10/30/15.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context context, int resource) {
        super(context, resource);
    }

    public MovieAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieViewHolder movieViewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
            movieViewHolder = new MovieViewHolder();
            movieViewHolder.backDropIv = (ImageView) convertView.findViewById(R.id.backdrop_iv);
            movieViewHolder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            movieViewHolder.yearReleasedTv = (TextView) convertView.findViewById(R.id.release_date_tv);
            convertView.setTag(movieViewHolder);
        }
        else{
            movieViewHolder = (MovieViewHolder) convertView.getTag();
        }

        Movie movie = getItem(position);
        movieViewHolder.titleTv.setText(movie.getTitle());
        movieViewHolder.yearReleasedTv.setText(movie.getReleaseDate());
        ImageLoader.load(movie.getBackdropPhotoUrl()).to(movieViewHolder.backDropIv).start();

        return convertView;
    }


    static class MovieViewHolder{
        public ImageView backDropIv;
        public TextView titleTv;
        public TextView yearReleasedTv;
    }
}
