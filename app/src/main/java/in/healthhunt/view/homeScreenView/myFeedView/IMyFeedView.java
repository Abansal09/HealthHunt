package in.healthhunt.view.homeScreenView.myFeedView;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.productResponse.ProductPostItem;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public interface IMyFeedView extends IView {
    RecyclerView.ViewHolder createArticleHolder(View view, int type);
    void onClickViewAll(String tag, Bundle bundle);
    void onClickCrossView(int index);
    List<ArticlePostItem> getTagArticles();
    List<ArticlePostItem> getTrendingArticles();
    List<ArticlePostItem> getLatestArticles();
    List<ArticlePostItem> getSponsoredArticles();
    List<ArticlePostItem> getContinueArticles();
    List<ProductPostItem> getTopProductArticles();
    List<ProductPostItem> getLatestProductArticles();
    void updateAdapter();
    void setBottomNavigation();
    void showHomeAlert(String msg);
    int getView(int type);
    void loadNonFooterFragment(String fragmentName, Bundle bundle);
    void updateArticleSaved(ArticlePostItem postItem);
    void updateProductSaved(ProductPostItem productPostItem);
    List<String> getCategories();
    void addContinueArticle(ArticlePostItem postItem, boolean needToAdd);
}
