package in.healthhunt.view.fullView.fullViewFragments;

import android.os.Bundle;
import android.view.View;

import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.view.fullView.commentView.CommentViewHolder;

/**
 * Created by abhishekkumar on 5/16/18.
 */

public interface IFullFragment {
    void showProgress();
    void hideProgress();
    void showAlert(String msg);
    //void showFullViewAlert(String msg);
    void setContent();
    int getPostType();
    void updateBookMarkIcon();
    CommentViewHolder createViewHolder(View view);
    void updateCommentAdapter();
    void updateLike();
    void updateRelatedArticleAdapter();
    void updateRelatedProductAdapter();
    void loadFragment(String fragmentName, Bundle bundle);
    void updateProductSaved(ProductPostItem postItem);
    void updateArticleSaved(ArticlePostItem articlePostItem);
    void updateVideoSaved(ArticlePostItem articlePostItem);
}

