package utilities;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import letsdecode.com.popularmovies.data.TrailerData;

/**
 * Created by aashi on 14/06/17.
 */

public class FetchTrailerTask extends AsyncTask<URL, Void, TrailerData> {
    int movieId;
    private OnFetchComplete onTrailerFetchComplete;


    public FetchTrailerTask(OnFetchComplete onTrailerFetchComplete, int id) {
        this.onTrailerFetchComplete = onTrailerFetchComplete;
        movieId = id;
    }

    @Override
    protected TrailerData doInBackground(URL... params) {
        URL searchUrl = NetworkUtils.createIntermediateTrailerUrl(movieId);
        TrailerData trailerData = null;
        try {
            trailerData = NetworkUtils.getTrailerResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerData;
    }

    @Override
    protected void onPostExecute(TrailerData trailerData) {
        onTrailerFetchComplete.onSuccessFetchTrailer(trailerData);


    }


}
