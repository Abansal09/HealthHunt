package in.healthhunt.presenter.homeScreenPresenter;

/**
 * Created by abhishekkumar on 5/19/18.
 */

public interface IPostPresenter {
    int getCount();
    void bookmark(String id, int type);
    void unBookmark(String id, int type);
    void updateBottomNavigation();
    void showAlert(String msg);
}
