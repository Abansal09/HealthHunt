package in.healthhunt.view.viewAll;

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
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.Author;
import in.healthhunt.model.articles.commonResponse.CategoriesItem;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.commonResponse.MediaItem;
import in.healthhunt.model.articles.commonResponse.TagsItem;
import in.healthhunt.model.articles.commonResponse.Title;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.viewAllPresenter.IViewAllPresenter;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public class ViewAllAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private IViewAllPresenter IViewAllPresenter;
    private Context mContext;
    private int mType;
    private ClickListener mClickListener;

    public ViewAllAdapter(Context context, IViewAllPresenter viewAllPresenter, int type) {
        mContext = context;
        IViewAllPresenter = viewAllPresenter;
        mType = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("TAg@@","viewType");

        int layout = IViewAllPresenter.getView();
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return IViewAllPresenter.createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (mType) {
            case ArticleParams.PRESCRIBED_FOR_YOU:
            case ArticleParams.READ_FRESH_ARTICLES:
            case ArticleParams.RELATED_ARTICLES:
                ArticlePostItem tagsItem = IViewAllPresenter.getArticle(position);
                setArticleContent(((ViewAllArticleHolder) holder), tagsItem);
                break;

            case ArticleParams.CHECK_OUT_THE_NEWBIES_PRODUCTS:
            case ArticleParams.RELATED_PRODUCTS:
                ProductPostItem postItem =  IViewAllPresenter.getProduct(position);
                setProductContent(((ViewAllProductHolder) holder), postItem);
                break;
        }
    }

    private void setArticleContent(ViewAllArticleHolder holder, ArticlePostItem postsItem) {
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
                Glide.with(mContext).load(url).dontAnimate().placeholder(R.mipmap.ic_no_my_feed_article_image).into(holder.mArticleImage);
            }
            else {
                holder.mArticleImage.setImageResource(R.mipmap.ic_no_my_feed_article_image);
            }


            String categoryName = null;
            List<CategoriesItem> categories = postsItem.getCategories();
            if(categories != null && !categories.isEmpty()){
                categoryName = categories.get(0).getName();
                holder.mCategoryName.setText(categoryName);
                int res = HealthHuntUtility.getCategoryIcon(categoryName);
                int color = HealthHuntUtility.getCategoryColor(categoryName);
                holder.mCategoryName.setTextColor(ContextCompat.getColor(mContext, color));
                holder.mCategoryImage.setColorFilter(ContextCompat.getColor(mContext, color), PorterDuff.Mode.SRC_IN);
                if(res != 0){
                    holder.mCategoryImage.setImageResource(res);
                }
                else {
                    holder.mCategoryImage.setImageResource(R.mipmap.ic_fitness);
                }
            }

            Author author = postsItem.getAuthor();
            if(author != null){
                holder.mAuthorName.setText(author.getName());

                String authorUrl = author.getUrl();
                authorUrl = authorUrl.replace("\n", "");
                Glide.with(mContext)
                        .load(authorUrl)
                        .bitmapTransform(new CropCircleTransformation(mContext))/*.placeholder(R.mipmap.default_profile)*/
                        .into(holder.mAuthorImage);

            }

            Title title = postsItem.getTitle();
            String articleTitle = null;
            if(title != null) {
                articleTitle = title.getRendered();
            }
            holder.mArticleTitle.setText(articleTitle);


            String tagsName = "";
            List<TagsItem> tagItems = postsItem.getTags();
            if(tagItems != null && !tagItems.isEmpty()) {
                for(TagsItem  tagItem: tagItems) {
                    tagsName = tagsName + "#" + tagItem.getName() + "  ";
                }
            }

            holder.mHashTags.setText(tagsName);

            CurrentUser currentUser = postsItem.getCurrent_user();
            if(currentUser != null) {
                if(!currentUser.isBookmarked()){
                    holder.mArticleBookMark.setColorFilter(null);
                    holder.mArticleBookMark.setImageResource(R.mipmap.ic_bookmark);
                }
                else {
                    holder.mArticleBookMark.setColorFilter(ContextCompat.getColor(mContext, R.color.hh_green_light2), PorterDuff.Mode.SRC_IN);
                }
            }


            String readingTime = postsItem.getRead_time();
            readingTime = readingTime + " min read";
            holder.mReadingTime.setText(readingTime);

            String date = postsItem.getDate();
            if(date != null) {
                date = HealthHuntUtility.getDateWithFormat(date);
            }
            holder.mArticleDate.setText(date);
        }
    }

    private void setProductContent(ViewAllProductHolder holder, ProductPostItem postsItem) {

        if(postsItem != null) {
            /*String productName = postsItem.getProduct_type_other_name();
            if (productName != null) {
                holder.mProductName.setText(productName);
            }*/

            Title title = postsItem.getTitle();
            if(title != null){
                String render = title.getRendered();
                if(render != null){
                    holder.mProductName.setText(render);
                }
            }


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


            String url = null;
            List<MediaItem> mediaItems = postsItem.getMedia();
            if(mediaItems != null && !mediaItems.isEmpty()) {
                MediaItem media = mediaItems.get(0);
                Log.i("TAGMedia", "media "+ media);
                if("image".equals(media.getMedia_type())) {
                    url = media.getUrl();
                    Log.i("TAGMedia", "URL "+ url);

                }
            }
            if (url != null) {
                Log.i("TAG11", "productUrl " + url);
                Glide.with(mContext).load(url).dontAnimate().placeholder(R.mipmap.ic_no_top_product_image).into(holder.mProductImage);
            } else {
                holder.mProductImage.setImageResource(R.mipmap.ic_no_top_product_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return IViewAllPresenter.getCount(mType);
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View v, int position);
    }

}
