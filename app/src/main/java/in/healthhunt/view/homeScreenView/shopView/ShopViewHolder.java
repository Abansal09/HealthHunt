package in.healthhunt.view.homeScreenView.shopView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.shopPresenter.IShopPresenter;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class ShopViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.shop_article_image)
    public ImageView mArticleImage;

    @BindView(R.id.shop_article_bookmark)
    public ImageView mBookMark;

    /* @BindView(R.id.shop_article_content)
     public TextView mShopContent;
 */
    /*@BindView(R.id.shop_category_image)
    public ImageView mCategoryImage;

    @BindView(R.id.shop_category_name)
    TextView mCategoryView;
*/
    @BindView(R.id.shop_article_item_view)
    LinearLayout mShopItemView;

    @BindView(R.id.shop_product_name)
    TextView mProductName;

    @BindView(R.id.shop_product_type)
    TextView mProductType;

    @BindView(R.id.shop_product_price)
    TextView mProductPrice;

    @BindView(R.id.shop_product_unit)
    TextView mProductUnit;

    @BindView(R.id.free_trail)
    TextView mFreeTrail;

    @BindView(R.id.price_view)
    LinearLayout mPriceView;


    private int mViewHolderPos;

    private Context mContext;
    private IShopPresenter IShopPresenter;
    private ShopAdapter.ClickListener mClickListener;
    public ShopViewHolder(View itemView, IShopPresenter shopPresenter, ShopAdapter.ClickListener clickListener) {
        super(itemView);
        IShopPresenter = shopPresenter;
        mClickListener = clickListener;
        mContext = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.shop_article_item_view)
    void onItemClick(View view) {
        if (HealthHuntUtility.checkInternetConnection(mContext)) {
            if(mClickListener != null) {
                mClickListener.ItemClicked(view, getAdapterPosition());
            }
        } else {
            IShopPresenter.showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
        }
    }

    @OnClick(R.id.shop_article_bookmark)
    void onBookMark(){
        if (HealthHuntUtility.checkInternetConnection(mContext)) {
            ProductPostItem postsItem = IShopPresenter.getProduct(getAdapterPosition());
            String id = String.valueOf(postsItem.getProduct_id());
            CurrentUser currentUser = postsItem.getCurrent_user();
            if(currentUser != null) {
                if(!currentUser.isBookmarked()){
                    IShopPresenter.bookmark(id, ArticleParams.PRODUCT);
                }
                else {
                    IShopPresenter.unBookmark(id, ArticleParams.PRODUCT);
                }
            }
        } else {
            IShopPresenter.showAlert(mContext.getString(R.string.please_check_internet_connectivity_status));
        }
    }
}