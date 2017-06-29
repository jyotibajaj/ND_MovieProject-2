package letsdecode.com.popularmovies.data;

/**
 * Created by aashi on 12/06/17.
 */

public class TrailerData {
    public static final String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";


    private String source;

    public TrailerData(String source) {
        this.source = source;

    }


    /*
      return youtube url string
     */
    public String getTrailerUrl() {
        return TRAILER_BASE_URL + getSource();

    }

    public String getSource() {
        return source;
    }


}
