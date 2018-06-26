package in.healthhunt.presenter.interactor.bookMarkInteractor;

import android.content.Context;

import java.util.List;

import framework.retrofit.ResponseResolver;
import framework.retrofit.RestError;
import framework.retrofit.WebServicesWrapper;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkData;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkInfo;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkResponse;
import retrofit2.Response;

/**
 * Created by abhishekkumar on 5/16/18.
 */

public class BookMarkInteractorImpl implements IBookMarkInteractor {

    @Override
    public void bookmark(Context context, final String id, final int type, final OnFinishListener finishListener) {
        WebServicesWrapper.getInstance(context).bookmark(id, new ResponseResolver<List<BookMarkResponse>>() {
            @Override
            public void onSuccess(List<BookMarkResponse> markResponse, Response response) {

                BookMarkResponse bookMarkResponse = markResponse.get(0);
                BookMarkData data = bookMarkResponse.getData();
                if(data != null) {
                    BookMarkInfo bookMarkInfo = data.getBookMarkInfo();
                    bookMarkInfo.setType(type);
                    bookMarkInfo.setBookMark(true);
                    finishListener.onBookMarkSuccess(bookMarkResponse.getData());
                }
            }

            @Override
            public void onFailure(RestError error, String msg) {
                finishListener.onBookMarkError(error);
            }
        });
    }

    @Override
    public void unBookmark(Context context,final String id,final int type, final OnFinishListener finishListener) {
        WebServicesWrapper.getInstance(context).unBookmark(id, new ResponseResolver<BookMarkData>() {
            @Override
            public void onSuccess(BookMarkData markResponse, Response response) {
                    if(markResponse.isStatus()) {
                        BookMarkInfo bookMarkInfo = markResponse.getBookMarkInfo();
                        bookMarkInfo.setType(type);
                        bookMarkInfo.setBookMark(false);
                        finishListener.onBookMarkSuccess(markResponse);
                    }

            }

            @Override
            public void onFailure(RestError error, String msg) {
                finishListener.onBookMarkError(error);
            }
        });
    }
}
