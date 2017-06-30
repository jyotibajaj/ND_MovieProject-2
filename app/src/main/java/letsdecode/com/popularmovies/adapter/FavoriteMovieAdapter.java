package letsdecode.com.popularmovies.adapter;

/**
 * Created by aashi on 28/06/17.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import letsdecode.com.popularmovies.DetailActivity;
import letsdecode.com.popularmovies.R;
import letsdecode.com.popularmovies.data.MovieContract;
import letsdecode.com.popularmovies.data.MovieData;




public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMoviesViewHolder> {

    public static final String FAVORITE_CHECK = "favoriteKey";

    private Context mContext;
    private Cursor mCursor;

    final static String PICASSO_IMAGE_BASE_URL =
            "http://image.tmdb.org/t/p/w342/";

    public FavoriteMovieAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private final FavoriteMoviesCursorAdapterOnClickHandler mClickHandler = new FavoriteMoviesCursorAdapterOnClickHandler() {
        @Override
        public void onClick(MovieData movieData) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(DetailActivity.MOVIE_DATA_KEY, movieData);
            intent.putExtra(FAVORITE_CHECK, true);
            mContext.startActivity(intent);
        }
    };

    public interface FavoriteMoviesCursorAdapterOnClickHandler {
        void onClick(MovieData movieData);
    }

    @Override
    public FavoriteMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.favorite_list_item, parent, false);

        return new FavoriteMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMovieAdapter.FavoriteMoviesViewHolder holder, int position) {
        // Indice for the ID and poster column
        int idIndex = mCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry._ID);
        int posterIndex = mCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        holder.itemView.setTag(id);
        String posterPath = mCursor.getString(posterIndex);
        Uri builtUri = Uri.parse(PICASSO_IMAGE_BASE_URL).buildUpon().appendEncodedPath(posterPath)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
//        Picasso.with(holder.favoriteMovieImageView.getContext()).load(url.toString()).into(holder.favoriteMovieImageView);

        Picasso.with(mContext).load(url.toString()).into(holder.favoriteMovieImageView);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        Log.d("COUNT FAVORITEMOVIE",mCursor.getCount() + "");
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    // Inner class for creating ViewHolders
    public class FavoriteMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_item_favorite_movie)
        ImageView favoriteMovieImageView;

        public FavoriteMoviesViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            int posterIndex = mCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER);
            int titleIndex = mCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE);
            int overviewIndex = mCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_OVERVIEW);
            int dateIndex = mCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_DATE);
            int voteIndex = mCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_VOTES);
            int idIndex = mCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID);

            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

            Float movieVotes = mCursor.getFloat(voteIndex);
            int movieId = mCursor.getInt(idIndex);
            //String moviePosterUrl, String date, String title, float vote_average, String overview, int id
            MovieData movieDataInFavorites = new MovieData(mCursor.getString(posterIndex),mCursor.getString(dateIndex),mCursor.getString(titleIndex),movieVotes,mCursor.getString(overviewIndex),Integer.valueOf(movieId));

            mClickHandler.onClick(movieDataInFavorites);
        }
    }
}

