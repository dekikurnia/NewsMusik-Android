package co.newsmusik.newsmusik;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by deki kurnia on 23/03/16.
 */
public class FeedListRowHolder extends RecyclerView.ViewHolder {
    protected ImageView thumbnail;
    protected TextView title;
    protected TextView category;
    protected TextView datetime;
    protected CardView cardview;

    public FeedListRowHolder(View view) {
        super(view);
        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.title = (TextView) view.findViewById(R.id.title);
        this.category = (TextView) view.findViewById(R.id.category);
        this.datetime = (TextView) view.findViewById(R.id.datetime);
        this.cardview = (CardView) view.findViewById(R.id.cardlist_item);
    }

}
