package in.healthhunt.presenter.searchPresenter;

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
import in.healthhunt.presenter.interactor.articleInteractor.ArticleInteractorImpl;
import in.healthhunt.presenter.interactor.articleInteractor.IArticleInteractor;
import in.healthhunt.presenter.interactor.bookMarkInteractor.BookMarkInteractorImpl;
import in.healthhunt.presenter.interactor.bookMarkInteractor.IBookMarkInteractor;
import in.healthhunt.view.searchView.ISearch;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class SearchPresenterImp implements ISearchPresenter, IArticleInteractor.OnArticleFinishListener,
        IBookMarkInteractor.OnFinishListener {

    private String TAG = SearchPresenterImp.class.getSimpleName();
    private ISearch ISearch;
    private Context mContext;
    private List<ArticlePostItem> mArticlePosts;
    private IBookMarkInteractor IBookMarkInteractor;
    private IArticleInteractor IArticleInteractor;

    public SearchPresenterImp(Context context, ISearch search) {
        mContext =  context;
        ISearch = search;
        IBookMarkInteractor = new BookMarkInteractorImpl();
        IArticleInteractor = new ArticleInteractorImpl();
    }

    @Override
    public int getCount() {
        int count = 0;
        if(mArticlePosts != null){
            count = mArticlePosts.size();
        }
        return count;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return ISearch.onCreateViewHolder(view);
    }

    @Override
    public void searchArticles(String searchStr) {

        ISearch.showProgress();
        Map<String, String> map = new HashMap<String, String>();

        map.put(ArticleParams.SEARCH, searchStr);
        map.put(ArticleParams.QTRANSLANG, ArticleParams.ENGLISH_LAN);
        // map.put(ArticleParams.APP, String.valueOf(1));
        IArticleInteractor.fetchArticle(mContext, ArticleParams.ARTICLE, map,this);
    }

    @Override
    public void bookmark(String id) {
        ISearch.showProgress();
        IBookMarkInteractor.bookmark(mContext, id, ISearch.getType(), this);
    }

    @Override
    public void unBookmark(String id) {
        ISearch.showProgress();
        IBookMarkInteractor.unBookmark(mContext, id, ISearch.getType(), this);
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        ISearch.loadFragment(fragmentName, bundle);
    }

    @Override
    public List<ArticlePostItem> getAllArticles() {
        return mArticlePosts;
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
    public void onBookMarkSuccess(BookMarkData markResponse) {
        ISearch.hideProgress();
        BookMarkInfo bookMarkInfo = markResponse.getBookMarkInfo();

        if(bookMarkInfo == null){
            Log.i("TAG", " Book Mark info is null");
            return;
        }
        for(ArticlePostItem postItem : mArticlePosts) {
            if(bookMarkInfo.getPost_id().equals(String.valueOf(postItem.getArticle_Id()))) {
                CurrentUser currentUser = postItem.getCurrent_user();
                if (currentUser != null) {
                    currentUser.setBookmarked(bookMarkInfo.isBookMark());
                    ISearch.updateArticleSaved(postItem);
                    break;
                }
            }
        }
        ISearch.updateAdapter();
    }

    @Override
    public void onBookMarkError(RestError errorInfo) {
        ISearch.hideProgress();
        if(errorInfo != null){
            ISearch.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onArticleSuccess(List<ArticlePostItem> items, int type) {
        Log.i("TAGITEMSDATA", "Search Data " + items);
        ISearch.hideProgress();
        mArticlePosts = items;
        ISearch.updateAdapter();

    }

    @Override
    public void onError(RestError errorInfo) {
        ISearch.hideProgress();
        if(errorInfo != null){
            ISearch.showAlert(errorInfo.getMessage());
        }
    }
}
