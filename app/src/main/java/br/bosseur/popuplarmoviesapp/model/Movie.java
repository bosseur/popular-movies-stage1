package br.bosseur.popuplarmoviesapp.model;

/**
 * Class containing the information from a movie that is retrieved from the Movie database API.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Movie implements Parcelable {

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie() {
    }

    public Movie(Parcel parcel) {
        adult = parcel.readByte() != 0;
        backdropPath = parcel.readString();
        parcel.createIntArray();
        id = parcel.readLong();
        originalLanguage = parcel.readString();
        originalTitle = parcel.readString();
        overview = parcel.readString();
        popularity = parcel.readFloat();
        posterPath = parcel.readString();
        releaseDate = parcel.readString();
        title = parcel.readString();
        video = parcel.readByte() != 0;
        voteAverage = parcel.readFloat();
        voteCount = parcel.readLong();
    }

    public Movie(boolean adult, String backdroPath, int[] genreIds, long id,
                 String originalLanguage, String originalTitle,
                 String overview, float popularity,
                 String posterPath, String releaseDate,
                 String title, boolean video,
                 float voteAverage, long voteCount) {
        this.adult = adult;
        this.backdropPath = backdroPath;
        this.genreIds = genreIds;
        this.id = id;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.title = title;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    private boolean adult;
    private String backdropPath;
    private int[] genreIds;
    private long id;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private float popularity;
    private String posterPath;
    private String releaseDate;
    private String title;
    private boolean video;
    private float voteAverage;
    private long voteCount;


    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "adult=" + adult +
                ", backdropPath='" + backdropPath + '\'' +
                ", genreIds=" + Arrays.toString(genreIds) +
                ", id=" + id +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + posterPath + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", title='" + title + '\'' +
                ", video=" + video +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(backdropPath);
        parcel.writeIntArray(genreIds);
        parcel.writeLong(id);
        parcel.writeString(originalLanguage);
        parcel.writeString(originalTitle);
        parcel.writeString(overview);
        parcel.writeFloat(popularity);
        parcel.writeString(posterPath);
        parcel.writeString(releaseDate);
        parcel.writeString(title);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeFloat(voteAverage);
        parcel.writeLong(voteCount);
    }
}
