package in.healthhunt.presenter.interactor.bookMarkInteractor;

import android.content.Context;

import framework.retrofit.RestError;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkData;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public interface IBookMarkInteractor {

    interface OnFinishListener {
        void onBookMarkSuccess(BookMarkData markResponse);
        void onBookMarkError(RestError errorInfo);
    }

    void bookmark(Context context, String id, int type, OnFinishListener finishListener);
    void unBookmark(Context context, String id, int type, OnFinishListener finishListener);
   // void bookmark(Context context, int id, Map<String, String> queryMap, OnProductFinishListener productFinishListener);
}
