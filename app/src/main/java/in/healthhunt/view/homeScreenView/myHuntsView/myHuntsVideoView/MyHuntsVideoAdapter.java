package in.healthhunt.view.homeScreenView.myHuntsView.myHuntsVideoView;

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
import in.healthhunt.model.articles.commonResponse.CategoriesItem;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.commonResponse.Title;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.myHuntPresenter.myHuntsVideoPresenter.IMyHuntsVideoPresenter;

/**
 * Created by abhishekkumar on 5/21/18.
 */

public class MyHuntsVideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private IMyHuntsVideoPresenter IMyHuntsVideoPresenter;
    private Context mContext;
    private ClickListener mClickListener;

    public MyHuntsVideoAdapter(Context context, IMyHuntsVideoPresenter myHuntsVideoPresenter) {
        mContext = context;
        IMyHuntsVideoPresenter = myHuntsVideoPresenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_hunts_article_item_view, parent, false);
        return IMyHuntsVideoPresenter.createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setContent((MyHuntsVideoHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return IMyHuntsVideoPresenter.getCount();
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View v, int position);
        void onLongClicked(int position);
    }

    private void setContent(MyHuntsVideoHolder holder, int pos) {

        ArticlePostItem postsItem = IMyHuntsVideoPresenter.getVideo(pos);
        if(postsItem != null) {
            String url = postsItem.getVideo_thumbnail();
            Log.i("TAG11", "url " + url);
            if(url != null) {
                Glide.with(mContext).load(url).placeholder(R.mipmap.ic_no_my_hunts_article_image).into(holder.mArticleImage);
            }
            else {
                holder.mArticleImage.setImageResource(R.mipmap.ic_no_my_hunts_article_image);
            }

            holder.mPlayIcon.setVisibility(View.VISIBLE);


            String categoryName = null;
            List<CategoriesItem> categories = postsItem.getCategories();
            if(categories != null && !categories.isEmpty()){
                categoryName = categories.get(0).getName();
                holder.mCategoryView.setText(categoryName);
                int res = HealthHuntUtility.getCategoryIcon(categoryName);
                int color = HealthHuntUtility.getCategoryColor(categoryName);
                holder.mCategoryView.setTextColor(ContextCompat.getColor(mContext, color));
                holder.mCategoryImage.setColorFilter(ContextCompat.getColor(mContext, color), PorterDuff.Mode.SRC_IN);
                if(res != 0){
                    holder.mCategoryImage.setImageResource(res);
                }
                else {
                    holder.mCategoryImage.setImageResource(R.mipmap.ic_fitness);
                }
            }
            else {
                holder.mCategoryView.setText("");
            }


            Title title = postsItem.getTitle();
            String articleTitle = "";
            Log.i("TAGTITLE", "title " + title);
            if(title != null) {
                articleTitle = title.getRendered();
            }
            holder.mArticleTitle.setText(articleTitle);



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
        }
    }
}
