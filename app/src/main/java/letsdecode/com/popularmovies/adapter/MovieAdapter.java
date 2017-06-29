package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import data.MovieData;
import letsdecode.com.popularmovies.R;

/**
 * Created by jyoti on 13/05/17.
 * adapter
 */

public class MovieAdapter extends
        RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    /*on-click handler to make it easy for an Activity to interface with
       RecyclerView
    */
    private CustomItemClickInterface mOnClickListener;
    private List<MovieData> mMovieData;
    // Store the context for easy access
    private Context mContext;


    /**
     * Constructor for MovieAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     *
     * @param list  Number of items to display in list
     * @param context
     */

    public MovieAdapter(Context context, List<MovieData> list) {
        mContext = context;
        this.mMovieData = list;
    }

    //interface for item click on recycler view
    public interface CustomItemClickInterface {
        void onListItemClick(View view, int itemClicked);
    }


    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The ViewGroup that these ViewHolders are contained within.
     * @param viewType If your RecyclerView has more than one type of item, viewType integer to provide a different layout.
     */
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a layout from XML and returning the holder
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View movieView = inflater.inflate(R.layout.custom_movie_layout, parent, false);

        // Return a new holder instance
        MovieViewHolder viewHolder = new MovieViewHolder(movieView);
        return viewHolder;
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
        // Involves populating data into the item through holder
        // Get the data model based on position
        MovieData movieData = mMovieData.get(position);
        Picasso.with(mContext).load(movieData.getMoviePosterUrl()).into(holder.posterImageView);

    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    /**
     * This method sets the CustomItemClickInterface to the current object.
     *
     * @param itemClickListener
     */
    public void setClickListener(CustomItemClickInterface itemClickListener) {
        this.mOnClickListener = itemClickListener;
    }


    /**
     * This method sets movie data into arraylist.
     *
     * @param movieData
     */
    public void setMovieData(ArrayList<MovieData> movieData) {
        mMovieData.clear();
        mMovieData.addAll(movieData);
        notifyDataSetChanged();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class MovieViewHolder
            extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView posterImageView;


        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View has been inflated in
         *                 {@link MovieAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public MovieViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.poster_image);
            itemView.setOnClickListener(this);
        }


        /**
         * Override onClick, passing the clicked item's position (getAdapterPosition()) to mOnClickListener via its onListItemClick method
         * Called whenever a user clicks on an item in the list.
         *
         * @param v The View that was clicked
         */

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onListItemClick(v, getAdapterPosition());
            }

        }


    }

}




