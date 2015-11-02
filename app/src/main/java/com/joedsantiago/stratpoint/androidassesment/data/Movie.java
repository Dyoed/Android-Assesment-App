package com.joedsantiago.stratpoint.androidassesment.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dyoed on 10/31/15.
 */
public class Movie implements Parcelable {

    private long id;
    private int runtime;
    private double rating;
    private String title;
    private String titleLong;
    private String releaseDate;

    private List<String> genres;

    private String url;
    private String overView;
    private String imdbCode;
    private String language;
    private String state;
    private String mpaRating;
    private String slug;
    private String coverPhotoUrl;
    private String backdropPhotoUrl;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleLong() {
        return titleLong;
    }

    public void setTitleLong(String titleLong) {
        this.titleLong = titleLong;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getUrl() {
        return url;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getImdbCode() {
        return imdbCode;
    }

    public void setImdbCode(String imdbCode) {
        this.imdbCode = imdbCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMpaRating() {
        return mpaRating;
    }

    public void setMpaRating(String mpaRating) {
        this.mpaRating = mpaRating;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public void setCoverPhotoUrl(String coverPhotoUrl) {
        this.coverPhotoUrl = coverPhotoUrl;
    }

    public String getBackdropPhotoUrl() {
        return backdropPhotoUrl;
    }

    public void setBackdropPhotoUrl(String backdropPhotoUrl) {
        this.backdropPhotoUrl = backdropPhotoUrl;
    }


    public Movie() {
    }

    protected Movie(Parcel in) {
        id = in.readLong();
        runtime = in.readInt();
        rating = in.readDouble();
        title = in.readString();
        titleLong = in.readString();
        releaseDate = in.readString();
        if (in.readByte() == 0x01) {
            genres = new ArrayList<String>();
            in.readList(genres, String.class.getClassLoader());
        } else {
            genres = null;
        }
        url = in.readString();
        overView = in.readString();
        imdbCode = in.readString();
        language = in.readString();
        state = in.readString();
        mpaRating = in.readString();
        slug = in.readString();
        coverPhotoUrl = in.readString();
        backdropPhotoUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(runtime);
        dest.writeDouble(rating);
        dest.writeString(title);
        dest.writeString(titleLong);
        dest.writeString(releaseDate);
        if (genres == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(genres);
        }
        dest.writeString(url);
        dest.writeString(overView);
        dest.writeString(imdbCode);
        dest.writeString(language);
        dest.writeString(state);
        dest.writeString(mpaRating);
        dest.writeString(slug);
        dest.writeString(coverPhotoUrl);
        dest.writeString(backdropPhotoUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
