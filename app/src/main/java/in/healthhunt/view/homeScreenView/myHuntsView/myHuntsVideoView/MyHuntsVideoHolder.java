package in.healthhunt.view.homeScreenView.myHuntsView.myHuntsVideoView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.myHuntPresenter.myHuntsVideoPresenter.IMyHuntsVideoPresenter;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class MyHuntsVideoHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.my_hunts_article_image)
    public ImageView mArticleImage;

    @BindView(R.id.my_hunts_play_icon)
    public ImageView mPlayIcon;

    @BindView(R.id.my_hunts_article_bookmark)
    public ImageView mBookMark;

    @BindView(R.id.my_hunts_category_image)
    public ImageView mCategoryImage;

    @BindView(R.id.my_hunts_category_name)
    TextView mCategoryView;

    @BindView(R.id.my_hunts_article_content)
    public TextView mArticleTitle;

    private Context mContext;
    private IMyHuntsVideoPresenter IMyHuntsVideoPresenter;
    private MyHuntsVideoAdapter.ClickListener mClickListener;
    public MyHuntsVideoHolder(View itemView, IMyHuntsVideoPresenter myHuntsVideoPresenter, MyHuntsVideoAdapter.ClickListener clickListener) {
        super(itemView);
        IMyHuntsVideoPresenter = myHuntsVideoPresenter;
        mClickListener = clickListener;
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
        mPlayIcon.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.my_hunts_article_item_view)
    void onItemClick(View view) {
        if(HealthHuntUtility.checkInternetConnection(mContext)) {
            if(mClickListener != null) {
                mClickListener.ItemClicked(view, getAdapterPosition());
            }
        }
        else {
            IMyHuntsVideoPresenter.showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
        }
    }

    @OnLongClick(R.id.my_hunts_article_item_view)
    boolean onLongClick(){
        if(mClickListener != null) {
            mClickListener.onLongClicked(getAdapterPosition());
        }
        return true;
    }

    @OnClick(R.id.my_hunts_article_bookmark)
    void onBookMark(){
        if(HealthHuntUtility.checkInternetConnection(mContext)) {
            ArticlePostItem postsItem = IMyHuntsVideoPresenter.getVideo(getAdapterPosition());
            String id = String.valueOf(postsItem.getArticle_Id());
            CurrentUser currentUser = postsItem.getCurrent_user();
            if(currentUser != null) {
                if(!currentUser.isBookmarked()){
                    IMyHuntsVideoPresenter.bookmark(id, ArticleParams.VIDEO);
                }
                else {
                    IMyHuntsVideoPresenter.unBookmark(id, ArticleParams.VIDEO);
                }
            }
        }
        else {
            IMyHuntsVideoPresenter.showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
        }
    }

}