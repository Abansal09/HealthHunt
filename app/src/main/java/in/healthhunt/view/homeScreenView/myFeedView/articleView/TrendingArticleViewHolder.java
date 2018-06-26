package in.healthhunt.view.homeScreenView.myFeedView.articleView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.beans.SpaceDecoration;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.articlePresenter.ArticlePresenterImp;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.articlePresenter.IArticlePresenter;
import in.healthhunt.view.fullView.fullViewFragments.FullArticleFragment;
import in.healthhunt.view.fullView.fullViewFragments.YoutubeFragment;
import in.healthhunt.view.homeScreenView.myFeedView.IMyFeedView;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class TrendingArticleViewHolder extends RecyclerView.ViewHolder implements IArticleView, TrendingAdapter.ClickListener {


    @BindView(R.id.trending_article_name)
    TextView mArticleName;

    @BindView(R.id.trending_recycler_list)
    public RecyclerView mTrendingViewer;

    @BindView(R.id.trending_view)
    LinearLayout mView;


    private IArticlePresenter IArticlePresenter;
    private in.healthhunt.view.homeScreenView.myFeedView.IMyFeedView IMyFeedView;
    private Context mContext;

    public TrendingArticleViewHolder(View articleView, IMyFeedView feedView) {
        super(articleView);
        mContext = articleView.getContext();
        IMyFeedView = feedView;
        ButterKnife.bind(this, articleView);
        IArticlePresenter = new ArticlePresenterImp(mContext, this);
        setAdapter();

    }

    private void setAdapter() {
        TrendingAdapter trendingAdapter = new TrendingAdapter(mContext, IArticlePresenter);
        trendingAdapter.setClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mTrendingViewer.setLayoutManager(layoutManager);
        mTrendingViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(8, mContext), SpaceDecoration.VERTICAL));
        mTrendingViewer.setAdapter(trendingAdapter);
        mTrendingViewer.setFocusableInTouchMode(false);
    }

    @Override
    public Fragment getFragmentArticleItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        List<ArticlePostItem> list = IMyFeedView.getTrendingArticles();
        int count = 0;
        if(list != null && !list.isEmpty()) {
            count = list.size();
        }
        return count;
    }

    @Override
    public ArticlePostItem getArticle(int pos) {
        List<ArticlePostItem> list = IMyFeedView.getTrendingArticles();
        ArticlePostItem postsItem = null;
        if(list != null && !list.isEmpty()){
            postsItem = list.get(pos);
        }
        return postsItem;
    }

    @Override
    public void showProgress() {
        IMyFeedView.showProgress();
    }

    @Override
    public void hideProgress() {
        IMyFeedView.hideProgress();
    }

    @Override
    public void showAlert(String msg) {
        IMyFeedView.showAlert(msg);
    }

    @Override
    public void updateBottomNavigation() {
        IMyFeedView.updateBottomNavigation();
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IMyFeedView.loadNonFooterFragment(fragmentName, bundle);
    }

    @Override
    public void updateAdapter() {
        mTrendingViewer.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void updateSavedData(ArticlePostItem articlePostItem) {
        IMyFeedView.updateArticleSaved(articlePostItem);
    }

    @Override
    public void ItemClicked(View v, int position) {
        ArticlePostItem postsItem = IArticlePresenter.getArticle(position);
        if(postsItem != null) {
            /*Intent intent = new Intent(mContext, FullViewActivity.class);
            intent.putExtra(ArticleParams.ID, String.valueOf(postsItem.getMedia_id()));
            intent.putExtra(ArticleParams.POST_TYPE, ArticleParams.ARTICLE);
            mContext.startActivity(intent);*/

            Bundle bundle = new Bundle();
            bundle.putString(ArticleParams.ID, String.valueOf(postsItem.getArticle_Id()));
            String url = postsItem.getVideo_thumbnail();
            if(url == null || url.isEmpty()) {
                bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.ARTICLE);
                IArticlePresenter.loadFragment(FullArticleFragment.class.getSimpleName(), bundle);
            }
            else {
                bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.VIDEO);
                IArticlePresenter.loadFragment(YoutubeFragment.class.getSimpleName(), bundle);
            }
        }
    }

    public void notifyDataChanged(){
        if(mTrendingViewer != null && mTrendingViewer.getAdapter() != null){
            mTrendingViewer.getAdapter().notifyDataSetChanged();
        }
    }

    public void hideView(){
        mView.setVisibility(View.GONE);
    }

    public void showView(){
        mView.setVisibility(View.VISIBLE);
    }

}