package in.healthhunt.view.homeScreenView.myFeedView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.IMyFeedPresenter;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.MyFeedPresenterImp;
import in.healthhunt.view.homeScreenView.IHomeView;
import in.healthhunt.view.homeScreenView.myFeedView.articleView.ArticleViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.articleView.ContinueArticleViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.articleView.LatestArticleViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.articleView.SponsoredArticleViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.articleView.TrendingArticleViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.productView.LatestProductViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.productView.TopProductViewHolder;

/**
 * Created by abhishekkumar on 5/3/18.
 */

public class MyFeedFragment extends Fragment implements IMyFeedView {

    @BindView(R.id.my_feed_recycler_list)
    RecyclerView mFeedViewer;

    @BindView(R.id.no_records)
    TextView mNoRecords;

    private IMyFeedPresenter IMyFeedPresenter;
    private FragmentManager mFragmentManager;
    private MyFeedAdapter mFeedAdapter;
    private IHomeView IHomeView;
    private RecyclerView.RecycledViewPool mPool;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IMyFeedPresenter = new MyFeedPresenterImp(getContext(), this);
        IHomeView = (IHomeView) getActivity();
        IMyFeedPresenter.fetchData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_feed, container, false);
        ButterKnife.bind(this, view);

        Log.i("TagFrag", "MyFeed Fragment");
        mFragmentManager = getFragmentManager();
        IHomeView.updateTitle(getString(R.string.my_feed));
        setBottomNavigation();
        setAdapter();
        return view;
    }

    private void setAdapter() {
        mFeedAdapter = new MyFeedAdapter(IMyFeedPresenter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mFeedViewer.setLayoutManager(layoutManager);
        mFeedViewer.setNestedScrollingEnabled(false);
        mFeedViewer.setAdapter(mFeedAdapter);
        mPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getCount() {
        Log.i("TAGCOUNT", " Count " + IMyFeedPresenter.getArticlesType().size());
        return IMyFeedPresenter.getArticlesType().size();
    }

    @Override
    public RecyclerView.ViewHolder createArticleHolder(View view, int type) {

        RecyclerView.ViewHolder viewHolder = null;
        switch (type) {
            case ArticleParams.BASED_ON_TAGS:
                viewHolder = new ArticleViewHolder(view, mFragmentManager, this);
                break;

            case ArticleParams.CONTINUE_ARTICLES:
                viewHolder = new ContinueArticleViewHolder(view, mFragmentManager, this, type);
                // ((ContinueArticleViewHolder)viewHolder).mContinueViewer.setRecycledViewPool(mPool);
                break;

            case ArticleParams.TRENDING_ARTICLES:
                viewHolder = new TrendingArticleViewHolder(view, this);
                ((TrendingArticleViewHolder)viewHolder).mTrendingViewer.setRecycledViewPool(mPool);
                break;

            case ArticleParams.SPONSORED_ARTICLES:
                viewHolder = new SponsoredArticleViewHolder(view, this, type);
                ((SponsoredArticleViewHolder)viewHolder).mSponsoredViewer.setRecycledViewPool(mPool);
                break;

            case ArticleParams.TOP_PRODUCTS:
                viewHolder = new TopProductViewHolder(view, this);
                ((TopProductViewHolder)viewHolder).mTopProductViewer.setRecycledViewPool(mPool);
                break;

            case ArticleParams.LATEST_ARTICLES:
                viewHolder = new LatestArticleViewHolder(view, mFragmentManager, this);
                break;

            case ArticleParams.LATEST_PRODUCTS:
                viewHolder = new LatestProductViewHolder(view, mFragmentManager, this);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onClickViewAll(String tag, Bundle bundle) {
        IHomeView.getHomePresenter().loadNonFooterFragment(tag, bundle);
    }

    @Override
    public void onClickCrossView(int pos) {
        IMyFeedPresenter.deleteArticleType(pos);
        Log.i("TAG1111", "pos " + pos);
        Log.i("TAG1111", "size " + IMyFeedPresenter.getArticlesType().size());
        mFeedAdapter.deleteItem(pos);
    }

    @Override
    public List<ArticlePostItem> getTagArticles() {
        return IMyFeedPresenter.getTagArticles();
    }

    @Override
    public List<ArticlePostItem> getTrendingArticles() {
        return IMyFeedPresenter.getTrendingArticles();
    }

    @Override
    public List<ArticlePostItem> getLatestArticles() {
        return IMyFeedPresenter.getLatestArticles();
    }

    @Override
    public List<ArticlePostItem> getSponsoredArticles() {
        return IMyFeedPresenter.getSponsoredArticles();
    }

    @Override
    public List<ArticlePostItem> getContinueArticles() {
        return IMyFeedPresenter.getContinueArticles();
    }

    @Override
    public List<ProductPostItem> getTopProductArticles() {
        return IMyFeedPresenter.getTopProducts();
    }

    @Override
    public List<ProductPostItem> getLatestProductArticles() {
        return IMyFeedPresenter.getLatestProducts();
    }

    @Override
    public void updateAdapter() {
        if(mFeedAdapter != null) {
            mFeedAdapter.notifyDataSetChanged();
            /*int count = mFeedViewer.getChildCount();
            for(int i=0; i<count; i++) {
                RecyclerView.ViewHolder viewHolder = mFeedViewer.getChildViewHolder(mFeedViewer.getChildAt(i));

                if(viewHolder instanceof ArticleViewHolder) {
                    ((ArticleViewHolder) viewHolder).notifyDataChanged();
                }
                else if(viewHolder instanceof TrendingArticleViewHolder) {
                    ((TrendingArticleViewHolder) viewHolder).notifyDataChanged();
                }
                else if(viewHolder instanceof LatestArticleViewHolder) {
                    ((LatestArticleViewHolder) viewHolder).notifyDataChanged();
                }
                else if(viewHolder instanceof SponsoredArticleViewHolder) {
                    ((SponsoredArticleViewHolder) viewHolder).notifyDataChanged();
                }
                else if(viewHolder instanceof TopProductViewHolder) {
                    ((TopProductViewHolder) viewHolder).notifyDataChanged();
                }
                else if(viewHolder instanceof LatestProductViewHolder) {
                    ((LatestProductViewHolder) viewHolder).notifyDataChanged();
                }
                else if(viewHolder instanceof LatestProductViewHolder) {
                    ((LatestProductViewHolder) viewHolder).notifyDataChanged();
                }
            }*/
            updateVisibility();
        }

        IHomeView.updateCategoryVisibility();
        //updateCategoryView();
    }

    public void updateVisibility(){
        int count = IMyFeedPresenter.getCount();
        if(count == 0){
            mNoRecords.setVisibility(View.VISIBLE);
            mFeedViewer.setVisibility(View.GONE);
        }
        else {
            mNoRecords.setVisibility(View.GONE);
            mFeedViewer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setBottomNavigation() {
        IHomeView.setBottomNavigation();
    }

    @Override
    public void showHomeAlert(String msg) {
        IHomeView.showHomeAlert(msg);
    }

    @Override
    public int getView(int type) {
        int layout = 0;
        switch (type){
            case ArticleParams.BASED_ON_TAGS:
                layout = R.layout.article_view;
                break;

            case ArticleParams.CONTINUE_ARTICLES:
                layout = R.layout.continue_article_view;
                break;

            case ArticleParams.TRENDING_ARTICLES:
                layout = R.layout.trending_article_view;
                break;

            case ArticleParams.SPONSORED_ARTICLES:
                layout = R.layout.sponsored_article_view;
                break;

            case ArticleParams.TOP_PRODUCTS:
                layout = R.layout.top_products_article_view;
                break;

            case ArticleParams.LATEST_ARTICLES:
                layout = R.layout.latest_article_view;
                break;

            case ArticleParams.LATEST_PRODUCTS:
                layout = R.layout.latest_products_article_view;
                break;
        }
        return layout;
    }

    @Override
    public void showProgress() {
        IHomeView.showProgress();
    }

    @Override
    public void hideProgress() {
        IHomeView.hideProgress();
    }

    @Override
    public void showAlert(String msg) {
        IHomeView.showAlert(msg);
    }

    @Override
    public void updateBottomNavigation() {
        IHomeView.hideBottomNavigationSelection();
    }

    @Override
    public void loadNonFooterFragment(String fragmentName, Bundle bundle) {
        IHomeView.getHomePresenter().loadNonFooterFragment(fragmentName, bundle);
    }

    @Override
    public void updateArticleSaved(ArticlePostItem postItem) {

        String url = postItem.getVideo_thumbnail();
        if(url == null || url.isEmpty()) {   // For article
            IHomeView.updateMyhuntsArticleSaved(postItem);
        }
        else {
            IHomeView.updateMyhuntsVideoSaved(postItem);
        }
        CurrentUser currentUser = postItem.getCurrent_user();
        showToast(currentUser);
        updateDataOfArticles(postItem);
    }

    private void showToast(CurrentUser currentUser) {
        boolean isBookMark = currentUser.isBookmarked();
        String str = getString(R.string.added_to_my_hunt);//getString(R.string.saved);
        if(!isBookMark){
            str = getString(R.string.removed_from_my_hunt);//getString(R.string.removed);
        }
        IHomeView.showToast(str);
    }

    @Override
    public void updateProductSaved(ProductPostItem productPostItem) {
        IHomeView.updateMyhuntsProductSaved(productPostItem);
        CurrentUser currentUser = productPostItem.getCurrent_user();
        showToast(currentUser);
        IHomeView.updateShop(productPostItem);
        updateDataOfProducts(productPostItem);
    }

    @Override
    public List<String> getCategories() {
        return IHomeView.getCategories();
    }

    @Override
    public void addContinueArticle(ArticlePostItem postItem, boolean needToAdd) {

        // List<ArticlePostItem> articlePostItemList = IMyFeedPresenter.getContinueArticles();

        /*if(articlePostItemList == null || articlePostItemList.isEmpty()){
            IMyFeedPresenter.addArticleType(1, ArticleParams.CONTINUE_ARTICLES);
            //mFeedAdapter.addItem(1);
        }*/

        if(!needToAdd){
            IMyFeedPresenter.removeContinueArticle(postItem);
        }
        else {
            removeLastContinueItem();
            IMyFeedPresenter.addContinueArticle(postItem);
        }


        //updateAdapter();
    }

    private void removeLastContinueItem(){
        List<ArticlePostItem> continueList = IMyFeedPresenter.getContinueArticles();

        if(continueList == null){
            return;
        }

        int count = continueList.size();
        if(count >= 20){
            continueList.remove(count - 1); // Removed last item from the list
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void updateData(){
        IMyFeedPresenter.fetchData();
    }

    public void updateDataOfArticles(ArticlePostItem postItem){
        updateBasedOnTags(postItem);
        updateTrending(postItem);
        updateSponsored(postItem);
        updateLatestArticles(postItem);
        mFeedAdapter.notifyDataSetChanged();
    }

    public void updateDataOfProducts(ProductPostItem postItem){
        updateTopProduct(postItem);
        updateLatestProduct(postItem);
        mFeedAdapter.notifyDataSetChanged();
    }


    private void updateBasedOnTags(ArticlePostItem articlePostItem){     // For Bookmark
        List<ArticlePostItem> list = IMyFeedPresenter.getTagArticles();
        for(ArticlePostItem postItem: list){
            if(postItem.getArticle_Id().equalsIgnoreCase(articlePostItem.getArticle_Id())){
                CurrentUser currentUser = postItem.getCurrent_user();
                if(currentUser != null){
                    currentUser.setBookmarked(articlePostItem.getCurrent_user().isBookmarked());
                }
                //updateBasedOnTagsAdapter();
                break;
            }
        }
    }

    private void updateTrending(ArticlePostItem articlePostItem){   // For Bookmark
        List<ArticlePostItem> list = IMyFeedPresenter.getTrendingArticles();
        for(ArticlePostItem postItem: list){
            if(postItem.getArticle_Id().equalsIgnoreCase(articlePostItem.getArticle_Id())){
                CurrentUser currentUser = postItem.getCurrent_user();
                if(currentUser != null){
                    currentUser.setBookmarked(articlePostItem.getCurrent_user().isBookmarked());
                }
                //updateTrendingAdapter();
                break;
            }
        }
    }

    private void updateSponsored(ArticlePostItem articlePostItem){   // For Bookmark
        Log.i("TAGSPONSPRED", "updateSponsored " + articlePostItem.getArticle_Id());
        List<ArticlePostItem> list = IMyFeedPresenter.getSponsoredArticles();
        for(ArticlePostItem postItem: list){
            if(postItem.getArticle_Id().equalsIgnoreCase(articlePostItem.getArticle_Id())){
                CurrentUser currentUser = postItem.getCurrent_user();
                if(currentUser != null){
                    currentUser.setBookmarked(articlePostItem.getCurrent_user().isBookmarked());
                }
                Log.i("TAGSPONSPRED", "SPONSPR");
                //updateSponsoredAdapter();
                break;
            }
        }
    }

    private void updateLatestArticles(ArticlePostItem articlePostItem){   // For Bookmark
        List<ArticlePostItem> list = IMyFeedPresenter.getLatestArticles();
        for(ArticlePostItem postItem: list){
            if(postItem.getArticle_Id().equalsIgnoreCase(articlePostItem.getArticle_Id())){
                CurrentUser currentUser = postItem.getCurrent_user();
                if(currentUser != null){
                    currentUser.setBookmarked(articlePostItem.getCurrent_user().isBookmarked());
                }
                //updateLatestArticleAdapter();
                break;
            }
        }
    }

    private void updateTopProduct(ProductPostItem productPostItem){
        List<ProductPostItem> list = IMyFeedPresenter.getTopProducts();
        for(ProductPostItem postItem: list){
            if(postItem.getProduct_id().equalsIgnoreCase(productPostItem.getProduct_id())){
                CurrentUser currentUser = postItem.getCurrent_user();
                if(currentUser != null){
                    currentUser.setBookmarked(productPostItem.getCurrent_user().isBookmarked());
                }
                //updateTopProductsAdapter();
                break;
            }
        }
    }

    private void updateLatestProduct(ProductPostItem productPostItem){
        List<ProductPostItem> list = IMyFeedPresenter.getLatestProducts();
        for(ProductPostItem postItem: list){
            if(postItem.getProduct_id().equalsIgnoreCase(productPostItem.getProduct_id())){
                CurrentUser currentUser = postItem.getCurrent_user();
                if(currentUser != null){
                    currentUser.setBookmarked(productPostItem.getCurrent_user().isBookmarked());
                }
                //updateLatestProductAdapter();
                break;
            }
        }
    }

    public void updateContinueArticles(){
        IMyFeedPresenter.updateContinueArticles();
    }

    /*private void updateBasedOnTagsAdapter(){
        if(mFeedAdapter != null) {
            mFeedAdapter.notifyDataSetChanged();
            int count = mFeedAdapter.getItemCount();
            for(int i=0; i<count; i++) {
                View view = mFeedViewer.getChildAt(i);

                RecyclerView.ViewHolder viewHolder = null;
                if(view != null) {
                    viewHolder = mFeedViewer.getChildViewHolder(mFeedViewer.getChildAt(i));
                }

                if(viewHolder instanceof ArticleViewHolder) {
                    ((ArticleViewHolder) viewHolder).notifyDataChanged();
                    break;
                }
            }
        }
    }

    private void updateTrendingAdapter(){
        if(mFeedAdapter != null) {
            mFeedAdapter.notifyDataSetChanged();
            int count = mFeedAdapter.getItemCount();
            for(int i=0; i<count; i++) {
                View view = mFeedViewer.getChildAt(i);

                RecyclerView.ViewHolder viewHolder = null;
                if(view != null) {
                    viewHolder = mFeedViewer.getChildViewHolder(mFeedViewer.getChildAt(i));
                }
                if(viewHolder instanceof TrendingArticleViewHolder) {
                    ((TrendingArticleViewHolder) viewHolder).notifyDataChanged();
                    break;
                }
            }
        }
    }

    private void updateSponsoredAdapter(){
        if(mFeedAdapter != null) {
            mFeedAdapter.notifyDataSetChanged();
            int count = mFeedViewer.getChildCount();
            for(int i=0; i<count; i++) {
                View view = mFeedViewer.getChildAt(i);

                Log.i("TagFeed", "View  " + view);
                RecyclerView.ViewHolder viewHolder = null;
                if(view != null) {
                    viewHolder = mFeedViewer.getChildViewHolder(view);
                }
                if(viewHolder instanceof SponsoredArticleViewHolder) {
                    ((SponsoredArticleViewHolder) viewHolder).notifyDataChanged();
                    break;
                }
            }
        }
    }

    private void updateTopProductsAdapter(){
        if(mFeedAdapter != null) {
            mFeedAdapter.notifyDataSetChanged();
            int count = mFeedViewer.getChildCount();
            for(int i=0; i<count; i++) {
                View view = mFeedViewer.getChildAt(i);

                RecyclerView.ViewHolder viewHolder = null;
                if(view != null) {
                    viewHolder = mFeedViewer.getChildViewHolder(view);
                }

                if(viewHolder instanceof TopProductViewHolder) {
                    ((TopProductViewHolder) viewHolder).notifyDataChanged();
                    break;
                }
            }
        }
    }


    private void updateLatestProductAdapter(){
        if(mFeedAdapter != null) {
            mFeedAdapter.notifyDataSetChanged();
            int count = mFeedViewer.getChildCount();
            for(int i=0; i<count; i++) {
                View view = mFeedViewer.getChildAt(i);

                RecyclerView.ViewHolder viewHolder = null;
                if(view != null) {
                    viewHolder = mFeedViewer.getChildViewHolder(view);
                }

                if(viewHolder instanceof LatestProductViewHolder) {
                    ((LatestProductViewHolder) viewHolder).notifyDataChanged();
                    break;
                }
            }
        }
    }

    private void updateLatestArticleAdapter(){
        if(mFeedAdapter != null) {
            mFeedAdapter.notifyDataSetChanged();
            int count = mFeedViewer.getChildCount();
            for(int i=0; i<count; i++) {

                View view = mFeedViewer.getChildAt(i);

                RecyclerView.ViewHolder viewHolder = null;
                if(view != null) {
                    viewHolder = mFeedViewer.getChildViewHolder(view);
                }


                if(viewHolder instanceof LatestArticleViewHolder) {
                    ((LatestArticleViewHolder) viewHolder).notifyDataChanged();
                    break;
                }
            }
        }
    }*/
}
