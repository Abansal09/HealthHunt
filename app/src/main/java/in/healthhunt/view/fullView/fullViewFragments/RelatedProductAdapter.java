package in.healthhunt.view.fullView.fullViewFragments;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.commonResponse.MediaItem;
import in.healthhunt.model.articles.commonResponse.Title;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.presenter.fullPresenter.IFullPresenter;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public class RelatedProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private IFullPresenter IFullPresenter;
    private Context mContext;
    private ClickListener mClickListener;


    public RelatedProductAdapter(Context context, IFullPresenter fullPresenter) {
        mContext = context;
        IFullPresenter = fullPresenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("TAg@@","viewType");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_products_article_item_view, parent, false);
        return new RelatedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setContent(((RelatedProductViewHolder)holder), position);
    }

    @Override
    public int getItemCount() {
        return IFullPresenter.getRelatedProductsCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener {
        void onRelatedItemClicked(View v, int position, int type);
    }

    private void setContent(RelatedProductViewHolder holder, int pos) {

        ProductPostItem postsItem = IFullPresenter.getRelatedProduct(pos);
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
                Glide.with(mContext).load(url).placeholder(R.drawable.artical).into(holder.mProductImage);
            }
            else {
                holder.mProductImage.setImageResource(R.drawable.artical);
            }


            /*String productName = postsItem.getPost_name();
            Log.i("TAGPRODU", "Product NAme " + productName);
            if(productName != null) {
                holder.mProductName.setText(productName);
            }*/

            Title title = postsItem.getTitle();
            if(title != null){
                String render = title.getRendered();
                holder.mProductName.setText(render);
            }

            String brandName = postsItem.getCompany_name();
            Log.i("TAGPRODU", "type " + brandName);
            if(brandName != null) {
                holder.mProductType.setText(brandName);
            }

            CurrentUser currentUser = postsItem.getCurrent_user();
            if(currentUser != null) {
                if(!currentUser.isBookmarked()){
                    holder.mProductBookMark.setColorFilter(null);
                    holder.mProductBookMark.setImageResource(R.mipmap.ic_bookmark);
                }
                else {
                    holder.mProductBookMark.setColorFilter(ContextCompat.getColor(mContext, R.color.hh_green_light2), PorterDuff.Mode.SRC_IN);
                }
            }

            String price = postsItem.getPost_price();
            if(price != null) {
                String postQuantity = postsItem.getPost_quantity();
                String rs = postsItem.getPost_currency();
                if(rs == null){
                    rs = "";
                }
                Spanned spanned = Html.fromHtml(rs) ;
                rs = spanned.toString();

                rs = rs + " " + price + "/" + postQuantity;
                holder.mProductPrice.setText(rs);
            }

            String postUnit = postsItem.getPost_unit();
            if(postUnit != null) {
                holder.mProductUnit.setText(postUnit);
            }
        }
    }
    public class RelatedProductViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_image)
        ImageView mProductImage;

        @BindView(R.id.product_bookmark)
        ImageView mProductBookMark;

        @BindView(R.id.top_product_name)
        TextView mProductName;

        @BindView(R.id.top_product_type)
        TextView mProductType;

        @BindView(R.id.top_product_price)
        TextView mProductPrice;

        @BindView(R.id.top_product_unit)
        TextView mProductUnit;

        public RelatedProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void notifyDataChanged() {
            notifyDataSetChanged();
        }

        @OnClick(R.id.top_product_item_view)
        void onClick(View view) {
            if(mClickListener != null) {
                mClickListener.onRelatedItemClicked(view, getAdapterPosition(), ArticleParams.PRODUCT);
            }
        }

        @OnClick(R.id.product_bookmark)
        void onBookMark(){
            ProductPostItem postsItem = IFullPresenter.getRelatedProduct(getAdapterPosition());
            String id = String.valueOf(postsItem.getProduct_id());
            CurrentUser currentUser = postsItem.getCurrent_user();
            if(currentUser != null) {
                if(!currentUser.isBookmarked()){
                    IFullPresenter.bookmark(id, ArticleParams.RELATED_PRODUCTS);
                }
                else {
                    IFullPresenter.unBookmark(id, ArticleParams.RELATED_PRODUCTS);
                }
            }
        }

    }
}
