package in.healthhunt.presenter.fullPresenter;

import android.content.Context;

import framework.retrofit.RestError;
import in.healthhunt.model.articles.postResponse.ArticlePost;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.presenter.homeScreenPresenter.IBookMarkInteractor;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public interface IFullInteractor extends IBookMarkInteractor{

    interface OnFinishListener {
        void onArticleSuccess(ArticlePost item);
        void onProductSuccess(ProductPostItem item);
        void onError(RestError errorInfo);
    }
    void fetchFullArticle(Context context, String id, OnFinishListener finishListener);
    void fetchFullProduct(Context context, String id, OnFinishListener finishListener);
}
