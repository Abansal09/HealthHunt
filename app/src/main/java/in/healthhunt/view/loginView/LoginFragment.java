package in.healthhunt.view.loginView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.healthhunt.R;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.loginPresenter.ILoginPresenter;
import in.healthhunt.view.socialLogin.GoogleLoginActivity;

/**
 * Created by abhishekkumar on 4/9/18.
 */

public class LoginFragment extends Fragment{
    private final String TAG = LoginFragment.class.getSimpleName();
    @BindView(R.id.login)
    Button mLogin;

    @BindView(R.id.sign_up)
    TextView mSignUp;

    @BindView(R.id.forgot_password)
    TextView mForgot_Password;

    @BindView(R.id.facebook)
    ImageButton mFacebook;

    @BindView(R.id.gmail)
    ImageButton mGmail;

    @BindView(R.id.email)
    EditText mEmail;

    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.login_view)
    LinearLayout mLoginView;

    private ILoginPresenter IPresenter;
    private Unbinder unbinder;
    private int isLoginType;
    private ILoginView ILoginView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container,  false);
        unbinder = ButterKnife.bind(this, view);
        ILoginView = (ILoginView) getActivity();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IPresenter = ((LoginActivity) context).getPresenter();
    }

    @OnClick(R.id.login)
    void onLogin() {
        //clearFocus();
        mLoginView.clearFocus();;

        if(HealthHuntUtility.checkInternetConnection(getContext())) {
            isLoginType = LoginActivity.LOGIN_TYPE_NORMAL;
            IPresenter.validateCredentialsLogIn(mEmail.getText().toString(), mPassword.getText().toString());
        }
        else {
            ILoginView.showAlert(getString(R.string.please_check_internet_connectivity_status));
        }
        //startActivity(new Intent(getActivity(), HomeActivity.class));
    }

    private void clearFocus() {
        mEmail.clearFocus();
        mPassword.clearFocus();
    }

    @OnClick(R.id.sign_up)
    void onSignUp() {

        if(HealthHuntUtility.checkInternetConnection(getContext())) {
            IPresenter.loadFragment(SignUpFragment.class.getSimpleName(), null);
        }
        else {
            ILoginView.showAlert(getString(R.string.please_check_internet_connectivity_status));
        }
    }

    @OnClick(R.id.forgot_password)
    void onForgotPassword(){

        if(HealthHuntUtility.checkInternetConnection(getContext())) {
            IPresenter.forgotPassword(mEmail.getText().toString(), mEmail.getText().toString());
        }
        else {
            ILoginView.showAlert(getString(R.string.please_check_internet_connectivity_status));
        }
        /*Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL, mEmail.getText().toString());
        IPresenter.loadFragment(ForgotPasswordFragment.class.getSimpleName(), bundle);*/
    }

    @OnClick(R.id.facebook)
    void onFacebook() {

        if(HealthHuntUtility.checkInternetConnection(getContext())) {
            isLoginType = LoginActivity.LOGIN_TYPE_FACEBOOK;
            IPresenter.loginFacebook(getContext());
        }
        else {
            ILoginView.showAlert(getString(R.string.please_check_internet_connectivity_status));
        }
    }

    @OnClick(R.id.gmail)
    void onGmail() {

        if(HealthHuntUtility.checkInternetConnection(getContext())) {
            isLoginType = LoginActivity.LOGIN_TYPE_GMAIL;
            Intent intent = new Intent(getContext(), GoogleLoginActivity.class);
            if (intent != null) {
                getActivity().startActivityForResult(intent, Constants.GMAIL_REQUEST_CODE);
            }
        }
        else {
            ILoginView.showAlert(getString(R.string.please_check_internet_connectivity_status));
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TAG", "requestCode " + requestCode);

        /*if (requestCode == Constants.GMAIL_REQUEST_CODE) {
            // [START get_auth_code]
            if(resultCode == GOOGLE_LOGIN_RESPONSE_OK) {
                String authID = data.getStringExtra("authCode");
                IPresenter.loginGoogle(authID);
                Log.i("TAG", "authCode = " + authID);
            }
        }*/
        // [END get_auth_code]
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void tryAgain() {
        switch (isLoginType){
            case LoginActivity.LOGIN_TYPE_NORMAL:
                IPresenter.validateCredentialsLogIn(mEmail.getText().toString(), mPassword.getText().toString());
                break;
            case LoginActivity.LOGIN_TYPE_FACEBOOK:
                IPresenter.loginFacebook(getContext());
                break;
            case LoginActivity.LOGIN_TYPE_GMAIL:
                break;


        }
    }
}
