package in.healthhunt.view.homeScreenView.myFeedView.articleView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.articlePresenter.ArticlePresenterImp;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.articlePresenter.IArticlePresenter;
import in.healthhunt.view.homeScreenView.myFeedView.IMyFeedView;
import in.healthhunt.view.viewAll.ViewAllFragment;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class LatestArticleViewHolder extends RecyclerView.ViewHolder implements IArticleView {

    @BindView(R.id.latest_view_all)
    LinearLayout mViewAll;

    @BindView(R.id.latest_article_name)
    TextView mArticleName;

    @BindView(R.id.latest_article_pager)
    ViewPager mArticlePager;

    @BindView(R.id.latest_view)
    LinearLayout mView;


    private IArticlePresenter IArticlePresenter;
    private in.healthhunt.view.homeScreenView.myFeedView.IMyFeedView IMyFeedView;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public LatestArticleViewHolder(View articleView, FragmentManager fragmentManager, IMyFeedView feedView) {
        super(articleView);
        mContext = articleView.getContext();
        ButterKnife.bind(this, articleView);
        mFragmentManager = fragmentManager;
        IMyFeedView = feedView;
        IArticlePresenter = new ArticlePresenterImp(mContext, this);
        setAdapter();
    }

    private void setAdapter() {
        ArticleAdapter articleAdapter = new ArticleAdapter(mFragmentManager,  IArticlePresenter, ArticleParams.READ_FRESH_ARTICLES);
        mArticlePager.setAdapter(articleAdapter);
        mArticlePager.setClipToPadding(false);
        mArticlePager.setPadding(0, 0, HealthHuntUtility.dpToPx(150, mContext),0);
        mArticlePager.setPageMargin(HealthHuntUtility.dpToPx(6, mContext));
    }

    @Override
    public Fragment getFragmentArticleItem(int position) {
        return new ArticleFragment(IArticlePresenter);
    }

    @Override
    public int getCount() {
        List<ArticlePostItem> list = IMyFeedView.getLatestArticles();
        int count = 0;
        if(list != null) {
            count = list.size();
        }
        return count;
    }

    @Override
    public ArticlePostItem getArticle(int pos) {
        List<ArticlePostItem> list = IMyFeedView.getLatestArticles();
        ArticlePostItem postItem = null;
        if(list != null) {
            postItem = list.get(pos);
        }
        return postItem;
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
        mArticlePager.getAdapter().notifyDataSetChanged();
        updateViewAllVisibility();
    }

    private void updateViewAllVisibility(){
        int count = IArticlePresenter.getCount();
        if(count < 5){
            mViewAll.setVisibility(View.GONE);
        }
        else {
            mViewAll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateSavedData(ArticlePostItem articlePostItem) {
        IMyFeedView.updateArticleSaved(articlePostItem);
    }

    @OnClick(R.id.latest_view_all)
    void onViewAll(){
        if (HealthHuntUtility.checkInternetConnection(mContext)) {
            openViewAllFragment();
        } else {
            showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
        }
    }

    private void openViewAllFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt(ArticleParams.ARTICLE_TYPE, ArticleParams.READ_FRESH_ARTICLES);
        IArticlePresenter.updateBottomNavigation();
        IArticlePresenter.loadFragment(ViewAllFragment.class.getSimpleName(), bundle);
    }

    public void notifyDataChanged() {
        mArticlePager.getAdapter().notifyDataSetChanged();
    }

    public void hideView(){
        mView.setVisibility(View.GONE);
    }

    public void showView(){
        mView.setVisibility(View.VISIBLE);
    }

}