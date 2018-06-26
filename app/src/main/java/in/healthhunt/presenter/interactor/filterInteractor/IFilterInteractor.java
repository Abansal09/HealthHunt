package in.healthhunt.presenter.interactor.filterInteractor;

import android.content.Context;

import java.util.List;
import java.util.Map;

import framework.retrofit.RestError;
import in.healthhunt.model.filter.DataItem;

/**
 * Created by abhishekkumar on 5/6/18.
 */

public interface IFilterInteractor {

    interface OnFinishListener {
        void onSuccess(String type, List<DataItem> dataItems);
        void onFilterError(RestError errorInfo);
    }
    void fetchFilters(Context context, String type, Map<String, String> queryMap, OnFinishListener finishListener);
}
