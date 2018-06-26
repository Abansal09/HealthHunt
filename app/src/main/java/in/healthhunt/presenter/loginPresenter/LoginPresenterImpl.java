package in.healthhunt.presenter.loginPresenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import java.util.List;

import framework.retrofit.RestError;
import in.healthhunt.R;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.login.ForgotPasswordRequest;
import in.healthhunt.model.login.LoginRequest;
import in.healthhunt.model.login.SignUpRequest;
import in.healthhunt.model.login.User;
import in.healthhunt.model.login.UserData;
import in.healthhunt.model.preference.HealthHuntPreference;
import in.healthhunt.model.response.HHResponse;
import in.healthhunt.presenter.interactor.loginInteractor.ILoginInteractor;
import in.healthhunt.presenter.interactor.loginInteractor.LoginInteractorImpl;
import in.healthhunt.view.loginView.ILoginView;

/**
 * Created by abhishekkumar on 4/9/18.
 */

public class LoginPresenterImpl implements ILoginPresenter, ILoginInteractor.OnLoginFinishListener, ILoginInteractor.OnPasswordChangeListener {

    private final String TAG = LoginPresenterImpl.class.getSimpleName();
    in.healthhunt.view.loginView.ILoginView ILoginView;
    ILoginInteractor ILoginInteractor;
    private Context mContext;

    public LoginPresenterImpl(Activity activity) {
        mContext = activity.getApplicationContext();
        ILoginView = (ILoginView) activity;
        ILoginInteractor = new LoginInteractorImpl();
    }

    @Override
    public void validateCredentialsLogIn(String email, String password) {
        if(!email.isEmpty() && !password.isEmpty()) {

            if(email.contains("@") && email.contains(".")) {
                HealthHuntPreference.putBoolean(mContext, Constants.IS_LOGIN_NORMAL, true);
                ILoginView.onShowProgress();
                ILoginInteractor.login(mContext, createLoginRequest(email, password, null, null), this);
                return;
            }
        }

        String str = mContext.getString(R.string.email_validation_msg);
        if(email.isEmpty()){
            str = mContext.getString(R.string.email_validation_msg);
        }
        else if(password.isEmpty()){
            str = mContext.getString(R.string.password_validation_msg);
        }
        ILoginView.showAlert(str);
    }


    @Override
    public void validateCredentialsSignUp(String username, String gender, String email, String password) {
        if(!email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
            //signup

           /* if(username == null || username.isEmpty()) {
                username = email;
            }*/
            if(!email.contains("@") || !email.contains(".")){
                String str = mContext.getString(R.string.email_validation_msg);
                ILoginView.showAlert(str);
                return;
            }

            if(email.contains(".")){
                int index = email.indexOf(".");
                int size = email.length();
                Log.i("TAGLOGIN" , "Index " + index + " size " + size);
                if(index+2 >= size){
                    String str = mContext.getString(R.string.email_validation_msg);
                    ILoginView.showAlert(str);
                    return;
                }

            }

            if(gender == null || gender.isEmpty()) {
                gender = "Not specified";
            }

            ILoginView.onShowProgress();
            SignUpRequest signUpRequest = createSignUpRequest(username, gender, email, password);
            ILoginInteractor.signUp(mContext, signUpRequest, this);
        }
        else {
            String str = mContext.getString(R.string.username_validation_msg);
            if(username.isEmpty()){
                str = mContext.getString(R.string.username_validation_msg);
            }
            else if(email.isEmpty()){
                str = mContext.getString(R.string.email_validation_msg);
            }
            else if(password.isEmpty()){
                str = mContext.getString(R.string.password_validation_msg);
            }

            ILoginView.showAlert(str);
        }
    }

    @Override
    public void forgotPassword(String email, String username) {
        String str = mContext.getString(R.string.email_validation_msg);
        if(!email.isEmpty()) {
            if(email.contains("@") && email.contains(".")) {
                ILoginView.onShowProgress();
                ForgotPasswordRequest request = createForgotPasswordRequest(email, username);
                ILoginInteractor.resetLoginPassword(mContext, request, this);
                return;
            }
        }

        ILoginView.showAlert(str);
    }

    private boolean validateGoogleServerClientID(Context context) {
        String serverClientId = context.getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;
            Log.d(TAG, message);
            return false;

        }
        return true;
    }

    @Override
    public void loadFragment(String tag, Bundle bundle) {
        ILoginView.showFragment(tag, bundle);
    }

    @Override
    public Intent loginGoogle(Context context) {
        Intent intent = null;
        if(validateGoogleServerClientID(context)) {
            ILoginView.onShowProgress();
            intent = ILoginInteractor.loginWithGmail(context);
        }
        return intent;
    }

    @Override
    public void loginFacebook(Context context) {
        HealthHuntPreference.putBoolean(mContext, Constants.IS_LOGIN_NORMAL, false);
        HealthHuntPreference.putString(mContext, Constants.SOCIAL_LOGIN, Constants.FACEBOOK);
        ILoginView.onShowProgress();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String token;
        if(accessToken != null && accessToken.getToken() != null) {
            token = accessToken.getToken();
            LoginRequest loginRequest = createLoginRequest(null, null, "facebook", token);
            ILoginInteractor.login(mContext, loginRequest, LoginPresenterImpl.this);
        }
        else {
            ILoginInteractor.loginWithFacebook(context, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // here write code When Login successfully
                    String token = loginResult.getAccessToken().getToken();
                    Log.i("TAG1", "Result " + token);
                    LoginRequest loginRequest = createLoginRequest(null, null, "facebook", token);
                    //ILoginView.onHideProgress();
                    ILoginInteractor.login(mContext, loginRequest, LoginPresenterImpl.this);
                }

                @Override
                public void onCancel() {
                    Log.i("TAG1", "onCancel ");
                    ILoginView.onHideProgress();
                }

                @Override
                public void onError(FacebookException e) {
                    ILoginView.onHideProgress();
                    Log.i("TAG1", "Facebook Error " + e);
                }
            });
        }
    }

    @Override
    public void onSuccess(User user) {
        ILoginView.onHideProgress();
        updateUserSession();

        if(!isUserExit(user)){
            Log.i("TAGIDUSER", "ID NEW" + user.getUserId());
            storeUserInfo(user);
            ILoginView.startTagActivity();
        }
        else {
            User savedUser = User.getUser(user.getUserId());
            String tagList = savedUser.getTagList();
            String continueList = savedUser.getContinueList();
            User.removeUser(savedUser.getUserId());



            user.setTagList(tagList);
            user.setContinueList(continueList);
            user.setCurrentLogin(true);
            user.save();

            Log.i("TAGIDUSER", "ID Already" + user.getUserId());

            if (!isTagAvailabel(user)) {
                ILoginView.startTagActivity();
            }
            else {
                ILoginView.startHomeActivity();
            }
        }

        /*Set<String> tags = HealthHuntPreference.getSet(mContext, Constants.SELECTED_TAGS_KEY);
        if(tags != null && !tags.isEmpty()) {
            HealthHuntPreference.putBoolean(mContext, Constants.IS_LOGIN_FIRST_KEY, true);

        }
        else {

        }*/
    }

    private boolean isUserExit(User user){
        User tempUser = User.getUser(user.getUserId());
        if(tempUser != null){
            return true;
        }
        return false;
    }

    private boolean isTagAvailabel(User user){
        if(user.getTagList() != null && !user.getTagList().isEmpty()){
            return true;
        }
        return false;
    }

    private void storeUserInfo(User user) {
        user.setCurrentLogin(true);
        user.save();
        // Log.i("TAG", "IMAGE " + user.getUser_image() + " ID " + user.getId());
        //Log.i("TAG", "URL " + user.getUrl());
       /* HealthHuntPreference.putString(mContext, Constants.USER_ID, String.valueOf(user.getId()));
        HealthHuntPreference.putString(mContext, Constants.USER_NAME, String.valueOf(user.getName()));
        HealthHuntPreference.putString(mContext, Constants.USER_URL, String.valueOf(user.getUrl()));*/
    }

    private void updateUserSession() {
        List<User> userList = User.getAllUser();
        for(User user: userList){
            user.setCurrentLogin(false);
            user.save();
        }
    }

    @Override
    public void onError(RestError errorInfo) {
        ILoginView.onHideProgress();
        HealthHuntPreference.clear(mContext);
        if(errorInfo != null) {
            ILoginView.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onNewUserSuccess(HHResponse<UserData> user) {
        ILoginView.onHideProgress();
       // storeUserInfo(user.getData().getUser());
        ILoginView.showSignUpSuccessAlert(user.getMessage());
    }

    @Override
    public void loginGoogle(String social_token) {
        HealthHuntPreference.putBoolean(mContext, Constants.IS_LOGIN_NORMAL, false);
        HealthHuntPreference.putString(mContext, Constants.SOCIAL_LOGIN, Constants.GOOGLE);
        ILoginView.onShowProgress();
        LoginRequest loginRequest = createLoginRequest(null,null, "google", social_token);
        ILoginInteractor.login(mContext, loginRequest, this);
    }

    /*@Override
    public void alreadyLogin() {

        boolean isNormalLogin = HealthHuntPreference.getBoolean(mContext, Constants.IS_LOGIN_NORMAL);

        if(isNormalLogin) {
            String email = HealthHuntPreference.getString(mContext, Constants.EMAIL);
            String password = HealthHuntPreference.getString(mContext, Constants.PASSWORD);
            validateCredentialsLogIn(email, password);
        }
        else {
            String socialLogin = HealthHuntPreference.getString(mContext, Constants.SOCIAL_LOGIN);
            if(Constants.FACEBOOK.equalsIgnoreCase(socialLogin)){
                loginFacebook(mContext);
            }
            else {
                Intent intent = new Intent(mContext, GoogleLoginActivity.class);
                if(intent != null) {
                    mActivity.startActivityForResult(intent, Constants.GMAIL_REQUEST_CODE);
                }
            }
        }
    }*/


    @Override
    public void onChangePassword(boolean status, String email, String msg) {
        ILoginView.onHideProgress();
        Spannable spannable = new SpannableString(msg);
        Log.i("TAGWW", "Email " + email);
        if(status) {
            String linkSent = mContext.getResources().getString(R.string.link_sent);
            msg = linkSent + " " + email;
            spannable = new SpannableString(msg);
            spannable.setSpan(new ForegroundColorSpan(Color.BLACK),
                    linkSent.length() + 1 , msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        ILoginView.showPasswordChangeAlert(spannable);
    }

    private SignUpRequest createSignUpRequest(String username, String gender, String email, String password) {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setmName(username);
        signUpRequest.setmGender(gender);
        signUpRequest.setmEmail(email);
        signUpRequest.setmPassword(password);
        return signUpRequest;
    }

    private LoginRequest createLoginRequest(String email, String password, String social, String social_token) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setmEmail(email);
        loginRequest.setmPassword(password);
        loginRequest.setmSocialNetwork(social);
        loginRequest.setmSocialToken(social_token);
        return loginRequest;
    }

    private ForgotPasswordRequest createForgotPasswordRequest(String email, String userName) {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setmEmail(email);
        forgotPasswordRequest.setmUsername(userName);
        return forgotPasswordRequest;
    }
}
