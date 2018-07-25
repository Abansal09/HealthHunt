package in.healthhunt.view.homeScreenView;

import android.os.Bundle;

import java.util.List;
import java.util.Map;

import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.presenter.homeScreenPresenter.IHomePresenter;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public interface IHomeView {
    void showHomeAlert(String msg);
    void showAlert(String msg);

    void finishActivity();
    void setBottomNavigation();
    void updateTitle(String msg);
    void showProgress();
    void hideProgress();
    void hideBottomNavigationSelection();
    IHomePresenter getHomePresenter();
    void hideBottomFooter();
    void showBottomFooter();
    void loadFooterFragment(String tag, Bundle bundle);
    void loadNonFooterFragment(String tag, Bundle bundle);
    void popTopBackStack();
    void sendFilterData(Map<Integer, List<String>> map);
    Map<Integer, List<String>> getFilterData();
    void closeDrawer();
    void updateMyFeed();
    void updateWatch();

    void updateCategoryVisibility();

    void updateDrawerFragment();
    void hideActionBar();
    void showActionBar();
    void setStatusBarTranslucent(boolean val);
    void updateDownloadData();
    void updateMyhuntsArticleSaved(ArticlePostItem  articlePostItem);
    void updateMyhuntsVideoSaved(ArticlePostItem  articlePostItem);
    void updateMyhuntsProductSaved(ProductPostItem productPostItem);
    void showDrawerMenu();
    void hideDrawerMenu();

    void updateMyFeedArticle(ArticlePostItem  articlePostItem);
    void updateMyFeedProduct(ProductPostItem productPostItem);
    void updateWatch(ArticlePostItem  articlePostItem);
    void updateShop(ProductPostItem productPostItem);
    void showToast(String msg);
    List<String> getCategories();
    void updateCategoryList();

    //void hideSearchView();
    //void showSearchView();
    void hideNotificationView();
    void showNotificationView();


    void addContinueArticle(ArticlePostItem postItem, boolean needToAdd);

    void hideKeyboardIfOpen();
}
