package in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.articlePresenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import framework.retrofit.RestError;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkData;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkInfo;
import in.healthhunt.model.articles.commonResponse.Author;
import in.healthhunt.model.articles.commonResponse.CategoriesItem;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.commonResponse.MediaItem;
import in.healthhunt.model.articles.commonResponse.TagsItem;
import in.healthhunt.model.articles.commonResponse.Title;
import in.healthhunt.presenter.interactor.bookMarkInteractor.BookMarkInteractorImpl;
import in.healthhunt.presenter.interactor.bookMarkInteractor.IBookMarkInteractor;
import in.healthhunt.view.homeScreenView.myFeedView.articleView.IArticleView;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public class ArticlePresenterImp implements IArticlePresenter, IBookMarkInteractor.OnFinishListener {

    private IArticleView IArticleView;
    private Context mContext;
    private IBookMarkInteractor IInteractor;
    public ArticlePresenterImp(Context context, IArticleView articleView) {
        mContext = context;
        IArticleView = articleView;
        IInteractor = new BookMarkInteractorImpl();
    }

    @Override
    public int getCount() {
        return IArticleView.getCount();
    }

    @Override
    public Fragment getItem(int position, int type) {
        Fragment fragment = IArticleView.getFragmentArticleItem(position);
        Bundle bundle = new Bundle();//createBundle(position);
        bundle.putInt(ArticleParams.ARTICLE_TYPE, type);
        bundle.putInt(ArticleParams.POSITION, position);

        int count = IArticleView.getCount();

        if(position == 4 && position == count - 1) {
            bundle.putBoolean(ArticleParams.IS_LAST_PAGE, true);
        }
        else {
            bundle.putBoolean(ArticleParams.IS_LAST_PAGE, false);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void bookmark(String id, int type) {
        IArticleView.showProgress();
        IInteractor.bookmark(mContext, id, type, this);
    }

    @Override
    public void unBookmark(String id, int type) {
        IArticleView.showProgress();
        IInteractor.unBookmark(mContext, id, type, this);
    }

    @Override
    public ArticlePostItem getArticle(int pos) {
        return IArticleView.getArticle(pos);
    }

    @Override
    public void updateBottomNavigation() {
        IArticleView.updateBottomNavigation();
    }

    @Override
    public void showAlert(String msg) {
        IArticleView.showAlert(msg);
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IArticleView.loadFragment(fragmentName, bundle);
    }

    private Bundle createBundle(int pos) {
        ArticlePostItem postItem = IArticleView.getArticle(pos);

        Bundle bundle = new Bundle();

        if(postItem != null) {

            bundle.putInt(ArticleParams.POSITION, pos);

            bundle.putString(ArticleParams.ID, String.valueOf(postItem.getArticle_Id()));

            String url = null;
            List<MediaItem> mediaItems = postItem.getMedia();
            if(mediaItems != null && !mediaItems.isEmpty()) {
                MediaItem media = mediaItems.get(0);
                Log.i("TAGMedia", "media "+ media);
                if("image".equals(media.getMedia_type())) {
                    url = media.getUrl();
                    Log.i("TAGMedia", "URL "+ url);

                }
            }
            bundle.putString(ArticleParams.ARTICLE_URL, url);

            String categoryName = null;
            List<CategoriesItem> categories = postItem.getCategories();
            if(categories != null && !categories.isEmpty()){
                categoryName = categories.get(0).getName();
            }
            bundle.putString(ArticleParams.CATEGORY_NAME, categoryName);

            Author author = postItem.getAuthor();
            if(author != null){
                bundle.putString(ArticleParams.AUTHOR_NAME, author.getName());
                bundle.putString(ArticleParams.AUTHOR_URL, author.getUrl());

            }

            Title title = postItem.getTitle();
            String articleTitle = null;
            if(title != null) {
                articleTitle = title.getRendered();
            }
            bundle.putString(ArticleParams.ARTICLE_TITLE, articleTitle);


            ArrayList<String> tagsName = null;
            List<TagsItem> tagItems = postItem.getTags();
            if(tagItems != null && !tagItems.isEmpty()) {
                tagsName = new ArrayList<String>();
                for(TagsItem  tagItem: tagItems) {
                    Log.i("TAG11", tagItem.getName());
                    tagsName.add(tagItem.getName());
                }
            }

            bundle.putStringArrayList(ArticleParams.ARTICLE_TAGS_NAME_LIST, tagsName);

            Log.i("TAGRead", "read " + postItem.getRead_time());
            bundle.putString(ArticleParams.ARTICLE_READ_TIME, postItem.getRead_time());

            String date = postItem.getDate();
            /*if(date != null) {
                date = HealthHuntUtility.getDateWithFormat("dd-MMM-yyyy", date);
            }*/
            bundle.putString(ArticleParams.ARTICLE_DATE, date);

            CurrentUser currentUser = postItem.getCurrent_user();
            if(currentUser != null) {
                bundle.putBoolean(ArticleParams.IS_BOOKMARK, currentUser.isBookmarked());
            }
        }

        return bundle;
    }

    @Override
    public void onBookMarkSuccess(BookMarkData markResponse) {
        IArticleView.hideProgress();

        for(int i=0; i<IArticleView.getCount(); i++) {
            ArticlePostItem postItem = IArticleView.getArticle(i);
            BookMarkInfo bookMarkInfo = markResponse.getBookMarkInfo();
            if(bookMarkInfo.getPost_id().equals(String.valueOf(postItem.getArticle_Id()))) {
                CurrentUser currentUser = postItem.getCurrent_user();
                if (currentUser != null) {
                    currentUser.setBookmarked(bookMarkInfo.isBookMark());
                    Log.i("TAGTRENDING","ID " + bookMarkInfo.getPost_id());
                    IArticleView.updateSavedData(postItem);
                }
                break;
            }
        }
        IArticleView.updateAdapter();
    }

    @Override
    public void onBookMarkError(RestError errorInfo) {
        IArticleView.hideProgress();
        String msg = mContext.getString(R.string.server_error);
        if(errorInfo != null) {
            msg = errorInfo.getMessage();
            IArticleView.showAlert(msg);
        }
    }
}
