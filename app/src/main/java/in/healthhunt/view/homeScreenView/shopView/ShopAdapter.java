package in.healthhunt.view.homeScreenView.shopView;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import in.healthhunt.R;
import in.healthhunt.model.articles.commonResponse.Author;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.commonResponse.MediaItem;
import in.healthhunt.model.articles.commonResponse.Title;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.shopPresenter.IShopPresenter;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopViewHolder> {

    private IShopPresenter IShopPresenter;
    private Context mContext;
    private ClickListener mClickListener;

    public ShopAdapter(Context context, IShopPresenter shopPresenter) {
        mContext = context;
        IShopPresenter = shopPresenter;
    }

    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shop_item_view, parent, false);
        return IShopPresenter.createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShopViewHolder holder, int position) {
        setContent(holder, position);
    }

    @Override
    public int getItemCount() {
        return IShopPresenter.getCount();
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View v, int position);
    }

    private void setContent(ShopViewHolder holder, int pos) {

        ProductPostItem postsItem = IShopPresenter.getProduct(pos);
        if(postsItem != null) {
            String url = null;
            List<MediaItem> mediaItems = postsItem.getMedia();
            if(mediaItems != null && !mediaItems.isEmpty()) {
                MediaItem media = mediaItems.get(0);
                if("image".equals(media.getMedia_type())) {
                    url = media.getUrl();

                }
            }
            if(url != null) {
                Log.i("TAG11", "url " + url);
                Glide.with(mContext).load(url).placeholder(R.drawable.artical).into(holder.mArticleImage);
            }
            else {
                holder.mArticleImage.setImageResource(R.drawable.artical);
            }


            /*String categoryName = null;
            List<CategoriesItem> categories = postsItem.getC();
            if(categories != null && !categories.isEmpty()){
                categoryName = categories.get(0).getName();

                holder.mCategoryView.setText(categoryName);
                holder.mCategoryImage.setColorFilter(ContextCompat.getColor(mContext, R.color.hh_blue_light), PorterDuff.Mode.SRC_IN);
                int res = HealthHuntUtility.getCategoryIcon(categoryName);
                if(res != 0){
                    holder.mCategoryImage.setImageResource(res);
                }
                else {
                    holder.mCategoryImage.setImageResource(R.mipmap.ic_fitness);
                }
            }*/

            //postsItem.getC
            Title title = postsItem.getTitle();
            String articleTitle = null;
            if(title != null) {
                articleTitle = title.getRendered();
                holder.mProductName.setText(articleTitle);
            }

            /*Content content = postsItem.getContent();
            if(content != null) {
                holder.mShopContent.setText(Html.fromHtml(content.getRendered()));
            }*/

           /* String productName = postsItem.getPost_name();//getProduct_type_other_name();
            if(productName != null) {
                holder.mProductName.setText(productName);
            }*/

            Author author = postsItem.getAuthor();
            if(author != null) {
                String authorName = author.getName();
                holder.mProductType.setText(authorName);
            }


            CurrentUser currentUser = postsItem.getCurrent_user();
            if(currentUser != null) {
                if(!currentUser.isBookmarked()){
                    holder.mBookMark.setColorFilter(null);
                    holder.mBookMark.setImageResource(R.mipmap.ic_bookmark);
                }
                else {
                    holder.mBookMark.setColorFilter(ContextCompat.getColor(mContext, R.color.hh_green_light2), PorterDuff.Mode.SRC_IN);
                }
            }

            Log.i("TAGISTRAIL", "ISTRAIL " + postsItem.getIs_free_trial());
            int is_free_trail = 0;
            String is_free = postsItem.getIs_free_trial();
            if(is_free != null && !is_free.isEmpty()){
                is_free_trail = Integer.parseInt(is_free);
            }

            if(is_free_trail == 0) {
                holder.mPriceView.setVisibility(View.VISIBLE);
                holder.mFreeTrail.setVisibility(View.GONE);
                String price = postsItem.getPost_price();
                if (price != null) {
                    String postQuantity = postsItem.getPost_quantity();
                    String rs = postsItem.getPost_currency();
                    if(rs == null){
                        rs = "";
                    }
                    Spanned spanned = Html.fromHtml(rs) ;
                    rs = spanned.toString();

                    price = HealthHuntUtility.addSeparator(price);
                    rs = rs + " " + price + "/" + postQuantity;
                    holder.mProductPrice.setText(rs);
                }

                String postUnit = postsItem.getPost_unit();
                if (postUnit != null) {
                    holder.mProductUnit.setText(postUnit);
                }
            }
            else {
                holder.mPriceView.setVisibility(View.GONE);
                holder.mFreeTrail.setVisibility(View.VISIBLE);
            }
        }
    }
}