package letsdecode.com.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import letsdecode.com.popularmovies.R;
import letsdecode.com.popularmovies.data.ReviewData;

/**
 * Created by aashi on 16/06/17.
 */

public class ReviewsAdapter extends
        RecyclerView.Adapter<ReviewsAdapter.ReviewDataViewHolder> {


    private List<ReviewData> reviewDataList;
    // Store the context for easy access


    /**
     * Constructor for MovieAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     *
     * @param list    Number of items to display in list
     * @param context
     */

    public ReviewsAdapter(Context context, List<ReviewData> list) {
        this.reviewDataList = list;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The ViewGroup that these ViewHolders are contained within.
     * @param viewType If your RecyclerView has more than one type of item, viewType integer to provide a different layout.
     */
    @Override
    public ReviewsAdapter.ReviewDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a layout from XML and returning the holder
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View movieView = inflater.inflate(R.layout.custom_reviews_list, parent, false);

        // Return a new holder instance
        ReviewDataViewHolder viewHolder = new ReviewDataViewHolder(movieView);
        return viewHolder;
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the letsdecode.com.popularmovies.data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the letsdecode.com.popularmovies.data set.
     * @param position The position of the item within the letsdecode.com.popularmovies.adapter's letsdecode.com.popularmovies.data set.
     */
    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewDataViewHolder holder, int position) {
        // Involves populating letsdecode.com.popularmovies.data into the item through holder
        // Get the letsdecode.com.popularmovies.data model based on position
        ReviewData reviewData = reviewDataList.get(position);
        int sequence = position + 1;
        holder.numberTextView.setText(sequence + ".");
        holder.authorNameTextView.setText("Author: " + reviewData.getAuthorName());
        holder.contentTextView.setText(reviewData.getContent());

    }


    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available
     */

    @Override
    public int getItemCount() {
        if (null == reviewDataList) return 0;
        return reviewDataList.size();
    }


    // Provide a direct reference to each of the views within a letsdecode.com.popularmovies.data item
    // Used to cache the views within the item layout for fast access
    public class ReviewDataViewHolder
            extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_authorName)
        public TextView authorNameTextView;

        @BindView(R.id.tv_content)
        public TextView contentTextView;

        @BindView(R.id.tv_sequence)
        public TextView numberTextView;




        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         *
         * @param itemView The View has been inflated in
         *                 {@link MovieAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public ReviewDataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}


