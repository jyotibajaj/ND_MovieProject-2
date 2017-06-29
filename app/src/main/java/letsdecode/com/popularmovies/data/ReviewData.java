package letsdecode.com.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aashi on 15/06/17.
 */

public class ReviewData implements Parcelable {

    String authorName;
    String content;

    public ReviewData(String authorName, String content){
        this.authorName = authorName;
        this.content = content;

    }

    protected ReviewData(Parcel in) {
        authorName = in.readString();
        content = in.readString();
    }

    public static final Creator<ReviewData> CREATOR = new Creator<ReviewData>() {
        @Override
        public ReviewData createFromParcel(Parcel in) {
            return new ReviewData(in);
        }

        @Override
        public ReviewData[] newArray(int size) {
            return new ReviewData[size];
        }
    };

    public String getAuthorName() {
        return authorName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authorName);
        dest.writeString(content);
    }
}
