package in.healthhunt.view.profileView;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public interface IEditProfileView {
    void showProgress();
    void hideProgress();
    void updateUserInfo();
    void showAlert(String msg);
}
