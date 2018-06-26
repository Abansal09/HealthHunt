package in.healthhunt.presenter.homeScreenPresenter.myHuntPresenter.myHuntsVideoPresenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
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
import in.healthhunt.view.homeScreenView.myHuntsView.IMyHuntsView;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class MyHuntsVideoPresenterImp implements IMyHuntsVideoPresenter, IArticleInteractor.OnArticleFinishListener,
        IBookMarkInteractor.OnFinishListener, IArticleInteractor.OnDeleteFinishListener {

    private String TAG = MyHuntsVideoPresenterImp.class.getSimpleName();
    private IArticleInteractor IArticleInteractor;
    private Context mContext;
    private IMyHuntsView IMyHuntsView;
    private List<ArticlePostItem> mVideoDownloadList;
    private List<ArticlePostItem> mVideoSavedList;
    private List<ArticlePostItem> mVideoApprovedList;
    private List<ArticlePostItem> mVideoReceivedList;
    private IBookMarkInteractor IBookMarkInteractor;
    private int mVideoCount;

    public MyHuntsVideoPresenterImp(Context context, IMyHuntsView myHuntsView) {
        mContext =  context;
        IArticleInteractor = new ArticleInteractorImpl();
        IBookMarkInteractor = new BookMarkInteractorImpl();
        IMyHuntsView = myHuntsView;
    }


    @Override
    public void onArticleSuccess(List<ArticlePostItem> items, int type) {
        mVideoCount--;

        /*if(items == null || items.isEmpty()){
            Log.i("TAGITEMS", "Video Data is empty");
            return;
        }*/

        Log.i("TAGITEMS", "ITEMS " + items);
        switch (type){
            case ArticleParams.SAVED:
                mVideoSavedList = items;
                break;

            case ArticleParams.APPROVED:
                mVideoApprovedList = items;
                break;

            case ArticleParams.RECEIVED:
                mVideoReceivedList = items;
                break;

            case ArticleParams.DOWNLOADED:
                mVideoDownloadList = items;
                break;
        }


        if(mVideoCount == 0) {
            IMyHuntsView.hideProgress();
            IMyHuntsView.updateAdapter();
        }
    }

    @Override
    public void onBookMarkSuccess(BookMarkData markResponse) {
        IMyHuntsView.hideProgress();

        int type = markResponse.getBookMarkInfo().getType();
        switch (type) {
            case ArticleParams.VIDEO:
                for (int i = 0; i < getCount(); i++) {
                    ArticlePostItem postItem = getVideo(i);
                    BookMarkInfo bookMarkInfo = markResponse.getBookMarkInfo();
                    if (bookMarkInfo.getPost_id().equals(String.valueOf(postItem.getArticle_Id()))) {
                        CurrentUser currentUser = postItem.getCurrent_user();
                        if (currentUser != null) {
                            currentUser.setBookmarked(bookMarkInfo.isBookMark());
                            if(!bookMarkInfo.isBookMark()){
                                updateVideoSaved(postItem);
                            }
                            IMyHuntsView.updateSavedArticle(postItem);
                        }
                        break;
                    }
                }
                break;
        }
        IMyHuntsView.updateAdapter();
    }

    @Override
    public void onBookMarkError(RestError errorInfo) {
        IMyHuntsView.hideProgress();
        if(errorInfo != null) {
            IMyHuntsView.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onDeleteArticleSuccess(ArticlePostItem item) {
        IMyHuntsView.hideProgress();
        IMyHuntsView.deletePost(item.getArticle_Id());
    }

    @Override
    public void onError(RestError errorInfo) {
        IMyHuntsView.hideProgress();
        if(errorInfo != null) {
            IMyHuntsView.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void bookmark(String id, int type) {
        IMyHuntsView.showProgress();
        IBookMarkInteractor.bookmark(mContext, id, type, this);
    }

    @Override
    public void unBookmark(String id, int type) {
        IMyHuntsView.showProgress();
        IBookMarkInteractor.unBookmark(mContext, id, type, this);
    }

    @Override
    public void updateBottomNavigation() {

    }

    @Override
    public void showAlert(String msg) {
        IMyHuntsView.showAlert(msg);
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return IMyHuntsView.createViewHolder(view);
    }

    @Override
    public List<ArticlePostItem> getVideoList() {

        List<ArticlePostItem> list = null;
        int type = IMyHuntsView.getType();
        switch (type){
            case ArticleParams.SAVED:
                list = mVideoSavedList;
                break;

            case ArticleParams.APPROVED:
                list = mVideoApprovedList;
                break;

            case ArticleParams.RECEIVED:
                list = mVideoReceivedList;
                break;

            case ArticleParams.DOWNLOADED:
                list = mVideoDownloadList;
                break;
        }
        return list;
    }

    @Override
    public void updateDownloadList(List<ArticlePostItem> articlePosts) {
        mVideoDownloadList = articlePosts;
        IMyHuntsView.updateAdapter();
    }

    @Override
    public int getCount() {
        int count = 0;
        int type = IMyHuntsView.getType();
        switch (type){
            case ArticleParams.SAVED:
                if (mVideoSavedList != null) {
                    count = mVideoSavedList.size();
                }
                break;

            case ArticleParams.APPROVED:
                if(mVideoApprovedList != null) {
                    count = mVideoApprovedList.size();
                }
                break;

            case ArticleParams.RECEIVED:
                if(mVideoReceivedList != null) {
                    count = mVideoReceivedList.size();
                }
                break;

            case ArticleParams.DOWNLOADED:
                if(mVideoDownloadList != null) {
                    count = mVideoDownloadList.size();
                }
                break;
        }
        return count;
    }

    @Override
    public ArticlePostItem getVideo(int pos) {
        ArticlePostItem articlePostItem = null;
        int type = IMyHuntsView.getType();
        switch (type){
            case ArticleParams.SAVED:
                if (mVideoSavedList != null) {
                    articlePostItem = mVideoSavedList.get(pos);
                }
                break;

            case ArticleParams.APPROVED:
                if(mVideoApprovedList != null) {
                    articlePostItem = mVideoApprovedList.get(pos);
                }
                break;

            case ArticleParams.RECEIVED:
                if(mVideoReceivedList != null) {
                    articlePostItem = mVideoReceivedList.get(pos);
                }
                break;

            case ArticleParams.DOWNLOADED:
                if(mVideoDownloadList != null) {
                    articlePostItem = mVideoDownloadList.get(pos);
                }
                break;
        }
        return articlePostItem;
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IMyHuntsView.loadFragment(fragmentName, bundle);
    }

    @Override
    public void fetchVideos(String userId) {
        IMyHuntsView.showProgress();
        mVideoCount = 3;
        fetchSavedArticles(userId);
        fetchApprovedArticles(userId);
        fetchReceivedArticles(userId);
    }

    @Override
    public void updateVideoSaved(ArticlePostItem articlePostItem) {
        if(mVideoSavedList == null){
            mVideoSavedList = new ArrayList<ArticlePostItem>();
        }

        boolean isContained = false;
        for(ArticlePostItem postItem: mVideoSavedList){
            if(postItem.getArticle_Id().equalsIgnoreCase(articlePostItem.getArticle_Id())){
                isContained = true;
                mVideoSavedList.remove(postItem);
                break;
            }
        }

        if(!isContained){
            CurrentUser currentUser = articlePostItem.getCurrent_user();
            if(currentUser.isBookmarked()) {
                mVideoSavedList.add(articlePostItem);
            }
        }
    }

    @Override
    public void deleteArticle(String id) {
        IMyHuntsView.showProgress();
        IArticleInteractor.deleteArticle(mContext, id, this);
    }

    private void fetchSavedArticles(String userId) {
        Map<String, String> map = new HashMap<String, String>();

        String filterCollection = ArticleParams.FILTER + "[" + ArticleParams.COLLECTION + "]";
        String filterFormat = ArticleParams.FILTER + "[" + ArticleParams.FORMAT + "]";

        //String author = ArticleParams.FILTER + "[" + ArticleParams.AUTHOR + "]";

                /*"'{"' +  + ArticleParams.COLLECTION + '"'
                + ":" + '"' + ArticleParams.COLLECTION_SAVED + '"'
                + "," + '"' + ArticleParams.AUTHOR + '"'
                + ":" + userId + "}";*/

        Log.i("TAGFILTER", "filter " + filterCollection);
        map.put(filterCollection, ArticleParams.COLLECTION_SAVED);
        map.put(filterFormat, ArticleParams.POST_FORMAT_VIDEO);

        map.put(ArticleParams.APP, String.valueOf(1));
        //map.put(author, userId);
        map.put(ArticleParams.OFFSET, String.valueOf(0));
        map.put(ArticleParams.LIMIT, String.valueOf(30));
        map.put(ArticleParams.FORMAT, ArticleParams.POST_FORMAT_VIDEO);
        IArticleInteractor.fetchArticle(mContext, ArticleParams.SAVED, map, this);

    }

    private void fetchApprovedArticles(String userId) {
        Map<String, String> map = new HashMap<String, String>();

        String filterCollection = ArticleParams.FILTER + "[" + ArticleParams.COLLECTION + "]";
        String filterFormat = ArticleParams.FILTER + "[" + ArticleParams.FORMAT + "]";
        //String author = ArticleParams.FILTER + "[" + ArticleParams.AUTHOR + "]";

                /*"'{"' +  + ArticleParams.COLLECTION + '"'
                + ":" + '"' + ArticleParams.COLLECTION_SAVED + '"'
                + "," + '"' + ArticleParams.AUTHOR + '"'
                + ":" + userId + "}";*/

        Log.i("TAGFILTER", "filter " + filterCollection);
        map.put(filterCollection, ArticleParams.COLLECTION_CREATED);
        map.put(filterFormat, ArticleParams.POST_FORMAT_VIDEO);

        map.put(ArticleParams.APP, String.valueOf(1));
        //map.put(author, userId);
        map.put(ArticleParams.OFFSET, String.valueOf(0));
        map.put(ArticleParams.LIMIT, String.valueOf(30));
        map.put(ArticleParams.FORMAT, ArticleParams.POST_FORMAT_VIDEO);
        IArticleInteractor.fetchArticle(mContext, ArticleParams.APPROVED, map, this);

    }

    private void fetchReceivedArticles(String userId) {
        Map<String, String> map = new HashMap<String, String>();

        String filterCollection = ArticleParams.FILTER + "[" + ArticleParams.COLLECTION + "]";
        String filterFormat = ArticleParams.FILTER + "[" + ArticleParams.FORMAT + "]";
        //String author = ArticleParams.FILTER + "[" + ArticleParams.AUTHOR + "]";

                /*"'{"' +  + ArticleParams.COLLECTION + '"'
                + ":" + '"' + ArticleParams.COLLECTION_SAVED + '"'
                + "," + '"' + ArticleParams.AUTHOR + '"'
                + ":" + userId + "}";*/

        Log.i("TAGFILTER", "filter " + filterCollection);
        map.put(filterCollection, ArticleParams.COLLECTION_RECEIVED);
        map.put(filterFormat, ArticleParams.POST_FORMAT_VIDEO);

        map.put(ArticleParams.APP, String.valueOf(1));
        //map.put(author, userId);
        map.put(ArticleParams.OFFSET, String.valueOf(0));
        map.put(ArticleParams.LIMIT, String.valueOf(30));
        map.put(ArticleParams.FORMAT, ArticleParams.POST_FORMAT_VIDEO);
        IArticleInteractor.fetchArticle(mContext, ArticleParams.RECEIVED, map, this);

    }
}
