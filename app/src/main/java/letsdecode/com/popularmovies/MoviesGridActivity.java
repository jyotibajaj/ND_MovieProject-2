package letsdecode.com.popularmovies;

import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import adapter.MovieAdapter;
import data.MovieData;
import utilities.NetworkUtils;

import static utilities.NetworkUtils.buildPopularMovies;
import static utilities.NetworkUtils.buildTopRated;

public class MoviesGridActivity extends BaseActivity implements MovieAdapter.CustomItemClickInterface {
    private String TAG = this.getClass().getSimpleName();
    private int GRID_COLUMNS = 2;

    //arraylist storing the data of the app.
    ArrayList<MovieData> movieDatas = new ArrayList<>();

    // Reference to Adapter
    static MovieAdapter mMovieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);
        // Lookup the recyclerview in activity layout
        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rvContacts);
        // Set layout manager to position the items
        rvMovies.setLayoutManager(new GridLayoutManager(this, GRID_COLUMNS));
        //instantiating the adapter
        mMovieAdapter = new MovieAdapter(this, movieDatas);
        // bind the listener
        mMovieAdapter.setClickListener(this);
        // Attach the adapter to the recyclerview to populate items
        rvMovies.setAdapter(mMovieAdapter);
        //  manually check the internet status and change the text status
        load(getNetworkInfo());
    }


    @Override
    protected void onNetworkChanged() {
//        Log.d(TAG, "onNetworkChanged Called ");
        load(getNetworkInfo());
//        Log.d(TAG, "load gets Called ");

    }

    /**
     * Checks internet connection and according to state change the
     * text of activity by calling method
     */

    private void load(NetworkInfo networkInfo) {
        if (networkInfo == null) {
            Toast.makeText(this, "No Network", Toast.LENGTH_LONG).show();
            return;
        }
        int val = getIntent().getIntExtra("CURRENTVIEW", R.id.popular);
        URL url = null;
        if (val == R.id.popular) {
            url = buildPopularMovies();
        } else if (val == R.id.top_rated) {
            url = buildTopRated();
        }
        if (url != null) {
            new MoviesQueryTask().execute(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClicked = item.getItemId();
        if (itemThatWasClicked == R.id.popular) {
            getIntent().putExtra("CURRENTVIEW", R.id.popular);
        } else if (itemThatWasClicked == R.id.top_rated) {
            getIntent().putExtra("CURRENTVIEW", R.id.top_rated);
        }
        load(getNetworkInfo());
        return super.onOptionsItemSelected(item);

    }


    /*
    This is where we receive our callback from
     * {@link com.example.android.recyclerview.MovieAdapter}
     *
     * This callback is invoked when you click on an item in the list.
     *
     * @param itemClicked Index in the list of the item that was clicked.
     */
    @Override
    public void onListItemClick(View view, int itemClicked) {
        MovieData item = movieDatas.get(itemClicked);
        //creating intent and adding data to transfer.
        startActivity(DetailActivity.createIntent(this, item));
    }


    //async task class
    public static class MoviesQueryTask extends AsyncTask<URL, Void, ArrayList<MovieData>> {

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

        //set movie data to adapter
        @Override
        protected void onPostExecute(ArrayList<MovieData> posterPathResultFromNetwork) {
            mMovieAdapter.setMovieData(posterPathResultFromNetwork);
        }
    }


}















