package in.healthhunt.view.profileView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.healthhunt.R;
import in.healthhunt.view.homeScreenView.IHomeView;
import in.healthhunt.view.privacyPolicy.PrivacyPolicy;
import in.healthhunt.view.termAndConditions.TermsAndConditions;

/**
 * Created by abhishekkumar on 7/4/18.
 */

public class LegalOtherFragment extends Fragment {

   /* @BindView(R.id.child_frame_layout)
    FrameLayout mChildFrame;
*/
    private IHomeView IHomeView;
    private Unbinder mUnbinder;
    //private FragmentManager mChildFragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IHomeView = (IHomeView) getActivity();
        //mChildFragmentManager = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_legal_others, null);
        mUnbinder = ButterKnife.bind(this, view);
        IHomeView.updateTitle(getString(R.string.legal_other));
        return view;
    }

    @OnClick(R.id.terms_and_conditions_view)
    void onTermsConditions(){
        //loadChildFragment(TermsAndConditions.class.getSimpleName(), null);
        IHomeView.loadNonFooterFragment(TermsAndConditions.class.getSimpleName(), null);
    }

    @OnClick(R.id.privacy_policy_view)
    void onPrivacyPolicy(){
        //loadChildFragment(PrivacyPolicy.class.getSimpleName(), null);
        IHomeView.loadNonFooterFragment(PrivacyPolicy.class.getSimpleName(), null);
    }

    @OnClick(R.id.quality_content_guidelines_view)
    void onQualityContent(){
        //loadChildFragment(QualityContentGuidelines.class.getSimpleName(), null);
        IHomeView.loadNonFooterFragment(QualityContentGuidelines.class.getSimpleName(), null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
    }

    /*public void loadChildFragment(String fragmentName, Bundle bundle) {
        mChildFrame.setVisibility(View.VISIBLE);

        Fragment fragment = null;

        if(fragmentName.equalsIgnoreCase(TermsAndConditions.class.getSimpleName())){
            fragment = new TermsAndConditions();
        }
        else if(fragmentName.equalsIgnoreCase(PrivacyPolicy.class.getSimpleName())){
            fragment = new PrivacyPolicy();
        }
        else if(fragmentName.equalsIgnoreCase(QualityContentGuidelines.class.getSimpleName())){
            fragment = new QualityContentGuidelines();
        }

        fragment.setArguments(bundle);
        showChildFragment(fragment, fragmentName);
    }

    private void showChildFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.child_frame_layout, fragment);
        fragmentTransaction.addToBackStack(tag);
        //getChildFragmentManager().executePendingTransactions();
        fragmentTransaction.commit();
    }*/

    /*public boolean onBackPressed(){

        int count = mChildFragmentManager.getBackStackEntryCount();
        Log.i("TAGCOUNTProfile", "Legal Count " + count);

        if(count > 0){
            mChildFragmentManager.popBackStackImmediate();
            mChildFrame.setVisibility(View.GONE);
            IHomeView.updateTitle(getString(R.string.legal_other));
            return true;
        }

        return false;
    }*/
}
