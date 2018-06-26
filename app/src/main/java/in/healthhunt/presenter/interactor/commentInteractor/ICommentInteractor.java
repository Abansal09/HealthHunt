package in.healthhunt.presenter.interactor.commentInteractor;

import android.content.Context;

import java.util.List;
import java.util.Map;

import framework.retrofit.RestError;
import in.healthhunt.model.comment.CommentData;
import in.healthhunt.model.comment.CommentRequest;
import in.healthhunt.model.comment.CommentsItem;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public interface ICommentInteractor {

    interface OnFinishListener {
        void onCommentError(RestError errorInfo);
        void onCommentSuccess(List<CommentsItem> item);
        void onCommentDeleteSuccess(String id);
        void onNewCommentSuccess(CommentData newComment);
        void onUpdateSuccess(CommentsItem commentsItem);
    }
    void fetchComments(Context context, Map<String, String> map, OnFinishListener finishListener);
    void deleteComment(Context context, String id, OnFinishListener finishListener);
    void addNewComment(Context context, CommentRequest commentRequest, OnFinishListener finishListener);
    void updateComment(Context context, String id, CommentRequest commentRequest, OnFinishListener finishListener);
}
