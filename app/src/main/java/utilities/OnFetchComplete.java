package utilities;

import java.util.ArrayList;

import letsdecode.com.popularmovies.data.TrailerData;

/**
 * Created by aashi on 30/06/17.
 * Interface is used to populate the data
 * Reviews and trailer
 */
public interface OnFetchComplete {
    void onSuccessFetchReviews(ArrayList arrayList);
    void onSuccessFetchTrailer(TrailerData trailerData);
    void onError(Throwable e);
}
