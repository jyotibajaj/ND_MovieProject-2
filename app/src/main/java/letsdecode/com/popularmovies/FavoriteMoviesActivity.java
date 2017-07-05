package letsdecode.com.popularmovies;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import letsdecode.com.popularmovies.adapter.FavoriteMovieAdapter;
import letsdecode.com.popularmovies.data.MovieContract.FavoriteMoviesEntry;

/*
This class displays the favorite movies list selected by user.
in a recycler view.
 */
public class FavoriteMoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Identifies a particular Loader being used in this component
    private static final int FAVORITE_MOVIES_LOADER = 0;

    FavoriteMovieAdapter mFavoriteMovieAdapter;
    private RecyclerView mFavoriteMoviesListRecyclerView;
    @BindView(R.id.favorite_movies_error_message_display)
    TextView mErrorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_movies);
        setTitle(R.string.favorites_title);
        //initialization
        mFavoriteMoviesListRecyclerView = (RecyclerView) findViewById(R.id.rv_favorite_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mFavoriteMoviesListRecyclerView.setLayoutManager(layoutManager);
        mFavoriteMoviesListRecyclerView.setHasFixedSize(true);

        mFavoriteMovieAdapter = new FavoriteMovieAdapter(this);
        ButterKnife.bind(this);


        //kick off the loader
        getLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this).forceLoad();
        mFavoriteMoviesListRecyclerView.setAdapter(mFavoriteMovieAdapter);
        Log.d("CREATE INSIDE", "FAVORITEMOVIEACTIVITY");

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        //Define a projection that specifies the columns from the table we care about
        String[] projection = {
                FavoriteMoviesEntry._ID,
                FavoriteMoviesEntry.COLUMN_MOVIE_POSTER,
                FavoriteMoviesEntry.COLUMN_MOVIE_TITLE,
                FavoriteMoviesEntry.COLUMN_MOVIE_OVERVIEW,
                FavoriteMoviesEntry.COLUMN_MOVIE_DATE,
                FavoriteMoviesEntry.COLUMN_MOVIE_VOTES,
                FavoriteMoviesEntry.COLUMN_MOVIE_ID};

        //this loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(
                this,   // Parent activity context
                FavoriteMoviesEntry.CONTENT_URI,        // Table to query
                projection,     // Projection to return
                null,            // No selection clause
                null,            // No selection arguments
                null             // Default sort order
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mFavoriteMovieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoriteMovieAdapter.swapCursor(null);
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            getLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER, null, this);
//        }
//    }




}

