package co.newsmusik.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import co.newsmusik.FeedItem;
import co.newsmusik.FeedListRowHolder;
import co.newsmusik.R;
import co.newsmusik.activity.ContentDetail;

/**
 * Created by deki kurnia on 19/04/16.
 */
public class MaleAdapter extends RecyclerView.Adapter<FeedListRowHolder>  {
    private List<FeedItem> feedItemList;
    private Context mContext;
    private static final String TAG_PICTURE = "extra_fields_search";
    private static final String TAG_INTROTEXT = "introtext";
    private static final String TAG_IMAGECREDITS = "image_credits";
    private static final String TAG_SHARELINK = "image_caption";

    public MaleAdapter(Context context, List<FeedItem> feedItemList) {
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
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        share.putExtra(Intent.EXTRA_TEXT, feedItemList.get(i).getShareLink());
                        mContext.startActivity(Intent.createChooser(share, "Share link "));
                        break;
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public void setFilter(List<FeedItem> itemModels) {
        feedItemList = new ArrayList<>();
        feedItemList.addAll(itemModels);
        notifyDataSetChanged();
    }
}
