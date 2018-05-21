package in.healthhunt.presenter.homeScreenPresenter.shopPresenter;

import android.content.Context;

import java.util.Map;

import framework.retrofit.RestError;
import in.healthhunt.presenter.homeScreenPresenter.IBookMarkInteractor;

/**
 * Created by abhishekkumar on 5/6/18.
 */

public interface IShopInteractor extends IBookMarkInteractor {

    interface OnFinishListener {
        void onSuccess();
        void onError(RestError errorInfo);
    }
    void fetchShop(Context context, Map<String, String> queryMap, OnFinishListener tagLoadFinishListener);
}