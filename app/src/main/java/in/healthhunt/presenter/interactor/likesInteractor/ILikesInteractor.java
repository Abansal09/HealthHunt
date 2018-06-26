package in.healthhunt.presenter.interactor.likesInteractor;

import android.content.Context;

import framework.retrofit.RestError;
import in.healthhunt.model.likes.LikesInfo;
import in.healthhunt.model.likes.LikesRequest;

/**
 * Created by abhishekkumar on 5/6/18.
 */

public interface ILikesInteractor {

    interface OnFinishListener {
        void onLikesError(RestError errorInfo);
        void onLikesSuccess(LikesInfo likesInfo);
    }
    void updateLikes(Context context, String id, LikesRequest likesRequest, OnFinishListener finishListener);
}
