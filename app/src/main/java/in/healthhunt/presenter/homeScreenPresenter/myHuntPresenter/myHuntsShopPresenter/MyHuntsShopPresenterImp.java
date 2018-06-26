package in.healthhunt.presenter.homeScreenPresenter.myHuntPresenter.myHuntsShopPresenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.retrofit.RestError;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkData;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkInfo;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.presenter.interactor.bookMarkInteractor.BookMarkInteractorImpl;
import in.healthhunt.presenter.interactor.bookMarkInteractor.IBookMarkInteractor;
import in.healthhunt.presenter.interactor.productInteractor.IProductInteractor;
import in.healthhunt.presenter.interactor.productInteractor.ProductInteractorImpl;
import in.healthhunt.view.homeScreenView.myHuntsView.IMyHuntsView;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class MyHuntsShopPresenterImp implements IMyHuntsProductsPresenter, IProductInteractor.OnProductFinishListener,
        IBookMarkInteractor.OnFinishListener, IProductInteractor.OnDeleteFinishListener {

    private String TAG = MyHuntsShopPresenterImp.class.getSimpleName();
    private Context mContext;
    private IMyHuntsView IMyHuntsView;
    private List<ProductPostItem> mProductDownloadList;
    private List<ProductPostItem> mProductSavedList;
    private List<ProductPostItem> mProductApprovedList;
    private List<ProductPostItem> mProductReceivedList;
    private IBookMarkInteractor IBookMarkInteractor;
    private IProductInteractor IProductInteractor;
    private int mProductCount;

    public MyHuntsShopPresenterImp(Context context, IMyHuntsView myHuntsView) {
        mContext =  context;
        IBookMarkInteractor = new BookMarkInteractorImpl();
        IProductInteractor = new ProductInteractorImpl();
        IMyHuntsView = myHuntsView;
    }


    @Override
    public void onBookMarkSuccess(BookMarkData markResponse) {
        IMyHuntsView.hideProgress();
        int type = markResponse.getBookMarkInfo().getType();
        Log.i("TAGBOOK", " TYPE "+ type );
        switch (type) {
            case ArticleParams.PRODUCT:
                for (int i = 0; i < getCount(); i++) {
                    ProductPostItem postItem = getProduct(i);
                    BookMarkInfo bookMarkInfo = markResponse.getBookMarkInfo();
                    if (bookMarkInfo.getPost_id().equals(String.valueOf(postItem.getProduct_id()))) {
                        Log.i("TAGBOOK", " ID EQUAL" );
                        CurrentUser currentUser = postItem.getCurrent_user();
                        if (currentUser != null) {
                            Log.i("TAGBOOK", " USER" );
                            currentUser.setBookmarked(bookMarkInfo.isBookMark());
                            if(!bookMarkInfo.isBookMark()){
                                updateProductSaved(postItem);
                            }
                            IMyHuntsView.updateSavedProduct(postItem);
                        }
                        break;
                    }
                }
                break;
        }
        IMyHuntsView.updateAdapter();
    }

    @Override
    public void onBookMarkError(RestError errorInfo) {
        IMyHuntsView.hideProgress();
        if(errorInfo != null) {
            IMyHuntsView.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onProductSuccess(List<ProductPostItem> items, int type) {
        mProductCount--;

        /*if(items == null || items.isEmpty()){
            Log.i("TAGITEMS", "Product Data is empty");
            return;
        }*/

        switch (type){
            case ArticleParams.SAVED:
                mProductSavedList = items;
                Log.i("TAGITEMS", " type " + type + " Items "  + items);
                break;

            case ArticleParams.APPROVED:
                mProductApprovedList = items;
                break;

            case ArticleParams.RECEIVED:
                mProductReceivedList = items;
                break;

            case ArticleParams.DOWNLOADED:
                mProductDownloadList = items;
                break;
        }


        if(mProductCount == 0) {
            IMyHuntsView.hideProgress();
            IMyHuntsView.updateAdapter();
        }
    }

    @Override
    public void onProductDeleteSuccess(ProductPostItem item) {
        IMyHuntsView.hideProgress();
        IMyHuntsView.deletePost(item.getProduct_id());
    }

    @Override
    public void onError(RestError errorInfo) {
        IMyHuntsView.hideProgress();
        if(errorInfo != null) {
            IMyHuntsView.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void bookmark(String id, int type) {
        IMyHuntsView.showProgress();
        IBookMarkInteractor.bookmark(mContext, id, type, this);
    }

    @Override
    public void unBookmark(String id, int type) {
        IMyHuntsView.showProgress();
        IBookMarkInteractor.unBookmark(mContext, id, type, this);
    }

    @Override
    public void updateBottomNavigation() {

    }

    @Override
    public void showAlert(String msg) {
        IMyHuntsView.showAlert(msg);
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return IMyHuntsView.createViewHolder(view);
    }

    @Override
    public List<ProductPostItem> getProductList() {
        List<ProductPostItem> list = null;
        int type = IMyHuntsView.getType();
        switch (type){
            case ArticleParams.SAVED:
                list = mProductSavedList;
                break;

            case ArticleParams.APPROVED:
                list = mProductApprovedList;
                break;

            case ArticleParams.RECEIVED:
                list = mProductReceivedList;
                break;

            case ArticleParams.DOWNLOADED:
                list = mProductDownloadList;
                break;
        }
        return list;
    }


    @Override
    public void updateDownloadList(List<ProductPostItem> productPostItems) {
        mProductDownloadList = productPostItems;
        IMyHuntsView.updateAdapter();
    }

    @Override
    public int getCount() {
        int count = 0;
        int type = IMyHuntsView.getType();
        switch (type){
            case ArticleParams.SAVED:
                if (mProductSavedList != null) {
                    count = mProductSavedList.size();
                }
                break;

            case ArticleParams.APPROVED:
                if(mProductApprovedList != null) {
                    count = mProductApprovedList.size();
                }
                break;

            case ArticleParams.RECEIVED:
                if(mProductReceivedList != null) {
                    count = mProductReceivedList.size();
                }
                break;

            case ArticleParams.DOWNLOADED:
                if(mProductDownloadList != null) {
                    count = mProductDownloadList.size();
                }
                break;
        }
        return count;
    }

    @Override
    public ProductPostItem getProduct(int pos) {
        ProductPostItem productPostItem = null;
        int type = IMyHuntsView.getType();
        switch (type){
            case ArticleParams.SAVED:
                if (mProductSavedList != null) {
                    productPostItem = mProductSavedList.get(pos);
                }
                break;

            case ArticleParams.APPROVED:
                if(mProductApprovedList != null) {
                    productPostItem = mProductApprovedList.get(pos);
                }
                break;

            case ArticleParams.RECEIVED:
                if(mProductReceivedList != null) {
                    productPostItem = mProductReceivedList.get(pos);
                }
                break;

            case ArticleParams.DOWNLOADED:
                if(mProductDownloadList != null) {
                    productPostItem = mProductDownloadList.get(pos);
                }
                break;
        }
        return productPostItem;
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IMyHuntsView.loadFragment(fragmentName, bundle);
    }

    @Override
    public void fetchProducts(String userId) {
        Log.i("TAGUSER", "USER " + userId);
        IMyHuntsView.showProgress();
        mProductCount = 3;
        fetchSavedProducts(userId);
        fetchApprovedProducts(userId);
        fetchReceivedProducts(userId);
    }

    @Override
    public void updateProductSaved(ProductPostItem productPostItem) {
        if(mProductSavedList == null){
            mProductSavedList = new ArrayList<ProductPostItem>();
        }

        boolean isContained = false;
        for(ProductPostItem postItem: mProductSavedList){
            if(postItem.getProduct_id().equalsIgnoreCase(productPostItem.getProduct_id())){
                isContained = true;
                mProductSavedList.remove(postItem);
                break;
            }
        }

        if(!isContained){
            CurrentUser currentUser = productPostItem.getCurrent_user();
            if(currentUser.isBookmarked()) {
                mProductSavedList.add(productPostItem);
            }
        }
    }

    @Override
    public void deleteProduct(String id) {
        IMyHuntsView.showProgress();
        IProductInteractor.deleteProduct(mContext, id, this);
    }

    private void fetchSavedProducts(String userId) {
        Map<String, String> map = new HashMap<String, String>();

        String filter = ArticleParams.FILTER + "[" + ArticleParams.COLLECTION + "]";
        //  String author = ArticleParams.FILTER + "[" + ArticleParams.AUTHOR + "]";

                /*"'{"' +  + ArticleParams.COLLECTION + '"'
                + ":" + '"' + ArticleParams.COLLECTION_SAVED + '"'
                + "," + '"' + ArticleParams.AUTHOR + '"'
                + ":" + userId + "}";*/

        Log.i("TAGFILTER", "filter " + filter);
        // map.put(author, userId);
        map.put(ArticleParams.TYPE, ArticleParams.MARKET);
        map.put(ArticleParams.MARKT_TYPE, String.valueOf(1));
        map.put(filter, ArticleParams.COLLECTION_SAVED);
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(0));
        map.put(ArticleParams.LIMIT, String.valueOf(30));
        IProductInteractor.fetchProduct(mContext, ArticleParams.SAVED, map, this);
    }

    private void fetchApprovedProducts(String userId) {
        Map<String, String> map = new HashMap<String, String>();

        String filter = ArticleParams.FILTER + "[" + ArticleParams.COLLECTION + "]";
        // String author = ArticleParams.FILTER + "[" + ArticleParams.AUTHOR + "]";

                /*"'{"' +  + ArticleParams.COLLECTION + '"'
                + ":" + '"' + ArticleParams.COLLECTION_SAVED + '"'
                + "," + '"' + ArticleParams.AUTHOR + '"'
                + ":" + userId + "}";*/

        Log.i("TAGFILTER", "filter " + filter);
        // map.put(author, userId);
        map.put(ArticleParams.TYPE, ArticleParams.MARKET);
        map.put(ArticleParams.MARKT_TYPE, String.valueOf(1));
        map.put(filter, ArticleParams.COLLECTION_CREATED);
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(0));
        map.put(ArticleParams.LIMIT, String.valueOf(30));
        IProductInteractor.fetchProduct(mContext, ArticleParams.APPROVED, map, this);
    }

    private void fetchReceivedProducts(String userId) {
        Map<String, String> map = new HashMap<String, String>();

        String filter = ArticleParams.FILTER + "[" + ArticleParams.COLLECTION + "]";
        // String author = ArticleParams.FILTER + "[" + ArticleParams.AUTHOR + "]";

                /*"'{"' +  + ArticleParams.COLLECTION + '"'
                + ":" + '"' + ArticleParams.COLLECTION_SAVED + '"'
                + "," + '"' + ArticleParams.AUTHOR + '"'
                + ":" + userId + "}";*/

        Log.i("TAGFILTER", "filter " + filter);
        // map.put(author, userId);
        map.put(ArticleParams.TYPE, ArticleParams.MARKET);
        map.put(ArticleParams.MARKT_TYPE, String.valueOf(1));
        map.put(filter, ArticleParams.COLLECTION_RECEIVED);
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(0));
        map.put(ArticleParams.LIMIT, String.valueOf(30));
        IProductInteractor.fetchProduct(mContext, ArticleParams.RECEIVED, map, this);
    }
}
