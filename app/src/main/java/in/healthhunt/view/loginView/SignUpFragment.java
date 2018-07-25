package in.healthhunt.view.loginView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.healthhunt.R;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.loginPresenter.ILoginPresenter;
import in.healthhunt.view.privacyPolicy.PrivacyPolicy;
import in.healthhunt.view.termAndConditions.TermsAndConditions;

/**
 * Created by abhishekkumar on 4/9/18.
 */

public class SignUpFragment extends Fragment {
    private final String TAG = SignUpFragment.class.getSimpleName();
    @BindView(R.id.sign_up)
    Button mSignUp;

    @BindView(R.id.sign_in)
    TextView mSignIn;

    @BindView(R.id.facebook)
    ImageButton mFacebook;

    @BindView(R.id.gmail)
    ImageButton mGmail;

    @BindView(R.id.email)
    EditText mEmail;

    @BindView(R.id.username)
    EditText mUsername;

    @BindView(R.id.gender)
    EditText mGender;

    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.skip)
    TextView mSkipView;

    @BindView(R.id.term_and_conditions)
    TextView mTermAndConditions;

    @BindView(R.id.login_view)
    LinearLayout mLoginView;

   /* @BindView(R.id.child_frame_layout)
    FrameLayout mChildFrame;
*/
    private ListPopupWindow mListPopupWindow;
    private ILoginPresenter IPresenter;
    private Unbinder unbinder;
    private int isLoginType;
    private String[] mGenders = {"Note Specified", "Male", "Female"};
    private ILoginView ILoginView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container,  false);
        unbinder = ButterKnife.bind(this, view);
        mSkipView.setVisibility(View.GONE);
        ILoginView = (ILoginView) getActivity();
        clearFields();
        addLink();
        addGenderWindow();
        addKeyboardDoneListener();
        return view;
    }

    public void clearFields() {
        mUsername.setText("");
        mEmail.setText("");
        mPassword.setText("");
        mGender.setText("");
    }

    private void addKeyboardDoneListener() {
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                ILoginView.hideKeyboardIfOpen();
                onSignUp();
                return false;
            }
        });
    }


    private void addGenderWindow() {

        mListPopupWindow = new ListPopupWindow(getContext());
        mListPopupWindow.setAdapter(new ArrayAdapter(getContext(), R.layout.gender_list_item_view, mGenders));


        mListPopupWindow.setAnchorView(mGender);
        mListPopupWindow.setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        width = width - (HealthHuntUtility.dpToPx(32, getContext()));
        mListPopupWindow.setWidth(width);
        mListPopupWindow.setHeight(HealthHuntUtility.dpToPx(144, getContext()));


        //mListPopupWindow.setModal(true);
        mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                mGender.setText(mGenders[pos]);
                mListPopupWindow.dismiss();
            }
        });

        mGender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mListPopupWindow.show();
                return false;
            }
        });

        mGender.setShowSoftInputOnFocus(false);
    }

    private void addLink() {

        String str = getString(R.string.terms_and_conditions_details);
        String terms = getString(R.string.terms_and_conditions);
        String privacy = getString(R.string.privacy_policy);
        makeLinkClickable(terms, privacy, str);

       /* String part1 = getString(R.string.terms_and_conditions_part1);
        String part2 = getString(R.string.terms_and_conditions_part2);
        String part3 = getString(R.string.privacy_policy);
        String str = part1 + " " + part2 + " and " + part3;

        Spannable spannable = new SpannableString(str);

        int len = part1.length() + 1  + part2.length();
        spannable.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.hh_text_color)),
                part1.length() + 1 , len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        len = part1.length() + 1 + part2.length() + " and ".length();
        spannable.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getContext(), R.color.hh_text_color)),
                len , str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTermAndConditions.setText(spannable, TextView.BufferType.SPANNABLE);*/
    }

    protected void makeLinkClickable(String terms, String privacy, String str)
    {
        int termStart = str.indexOf(terms);
        int termEnd = termStart + terms.length();
        Spannable spannable = new SpannableString(str);

        ClickableSpan termClickable = new ClickableSpan() {
            public void onClick(View view) {
                Log.i("TAGTAG","terms onClick");
                //loadChildFragment(TermsAndConditions.class.getSimpleName(), null);
                ILoginView.loadBackStackFragment(TermsAndConditions.class.getSimpleName(), null);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getContext(), R.color.hh_text_color));
                //ds.setUnderlineText(false);
            }
        };
        spannable.setSpan(termClickable, termStart, termEnd, 0);


        int privacyStart = str.indexOf(privacy);
        int privacyEnd = privacyStart + privacy.length();


        ClickableSpan privacyClickable = new ClickableSpan() {
            public void onClick(View view) {
                Log.i("TAGTAG","privacy onClick");

                //loadChildFragment(PrivacyPolicy.class.getSimpleName(), null);
                ILoginView.loadBackStackFragment(PrivacyPolicy.class.getSimpleName(), null);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(getContext(), R.color.hh_text_color));
                //ds.setUnderlineText(false);
            }
        };

        spannable.setSpan(privacyClickable, privacyStart, privacyEnd, 0);

        mTermAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
        mTermAndConditions.setText(spannable);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IPresenter = ((LoginActivity) context).getPresenter();
    }

    @OnClick(R.id.sign_up)
    void onSignUp() {
        // mLoginView.clearFocus();
        isLoginType = LoginActivity.LOGIN_TYPE_NORMAL;
        IPresenter.validateCredentialsSignUp(mUsername.getText().toString(), mGender.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString());
    }

    @OnClick(R.id.sign_in)
    void OnSignIn(){
        IPresenter.loadFragment(LoginFragment.class.getSimpleName(), null);
    }

    @OnClick(R.id.facebook)
    void onFacebook() {
        isLoginType = LoginActivity.LOGIN_TYPE_FACEBOOK;
        IPresenter.loginFacebook(getContext());
    }

    @OnClick(R.id.gmail)
    void onGmail() {
        isLoginType = LoginActivity.LOGIN_TYPE_GMAIL;
        Intent intent = IPresenter.loginGoogle(getContext());
        if(intent != null) {
            startActivityForResult(intent, Constants.GMAIL_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("TAG", "requestCode " + requestCode);

        if (requestCode == Constants.GMAIL_REQUEST_CODE) {
            // [START get_auth_code]
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String authCode = account.getIdToken();
                IPresenter.loginGoogle(authCode);

                Log.d(TAG, "AuthCode = " + authCode);
                // TODO(developer): send code to server and exchange for access/refresh/ID tokens
            } catch (ApiException e) {
                ((LoginActivity)getActivity()).onHideProgress();
                e.printStackTrace();
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
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

    /*public void loadChildFragment(String tag, Bundle bundle) {
        Fragment fragment = null;

        if (tag != null && tag.equals(TermsAndConditions.class.getSimpleName())) {
            fragment = new TermsAndConditions();
        }
        else if (tag != null && tag.equals(PrivacyPolicy.class.getSimpleName())) {
            fragment = new PrivacyPolicy();
        }

        fragment.setArguments(bundle);
        showChildFragment(fragment, tag);
        mChildFrame.setVisibility(View.VISIBLE);
    }*/

    /*private void showChildFragment(Fragment fragment, String tag) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.child_frame_layout, fragment);
        //fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }

    public boolean onBackPressed(){

        int count = getChildFragmentManager().getBackStackEntryCount();
        Log.i("TAGCOUNTProfile", "SignUp Count " + count);

        if(count > 0){
            mChildFrame.setVisibility(View.GONE);
            getChildFragmentManager().popBackStack();
            return true;
        }
        return false;
    }*/
}
