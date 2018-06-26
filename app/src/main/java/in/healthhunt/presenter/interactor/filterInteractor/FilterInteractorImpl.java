package in.healthhunt.presenter.interactor.filterInteractor;

import android.content.Context;

import java.util.Map;

import framework.retrofit.ResponseResolver;
import framework.retrofit.RestError;
import framework.retrofit.WebServicesWrapper;
import in.healthhunt.model.filter.FilterData;
import retrofit2.Response;

/**
 * Created by abhishekkumar on 5/6/18.
 */

public class FilterInteractorImpl implements IFilterInteractor {

    @Override
    public void fetchFilters(Context context, final String type, Map<String, String> queryMap, final OnFinishListener finishListener) {
        WebServicesWrapper.getInstance(context).fetchFilters(queryMap, new ResponseResolver<FilterData>() {
            @Override
            public void onSuccess(FilterData filterData, Response response) {
                finishListener.onSuccess(type, filterData.getData());
            }

            @Override
            public void onFailure(RestError error, String msg) {
                finishListener.onFilterError(error);
            }
        });
    }
}
