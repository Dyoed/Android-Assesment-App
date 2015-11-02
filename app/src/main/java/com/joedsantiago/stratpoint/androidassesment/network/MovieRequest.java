package com.joedsantiago.stratpoint.androidassesment.network;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.joedsantiago.stratpoint.androidassesment.data.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dyoed on 10/31/15.
 * A HTTP GET request with single worker for getting movies
 */
public class MovieRequest implements Request<List<Movie>> {

    private ExecutorService mExecutor;
    private Handler mResponseHandler;
    private static final String TAG = "MovieRequest";
    private static final String IMG_ENDPOINT = "https://dl.dropboxusercontent.com/u/5624850/movielist/images/";
    private static final String BACKDROP_EXT = "-backdrop.jpg";
    private static final String COVER_EXT = "-cover.jpg";

    public MovieRequest() {
        mExecutor = Executors.newSingleThreadExecutor();
        mResponseHandler = new Handler(Looper.getMainLooper());
    }

    private  JSONObject parseToJson(InputStream inputStream) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        Log.d(TAG, "Reading response...");
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }

        br.close();
        JSONObject jsonObject = new JSONObject(sb.toString());
        Log.d(TAG, "Conversion to JSON success");
        return jsonObject;
    }

    private List<Movie> parseToMovies(JSONObject jsonObject) throws JSONException {
        if(jsonObject.getString(Key.STATUS).equals(Key.OK)){

            List<Movie> movies;
            JSONArray jsonArray = jsonObject.getJSONObject(Key.DATA).getJSONArray(Key.MOVIES);
            movies = new ArrayList<>(jsonArray.length());
            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i<jsonArray.length() ; i++){
                JSONObject j = jsonArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setId(j.getLong(Key.ID));
                movie.setTitle(j.getString(Key.TITLE));
                movie.setTitleLong(j.getString(Key.TITLE_FULL));
                movie.setOverView(j.getString(Key.OVERVIEW));
                movie.setSlug(j.getString(Key.SLUG));
                movie.setReleaseDate(j.getString(Key.YEAR));
                movie.setState(j.getString(Key.STATE));
                movie.setRating(j.getDouble(Key.RATING));
                JSONArray genres = j.optJSONArray(Key.GENRE);
                if(genres != null){
                    List<String> g = new ArrayList<>(genres.length());
                    for (int ctr =0; ctr<genres.length(); ctr++){
                        g.add(genres.getString(ctr));
                    }
                    movie.setGenres(g);
                }
                movie.setImdbCode(j.getString(Key.IMDB_CODE));
                movie.setMpaRating(j.getString(Key.MPA_RATING));
                movie.setRuntime(j.getInt(Key.RUNTIME));
                movie.setLanguage(j.getString(Key.LANG));
                movie.setUrl(j.getString(Key.URL));
                stringBuilder.setLength(0);
                movie.setBackdropPhotoUrl(stringBuilder.append(IMG_ENDPOINT)
                        .append(movie.getSlug())
                        .append(BACKDROP_EXT)
                        .toString());
                stringBuilder.setLength(0);
                movie.setCoverPhotoUrl(stringBuilder.append(IMG_ENDPOINT)
                        .append(movie.getSlug())
                        .append(COVER_EXT)
                        .toString());
                movies.add(movie);
            }
            Log.d(TAG, "Conversion to Movie List successful");
            return movies;

        }
        return null;
    }

    private BufferedInputStream connect(String requestUrl) throws MalformedURLException, IOException {
        URL url = new URL(requestUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        switch (httpURLConnection.getResponseCode()){
            case HttpURLConnection.HTTP_ACCEPTED:
            case HttpURLConnection.HTTP_OK:
                return new BufferedInputStream(httpURLConnection.getInputStream());

            default:
                return null;
        }
    }

    @Override
    public void get(final String url, final RequestCallback<List<Movie>> requestCallback) {

            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "Movie request started for "+url);
                        BufferedInputStream inputStream = connect(url);
                        JSONObject jsonObject = parseToJson(inputStream);
                        final List<Movie> movies = parseToMovies(jsonObject);
                        mResponseHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                requestCallback.onCompleted(movies);
                            }
                        });

                        Log.d(TAG, "Movie request successful");
                    } catch (IOException e) {
                        mResponseHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                requestCallback.onError("Please check your internet connection");
                            }
                        });

                        e.printStackTrace();
                    } catch (JSONException e) {
                        mResponseHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                requestCallback.onError("Something went wrong");
                            }
                        });

                        e.printStackTrace();
                    }
                }
            });
    }

    @Override
    public void cancelRequest(String url) {

    }
}
