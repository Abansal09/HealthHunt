package in.healthhunt.view.loginView;

import android.os.Bundle;
import android.text.Spannable;

/**
 * Created by abhishekkumar on 4/9/18.
 */

public interface ILoginView {

    void onShowProgress();
    void onHideProgress();
    void onEmailError();
    void onPasswordError();
    void loadFragment(String tag, Bundle bundle);
    void loadBackStackFragment(String tag, Bundle bundle);
    void showPasswordChangeAlert(Spannable spannable);
    void showAlert(String msg);
    void showSignUpSuccessAlert(String msg);
    void showToast(String message);
    void startTagActivity();
    void startHomeActivity();
    void hideKeyboardIfOpen();
}
