package in.healthhunt.presenter.interactor.likesInteractor;

import android.content.Context;
import android.util.Log;

import framework.retrofit.ResponseResolver;
import framework.retrofit.RestError;
import framework.retrofit.WebServicesWrapper;
import in.healthhunt.model.likes.LikesInfo;
import in.healthhunt.model.likes.LikesRequest;
import in.healthhunt.model.response.HHResponse;
import retrofit2.Response;

/**
 * Created by abhishekkumar on 5/6/18.
 */

public class LikesInteractorImpl implements ILikesInteractor {

    @Override
    public void updateLikes(Context context, String id, LikesRequest likesRequest, final OnFinishListener finishListener) {
        WebServicesWrapper.getInstance(context).updateLikes(id, likesRequest, new ResponseResolver<HHResponse<LikesInfo>>() {
            @Override
            public void onSuccess(HHResponse<LikesInfo> likesInfo, Response response) {
                Log.i("TAGTAGTALIKES", "LIKEs");
                finishListener.onLikesSuccess(likesInfo.getData());
            }

            @Override
            public void onFailure(RestError error, String msg) {
                finishListener.onLikesError(error);
            }
        });
    }
}
