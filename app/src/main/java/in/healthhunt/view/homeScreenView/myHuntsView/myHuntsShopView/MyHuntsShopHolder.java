package in.healthhunt.view.homeScreenView.myHuntsView.myHuntsShopView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.myHuntPresenter.myHuntsShopPresenter.IMyHuntsProductsPresenter;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class MyHuntsShopHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.my_hunts_product_image)
    public ImageView mProductImage;

    @BindView(R.id.my_hunts_product_bookmark)
    public ImageView mBookMark;

    @BindView(R.id.my_hunts_product_name)
    public TextView mProductName;

    @BindView(R.id.my_hunts_product_type)
    public TextView mProductType;

    @BindView(R.id.my_hunts_product_price)
    public TextView mProductPrice;

    @BindView(R.id.my_hunts_product_unit)
    public TextView mProductUnit;

    @BindView(R.id.free_trail)
    TextView mFreeTrail;

    @BindView(R.id.price_view)
    LinearLayout mPriceView;

    private Context mContext;
    private IMyHuntsProductsPresenter IMyHuntsProductsPresenter;
    private MyHuntsShopAdapter.ClickListener mClickListener;
    public MyHuntsShopHolder(View itemView, IMyHuntsProductsPresenter myHuntsProductsPresenter, MyHuntsShopAdapter.ClickListener clickListener) {
        super(itemView);
        IMyHuntsProductsPresenter = myHuntsProductsPresenter;
        mClickListener = clickListener;
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.my_hunts_product_image)
    void onItemClick(View view) {
        if(HealthHuntUtility.checkInternetConnection(mContext)) {
            if(mClickListener != null) {
                mClickListener.ItemClicked(view, getAdapterPosition());
            }
        }
        else {
            IMyHuntsProductsPresenter.showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
        }
    }

    @OnLongClick(R.id.my_hunts_product_image)
    boolean onLongClick(){
        if(mClickListener != null) {
            mClickListener.onLongClicked(getAdapterPosition());
        }
        return true;
    }

    @OnClick(R.id.my_hunts_product_bookmark)
    void onBookMark(){

        if(HealthHuntUtility.checkInternetConnection(mContext)) {
            ProductPostItem postsItem = IMyHuntsProductsPresenter.getProduct(getAdapterPosition());
            String id = String.valueOf(postsItem.getProduct_id());
            CurrentUser currentUser = postsItem.getCurrent_user();
            if (currentUser != null) {
                if (!currentUser.isBookmarked()) {
                    IMyHuntsProductsPresenter.bookmark(id, ArticleParams.PRODUCT);
                } else {
                    IMyHuntsProductsPresenter.unBookmark(id, ArticleParams.PRODUCT);
                }
            }
        }
        else {
            IMyHuntsProductsPresenter.showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
        }
    }
}