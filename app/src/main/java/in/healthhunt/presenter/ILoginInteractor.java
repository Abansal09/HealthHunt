package in.healthhunt.presenter;

import android.content.Context;
import android.content.Intent;

import com.facebook.FacebookCallback;
import com.facebook.login.LoginResult;

import framework.retrofit.RestError;
import in.healthhunt.model.beans.LoginRequest;
import in.healthhunt.model.beans.SignUpRequest;

/**
 * Created by abhishekkumar on 4/9/18.
 */

public interface ILoginInteractor {

    interface OnLoginFinishListener {
        void onSuccess();
        void onError(RestError errorInfo);
    }

    void login(LoginRequest loginRequest, OnLoginFinishListener loginFinishListener);
    String loginWithFacebook(Context context, FacebookCallback<LoginResult> facebookCallback);
    Intent loginWithGmail(Context context);
    void signUp(SignUpRequest signUpRequest, OnLoginFinishListener onLoginFinishListener);
}
