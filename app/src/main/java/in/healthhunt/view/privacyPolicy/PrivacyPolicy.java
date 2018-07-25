package in.healthhunt.view.privacyPolicy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.healthhunt.R;
import in.healthhunt.view.homeScreenView.HomeActivity;
import in.healthhunt.view.homeScreenView.IHomeView;

/**
 * Created by abhishekkumar on 6/27/18.
 */

public class PrivacyPolicy extends Fragment {

    @BindView(R.id.webView)
    WebView mPrivacyPolicy;

    private IHomeView IHomeView;
    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity() instanceof HomeActivity) {
            IHomeView = (IHomeView) getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, null);
        mUnbinder = ButterKnife.bind(this, view);
        if(IHomeView != null) {
            IHomeView.updateTitle(getString(R.string.privacy_policy));
        }
        openPrivacyPolicy();
        return view;
    }

    private void openPrivacyPolicy() {
        WebSettings webSetting = mPrivacyPolicy.getSettings();
        //webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);

        mPrivacyPolicy.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // open in Webview
                if (url.contains("android_asset") ){
                    // Can be clever about it like so where myshost is defined in your strings file
                    // if (Uri.parse(url).getHost().equals(getString(R.string.myhost)))
                    return false;
                }

                if(url.contains("mailto")){
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SENDTO);
                    sendIntent.setData(Uri.parse(url));
                    startActivity(Intent.createChooser(sendIntent, getString(R.string.email)));
                }
                else {
                    // open rest of URLS in default browser
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(Intent.createChooser(intent, getString(R.string.browser)));
                }
                return true;
            }
        });
        mPrivacyPolicy.loadUrl("file:///android_asset/privacy_policy.html");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
    }
}
