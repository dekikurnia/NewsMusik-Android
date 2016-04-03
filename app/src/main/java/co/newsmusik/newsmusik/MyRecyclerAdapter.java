package co.newsmusik.newsmusik;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import java.util.List;

/**
 * Created by deki kurnia on 23/03/16.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<FeedListRowHolder> {
    ImageButton imgShare;
    private List<FeedItem> feedItemList;
    private Context mContext;
    private static final String TAG_PICTURE = "extra_fields_search";
    private static final String TAG_INTROTEXT = "introtext";
    private static final String TAG_IMAGECREDITS = "image_credits";


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

        Glide.with(mContext).load(feedItem.getThumbnail()).asBitmap()
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
                intent.putExtra(TAG_PICTURE, feedItemList.get(i).getThumbnail());
                intent.putExtra(TAG_INTROTEXT, feedItemList.get(i).getContentDetail());
                intent.putExtra(TAG_IMAGECREDITS, feedItemList.get(i).getImageCredit());
                view.getContext().startActivity(intent);
            }
        });
        feedListRowHolder.cardview.setTag(feedListRowHolder);

        feedListRowHolder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageShare:
                        shareTextUrl();
                        break;
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }


    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_TEXT, "http://www.newsmusik.co");

        mContext.startActivity(Intent.createChooser(share, "Share link!"));
    }
}