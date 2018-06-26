package in.healthhunt.view.searchView;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import in.healthhunt.model.articles.articleResponse.ArticlePostItem;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public interface ISearch {
    RecyclerView.ViewHolder onCreateViewHolder(View view);
    void showProgress();
    void hideProgress();
    void updateAdapter();
    void showAlert(String msg);
    int getType();
    void loadFragment(String fragmentName, Bundle bundle);
    void updateArticleSaved(ArticlePostItem postItem);
}
