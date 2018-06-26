package in.healthhunt.view.viewAll;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.productResponse.ProductPostItem;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public interface IViewAll {
    RecyclerView.ViewHolder onCreateViewHolder(View view);
    void showProgress();
    void hideProgress();
    void showAlert(String msg);
    //void showViewAllAlert(String msg);
    void updateAdapter();
    int getViewLayout();
    int getType();
    void loadFragment(String fragmentName, Bundle bundle);
    void updateArticleSaved(ArticlePostItem postItem);
    void updateProductSaved(ProductPostItem postItem);
    boolean isRelated();
    List<String> getCategories();
}
