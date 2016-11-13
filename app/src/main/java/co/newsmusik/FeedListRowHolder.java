package co.newsmusik;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by deki kurnia on 23/03/16.
 */
public class FeedListRowHolder extends RecyclerView.ViewHolder {
    public ImageView thumbnail;
    public TextView title;
    public TextView category;
    public TextView datetime;
    public CardView cardview;
    public ImageButton imgShare;

    public FeedListRowHolder(View view) {
        super(view);
        this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        this.title = (TextView) view.findViewById(R.id.title);
        this.category = (TextView) view.findViewById(R.id.category);
        this.datetime = (TextView) view.findViewById(R.id.datetime);
        this.cardview = (CardView) view.findViewById(R.id.cardlist_item);
        this.imgShare = (ImageButton) view.findViewById(R.id.imageShare);
    }


}
