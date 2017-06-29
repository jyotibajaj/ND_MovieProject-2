package data;

import android.os.Parcel;
import android.os.Parcelable;


public class MovieData implements Parcelable {
    public static final String BASE_URL = "http://image.tmdb.org/t/p/";


    private String imageUrl;
    private String title;
    private String release_date;
    private double vote_average;
    private String overview;
    private int id;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public MovieData(String moviePosterUrl, String date, String title, double vote_average, String overview, int id) {
        this.imageUrl = moviePosterUrl;
        this.release_date = date;
        this.title = title;
        this.vote_average = vote_average;
        this.overview = overview;
        this.id = id;

    }

    public String getMoviePosterUrl() {
        return BASE_URL + "w500" + imageUrl;
    }


    public String getMoviePosterUrlw300() {
        return BASE_URL + "w300" + imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.title);
        dest.writeString(this.release_date);
        dest.writeDouble(this.vote_average);
        dest.writeString(this.overview);
        dest.writeInt(this.id);
    }

    protected MovieData(Parcel in) {
        this.imageUrl = in.readString();
        this.title = in.readString();
        this.release_date = in.readString();
        this.vote_average = in.readDouble();
        this.overview = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel source) {
            return new MovieData(source);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
}






