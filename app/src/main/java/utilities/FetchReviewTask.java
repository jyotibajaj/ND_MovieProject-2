package utilities;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import letsdecode.com.popularmovies.data.ReviewData;

/**
 * Created by aashi on 15/06/17.
 */

public class FetchReviewTask extends AsyncTask<URL, Void, ArrayList<ReviewData>> {
    private int movieId;
    private OnFetchComplete onFetchComplete;

//    public interface OnFetchComplete {
//        void onSuccessFetchReviews(ArrayList<ReviewData> arrayList);
//        void onError(Throwable e);
//
//    }

    public FetchReviewTask(OnFetchComplete onFetchComplete, int id) {
        this.onFetchComplete = onFetchComplete;
        movieId = id;
    }

    @Override
    protected ArrayList<ReviewData> doInBackground(URL... params) {
        ArrayList<ReviewData> reviewDataArrayList = null;
        URL searchUrl = NetworkUtils.createReviewsUrl(movieId);
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
        onFetchComplete.onSuccessFetchReviews(reviewDataArrayList);
    }
}
