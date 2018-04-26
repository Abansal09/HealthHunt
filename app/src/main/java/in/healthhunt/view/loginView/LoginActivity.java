package in.healthhunt.view.loginView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import in.healthhunt.R;
import in.healthhunt.presenter.loginPresenter.ILoginPresenter;
import in.healthhunt.presenter.loginPresenter.LoginInteractorImpl;
import in.healthhunt.presenter.loginPresenter.LoginPresenterImpl;
import in.healthhunt.presenter.loginPresenter.Facebook;

/**
 * Created by abhishekkumar on 4/9/18.
 */

public class LoginActivity extends AppCompatActivity implements ILoginView{

    private final String TAG = LoginActivity.class.getSimpleName();
    private ILoginPresenter IPresenter;
    private ProgressDialog mProgress;


    private Map<String,Fragment> fragmentMap = new HashMap<String, Fragment>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgress = new ProgressDialog(this);
        mProgress.setIndeterminate(true);
        mProgress.setMessage(getResources().getString(R.string.please_wait));

        ButterKnife.bind(this);

        IPresenter = new LoginPresenterImpl(getApplicationContext(), this, new LoginInteractorImpl());
        IPresenter.loadFragment(LoginFragment.class.getSimpleName(), null);

    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        if(ForgotPasswordFragment.class.getSimpleName().equals(tag)) { // Needed when user press the back button on forgot password screen
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }

    public ILoginPresenter getPresenter() {
        return IPresenter;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onShowProgress() {
        if(mProgress != null) {
            mProgress.show();
        }
    }

    @Override
    public void onHideProgress() {
        if(mProgress != null) {
            mProgress.cancel();
        }
    }

    @Override
    public void onEmailError() {

    }

    @Override
    public void onPasswordError() {

    }

    @Override
    public void showFragment(String tag, Bundle bundle) {
        Fragment fragment = fragmentMap.get(tag);

        if(fragment == null) {
            if (tag != null && tag.equals(LoginFragment.class.getSimpleName())) {
                fragment = new LoginFragment();
            } else if (tag != null && tag.equals(SignUpFragment.class.getSimpleName())) {
                fragment = new SignUpFragment();
            } else if (tag != null && tag.equals(ForgotPasswordFragment.class.getSimpleName())) {
                fragment = new ForgotPasswordFragment();
            }
            fragmentMap.put(tag, fragment);
        }
        fragment.setArguments(bundle);
        loadFragment(fragment, tag);
    }

    @Override
    public void showPasswordChangeAlert(Spannable  spannable) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alertdialog_view);

        TextView message = dialog.findViewById(R.id.alert_message);
        message.setText(spannable, TextView.BufferType.SPANNABLE);

        TextView title = dialog.findViewById(R.id.alert_title);
        title.setVisibility(View.GONE);

        Button okButton = dialog.findViewById(R.id.action_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showLoginAlert(String msg) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.alertdialog_view);

        TextView message = dialog.findViewById(R.id.alert_message);
        message.setText(msg);

        String str = getResources().getString(R.string.could_not_log_in);
        TextView title = dialog.findViewById(R.id.alert_title);
        title.setText(str);

        str = getResources().getString(R.string.try_again);
        Button okButton = dialog.findViewById(R.id.action_button);
        okButton.setText(str);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private View getAlertView() {
        return getLayoutInflater().inflate(R.layout.alertdialog_view, null, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Facebook.getInstance(
                getApplicationContext()).getCallbackManager().onActivityResult(requestCode, resultCode, data);
        Log.i("TAGActivity", "Facebook token " + requestCode);
    }
}