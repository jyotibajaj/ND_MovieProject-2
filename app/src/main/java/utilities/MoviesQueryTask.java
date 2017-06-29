package utilities;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import letsdecode.com.popularmovies.data.MovieData;
import letsdecode.com.popularmovies.MoviesGridActivity;

/**
 * Created by aashi on 12/06/17.
 */

//async task class
public  class MoviesQueryTask extends AsyncTask<URL, Void, ArrayList<MovieData>> {

    @Override
    protected ArrayList<MovieData> doInBackground(URL... params) {
        URL searchUrl = params[0];
        ArrayList<MovieData> results = null;
        try {
            results = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return results;
    }

    //set movie letsdecode.com.popularmovies.data to letsdecode.com.popularmovies.adapter
    @Override
    protected void onPostExecute(ArrayList<MovieData> posterPathResultFromNetwork) {
        MoviesGridActivity.mMovieAdapter.setMovieData(posterPathResultFromNetwork);
    }
}
