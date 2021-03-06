package letsdecode.com.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import letsdecode.com.popularmovies.data.MovieContract;
import letsdecode.com.popularmovies.data.MovieData;
import letsdecode.com.popularmovies.data.ReviewData;
import letsdecode.com.popularmovies.data.TrailerData;
import utilities.FetchReviewTask;
import utilities.FetchTrailerTask;
import utilities.OnFetchComplete;

public class DetailActivity extends BaseActivity implements OnFetchComplete {


    public static final String MOVIE_DATA_KEY = "movieDataKey";
    String id;
    String cursorMovieId;
    //receive letsdecode.com.popularmovies.data from intent
    MovieData movieData;


    TrailerData trailerData;

    @BindView(R.id.favorite_button)
    CheckBox favoriteButton;

    @BindView(R.id.selected_poster_image)
    ImageView tv_PosterImageView;

    @BindView(R.id.tv_release_date)
    TextView dateTextView;


    @BindView(R.id.tv_title)
    TextView titleTextView;

    @BindView(R.id.tv_vote)
    TextView voteTextView;

    @BindView(R.id.tv_overview)
    TextView overviewTextView;

    @BindView(R.id.image_play_btn)
    public ImageButton playIconButton;

    @BindView(R.id.tv_check_reviews)
    public TextView reviewTextViewClickable;

    @BindView(R.id.pop_ratingbar)
    RatingBar ratingBar;

    /**
     * This method creates the intent to start the Detail activity.
     *
     * @param activity  the caller activity.
     * @param movieData object of movie letsdecode.com.popularmovies.data.
     * @return intent.
     */
    public static Intent createIntent(Activity activity, final MovieData movieData) {
        Intent detailIntent = new Intent(activity, DetailActivity.class);
        detailIntent.putExtra(DetailActivity.MOVIE_DATA_KEY, movieData);
        if (movieData == null) {
            throw new IllegalStateException("illegal movie letsdecode.com.popularmovies.data");
        }
        return detailIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //initializing the views
        ButterKnife.bind(this);


        Intent detailIntent = getIntent();
        movieData = detailIntent.getParcelableExtra(MOVIE_DATA_KEY);
        if (movieData == null) {
            throw new IllegalStateException("illegal movie letsdecode.com.popularmovies.data");
        }

        //setting the data into views
        dateTextView.setText(movieData.getRelease_date());
        titleTextView.setText(movieData.getTitle());
        String voteAverage = movieData.getVote_average() + "";
        voteTextView.setText(voteAverage);
        if(movieData.getVote_average() > 0) {
            ratingBar.setRating(movieData.getVote_average());
        }
        id = movieData.getId() + "";
        overviewTextView.setText(movieData.getOverview());
        Picasso.with(getApplicationContext()).load(movieData.getMoviePosterUrlw300()).into(tv_PosterImageView);

        //fetching you tube url source.
        new FetchTrailerTask(this,movieData.getId()).execute();

        //fetching reviews on click of review textView
        new FetchReviewTask(this, movieData.getId()).execute();


        //enabling back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String mSelection = MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] mSelectionArgs = new String[]{id};


        Uri idQueryUri = MovieContract.FavoriteMoviesEntry.CONTENT_URI
                .buildUpon().appendQueryParameter(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, id).build();

        Cursor checkFavoritesMoviesCursor = getContentResolver().query(
                idQueryUri,
                null,
                mSelection,
                mSelectionArgs,
                null);

        if (checkFavoritesMoviesCursor != null && checkFavoritesMoviesCursor.moveToFirst()) {
            cursorMovieId = checkFavoritesMoviesCursor.getString(checkFavoritesMoviesCursor.getColumnIndex(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID));
            checkFavoritesMoviesCursor.close();
        }

        if (detailIntent.hasExtra("favoritesCheck")) {
            if (detailIntent.getBooleanExtra("favoritesCheck", true)) {
                favoriteButton.toggle();
            }
        } else if (cursorMovieId != null && cursorMovieId.equals(id)) {
            favoriteButton.toggle();
        }


    }

    /*
    play icon button click.
     */

    public void clickToPlay(View v) {
        trailerData = (TrailerData) playIconButton.getTag();
        String chooserTitle = "R.String.chooserTitle";
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
        trailerIntent.setData(Uri.parse(trailerData.getTrailerUrl()));
        trailerIntent = Intent.createChooser(trailerIntent, chooserTitle);
        startActivity(trailerIntent);
    }

    public void checkReviewsClick(View v) {
        ArrayList<ReviewData> reviewDataArrayList = (ArrayList<ReviewData>) reviewTextViewClickable.getTag();
        startActivity(ReviewsActivity.createIntentFromDetailActivity(this, reviewDataArrayList));
    }


    @Override
    protected void onNetworkChanged() {

    }


    // on click of favorite star button
    public void addOrRemoveFromFavorites(View view) {
        if (favoriteButton.isChecked()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER, movieData.getImageUrl());
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE, movieData.getTitle());
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_OVERVIEW, movieData.getOverview());
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_DATE, movieData.getRelease_date());
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_VOTES, movieData.getVote_average() + "");
            contentValues.put(MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, movieData.getId() + "");
            Uri uri = getContentResolver().insert(MovieContract.FavoriteMoviesEntry.CONTENT_URI, contentValues);
            if (uri != null) {
                System.out.println("Saved " + uri.toString());
            }
        }
        if (!favoriteButton.isChecked()) {
            String movieId = movieData.getId() + "";
            int rowsDeleted = getContentResolver().delete(MovieContract.FavoriteMoviesEntry.CONTENT_URI,
                    MovieContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + "=" + movieId, null);
            if (rowsDeleted > 0) {
                System.out.println("Deleted " + movieData.getId() + "");
            } else {
                System.out.println("Delete failed");
            }
        }
    }




    @Override
    public void onSuccessFetchReviews(ArrayList arrayList) {
        reviewTextViewClickable.setTag(arrayList);

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onSuccessFetchTrailer(TrailerData trailerData) {
        playIconButton.setTag(trailerData);
    }
}




