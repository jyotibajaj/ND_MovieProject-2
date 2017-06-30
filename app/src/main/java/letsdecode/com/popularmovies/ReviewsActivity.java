package letsdecode.com.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

import letsdecode.com.popularmovies.adapter.ReviewsAdapter;
import letsdecode.com.popularmovies.data.ReviewData;
import utilities.DividerItemDecoration;

public class ReviewsActivity extends AppCompatActivity {
    public static final String REVIEW_DATA_KEY = "reviewDataKey";

    // Reference to Adapter
    public static ReviewsAdapter mReviewsAdapter;



    /**
     * This method creates the intent to start the Detail activity.
     *
     * @param activity            the caller activity.
     * @param reviewDataArrayList list of review letsdecode.com.popularmovies.data object type.
     * @return intent.
     */
    public static Intent createIntentFromDetailActivity(Activity activity, final ArrayList<ReviewData> reviewDataArrayList) {
        Intent intent = new Intent(activity, ReviewsActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(ReviewsActivity.REVIEW_DATA_KEY,(Serializable)reviewDataArrayList);
        intent.putExtra("BUNDLE",args);
        if (reviewDataArrayList == null) {
            throw new IllegalStateException("illegal movie letsdecode.com.popularmovies.data");
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        //receive letsdecode.com.popularmovies.data from intent
        ArrayList<ReviewData> reviewDataArrayList = getReviewsDataArrayList();
        if (reviewDataArrayList == null) {
            throw new IllegalStateException("illegal review letsdecode.com.popularmovies.data");
        }
        // Lookup the recyclerview in activity layout
        RecyclerView rvReviews = (RecyclerView) findViewById(R.id.rv_reviews_list);
        // Set layout manager to position the items
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        //instantiating the letsdecode.com.popularmovies.adapter
        mReviewsAdapter = new ReviewsAdapter(this, reviewDataArrayList);
        //or
        rvReviews.addItemDecoration(new DividerItemDecoration(this));

        // Attach the letsdecode.com.popularmovies.adapter to the recyclerview to populate items
        rvReviews.setAdapter(mReviewsAdapter);
    }

    private ArrayList<ReviewData> getReviewsDataArrayList() {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<ReviewData> reviewDataArrayList = (ArrayList<ReviewData>) args.getSerializable(ReviewsActivity.REVIEW_DATA_KEY);
        return reviewDataArrayList;
    }
}

