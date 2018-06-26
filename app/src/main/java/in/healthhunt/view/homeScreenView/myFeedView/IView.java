package in.healthhunt.view.homeScreenView.myFeedView;

/**
 * Created by abhishekkumar on 5/19/18.
 */

public interface IView {
    int getCount();
    void showProgress();
    void hideProgress();
    void showAlert(String msg);
    void updateBottomNavigation();
}
