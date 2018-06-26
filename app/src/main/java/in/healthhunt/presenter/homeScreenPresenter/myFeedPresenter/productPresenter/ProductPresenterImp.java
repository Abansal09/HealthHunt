package in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.productPresenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.List;

import framework.retrofit.RestError;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkData;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkInfo;
import in.healthhunt.model.articles.commonResponse.MediaItem;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.presenter.interactor.bookMarkInteractor.BookMarkInteractorImpl;
import in.healthhunt.presenter.interactor.bookMarkInteractor.IBookMarkInteractor;
import in.healthhunt.view.homeScreenView.myFeedView.productView.IProductView;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class ProductPresenterImp implements IProductPresenter, IBookMarkInteractor.OnFinishListener {

    private String TAG = ProductPresenterImp.class.getSimpleName();
    private IProductView IProductView;
    private Context mContext;
    private IBookMarkInteractor IInteractor;

    public ProductPresenterImp(Context context, IProductView latestProductView) {
        mContext = context;
        IProductView = latestProductView;
        IInteractor = new BookMarkInteractorImpl();
    }

    @Override
    public int getCount() {
        return IProductView.getCount();
    }

    @Override
    public void bookmark(String id, int type) {
        IProductView.showProgress();
        IInteractor.bookmark(mContext, id, type, this);
    }

    @Override
    public void unBookmark(String id, int type) {
        IProductView.showProgress();
        IInteractor.unBookmark(mContext, id, type, this);
    }

    @Override
    public void updateBottomNavigation() {
        IProductView.updateBottomNavigation();
    }

    @Override
    public void showAlert(String msg) {
        IProductView.showAlert(msg);
    }


    @Override
    public ProductPostItem getProduct(int pos) {
        return IProductView.getProduct(pos);
    }

    @Override
    public Fragment getItem(int position, int type) {
        Fragment fragment = IProductView.getFragmentArticleItem(position);
        Bundle bundle = new Bundle();//createBundle(position);
        bundle.putInt(ArticleParams.ARTICLE_TYPE, type);
        bundle.putInt(ArticleParams.POSITION, position);
        int count = IProductView.getCount();

        if (position == 4 && position == count - 1) {
            bundle.putBoolean(ArticleParams.IS_LAST_PAGE, true);
        } else {
            bundle.putBoolean(ArticleParams.IS_LAST_PAGE, false);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IProductView.loadFragment(fragmentName, bundle);
    }

    private Bundle createBundle(int pos) {
        ProductPostItem postItem = IProductView.getProduct(pos);

        Bundle bundle = new Bundle();

        if (postItem != null) {

            bundle.putString(ArticleParams.ID, String.valueOf(postItem.getProduct_id()));

            String url = null;
            List<MediaItem> mediaItems = postItem.getMedia();
            if (mediaItems != null && !mediaItems.isEmpty()) {
                MediaItem media = mediaItems.get(0);
                Log.i("TAGMedia", "media " + media);
                if ("image".equals(media.getMedia_type())) {
                    url = media.getUrl();
                    Log.i("TAGMedia", "URL " + url);

                }
            }
            bundle.putString(ArticleParams.ARTICLE_URL, url);

            String productName = postItem.getProduct_type_other_name();
            if (productName != null) {
                bundle.putString(ArticleParams.PRODUCT_NAME, productName);
            }

            String brandName = postItem.getBrand_other_name();
            if (brandName != null) {
                bundle.putString(ArticleParams.BRAND_NAME, brandName);
            }

            String price = postItem.getPost_price();
            if (price != null) {
                bundle.putString(ArticleParams.PRODUCT_PRICE, price);
            }

            String quantity = postItem.getPost_quantity();
            if (quantity != null) {
                bundle.putString(ArticleParams.PRODUCT_QUANTITY, quantity);
            }

            String unit = postItem.getPost_unit();
            if (unit != null) {
                bundle.putString(ArticleParams.PRODUCT_UNIT, unit);
            }
        }
        return bundle;
    }

    @Override
    public void onBookMarkSuccess(BookMarkData markResponse) {
        IProductView.hideProgress();
        for(int i=0; i<IProductView.getCount(); i++){
            ProductPostItem postItem = IProductView.getProduct(i);
            BookMarkInfo info = markResponse.getBookMarkInfo();
            if(info != null) {
                if (postItem.getProduct_id().equals(info.getPost_id())) {
                    postItem.getCurrent_user().setBookmarked(info.isBookMark());
                    IProductView.updateProductSaved(postItem);
                    break;
                }
            }
        }

        IProductView.updateAdapter();
    }

    @Override
    public void onBookMarkError(RestError errorInfo) {
        IProductView.hideProgress();
        String msg = mContext.getString(R.string.server_error);
        if(errorInfo != null) {
            msg = errorInfo.getMessage();
            IProductView.showAlert(msg);
        }
    }
}
