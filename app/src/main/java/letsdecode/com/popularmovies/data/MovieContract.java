package data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.moviesapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favoriteMovies";

    public static final class FavoriteMoviesEntry implements BaseColumns {

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of favorite movies.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single favorite movie.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favoriteMoviesTable";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_DATE = "date";
        public static final String COLUMN_MOVIE_VOTES = "votes";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_TRAILER = "trailer";
    }
}




