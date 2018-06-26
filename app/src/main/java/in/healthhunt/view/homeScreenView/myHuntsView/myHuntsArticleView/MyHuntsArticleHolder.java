package in.healthhunt.view.homeScreenView.myHuntsView.myHuntsArticleView;

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
import in.healthhunt.presenter.homeScreenPresenter.myHuntPresenter.myHuntArticlesPresenter.IMyHuntsArticlesPresenter;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class MyHuntsArticleHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.my_hunts_article_image)
    public ImageView mArticleImage;

    @BindView(R.id.my_hunts_play_icon)
    public ImageView mPlayIcon;

    @BindView(R.id.my_hunts_article_bookmark)
    public ImageView mArticleBookMark;

    @BindView(R.id.my_hunts_article_content)
    public TextView mArticleTitle;

    @BindView(R.id.my_hunts_category_image)
    public ImageView mCategoryImage;

    @BindView(R.id.my_hunts_category_name)
    public TextView mCategoryName;



    private Context mContext;
    private IMyHuntsArticlesPresenter IMyHuntsArticlePresenter;
    private MyHuntsArticleAdapter.ClickListener mClickListener;
    public MyHuntsArticleHolder(View itemView, IMyHuntsArticlesPresenter myHuntsArticlePresenter, MyHuntsArticleAdapter.ClickListener clickListener) {
        super(itemView);
        IMyHuntsArticlePresenter = myHuntsArticlePresenter;
        mClickListener = clickListener;
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
        mPlayIcon.setVisibility(View.GONE);
    }

    @OnClick(R.id.my_hunts_article_item_view)
    void onItemClick(View view) {
        if(HealthHuntUtility.checkInternetConnection(mContext)) {
            if(mClickListener != null) {
                mClickListener.ItemClicked(view, getAdapterPosition());
            }
        }
        else {
            IMyHuntsArticlePresenter.showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
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
            ArticlePostItem postsItem = IMyHuntsArticlePresenter.getArticle(getAdapterPosition());
            String id = String.valueOf(postsItem.getArticle_Id());
            CurrentUser currentUser = postsItem.getCurrent_user();
            if (currentUser != null) {
                if (!currentUser.isBookmarked()) {
                    IMyHuntsArticlePresenter.bookmark(id, ArticleParams.ARTICLE);
                } else {
                    IMyHuntsArticlePresenter.unBookmark(id, ArticleParams.ARTICLE);
                }
            }
        }
        else {
            IMyHuntsArticlePresenter.showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
        }
    }
}