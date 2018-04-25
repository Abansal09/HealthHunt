package in.healthhunt.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import in.healthhunt.model.Constants;
import in.healthhunt.view.IOnBoardingView;

/**
 * Created by abhishekkumar on 4/24/18.
 */

public class OnBoardingPresenter implements IOnBoardingPresenter{

    private Context mContext;
    private IOnBoardingView IOnBoardingView;

    public OnBoardingPresenter(Context context, IOnBoardingView boardingView){
        mContext =  context;
        IOnBoardingView = boardingView;
    }

    @Override
    public int getCount() {
        return IOnBoardingView.getCount();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = IOnBoardingView.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.VIEWPAGER_FRAGMENT_NO_KEY, position);
        fragment.setArguments(bundle);
        return fragment;
    }
}
