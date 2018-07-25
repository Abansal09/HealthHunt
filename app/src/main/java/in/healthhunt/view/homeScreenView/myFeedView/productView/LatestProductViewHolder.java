package in.healthhunt.view.homeScreenView.myFeedView.productView;

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
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.productPresenter.IProductPresenter;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.productPresenter.ProductPresenterImp;
import in.healthhunt.view.homeScreenView.myFeedView.IMyFeedView;
import in.healthhunt.view.viewAll.ViewAllFragment;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class LatestProductViewHolder extends RecyclerView.ViewHolder implements IProductView {

    @BindView(R.id.view_all)
    LinearLayout mViewAll;

    @BindView(R.id.latest_article_name)
    TextView mLatestArticleName;

    @BindView(R.id.latest_product_article_pager)
    ViewPager mLatestArticlePager;

    @BindView(R.id.latest_product_view)
    LinearLayout mView;


    private IProductPresenter IProductPresenter;
    private in.healthhunt.view.homeScreenView.myFeedView.IMyFeedView IMyFeedView;
    private Context mContext;
    private FragmentManager mFragmentManager;

    public LatestProductViewHolder(View articleView, FragmentManager fragmentManager, IMyFeedView feedView) {
        super(articleView);
        mContext = articleView.getContext();
        IMyFeedView = feedView;
        ButterKnife.bind(this, articleView);
        mFragmentManager = fragmentManager;
        IProductPresenter = new ProductPresenterImp(mContext, this);
        setAdapter();

    }

    private void setAdapter() {
        LatestProductAdapter productAdapter = new LatestProductAdapter(mFragmentManager,  IProductPresenter, ArticleParams.CHECK_OUT_THE_NEWBIES_PRODUCTS);
        mLatestArticlePager.setAdapter(productAdapter);
        mLatestArticlePager.setClipToPadding(false);
        mLatestArticlePager.setPadding(0, 0, HealthHuntUtility.dpToPx(100, mContext),0);
        mLatestArticlePager.setPageMargin(HealthHuntUtility.dpToPx(6, mContext));

        /*mLatestArticlePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position == IProductPresenter.getCount() - 1){
                    mLatestArticlePager.setPadding(HealthHuntUtility.dpToPx(100, mContext), 0, 0,0);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

    @Override
    public Fragment getFragmentArticleItem(int position) {
        return new LatestProductFragment(IProductPresenter);
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IMyFeedView.loadNonFooterFragment(fragmentName, bundle);
    }

    @Override
    public void updateAdapter() {
        mLatestArticlePager.getAdapter().notifyDataSetChanged();
        updateViewAllVisibility();
    }

    private void updateViewAllVisibility(){
        int count = IProductPresenter.getCount();
        if(count < 5){
            mViewAll.setVisibility(View.GONE);
        }
        else {
            mViewAll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateProductSaved(ProductPostItem postItem) {
        IMyFeedView.updateProductSaved(postItem);
    }

    @Override
    public int getCount() {
        List<ProductPostItem> list = IMyFeedView.getLatestProductArticles();
        int count = 0;
        if(list != null) {
            count = list.size();
        }
        return count;
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
    public ProductPostItem getProduct(int pos) {
        List<ProductPostItem> list = IMyFeedView.getLatestProductArticles();
        ProductPostItem postItem = null;
        if(list != null) {
            postItem = list.get(pos);
        }
        return postItem;
    }

    @OnClick(R.id.view_all)
    void onViewAll(){
        if(HealthHuntUtility.checkInternetConnection(mContext)) {
            openViewAllFragment();
        }
        else {
            showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
        }
    }

    private void openViewAllFragment() {
        Bundle bundle = new Bundle();
        bundle.putInt(ArticleParams.ARTICLE_TYPE, ArticleParams.CHECK_OUT_THE_NEWBIES_PRODUCTS);
        IProductPresenter.updateBottomNavigation();
        IProductPresenter.loadFragment(ViewAllFragment.class.getSimpleName(), bundle);
    }

    public void notifyDataChanged() {
        mLatestArticlePager.getAdapter().notifyDataSetChanged();
    }

    public void hideView(){
        mView.setVisibility(View.GONE);
    }

    public void showView(){
        mView.setVisibility(View.VISIBLE);
    }
}