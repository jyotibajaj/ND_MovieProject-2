package utilities;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import letsdecode.com.popularmovies.data.ReviewData;
import letsdecode.com.popularmovies.DetailActivity;

/**
 * Created by aashi on 15/06/17.
 */

public class FetchReviewTask extends AsyncTask<URL, Void, ArrayList<ReviewData>> {
    int movieId;
    ReviewData reviewData = null;

    public FetchReviewTask(int id) {
        movieId = id;
    }
    @Override
    protected ArrayList<ReviewData> doInBackground(URL... params) {
        ArrayList<ReviewData> reviewDataArrayList = null;
        URL searchUrl = NetworkUtils.createReviewsUrl(movieId);
        ReviewData reviewData = null;
        try {
            reviewDataArrayList = NetworkUtils.getReviewResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewDataArrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<ReviewData> reviewDataArrayList) {
        DetailActivity.reviewTextViewClickable.setTag(reviewDataArrayList);



    }
}
