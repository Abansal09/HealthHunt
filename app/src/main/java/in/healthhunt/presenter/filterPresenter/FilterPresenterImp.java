package in.healthhunt.presenter.filterPresenter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.retrofit.RestError;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.filter.DataItem;
import in.healthhunt.presenter.interactor.filterInteractor.FilterInteractorImpl;
import in.healthhunt.presenter.interactor.filterInteractor.IFilterInteractor;
import in.healthhunt.view.homeScreenView.filterView.IFilterView;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class FilterPresenterImp implements IFilterPresenter, IFilterInteractor.OnFinishListener{

    private String TAG = FilterPresenterImp.class.getSimpleName();
    private IFilterInteractor IFilterInteractor;
    private Context mContext;
    private IFilterView IFilterView;
    private List<ProductPostItem> mProductPosts;
    private List<DataItem> mFilterProductItems;
    private List<DataItem> mFilterBrandItems;
    private List<String> mFilterCityItems;
    private int mFilterCount = 2;
    private String mCity;
    private List<String> mBrands;
    private List<String> mProductTypes;

    public FilterPresenterImp(Context context, IFilterView filterView) {
        mContext =  context;
        IFilterInteractor = new FilterInteractorImpl();
        IFilterView = filterView;
    }


    @Override
    public void onSuccess(String type, List<DataItem> dataItems) {
        mFilterCount--;
        Log.i("TAGTYPE" , "TYPE " + type  + " DATA " + dataItems);
        if(type.equals(ArticleParams.PRODUCT_TYPE)){
            mFilterProductItems = dataItems;
        }
        else if(type.equals(ArticleParams.BRANDS)) {
            mFilterBrandItems = dataItems;
        }

        if(mFilterCount == 0) {
            IFilterView.hideProgress();
        }
    }

    @Override
    public void onFilterError(RestError errorInfo) {
        Log.i("TAGERROR", "ERRO " + errorInfo );
        IFilterView.hideProgress();
        if(errorInfo != null){
            IFilterView.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void fetchFilters(String type) {
        IFilterView.showProgress();
        Map<String,String> map = new HashMap<String, String>();
        map.put(ArticleParams.FILTER_TYPE, type);
        IFilterInteractor.fetchFilters(mContext, type, map,  this);
    }

    @Override
    public int getFilterProductCount() {
        int count = 0;
        if(mFilterProductItems != null){
            count = mFilterProductItems.size();
        }
        return count;
    }

    @Override
    public int getFilterBrandCount() {
        int count = 0;
        if(mFilterBrandItems != null){
            count = mFilterBrandItems.size();
        }
        return count;
    }

    @Override
    public DataItem getProductItem(int pos) {
        return mFilterProductItems.get(pos);
    }

    @Override
    public DataItem getBrandItem(int pos) {
        return mFilterBrandItems.get(pos);
    }

    @Override
    public IFilterView getFilterView() {
        return IFilterView;
    }

    @Override
    public void addProduct(String id) {
        if(mProductTypes == null){
            mProductTypes = new ArrayList<String>();
        }
        if(id != null && !id.isEmpty()) {
            mProductTypes.add(id);
        }
    }

    @Override
    public void removeProduct(String id) {
        mProductTypes.remove(id);
    }

    @Override
    public boolean isProductContain(String id) {
        if(mProductTypes != null && mProductTypes.contains(id)){
            return true;
        }
        return false;
    }

    @Override
    public List<String> getProductList() {
        return mProductTypes;
    }

    @Override
    public void addBrand(String id) {
        if(mBrands == null){
            mBrands = new ArrayList<String>();
        }
        if(id != null && !id.isEmpty()) {
            mBrands.add(id);
        }
    }

    @Override
    public void removeBrand(String id) {
        mBrands.remove(id);
    }

    @Override
    public boolean isBrandContain(String id) {
        if(mBrands != null && mBrands.contains(id)){
            return true;
        }
        return false;
    }

    @Override
    public List<String> getBrandList() {
        return mBrands;
    }

    @Override
    public List<String> getCityList() {
        return mFilterCityItems;
    }

    @Override
    public String getCity() {
        return mCity;
    }

    @Override
    public void setCity(String city) {
        mCity = city;
    }
}
