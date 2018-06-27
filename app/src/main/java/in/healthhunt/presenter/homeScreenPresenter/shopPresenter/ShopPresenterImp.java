package in.healthhunt.presenter.homeScreenPresenter.shopPresenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.retrofit.RestError;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkData;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkInfo;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.filter.DataItem;
import in.healthhunt.presenter.interactor.bookMarkInteractor.BookMarkInteractorImpl;
import in.healthhunt.presenter.interactor.bookMarkInteractor.IBookMarkInteractor;
import in.healthhunt.presenter.interactor.filterInteractor.FilterInteractorImpl;
import in.healthhunt.presenter.interactor.filterInteractor.IFilterInteractor;
import in.healthhunt.presenter.interactor.productInteractor.IProductInteractor;
import in.healthhunt.presenter.interactor.productInteractor.ProductInteractorImpl;
import in.healthhunt.view.homeScreenView.shopView.IShopView;
import in.healthhunt.view.homeScreenView.shopView.ShopViewHolder;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class ShopPresenterImp implements IShopPresenter, IProductInteractor.OnViewAllFinishListener,
        IBookMarkInteractor.OnFinishListener, IFilterInteractor.OnFinishListener{

    private String TAG = ShopPresenterImp.class.getSimpleName();
    private IProductInteractor IProductInteractor;
    private IBookMarkInteractor IBookMarkInteractor;
    private IFilterInteractor IFilterInteractor;
    private Context mContext;
    private IShopView IShopView;
    private List<ProductPostItem> mProductPosts;
    private List<DataItem> mFilterProductItems;
    private List<DataItem> mFilterBrandItems;

    public ShopPresenterImp(Context context, IShopView shopView) {
        mContext =  context;
        IProductInteractor = new ProductInteractorImpl();
        IBookMarkInteractor = new BookMarkInteractorImpl();
        IFilterInteractor = new FilterInteractorImpl();
        IShopView = shopView;
    }

    @Override
    public void onProductSuccess(List<ProductPostItem> items) {
        if(mProductPosts != null){
            mProductPosts.clear();
        }

        mProductPosts = items;
        IShopView.hideProgress();
        IShopView.updateAdapter();
    }

    @Override
    public void onBookMarkSuccess(BookMarkData markResponse) {
        IShopView.hideProgress();
        for(int i = 0; i< mProductPosts.size(); i++) {
            ProductPostItem postItem = mProductPosts.get(i);
            BookMarkInfo bookMarkInfo = markResponse.getBookMarkInfo();
            if(bookMarkInfo.getPost_id().equals(String.valueOf(postItem.getProduct_id()))) {
                CurrentUser currentUser = postItem.getCurrent_user();
                if (currentUser != null) {
                    currentUser.setBookmarked(bookMarkInfo.isBookMark());
                    IShopView.updateProductSaved(postItem);
                }
                break;
            }
        }
        IShopView.updateAdapter();
    }

    @Override
    public void onBookMarkError(RestError errorInfo) {
        IShopView.hideProgress();
        if(errorInfo != null){
            IShopView.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onSuccess(String type, List<DataItem> dataItems) {
        if(type.equals(ArticleParams.PRODUCT_TYPE)){
            mFilterProductItems = dataItems;
        }
        else if(type.equals(ArticleParams.BRANDS)) {
            mFilterBrandItems = dataItems;
        }
    }

    @Override
    public void onFilterError(RestError errorInfo) {
        IShopView.hideProgress();
        if(errorInfo != null){
            IShopView.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onError(RestError errorInfo) {
        IShopView.hideProgress();
        if(errorInfo != null){
            IShopView.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public int getCount() {
        int count = 0;
        if(mProductPosts != null){
            count = mProductPosts.size();
        }
        return count;
    }

    @Override
    public void bookmark(String id, int type) {
        IShopView.showProgress();
        IBookMarkInteractor.bookmark(mContext, id, type, this);
    }

    @Override
    public void unBookmark(String id, int type) {
        IShopView.showProgress();
        IBookMarkInteractor.unBookmark(mContext, id, type, this);
    }

    @Override
    public void updateBottomNavigation() {
        IShopView.updateBottomNavigation();
    }

    @Override
    public void showAlert(String msg) {
        IShopView.showAlert(msg);
    }

    @Override
    public ShopViewHolder createViewHolder(View view) {
        return IShopView.createViewHolder(view);
    }

    @Override
    public void fetchMarkets() {
        IShopView.showProgress();
        Map<String, String> map = new HashMap<String, String>();
        map.put(ArticleParams.TYPE, ArticleParams.MARKET);
        map.put(ArticleParams.MARKT_TYPE, String.valueOf(ArticleParams.PRODUCT_SERVICES));
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(0));
        map.put(ArticleParams.LIMIT, String.valueOf(30));
        IProductInteractor.fetchAllProduct(mContext, map, this);
    }

    @Override
    public void fetchMarketFilters(Map<Integer, List<String>> listMap) {
        IShopView.showProgress();
        Map<String, String> map = new HashMap<String, String>();
        map.put(ArticleParams.TYPE, ArticleParams.MARKET);
        map.put(ArticleParams.MARKT_TYPE, String.valueOf(ArticleParams.PRODUCT_SERVICES));
        // map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(0));
        map.put(ArticleParams.LIMIT, String.valueOf(30));

        List<String> productList = listMap.get(Constants.PRODUCT_FILTER);
        if(productList != null && !productList.isEmpty()) {
            String productIds = "";
            for (int i = 0; i < productList.size(); i++) {
                String productID = productList.get(i);
                if (i < productList.size() - 1) {
                    productIds = productIds + productID + ",";
                } else {
                    productIds = productIds + productID;
                }
            }
            Log.i("TAGFILTERVA", " Product IDs "+ productIds);
            map.put(ArticleParams.PRODUCT_TYPE_ID, productIds);
        }

        List<String> location = listMap.get(Constants.CITY_FILTER);
        if(location != null && !location.isEmpty()){
            String city = location.get(0);

            if(city.contains(",")){
                //int index = city.indexOf(",");
                //city = city.substring(0, index);
                city = city.replace(", ","+");
                Log.i("TAGFILTERVA", " city "+ city);
            }

            if(city != null){
                city.trim();
            }
            map.put(ArticleParams.LOCATION, city);
        }

        List<String> brandList = listMap.get(Constants.BRAND_FILTER);
        if(brandList != null && !brandList.isEmpty()){
            String ids ="";
            for(int i=0; i<brandList.size(); i++){
                String brandID = brandList.get(i);
                if(i < brandList.size() - 1){
                    ids = ids + brandID + ",";
                }
                else {
                    ids = ids + brandID;
                }
            }

            Log.i("TAGFILTERVA", " Brand IDs "+ ids);
            map.put(ArticleParams.BRAND, ids);
        }

        IProductInteractor.fetchAllProduct(mContext, map, this);

    }

    @Override
    public ProductPostItem getProduct(int pos) {

        ProductPostItem postItem = null;

        if(mProductPosts != null){
            postItem = mProductPosts.get(pos);
        }
        return postItem;
    }

    @Override
    public List<ProductPostItem> getAllProducts() {
        return mProductPosts;
    }

    @Override
    public void fetchFilters(String type) {
        IShopView.showProgress();
        Map<String,String> map = new HashMap<String, String>();
        map.put(ArticleParams.FILTER_TYPE, type);
        IFilterInteractor.fetchFilters(mContext, type,map,  this);
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
    public void loadFragment(String fragmentName, Bundle bundle) {
        IShopView.loadFragment(fragmentName, bundle);
    }
}
