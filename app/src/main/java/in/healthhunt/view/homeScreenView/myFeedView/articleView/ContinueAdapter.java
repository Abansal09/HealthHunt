package in.healthhunt.view.homeScreenView.myFeedView.articleView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.MediaItem;
import in.healthhunt.model.articles.commonResponse.Title;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.articlePresenter.IArticlePresenter;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public class ContinueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private IArticlePresenter IArticlePresenter;
    private Context mContext;
    private ClickListener mClickListener;
    private int mItemWidth = 0;

    public ContinueAdapter(Context context, IArticlePresenter presenter) {
        IArticlePresenter = presenter;
        mContext = context;
        createItemWidth();
    }

    private void createItemWidth() {
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        width = width - HealthHuntUtility.dpToPx(104, mContext);
        mItemWidth = width;
        Log.i("TAGWIDTH", " mWidth " + mItemWidth);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("TAg@@","viewType");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.continue_article_item_view, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = mItemWidth;
        view.setLayoutParams(layoutParams);
        return new ContinueItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setContent(((ContinueItemViewHolder)holder), position);
    }

    private void setContent(ContinueItemViewHolder holder, int pos) {

        ArticlePostItem postsItem = IArticlePresenter.getArticle(pos);
        if(postsItem != null) {

            int width = holder.getViewWidth();
            if(width > 0) {
                ViewGroup.LayoutParams layoutParams = holder.mReadProgressGreenView.getLayoutParams();
                layoutParams.width = (width/2);
                holder.mReadProgressGreenView.setLayoutParams(layoutParams);
            }

            String url = postsItem.getVideo_thumbnail();

            if(url == null || url.isEmpty()) {
                List<MediaItem> mediaItems = postsItem.getMedia();
                if (mediaItems != null && !mediaItems.isEmpty()) {
                    MediaItem media = mediaItems.get(0);
                    if ("image".equals(media.getMedia_type())) {
                        url = media.getUrl();

                    }
                }
                holder.mPlayImage.setVisibility(View.GONE);
            }
            else {
                holder.mPlayImage.setVisibility(View.VISIBLE);
            }


            if (url != null) {
                Log.i("TAG11", "url " + url);
                Glide.with(mContext).load(url).//dontAnimate().
                        // bitmapTransform(new RoundedCornersTransformation( mContext,HealthHuntUtility.dpToPx(2, mContext), 2)).
                                placeholder(R.mipmap.ic_no_my_feed_article_image).into(holder.mArticleImage);
            } else {
                holder.mArticleImage.setImageResource(R.mipmap.ic_no_my_feed_article_image);
            }

            Title title = postsItem.getTitle();
            String articleTitle = null;
            if(title != null) {
                articleTitle = title.getRendered();
            }
            holder.mArticleTitle.setText(articleTitle);


            String readingTime = postsItem.getRead_time();
            readingTime = readingTime + " min read";
            holder.mReadingTime.setText(readingTime);

            String date = postsItem.getDate();
            if(date != null) {
                date = HealthHuntUtility.getDateWithFormat(date);
            }
            holder.mArticleDate.setText(date);
        }
    }


    @Override
    public int getItemCount() {
        return IArticlePresenter.getCount();
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View v, int position);
    }

    public class ContinueItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.article_image)
        ImageView mArticleImage;

        @BindView(R.id.play_icon)
        ImageView mPlayImage;

        @BindView(R.id.article_content)
        TextView mArticleTitle;

        @BindView(R.id.reading_time)
        TextView mReadingTime;

        @BindView(R.id.article_date)
        TextView mArticleDate;

        @BindView(R.id.read_progress_green_view)
        View mReadProgressGreenView;

        private int mViewWidth;

        public ContinueItemViewHolder(View itemView) {
            super(itemView);
            mViewWidth = itemView.getLayoutParams().width;
            ButterKnife.bind(this, itemView);
        }

        public void notifyDataChanged() {
            notifyDataSetChanged();
        }

        @OnClick(R.id.article_item_view)
        void onClick(View view) {
            if(mClickListener != null) {

                if(HealthHuntUtility.checkInternetConnection(mContext)) {
                    mClickListener.ItemClicked(view, getAdapterPosition());
                }
                else {
                    IArticlePresenter.showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
                }
            }
        }

        public int getViewWidth(){
            return mViewWidth;
        }
    }
}
