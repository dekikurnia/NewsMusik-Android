package co.newsmusik.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
public class EventOrganizerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FeedItem> feedItemList;
    private Context mContext;
    private static final String TAG_PICTURE = "extra_fields_search";
    private static final String TAG_INTROTEXT = "introtext";
    private static final String TAG_IMAGECREDITS = "image_credits";
    private static final String TAG_SHARELINK = "image_caption";
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private boolean loading = true;

    public EventOrganizerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    public EventOrganizerAdapter() {
        feedItemList = new ArrayList<>();
    }


    private void add(FeedItem item) {
        feedItemList.add(item);
        notifyItemInserted(feedItemList.size() - 1);
    }

    public void addAll(List<FeedItem> feedItemList) {
        for (FeedItem feedItem : feedItemList) {
            add(feedItem);
        }
    }

    public void remove(FeedItem item) {
        int position = feedItemList.indexOf(item);
        if (position > -1) {
            feedItemList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public FeedItem getItem(int position){
        return feedItemList.get(position);
    }

    @Override
    public int getItemViewType (int position) {
        if(isPositionFooter (position)) {
            return VIEW_TYPE_LOADING;
        }
        return VIEW_TYPE_ITEM;
    }

    private boolean isPositionFooter (int position) {
        return position == getItemCount() - 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);
            return new ArticleViewHolder(view);
        } else if (i == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.footer_loading, viewGroup, false);
            return new LoadingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof ArticleViewHolder) {
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            FeedItem feedItem = feedItemList.get(i);

            Glide.with(mContext).load(feedItem.getThumbnail())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(articleViewHolder.thumbnail);

            articleViewHolder.title.setText(Html.fromHtml(feedItem.getTitle()));
            articleViewHolder.category.setText(Html.fromHtml(feedItem.getCategory()));
            articleViewHolder.datetime.setText(Html.fromHtml(feedItem.getDate()));

            articleViewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ContentDetail.class);
                    intent.putExtra(TAG_PICTURE, feedItemList.get(i).getThumbnail());
                    intent.putExtra(TAG_INTROTEXT, feedItemList.get(i).getContentDetail());
                    intent.putExtra(TAG_IMAGECREDITS, feedItemList.get(i).getImageCredit());
                    view.getContext().startActivity(intent);
                }
            });
            articleViewHolder.cardview.setTag(holder);

            articleViewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
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
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
            loadingViewHolder.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }


    }

    @Override
    public int getItemCount() {

        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public void setLoading(boolean loading){
        this.loading = loading;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void setFilter(List<FeedItem> itemModels) {
        feedItemList = new ArrayList<>();
        feedItemList.addAll(itemModels);
        notifyDataSetChanged();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView title;
        TextView category;
        TextView datetime;
        CardView cardview;
        ImageButton imgShare;

        public ArticleViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            title = (TextView) view.findViewById(R.id.title);
            category = (TextView) view.findViewById(R.id.category);
            datetime = (TextView) view.findViewById(R.id.datetime);
            cardview = (CardView) view.findViewById(R.id.cardlist_item);
            imgShare = (ImageButton) view.findViewById(R.id.imageShare);
        }

    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loading);
        }
    }
}
