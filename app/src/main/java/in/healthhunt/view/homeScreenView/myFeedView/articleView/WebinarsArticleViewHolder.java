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

import butterknife.ButterKnife;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.articlePresenter.ArticlePresenterImp;
import in.healthhunt.view.homeScreenView.myFeedView.IMyFeedView;
import in.healthhunt.view.viewAll.ViewAllFragment;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class WebinarsArticleViewHolder extends RecyclerView.ViewHolder implements IArticleView {

  //  @BindView(R.id.webinars_view_all)
    LinearLayout mViewAll;

    //@BindView(R.id.webinars_article_name)
    TextView mArticleName;

//    @BindView(R.id.webinars_article_pager)
    ViewPager mArticlePager;

    private ArticlePresenterImp mArticlePresenter;
    private in.healthhunt.view.homeScreenView.myFeedView.IMyFeedView IMyFeedView;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public WebinarsArticleViewHolder(View articleView, FragmentManager fragmentManager, IMyFeedView feedView) {
        super(articleView);
        mContext = articleView.getContext();
        ButterKnife.bind(this, articleView);
        mFragmentManager = fragmentManager;
        IMyFeedView = feedView;
        mArticlePresenter = new ArticlePresenterImp(mContext, this);
        setAdapter();

    }

    private void setAdapter() {
        ArticleAdapter articleAdapter = new ArticleAdapter(mFragmentManager,  mArticlePresenter, ArticleParams.WEBINARS_ARTICLES);
        mArticlePager.setAdapter(articleAdapter);
        mArticlePager.setClipToPadding(false);
        mArticlePager.setPadding(0, 0, HealthHuntUtility.dpToPx(100, mContext),0);
        mArticlePager.setPageMargin(HealthHuntUtility.dpToPx(6, mContext));
    }

    @Override
    public Fragment getFragmentArticleItem(int position) {
        return new WebinarsArticleFragment();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public ArticlePostItem getArticle(int pos) {
        return null;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showAlert(String msg) {

    }

    @Override
    public void updateBottomNavigation() {
        IMyFeedView.updateBottomNavigation();
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        //IMyFeedView.loadFragment(fragmentName, bundle);
    }

    @Override
    public void updateAdapter() {

    }

    @Override
    public void updateSavedData(ArticlePostItem articlePostItem) {

    }

    //@OnClick(R.id.webinars_view_all)
    void onViewAll(){
        Bundle bundle = new Bundle();
        bundle.putString("article_name", "Webinars");
        IMyFeedView.onClickViewAll(ViewAllFragment.class.getSimpleName(), bundle);
    }
}