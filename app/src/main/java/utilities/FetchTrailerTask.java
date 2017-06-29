package utilities;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import letsdecode.com.popularmovies.data.TrailerData;
import letsdecode.com.popularmovies.DetailActivity;

/**
 * Created by aashi on 14/06/17.
 */

public class FetchTrailerTask extends AsyncTask<URL, Void, TrailerData> {
    int movieId;

    public FetchTrailerTask(int id) {
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
        DetailActivity.playIconButton.setTag(trailerData);


    }


}
