package in.healthhunt.view.homeScreenView.watchView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.watchPresenter.IWatchPresenter;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class WatchViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.play_icon)
    public ImageView mPlayIcon;

    @BindView(R.id.article_image)
    public ImageView mArticleImage;

    @BindView(R.id.article_bookmark)
    public ImageView mBookMark;

    @BindView(R.id.article_content)
    public TextView mArticleTitle;

    @BindView(R.id.category_image)
    public ImageView mCategoryImage;

    @BindView(R.id.category_name)
    TextView mCategoryName;

    @BindView(R.id.author_pic)
    ImageView mAuthorPic;

    @BindView(R.id.author_name)
    TextView mAuthorName;

    @BindView(R.id.hash_tags)
    TextView mHashTags;

    @BindView(R.id.reading_time)
    TextView mReadingTime;

    @BindView(R.id.article_date)
    TextView mArticleDate;

    @BindView(R.id.article_item_view)
    RelativeLayout mShopItemView;

    private Context mContext;
    private IWatchPresenter IWatchPresenter;
    private WatchAdapter.ClickListener mClickListener;

    public WatchViewHolder(View itemView, IWatchPresenter watchPresenter, WatchAdapter.ClickListener clickListener) {
        super(itemView);
        IWatchPresenter = watchPresenter;
        mClickListener = clickListener;
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.article_item_view)
    void onItemClick(View view) {
        if (HealthHuntUtility.checkInternetConnection(mContext)) {
            if (mClickListener != null) {
                mClickListener.ItemClicked(view, getAdapterPosition());
            }
        } else {
            IWatchPresenter.showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
        }
    }

    @OnClick(R.id.article_bookmark)
    void onBookMark() {
        if (HealthHuntUtility.checkInternetConnection(mContext)) {
            ArticlePostItem postsItem = IWatchPresenter.getArticle(getAdapterPosition());
            String id = String.valueOf(postsItem.getArticle_Id());
            CurrentUser currentUser = postsItem.getCurrent_user();
            if (currentUser != null) {
                if (!currentUser.isBookmarked()) {
                    IWatchPresenter.bookmark(id, ArticleParams.VIDEO);
                } else {
                    IWatchPresenter.unBookmark(id, ArticleParams.VIDEO);
                }
            } else {
                IWatchPresenter.showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
            }
        }
    }
}