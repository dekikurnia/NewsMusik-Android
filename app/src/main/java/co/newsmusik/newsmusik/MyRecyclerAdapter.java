package co.newsmusik.newsmusik;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by deki kurnia on 23/03/16.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {



    private List<FeedItem> feedItemList;

    private Context mContext;
    private static final String TAG_PICTURE = "extra_fields_search";
    private static final String TAG_INTROTEXT = "introtext";

    public MyRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        FeedListRowHolder mh = new FeedListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, final int i) {
        FeedItem feedItem = feedItemList.get(i);

        Glide.with(mContext).load(feedItem.getThumbnail())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(feedListRowHolder.thumbnail);

        feedListRowHolder.title.setText(Html.fromHtml(feedItem.getTitle()));
        feedListRowHolder.category.setText(Html.fromHtml(feedItem.getCategory()));
        feedListRowHolder.datetime.setText(Html.fromHtml(feedItem.getDate()));

        feedListRowHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ContentDetail.class);
                intent.putExtra(TAG_PICTURE,feedItemList.get(i).getThumbnail());
                intent.putExtra(TAG_INTROTEXT,feedItemList.get(i).getContentDetail());
                view.getContext().startActivity(intent);
            }
        });
        feedListRowHolder.cardview.setTag(feedListRowHolder);
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


}