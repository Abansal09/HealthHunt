package in.healthhunt.presenter.interactor.commentInteractor;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Map;

import framework.retrofit.ResponseResolver;
import framework.retrofit.RestError;
import framework.retrofit.WebServicesWrapper;
import in.healthhunt.model.comment.AllCommentInfo;
import in.healthhunt.model.comment.CommentData;
import in.healthhunt.model.comment.CommentRequest;
import in.healthhunt.model.comment.CommentsItem;
import in.healthhunt.model.response.HHResponse;
import retrofit2.Response;

/**
 * Created by abhishekkumar on 5/11/18.
 */

public class CommentInteractorImpl implements ICommentInteractor {

    @Override
    public void fetchComments(Context context, Map<String, String> queryMap, final OnFinishListener finishListener) {
        WebServicesWrapper.getInstance(context).fetchComments(queryMap, new ResponseResolver<HHResponse<AllCommentInfo>>() {
            @Override
            public void onSuccess(HHResponse<AllCommentInfo> commentResponse, Response response) {

                if(commentResponse.isStatus()){
                    List<CommentsItem> list = commentResponse.getData().getComments();
                    finishListener.onCommentSuccess(list);
                }
                else {
                    Log.i("TAGCOMMENT", "Status is " + commentResponse.isStatus());
                }
            }

            @Override
            public void onFailure(RestError error, String msg) {
                finishListener.onCommentError(error);
            }
        });
    }

    @Override
    public void deleteComment(Context context, final String id, final OnFinishListener finishListener) {
        WebServicesWrapper.getInstance(context).deleteComment(id, new ResponseResolver<HHResponse<CommentsItem>>() {
            @Override
            public void onSuccess(HHResponse<CommentsItem> commentsItem, Response response) {
                finishListener.onCommentDeleteSuccess(id);
            }

            @Override
            public void onFailure(RestError error, String msg) {
                finishListener.onCommentError(error);
            }
        });
    }

    @Override
    public void addNewComment(Context context, CommentRequest commentRequest, final OnFinishListener finishListener) {
        WebServicesWrapper.getInstance(context).addNewComment(commentRequest, new ResponseResolver<HHResponse<CommentData>>() {
            @Override
            public void onSuccess(HHResponse<CommentData> newComment, Response response) {
                    finishListener.onNewCommentSuccess(newComment.getData());
            }

            @Override
            public void onFailure(RestError error, String msg) {
                    finishListener.onCommentError(error);
            }
        });
    }

    @Override
    public void updateComment(Context context, final String id, CommentRequest commentRequest, final OnFinishListener finishListener) {
        WebServicesWrapper.getInstance(context).updateComment(id, commentRequest, new ResponseResolver<HHResponse<CommentData>>() {
            @Override
            public void onSuccess(HHResponse<CommentData> comments, Response response) {
                finishListener.onUpdateSuccess(comments.getData().getComment());
            }

            @Override
            public void onFailure(RestError error, String msg) {
                finishListener.onCommentError(error);
            }
        });
    }
}
