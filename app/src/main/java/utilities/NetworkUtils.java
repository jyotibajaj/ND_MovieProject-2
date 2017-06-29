package utilities;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import letsdecode.com.popularmovies.data.MovieData;
import letsdecode.com.popularmovies.data.ReviewData;
import letsdecode.com.popularmovies.data.TrailerData;


public class NetworkUtils {
    final static String BASE_URL = "https://api.themoviedb.org/";
    final static String API_VERSION = "3/";
    final static String END_POINT_POPULAR = "movie/popular";
    final static String END_POINT_TOP_RATED = "movie/top_rated";
    final static String END_POINT_TRAILER = "movie/{id}/trailer";
    final static String END_POINT_REVIEWS = "movie/{id}/reviews";
    final static String API_KEY_VALUE = Keys.API_KEY_VALUE;
    final static String API_KEY = "api_key";
    final static String MOVIE_BASE_URL = BASE_URL + API_VERSION;


    public static Uri.Builder createUrlBuilder(String suffix) {
        return Uri.parse(MOVIE_BASE_URL + suffix).buildUpon()
                .appendQueryParameter(API_KEY, API_KEY_VALUE);
    }

    public static URL createIntermediateTrailerUrl(int id) {
        String EndPointTrailer = "movie/" + id + "/trailers";
        Uri.Builder builder = NetworkUtils.createUrlBuilder(EndPointTrailer);
        return buildUrl(builder);
    }


    public static URL createReviewsUrl(int id) {
        String EndPointReview = "movie/" + id + "/reviews";
        Uri.Builder builder = NetworkUtils.createUrlBuilder(EndPointReview);
        return buildUrl(builder);
    }

    /**
     * @return The URL to use.
     */
    public static URL buildUrl(Uri.Builder builder) {

        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * This method build the url for fetching movie letsdecode.com.popularmovies.data sorted for popularity
     */
    public static URL buildPopularMoviesUrl() {
        Uri.Builder builder = createUrlBuilder(END_POINT_POPULAR);
        return buildUrl(builder);

    }

    /**
     * This method build the url for fetching movie letsdecode.com.popularmovies.data as per rating
     */
    public static URL buildTopRatedUrl() {
        Uri.Builder builder = createUrlBuilder(END_POINT_TOP_RATED);
        return buildUrl(builder);

    }

    public static URL buildIntermediateTrailerUrl() {
        Uri.Builder builder = createUrlBuilder(END_POINT_TOP_RATED);
        return buildUrl(builder);

    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static ArrayList<MovieData> getResponseFromHttpUrl(URL url) throws IOException, JSONException {
        ArrayList<MovieData> list = new ArrayList<>();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
//            conn.setReadTimeout(10000 /* milliseconds */);
//            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.d("NetworkUtils", "The response code is: " + responseCode + " " + conn.getResponseMessage());
            InputStream in = conn.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                parseNetworkResponse(list, scanner);

            } else {
                return list;
            }
        } finally {
            conn.disconnect();
        }
        return list;

    }


    public static TrailerData getTrailerResponseFromHttpUrl(URL url) throws IOException, JSONException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        TrailerData trailerData = null;
        try {
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.d("NetworkUtils", "The response code is: " + responseCode + " " + conn.getResponseMessage());
            InputStream in = conn.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                JSONObject movieJSON = new JSONObject(scanner.next());
                trailerData = parseTrailerResponse(movieJSON);
            }
        } finally {
            conn.disconnect();
        }
        return trailerData;

    }

    public static ArrayList<ReviewData> getReviewResponseFromHttpUrl(URL url) throws IOException, JSONException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        ArrayList<ReviewData> list = new ArrayList<ReviewData>();
        ReviewData reviewData = null;
        try {
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.addRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.connect();
            int responseCode = conn.getResponseCode();
            Log.d("NetworkUtils", "The response code is: " + responseCode + " " + conn.getResponseMessage());
            InputStream in = conn.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                JSONObject movieJSON = new JSONObject(scanner.next());
                parseReviewDataResponse(movieJSON, list);
            }
        } finally {
            conn.disconnect();
        }
        return list;

    }

    /**
     * This method returns the parsed result.
     *
     * @param list    empty list to store the parsed letsdecode.com.popularmovies.data.
     * @param scanner object with network reponse to parse..
     * @return list with parsed.
     * @throws JSONException Related to json parsing
     */

    public static ArrayList<MovieData> parseNetworkResponse(ArrayList<MovieData> list, Scanner scanner) throws JSONException {

        JSONObject movieJSON = new JSONObject(scanner.next());
        JSONArray resultsArray = movieJSON.getJSONArray("results");
        Log.d("RESULT", resultsArray.toString());
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject arrayObject = resultsArray.getJSONObject(i);
            String posterPath = arrayObject.getString("poster_path");
            String release_date = arrayObject.getString("release_date");
            String title = arrayObject.getString("title");
            double vote_average = arrayObject.getDouble("vote_average");
            float vote = (float)vote_average;
            String overview = arrayObject.getString("overview");
            int id = arrayObject.getInt("id");
            list.add(new MovieData(posterPath, release_date, title, vote, overview, id));
        }
        return list;
    }

    /**
     * Creates trailer letsdecode.com.popularmovies.data from Movie Json
     *
     * @param movieJSON
     * @return
     * @throws JSONException
     */
    public static TrailerData parseTrailerResponse(final JSONObject movieJSON) throws JSONException {
        TrailerData trailerData = null;
        if (movieJSON == null) {
            throw new NullPointerException("MovieJson should not be null");
        }
        JSONArray trailerArray = movieJSON.getJSONArray("youtube");
        Log.d("RESULT", trailerArray.toString());
        for (int i = 0; i < trailerArray.length(); i++) {
            JSONObject arrayObject = trailerArray.getJSONObject(i);
            String type = arrayObject.getString("type");
            if (type.equals("Trailer")) {
                String source = arrayObject.getString("source");
                trailerData = new TrailerData(source);
                break;
            }
        }
        return trailerData;
    }

    /**
     * Creates list with reviews in it.
     *
     * @param movieJSON, list
     * @return ArrayList
     * @throws JSONException
     */
    public static ArrayList<ReviewData> parseReviewDataResponse(final JSONObject movieJSON, ArrayList<ReviewData> list) throws JSONException {
        if (movieJSON == null) {
            throw new NullPointerException("MovieJson should not be null");
        }
        JSONArray resultsArray = movieJSON.getJSONArray("results");
        Log.d("RESULT", resultsArray.toString());
        for (int i = 0; i < resultsArray.length(); i++) {
            JSONObject arrayObject = resultsArray.getJSONObject(i);
            String authorName = arrayObject.getString("author");
            String content = arrayObject.getString("content");
            list.add(new ReviewData(authorName, content));
        }
        return list;
    }
}