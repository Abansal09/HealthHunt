package in.healthhunt.view.homeScreenView.myFeedView.articleView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import in.healthhunt.model.beans.SpaceDecoration;
import in.healthhunt.model.login.User;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.articlePresenter.ArticlePresenterImp;
import in.healthhunt.view.fullView.fullViewFragments.FullArticleFragment;
import in.healthhunt.view.fullView.fullViewFragments.YoutubeFragment;
import in.healthhunt.view.homeScreenView.myFeedView.IMyFeedView;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class ContinueArticleViewHolder extends RecyclerView.ViewHolder implements IArticleView, ContinueAdapter.ClickListener {


    @BindView(R.id.article_name)
    TextView mArticleName;

    @BindView(R.id.continue_recycler_list)
    public RecyclerView mContinueViewer;

    @BindView(R.id.continue_cross_image)
    LinearLayout mCrossButton;

    @BindView(R.id.continue_view)
    LinearLayout mView;

    private ArticlePresenterImp IArticlePresenter;
    private in.healthhunt.view.homeScreenView.myFeedView.IMyFeedView IMyFeedView;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private int mIndex;

    public ContinueArticleViewHolder(View articleView, FragmentManager fragmentManager, IMyFeedView feedView, int index) {
        super(articleView);
        mContext = articleView.getContext();
        ButterKnife.bind(this, articleView);
        mFragmentManager = fragmentManager;
        IMyFeedView = feedView;
        mIndex = index;
        IArticlePresenter = new ArticlePresenterImp(mContext, this);
        setAdapter();

    }

    private void setAdapter() {
        ContinueAdapter continueAdapter = new ContinueAdapter(mContext, IArticlePresenter);
        continueAdapter.setClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        mContinueViewer.setLayoutManager(layoutManager);
        mContinueViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(6, mContext), SpaceDecoration.HORIZONTAL));
        mContinueViewer.setAdapter(continueAdapter);
        mContinueViewer.setFocusableInTouchMode(false);
    }


    @Override
    public int getCount() {
        int count = 0;
        List<ArticlePostItem> articlePostItems = IMyFeedView.getContinueArticles();
        if(articlePostItems != null){
            count = articlePostItems.size();
        }

        return count;
    }


    @Override
    public Fragment getFragmentArticleItem(int position) {
        return null;
    }

    @Override
    public ArticlePostItem getArticle(int pos) {
        List<ArticlePostItem> articlePostItems = IMyFeedView.getContinueArticles();
        Log.i("TAGTAGArticlePos", " Pos " + articlePostItems);
        ArticlePostItem postItem = null;
        if(articlePostItems != null && !articlePostItems.isEmpty()){
            postItem = articlePostItems.get(pos);
        }
        return postItem;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

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
        mContinueViewer.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void updateSavedData(ArticlePostItem articlePostItem) {

    }

    @OnClick(R.id.continue_cross_image)
    void onCrossClick() {
        IMyFeedView.onClickCrossView(getAdapterPosition());
        IMyFeedView.getContinueArticles().clear();
        User user = User.getCurrentUser();
        user.setContinueList("");
        user.save();
    }

    public void hideView(){
        mView.setVisibility(View.GONE);
        //mContinueViewer.setVisibility(View.GONE);
        //mContinueView.setVisibility(View.GONE);
    }

    public void showView(){
        mView.setVisibility(View.VISIBLE);
       // mContinueViewer.setVisibility(View.VISIBLE);
        //mContinueView.setVisibility(View.VISIBLE);
    }

    public void notifyDataChanged(){
        if(mContinueViewer != null && mContinueViewer.getAdapter() != null){
            updateAdapter();
        }
    }

    @Override
    public void ItemClicked(View v, int position) {
        ArticlePostItem postItem = IArticlePresenter.getArticle(position);

        String thumbnailImage = postItem.getVideo_thumbnail();
        if(thumbnailImage == null || thumbnailImage.isEmpty()){
            openFullViewArticle(postItem.getArticle_Id());
        }
        else {
            openFullViewVideo(postItem.getArticle_Id());
        }
    }

    private void openFullViewArticle(String id){
        Bundle bundle = new Bundle();
        bundle.putString(ArticleParams.ID, id);
        bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.ARTICLE);
        IArticlePresenter.loadFragment(FullArticleFragment.class.getSimpleName(), bundle);
    }

    private void openFullViewVideo(String id){
        Bundle bundle = new Bundle();
        bundle.putString(ArticleParams.ID, id);
        bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.VIDEO);
        IArticlePresenter.loadFragment(YoutubeFragment.class.getSimpleName(), bundle);
    }
}