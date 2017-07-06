package letsdecode.com.popularmovies;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

import letsdecode.com.popularmovies.adapter.MovieAdapter;
import letsdecode.com.popularmovies.data.MovieData;
import utilities.MoviesQueryTask;

import static utilities.NetworkUtils.buildPopularMoviesUrl;
import static utilities.NetworkUtils.buildTopRatedUrl;

public class MoviesGridActivity extends BaseActivity implements MovieAdapter.CustomItemClickInterface {

    private static final String MOVIES_KEY = "movies";
    private String TAG = this.getClass().getSimpleName();
    private int GRID_COLUMNS = 2;
    public final static String LIST_STATE_KEY = "recycler_list_state";
    Parcelable listState;
    GridLayoutManager mGridLayoutManager;


    //arraylist storing the letsdecode.com.popularmovies.data of the app.
    ArrayList<MovieData> movieDatas = new ArrayList<>();

    // Reference to Adapter
    public static MovieAdapter mMovieAdapter;
    URL mRequiredURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_grid);
        // Lookup the recyclerview in activity layout
        if (savedInstanceState != null) {
            movieDatas = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
        } else {
            mRequiredURL = buildPopularMoviesUrl();
            load(getNetworkInfo(), mRequiredURL);
        }

        RecyclerView rvMovies = (RecyclerView) findViewById(R.id.rv_movie_posters);
        // Set layout manager to position the items
        mGridLayoutManager = new GridLayoutManager(this, GRID_COLUMNS);
        rvMovies.setLayoutManager(mGridLayoutManager);
        //instantiating the letsdecode.com.popularmovies.adapter
        mMovieAdapter = new MovieAdapter(this, movieDatas);
        // bind the listener
        mMovieAdapter.setClickListener(this);
        // Attach the letsdecode.com.popularmovies.adapter to the recyclerview to populate items
        rvMovies.setAdapter(mMovieAdapter);
        //  manually check the internet status and change the text status
//        int index = rvMovies.


    }


    @Override
    protected void onNetworkChanged() {
//        Log.d(TAG, "onNetworkChanged Called ");
        load(getNetworkInfo(), mRequiredURL);
//        Log.d(TAG, "load gets Called ");

    }

    /**
     * Checks internet connection and according to state change the
     * text of activity by calling method
     */

    private void load(NetworkInfo networkInfo, URL url) {
        if (networkInfo == null) {
            Toast.makeText(this, "No Network", Toast.LENGTH_LONG).show();
            return;
        }
        int val = getIntent().getIntExtra("CURRENTVIEW", R.id.popular);
        if (val == R.id.top_rated) {
            url = buildTopRatedUrl();
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
        } else if (itemThatWasClicked == R.id.favorites) {
            Intent intent = new Intent(MoviesGridActivity.this, FavoriteMoviesActivity.class);
            startActivity(intent);
        }
        load(getNetworkInfo(), mRequiredURL);

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
        //creating intent and adding letsdecode.com.popularmovies.data to transfer.
        startActivity(DetailActivity.createIntent(this, item));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES_KEY, movieDatas);
        // Save list state
        listState = mGridLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, listState);
    }


    @Override
    protected void onResume() {

        if (listState != null) {
            mGridLayoutManager.onRestoreInstanceState(listState);
        }
        super.onResume();
    }


    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        // Retrieve list state and list/item positions
        if (state != null)
            listState = state.getParcelable(LIST_STATE_KEY);
    }
}


