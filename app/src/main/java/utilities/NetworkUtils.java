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

import data.MovieData;


public class NetworkUtils {
    final static String BASE_URL = "https://api.themoviedb.org/";
    final static String API_VERSION = "3/";
    final static String END_POINT_POPULAR = "movie/popular";
    final static String END_POINT_TOP_RATED = "movie/top_rated";
    final static String API_KEY_VALUE = Keys.API_KEY_VALUE;
    final static String API_KEY = "api_key";
    final static String MOVIE_BASE_URL = BASE_URL + API_VERSION;


    private static Uri.Builder createUrlBuilder(String suffix) {
        return Uri.parse(MOVIE_BASE_URL + suffix).buildUpon()
                .appendQueryParameter(API_KEY, API_KEY_VALUE);
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
     * This method build the url for fetching movie data sorted for popularity
     */
    public static URL buildPopularMovies() {
        Uri.Builder builder = createUrlBuilder(END_POINT_POPULAR);
        return buildUrl(builder);

    }

    /**
     * This method build the url for fetching movie data as per rating
     */
    public static URL buildTopRated() {
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
                parseNetworkResponse(list, scanner);

            } else {
                return list;
            }
        } finally {
            conn.disconnect();
        }
        return list;

    }


    /**
     * This method returns the parsed result.
     *
     * @param list    empty list to store the parsed data.
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
                String overview = arrayObject.getString("overview");
                int id = arrayObject.getInt("id");
                list.add(new MovieData(posterPath, release_date, title, vote_average, overview, id));
            }
        return list;
    }


}