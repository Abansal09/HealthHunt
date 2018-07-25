package in.healthhunt.presenter.viewAllPresenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.retrofit.RestError;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkData;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkInfo;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.login.User;
import in.healthhunt.presenter.interactor.articleInteractor.ArticleInteractorImpl;
import in.healthhunt.presenter.interactor.articleInteractor.IArticleInteractor;
import in.healthhunt.presenter.interactor.bookMarkInteractor.BookMarkInteractorImpl;
import in.healthhunt.presenter.interactor.bookMarkInteractor.IBookMarkInteractor;
import in.healthhunt.presenter.interactor.productInteractor.IProductInteractor;
import in.healthhunt.presenter.interactor.productInteractor.ProductInteractorImpl;
import in.healthhunt.view.viewAll.IViewAll;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class ViewAllPresenterImp implements IViewAllPresenter, IArticleInteractor.OnViewAllFinishListener,
        IProductInteractor.OnViewAllFinishListener,IBookMarkInteractor.OnFinishListener {

    private String TAG = ViewAllPresenterImp.class.getSimpleName();
    private IViewAll IViewAll;
    private Context mContext;
    // private IViewAllInteractor IViewAllInteractor;
    private List<ArticlePostItem> mArticlePosts;
    private List<ProductPostItem> mProductPosts;
    private IBookMarkInteractor IBookMarkInteractor;
    private IArticleInteractor IArticleInteractor;
    private IProductInteractor IProductInteractor;

    public ViewAllPresenterImp(Context context, IViewAll viewAll) {
        mContext =  context;
        IViewAll = viewAll;
        //IViewAllInteractor = new ViewAllInteractorImpl();
        IBookMarkInteractor = new BookMarkInteractorImpl();
        IArticleInteractor = new ArticleInteractorImpl();
        IProductInteractor = new ProductInteractorImpl();
    }

    @Override
    public int getCount(int type) {
        int count = 0;
        switch (type){
            case ArticleParams.PRESCRIBED_FOR_YOU:
            case ArticleParams.READ_FRESH_ARTICLES:
            case ArticleParams.RELATED_ARTICLES:
                if(mArticlePosts != null) {
                    count = mArticlePosts.size();
                }
                break;

            case ArticleParams.CHECK_OUT_THE_NEWBIES_PRODUCTS:
            case ArticleParams.RELATED_PRODUCTS:
                if(mProductPosts != null) {
                    count = mProductPosts.size();
                }
                break;
        }

        return count;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return IViewAll.onCreateViewHolder(view);
    }

    @Override
    public void fetchAll(int type, String id) {

        IViewAll.showProgress();
        Map<String, String> map = new HashMap<String, String>();
        int limit = 30;
        if(IViewAll.isRelated()){
            limit = 9;
        }

        String filter = ArticleParams.FILTER + "[" + ArticleParams.FORMAT + "]";
        List<String> categories = IViewAll.getCategories();

        switch (type) {
            case ArticleParams.READ_FRESH_ARTICLES:

                //map.put(ArticleParams.SECTION, ArticleParams.LATEST_BY_MONTH);
                map.put(ArticleParams.ORDER, ArticleParams.DESC);
                map.put(ArticleParams.ORDER_BY, ArticleParams.DATE/*ArticleParams.ID*/);
                map.put(filter, ArticleParams.POST_FORMAT_IMAGE);
                map.put(ArticleParams.OFFSET, String.valueOf(0));
                map.put(ArticleParams.LIMIT, String.valueOf(limit));
                map.put(ArticleParams.APP, String.valueOf(1));

                if(categories != null && !categories.isEmpty() && !categories.contains("1")) {  // 1 For ALL
                    IArticleInteractor.fetchAllArticleCategory(mContext, map, categories, this);
                }
                else{
                    IArticleInteractor.fetchAllArticle(mContext, map,this);
                }

                break;

            case ArticleParams.RELATED_ARTICLES:
                map.put(ArticleParams.OFFSET, String.valueOf(0));
                map.put(ArticleParams.LIMIT, String.valueOf(limit));
                map.put(ArticleParams.RELATED, id);
                map.put(ArticleParams.APP, String.valueOf(1));
                map.put(filter, ArticleParams.POST_FORMAT_IMAGE);
                IArticleInteractor.fetchAllArticle(mContext, map,this);
                break;



            /*case ArticleParams.RELATED_ARTICLES:
                map.put(ArticleParams.SECTION, ArticleParams.LATEST_BY_MONTH);
                map.put(ArticleParams.OFFSET, String.valueOf(0));
                map.put(ArticleParams.LIMIT, String.valueOf(30));
                IArticleInteractor.fetchAllArticle(mContext, map,this);
                break;*/
            case ArticleParams.PRESCRIBED_FOR_YOU:
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
                map.put(ArticleParams.TAGS, tags);
                map.put(ArticleParams.OFFSET, String.valueOf(0));
                map.put(ArticleParams.LIMIT, String.valueOf(limit));
                map.put(filter, ArticleParams.POST_FORMAT_IMAGE);
                map.put(ArticleParams.APP, String.valueOf(1));

                if(categories != null && !categories.isEmpty() && !categories.contains("1")) {  // 1 For ALL
                    IArticleInteractor.fetchAllArticleCategory(mContext, map, categories, this);
                }
                else {
                    IArticleInteractor.fetchAllArticle(mContext, map, this);
                }
                break;

            case ArticleParams.CHECK_OUT_THE_NEWBIES_PRODUCTS:
                map.put(ArticleParams.TYPE, ArticleParams.MARKET);
                map.put(ArticleParams.MARKET_TYPE, String.valueOf(ArticleParams.PRODUCT_SERVICES));
                map.put(ArticleParams.ORDER, ArticleParams.DESC);
                map.put(ArticleParams.ORDER_BY, ArticleParams.DATE/*ArticleParams.ID*/);

                //map.put(ArticleParams.SECTION, ArticleParams.LATEST_BY_MONTH);
                map.put(ArticleParams.APP, String.valueOf(1));
                map.put(ArticleParams.OFFSET, String.valueOf(0));
                map.put(ArticleParams.LIMIT, String.valueOf(limit));
                IProductInteractor.fetchAllProduct(mContext, map, this);
                break;
            case ArticleParams.RELATED_PRODUCTS:
                map.put(ArticleParams.TYPE, ArticleParams.MARKET);
                map.put(ArticleParams.OFFSET, String.valueOf(0));
                map.put(ArticleParams.LIMIT, String.valueOf(limit));
                map.put(ArticleParams.APP, String.valueOf(1));
                map.put(ArticleParams.RELATED, id);
                IProductInteractor.fetchAllProduct(mContext, map, this);
                break;

            /*case ArticleParams.RELATED_PRODUCTS:
                map.put(ArticleParams.TYPE, ArticleParams.MARKET);
                map.put(ArticleParams.MARKT_TYPE, String.valueOf(1));
                map.put(ArticleParams.SECTION, ArticleParams.LATEST_BY_WEEK);
                map.put(ArticleParams.OFFSET, String.valueOf(0));
                map.put(ArticleParams.LIMIT, String.valueOf(30));
                IProductInteractor.fetchAllProduct(mContext, map, this);
                break;*/
        }
    }

    @Override
    public int getView() {
        return IViewAll.getViewLayout();
    }

    @Override
    public void bookmark(String id) {
        IViewAll.showProgress();
        IBookMarkInteractor.bookmark(mContext, id, IViewAll.getType(), this);
    }

    @Override
    public void unBookmark(String id) {
        IViewAll.showProgress();
        IBookMarkInteractor.unBookmark(mContext, id, IViewAll.getType(), this);
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IViewAll.loadFragment(fragmentName, bundle);
    }

    @Override
    public List<ArticlePostItem> getAllArticles() {
        return mArticlePosts;
    }

    @Override
    public List<ProductPostItem> getAllProduct() {
        return mProductPosts;
    }

    @Override
    public ArticlePostItem getArticle(int pos) {
        ArticlePostItem postItem = null;
        if(mArticlePosts != null && pos < mArticlePosts.size()){
            postItem =  mArticlePosts.get(pos);
        }
        return postItem;
    }

    @Override
    public ProductPostItem getProduct(int pos) {
        ProductPostItem postItem = null;
        if(mProductPosts != null && pos < mProductPosts.size()){
            postItem =  mProductPosts.get(pos);
        }
        return postItem;
    }


    @Override
    public void onArticleSuccess(List<ArticlePostItem> items) {
        IViewAll.hideProgress();
        mArticlePosts = items;
        IViewAll.updateAdapter();
        Log.i("TAGVIEWLALL", "VIEQLL " + mArticlePosts);
    }

    @Override
    public void onProductSuccess(List<ProductPostItem> items) {
        IViewAll.hideProgress();
        mProductPosts = items;
        IViewAll.updateAdapter();
    }

    @Override
    public void onBookMarkSuccess(BookMarkData markResponse) {
        IViewAll.hideProgress();
        BookMarkInfo bookMarkInfo = markResponse.getBookMarkInfo();

        if(bookMarkInfo == null){
            Log.i("TAG", " Book Mark info is null");
            return;
        }

        int type = bookMarkInfo.getType();
        switch (type){
            case ArticleParams.PRESCRIBED_FOR_YOU:
            case ArticleParams.READ_FRESH_ARTICLES:
            case ArticleParams.RELATED_ARTICLES:


                for(ArticlePostItem postItem : mArticlePosts) {
                    if(bookMarkInfo.getPost_id().equals(String.valueOf(postItem.getArticle_Id()))) {
                        CurrentUser currentUser = postItem.getCurrent_user();
                        if (currentUser != null) {
                            currentUser.setBookmarked(bookMarkInfo.isBookMark());
                            IViewAll.updateArticleSaved(postItem);
                            break;
                        }
                    }
                }
                break;

            case ArticleParams.CHECK_OUT_THE_NEWBIES_PRODUCTS:
            case ArticleParams.RELATED_PRODUCTS:
                for(ProductPostItem postItem : mProductPosts) {
                    if(bookMarkInfo.getPost_id().equals(postItem.getProduct_id())) {
                        CurrentUser currentUser = postItem.getCurrent_user();
                        if (currentUser != null) {
                            currentUser.setBookmarked(bookMarkInfo.isBookMark());
                            IViewAll.updateProductSaved(postItem);
                            break;
                        }
                    }
                }
                break;
        }

        IViewAll.updateAdapter();
    }

    @Override
    public void onBookMarkError(RestError errorInfo) {
        IViewAll.hideProgress();
        if(errorInfo != null){
            IViewAll.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onError(RestError errorInfo) {
        IViewAll.hideProgress();
        if(errorInfo != null){
            IViewAll.showAlert(errorInfo.getMessage());
        }
    }
}
