package in.healthhunt.view.socialLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

import in.healthhunt.R;
import io.fabric.sdk.android.Fabric;

public class GoogleLoginActivity extends AppCompatActivity {
    GoogleApiClient mGoogleApiClient = null;
    public static final int GOOGLE_LOGIN_REQUEST_CODE = 1;
    public static final int GOOGLE_LOGIN_RESPONSE_OK = 2;
    public static final int GOOGLE_LOGIN_RESPONSE_FAIL = 3;

    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        initializeGoogleLogin();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        mProgress = new ProgressDialog(this);
        mProgress.setIndeterminate(true);
        mProgress.setMessage(getResources().getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.show();
        startActivityForResult(signInIntent, GOOGLE_LOGIN_REQUEST_CODE);
    }


    private void initializeGoogleLogin() {
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getResources().getString(R.string.server_client_id))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestServerAuthCode(getResources().getString(R.string.server_client_id))
                .requestIdToken(getResources().getString(R.string.server_client_id))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();




    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mProgress.dismiss();
        if (data == null) {
            finish();
            return;
        }

        if (requestCode == GOOGLE_LOGIN_REQUEST_CODE) {

            String authCode =  handleSignInResult(data);

            if(authCode == null) {
                setResult(GOOGLE_LOGIN_RESPONSE_FAIL);
            } else {
                Intent userIntent = new Intent();
                userIntent.putExtra("authCode", authCode);
                setResult(GOOGLE_LOGIN_RESPONSE_OK,userIntent);
            }

        }
        finish();
    }

    private String handleSignInResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        if (task.isSuccessful()) {
            // Signed in successfully, show authenticated UI.

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                return account.getServerAuthCode();


            } catch (ApiException e) {
                return null;
            }
            //checking if photo is not null
        }
        return null;
    }

    /*private String handleSignInResult(Intent data) {

        if (task.isSuccessful()) {
            // Signed in successfully, show authenticated UI.

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
               return account.getServerAuthCode();


            } catch (ApiException e) {
                return null;
            }
            //checking if photo is not null
        }
        return null;
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        /*OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            GoogleSignInResult result = opr.get();
            Log.i("TAGSTART", "Got cachec " + result.getSignInAccount().getServerAuthCode());

        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            mProgress.show();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    mProgress.dismiss();
                    Log.i("TAGSTART", "After Got cachec " + googleSignInResult.getSignInAccount().getServerAuthCode());
                }
            });
        }*/
    }
}

