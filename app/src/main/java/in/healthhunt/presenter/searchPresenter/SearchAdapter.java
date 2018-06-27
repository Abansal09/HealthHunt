package in.healthhunt.presenter.searchPresenter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import in.healthhunt.R;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.Author;
import in.healthhunt.model.articles.commonResponse.CategoriesItem;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.commonResponse.MediaItem;
import in.healthhunt.model.articles.commonResponse.TagsItem;
import in.healthhunt.model.articles.commonResponse.Title;
import in.healthhunt.model.utility.HealthHuntUtility;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ISearchPresenter ISearchPresenter;
    private Context mContext;
    private ClickListener mClickListener;

    public SearchAdapter(Context context, ISearchPresenter searchPresenter) {
        mContext = context;
        ISearchPresenter = searchPresenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("TAg@@","viewType");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_article_item_view, parent, false);
        return ISearchPresenter.createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ArticlePostItem tagsItem = ISearchPresenter.getArticle(position);
        setArticleContent(((SearchArticleHolder) holder), tagsItem);
    }

    private void setArticleContent(SearchArticleHolder holder, ArticlePostItem postsItem) {
        if(postsItem != null) {


            String url = postsItem.getVideo_thumbnail();

            if(url == null || url.isEmpty()) {
                List<MediaItem> mediaItems = postsItem.getMedia();
                if (mediaItems != null && !mediaItems.isEmpty()) {
                    MediaItem media = mediaItems.get(0);
                    if ("image".equals(media.getMedia_type())) {
                        url = media.getUrl();

                    }
                }
                holder.mPlayImage.setVisibility(View.GONE);
            }else {
                holder.mPlayImage.setVisibility(View.VISIBLE);
            }

            if(url != null) {
                Log.i("TAG11", "url " + url);
                Glide.with(mContext).load(url).placeholder(R.mipmap.ic_no_my_feed_article_image).into(holder.mArticleImage);
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

    @Override
    public int getItemCount() {
        return ISearchPresenter.getCount();
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View v, int position);
    }

}
