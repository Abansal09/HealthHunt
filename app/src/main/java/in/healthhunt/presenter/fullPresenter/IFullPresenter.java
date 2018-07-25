package in.healthhunt.presenter.fullPresenter;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.comment.CommentsItem;
import in.healthhunt.view.fullView.commentView.CommentViewHolder;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public interface IFullPresenter {
    void loadFragment(String tag, Bundle bundle);
    void fetchArticle(String id);
    void fetchProduct(String id);
    ArticlePostItem getArticle();
    ProductPostItem getProduct();
    void bookmark(String id, int type);
    void unBookmark(String id, int type);
    CommentViewHolder createViewHolder(View view);
    int getCommentCount();
    void fetchComments(String id);
    void fetchMoreComments(String id);
    List<CommentsItem> getComments();
    CommentsItem getComment(int pos);
    void  deleteComment(String id);
    void addNewComment(String post_id, String content);
    void updateComment(String id, String content);
    void updateLike(String id);
    ArticlePostItem getRelatedArticle(int pos);
    int getRelatedArticlesCount();
    ProductPostItem getRelatedProduct(int pos);
    int getRelatedProductsCount();
    void updateArticle(ArticlePostItem articlePost);
    void updateProduct(ProductPostItem productPost);
}

