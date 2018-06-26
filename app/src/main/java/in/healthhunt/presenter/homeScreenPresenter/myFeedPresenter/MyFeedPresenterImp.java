package in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.retrofit.RestError;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.login.User;
import in.healthhunt.presenter.interactor.articleInteractor.ArticleInteractorImpl;
import in.healthhunt.presenter.interactor.articleInteractor.IArticleInteractor;
import in.healthhunt.presenter.interactor.productInteractor.IProductInteractor;
import in.healthhunt.presenter.interactor.productInteractor.ProductInteractorImpl;
import in.healthhunt.view.homeScreenView.myFeedView.IMyFeedView;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class MyFeedPresenterImp implements IMyFeedPresenter, IArticleInteractor.OnArticleFinishListener,
        IProductInteractor.OnProductFinishListener/*, IArticleInteractor.OnFullViewFinishListener*/{

    private String TAG = MyFeedPresenterImp.class.getSimpleName();
    private IMyFeedView IMyFeedView;
    private Context mContext;
    //private IMyFeedInteractor IMyFeedInteractor;
    private List<ArticlePostItem> mTagArticles;
    private List<ArticlePostItem> mContinueArticles;
    private List<ArticlePostItem> mTrendingArticles;
    private List<ArticlePostItem> mLatestArticles;
    private List<ArticlePostItem> mSponsoredArticles;
    private List<ProductPostItem> mTopProduct;
    private List<ProductPostItem> mLatestProduct;
    private Map<Integer, Integer> mArticlesType;

    private IArticleInteractor IArticleInteractor;
    private IProductInteractor IProductInteractor;

    private int fetchCount;


    public MyFeedPresenterImp(Context context, IMyFeedView feedView) {
        mContext =  context;
        IMyFeedView = feedView;
        //IMyFeedInteractor = new MyFeedInteractorImpl();
        IArticleInteractor = new ArticleInteractorImpl();
        IProductInteractor = new ProductInteractorImpl();
        mArticlesType = new HashMap<Integer, Integer>();
    }

    @Override
    public int getCount() {
        return IMyFeedView.getCount();
    }

    @Override
    public RecyclerView.ViewHolder createArticleHolder(View view, int type) {
        return IMyFeedView.createArticleHolder(view, type);
    }

    @Override
    public void fetchTagsArticle(int offset, int limit) {

        /*if(mTagArticles != null){
            mTagArticles.clear();
        }*/
        /*Set<String> tagIds = HealthHuntPreference.getSet(mContext, Constants.SELECTED_TAGS_KEY);
        String tags = "";
        Iterator iterator = tagIds.iterator();
        while (iterator.hasNext()) {
            tags = tags + iterator.next();
            if(iterator.hasNext()){
                tags = tags + ",";
            }
        }*/


        User user = User.getCurrentUser();
        String tags = user.getTagList();
        Log.i("TAG1111", "ids = " + tags);
        String filter = ArticleParams.FILTER + "[" + ArticleParams.FORMAT + "]";

        Map<String, String> map = new HashMap<String, String>();
        map.put(ArticleParams.TAGS, tags);
        map.put(filter, ArticleParams.POST_FORMAT_IMAGE);
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(offset));
        map.put(ArticleParams.LIMIT, String.valueOf(limit));

        List<String> categories = IMyFeedView.getCategories();
        if(categories != null && !categories.isEmpty() && !categories.contains("1")) {  // 1 For ALL
            IArticleInteractor.fetchArticlesCategory(mContext, ArticleParams.BASED_ON_TAGS, map, categories,this);
        }
        else {
            IArticleInteractor.fetchArticle(mContext, ArticleParams.BASED_ON_TAGS, map, this);
        }
    }

    @Override
    public void fetchContinueArticles(String continueList) {

        Map<String, String> map = new HashMap<String, String>();
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.INCLUDE, continueList);
        IArticleInteractor.fetchArticle(mContext, ArticleParams.CONTINUE_ARTICLES, map, this);

    }

    @Override
    public void addContinueArticle(ArticlePostItem postItem) {
        if(mContinueArticles == null){
            mContinueArticles = new ArrayList<ArticlePostItem>();
        }

        removeContinueArticle(postItem);  // to update the position of previous articles.
        mContinueArticles.add(0, postItem);  // always add to first position if new article come.
    }

    @Override
    public void removeContinueArticle(ArticlePostItem postItem) {

        for(int i=0; i<mContinueArticles.size(); i++){
            ArticlePostItem articlePostItem = mContinueArticles.get(i);
            if(articlePostItem.getArticle_Id().equalsIgnoreCase(postItem.getArticle_Id())){
                mContinueArticles.remove(i);
                break;
            }
        }
    }

    @Override
    public void updateContinueArticles() {
        mArticlesType.clear();
        buildMap();
        IMyFeedView.updateAdapter();
    }

    @Override
    public void fetchTrendingArticle(int offset, int limit) {
        //  String filter = ArticleParams.FILTER + "[" + ArticleParams.FORMAT + "]";

       /* if(mTrendingArticles != null){
            mTrendingArticles.clear();
        }*/

        Map<String, String> map = new HashMap<String, String>();
        map.put(ArticleParams.TRENDING, String.valueOf(1));
        map.put(ArticleParams.QTRANSLANG, ArticleParams.ENGLISH_LAN);
        // map.put(filter, ArticleParams.POST_FORMAT_IMAGE);
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(offset));
        map.put(ArticleParams.LIMIT, String.valueOf(limit));
        IArticleInteractor.fetchArticle(mContext, ArticleParams.TRENDING_ARTICLES, map, this);
    }

    @Override
    public void fetchLatestArticle(int offset, int limit) {

        /*if(mLatestArticles != null){
            mLatestArticles.clear();
        }*/

        String filter = ArticleParams.FILTER + "[" + ArticleParams.FORMAT + "]";

        Map<String, String> map = new HashMap<String, String>();
        //map.put(ArticleParams.SECTION, ArticleParams.LATEST_BY_MONTH);

        map.put(ArticleParams.ORDER, ArticleParams.DESC);
        map.put(ArticleParams.ORDER_BY, ArticleParams.DATE/*ArticleParams.ID*/);
        map.put(filter, ArticleParams.POST_FORMAT_IMAGE);
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(offset));
        map.put(ArticleParams.LIMIT, String.valueOf(limit));

        List<String> categories = IMyFeedView.getCategories();
        if(categories != null && !categories.isEmpty() && !categories.contains("1")) {  // 1 For ALL
            IArticleInteractor.fetchArticlesCategory(mContext, ArticleParams.LATEST_ARTICLES, map, categories, this);
        }
        else {
            IArticleInteractor.fetchArticle(mContext, ArticleParams.LATEST_ARTICLES, map, this);
        }
    }

    @Override
    public void fetchSponsoredArticle(String type, int offset, int limit) {

        /*if(mSponsoredArticles != null){
            mSponsoredArticles.clear();
        }*/

        String filter = ArticleParams.FILTER + "[" + ArticleParams.FORMAT + "]";
        Map<String, String> map = new HashMap<String, String>();
        map.put(ArticleParams.SPONSORED, type);
        map.put(filter, ArticleParams.POST_FORMAT_IMAGE);
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(offset));
        map.put(ArticleParams.LIMIT, String.valueOf(limit));
        IArticleInteractor.fetchArticle(mContext, ArticleParams.SPONSORED_ARTICLES, map, this);
    }

    @Override
    public void fetchTopProduct(int offset, int limit) {

        /*if(mTopProduct != null){
            mTopProduct.clear();
        }*/

        Map<String, String> map = new HashMap<String, String>();
        map.put(ArticleParams.TYPE, ArticleParams.MARKET);
        map.put(ArticleParams.MARKT_TYPE, String.valueOf(1));
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(offset));
        map.put(ArticleParams.LIMIT, String.valueOf(limit));
        IProductInteractor.fetchProduct(mContext, ArticleParams.TOP_PRODUCTS, map, this);

    }

    @Override
    public void fetchLatestProduct(int offset, int limit) {

        /*if(mLatestProduct != null){
            mLatestProduct.clear();
        }*/

        Map<String, String> map = new HashMap<String, String>();
        map.put(ArticleParams.TYPE, ArticleParams.MARKET);
        //map.put(ArticleParams.MARKT_TYPE, String.valueOf(1));
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(offset));
        map.put(ArticleParams.LIMIT, String.valueOf(limit));
        map.put(ArticleParams.ORDER, ArticleParams.DESC);
        map.put(ArticleParams.ORDER_BY, ArticleParams.DATE/*ArticleParams.ID*/);

        //map.put(ArticleParams.SECTION, ArticleParams.LATEST_BY_MONTH);
        IProductInteractor.fetchProduct(mContext, ArticleParams.LATEST_PRODUCTS, map, this);

    }

    /*@Override
    public void addContinueArticle(String id) {
        boolean isContain = false;
        if(mContinueArticles != null && !mContinueArticles.isEmpty()){
            for(ArticlePostItem postItem: mContinueArticles){
                if(postItem.getArticle_Id().equalsIgnoreCase(id)){
                    isContain = true;
                    mContinueArticles.remove(postItem);
                    mContinueArticles.add(0, postItem);
                    IMyFeedView.updateAdapter();
                    break;
                }
            }
        }

        if(!isContain){
            IArticleInteractor.fetchFullArticle(mContext, id, this);
        }

    }*/

    @Override
    public List<ArticlePostItem> getTagArticles() {
        return mTagArticles;
    }

    @Override
    public List<ArticlePostItem> getTrendingArticles() {
        return mTrendingArticles;
    }

    @Override
    public List<ArticlePostItem> getLatestArticles() {
        return mLatestArticles;
    }

    @Override
    public List<ArticlePostItem> getSponsoredArticles() {
        return mSponsoredArticles;
    }

    @Override
    public List<ArticlePostItem> getContinueArticles() {
        return mContinueArticles;
    }

    @Override
    public List<ProductPostItem> getTopProducts() {
        return mTopProduct;
    }

    @Override
    public List<ProductPostItem> getLatestProducts() {
        return mLatestProduct;
    }

    @Override
    public Map<Integer, Integer> getArticlesType() {
        return mArticlesType;
    }

    @Override
    public void fetchData() {
        IMyFeedView.showProgress();
        mArticlesType.clear();

        User user = User.getCurrentUser();
        String continueList = user.getContinueList();
        if(continueList != null && !continueList.isEmpty()) {
            fetchCount = 7;
            fetchContinueArticles(continueList);
        }
        else {
            fetchCount = 6;
        }

        fetchTagsArticle(0, 5);
        fetchTrendingArticle(0, 2);
        fetchLatestArticle(0, 5);
        fetchSponsoredArticle(ArticleParams.POST_FORMAT_IMAGE, 0, 2);
        fetchTopProduct(0, 2);
        fetchLatestProduct(0, 5);

    }

    @Override
    public int getView(int type) {
        return IMyFeedView.getView(type);
    }

    @Override
    public void deleteArticleType(int pos) {
        if(mArticlesType != null && pos < mArticlesType.size()) {
            updateMapWithRemoved(pos);
        }
    }

    @Override
    public void addArticleType(int pos, int type) {
        if(mArticlesType != null && pos < mArticlesType.size()) {
            updateMapWithAdded(pos, type);
        }
    }

    @Override
    public void onArticleSuccess(List<ArticlePostItem> items, int type) {
        Log.i("TAGARTICLES" , "article type " + type);
        switch (type) {
            case ArticleParams.BASED_ON_TAGS:
                mTagArticles = items;
                fetchCount--;
                break;

            case ArticleParams.TRENDING_ARTICLES:
                mTrendingArticles = items;
                fetchCount--;
                break;

            case ArticleParams.LATEST_ARTICLES:
                mLatestArticles = items;
                fetchCount--;
                break;

            case ArticleParams.SPONSORED_ARTICLES:
                mSponsoredArticles = items;
                fetchCount--;
                break;

            case ArticleParams.CONTINUE_ARTICLES:
                mContinueArticles = items;
                fetchCount--;
                break;
        }

        if(fetchCount == 0/*  && continueCount == 0*/) {
            buildMap();
            IMyFeedView.hideProgress();
            IMyFeedView.updateAdapter();
        }
    }

    @Override
    public void onProductSuccess(List<ProductPostItem> items, int type) {
        Log.i("TAGARTICLES" , "product type " + type);
        switch (type) {
            case ArticleParams.TOP_PRODUCTS:
                mTopProduct = items;
                fetchCount--;
                break;
            case ArticleParams.LATEST_PRODUCTS:
                mLatestProduct = items;
                fetchCount--;
                break;
        }

        if(fetchCount == 0/* && continueCount == 0*/) {
            buildMap();
            IMyFeedView.hideProgress();
            IMyFeedView.updateAdapter();
        }
    }

    /*@Override
    public void onArticleSuccess(ArticlePostItem item) {    // Will call for only continue articles
        continueCount--;
        if(mContinueArticles == null){
            mContinueArticles = new ArrayList<ArticlePostItem>();
        }

        if(item != null){
            String id = item.getArticle_Id();
            if(id != null) {
                mContinueArticles.add(item);
            }
        }

        if(fetchCount == 0  && continueCount == 0) {
            sortContinueArticles();
            buildMap();
            IMyFeedView.hideProgress();
            IMyFeedView.updateAdapter();
        }
    }*/


    @Override
    public void onError(RestError errorInfo) {
        IMyFeedView.hideProgress();
        String msg = mContext.getString(R.string.server_error);
        if(errorInfo != null && errorInfo.getMessage() != null) {
            msg = errorInfo.getMessage();
        }
        IMyFeedView.showHomeAlert(msg);
    }

    private void buildMap() {

        int index = 0;

        if(mTagArticles != null && !mTagArticles.isEmpty()){
            mArticlesType.put(index, ArticleParams.BASED_ON_TAGS);
            index++;
        }

        if(mContinueArticles != null) {
            Log.i("TAGCONITNUE", "Continue " + mContinueArticles.size());
        }

        if(mContinueArticles != null && !mContinueArticles.isEmpty()){
            mArticlesType.put(index, ArticleParams.CONTINUE_ARTICLES);
            index++;
        }

        if(mTrendingArticles != null && !mTrendingArticles.isEmpty()){
            mArticlesType.put(index, ArticleParams.TRENDING_ARTICLES);
            index++;
        }

        if(mSponsoredArticles != null && !mSponsoredArticles.isEmpty()){
            mArticlesType.put(index, ArticleParams.SPONSORED_ARTICLES);
            index++;
        }

        if(mTopProduct != null && !mTopProduct.isEmpty()){
            mArticlesType.put(index, ArticleParams.TOP_PRODUCTS);
            index++;
        }

        if(mLatestArticles != null && !mLatestArticles.isEmpty()){
            mArticlesType.put(index, ArticleParams.LATEST_ARTICLES);
            index++;
        }

        if(mLatestProduct != null && !mLatestProduct.isEmpty()){
            mArticlesType.put(index, ArticleParams.LATEST_PRODUCTS);
            index++;
        }
    }

    private void updateMapWithRemoved(int pos) {
        for (int i = pos; i < mArticlesType.size() - 1; i++) {
            int type = mArticlesType.get(i + 1);
            mArticlesType.put(i, type);
        }
        mArticlesType.remove(mArticlesType.size()-1);
    }

    private void updateMapWithAdded(int pos, int articleType) {
        for (int i = pos; i < mArticlesType.size(); i++) {
            int type = mArticlesType.get(i);
            mArticlesType.put(i+1, type);
        }
        mArticlesType.put(pos,articleType);
    }


}
