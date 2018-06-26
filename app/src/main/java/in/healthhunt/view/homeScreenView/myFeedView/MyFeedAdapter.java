package in.healthhunt.view.homeScreenView.myFeedView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.IMyFeedPresenter;
import in.healthhunt.view.homeScreenView.myFeedView.articleView.ArticleViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.articleView.ContinueArticleViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.articleView.LatestArticleViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.articleView.SponsoredArticleViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.articleView.TrendingArticleViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.productView.LatestProductViewHolder;
import in.healthhunt.view.homeScreenView.myFeedView.productView.TopProductViewHolder;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public class MyFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private IMyFeedPresenter IMyFeedPresenter;


    public MyFeedAdapter(IMyFeedPresenter feedPresenter) {
        IMyFeedPresenter = feedPresenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        Log.i("TAG561", "onCreate ViewType " + viewType);
        int layout = IMyFeedPresenter.getView(viewType);
        view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return IMyFeedPresenter.createArticleHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder != null && holder instanceof ArticleViewHolder) {
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            articleViewHolder.notifyDataChanged();
            /*int count = articleViewHolder.getCount();
            if(count == 0){
                articleViewHolder.hideView();
            }
            else {
                articleViewHolder.showView();
                articleViewHolder.notifyDataChanged();
            }*/
        }
        else if(holder != null && holder instanceof ContinueArticleViewHolder) {
            ContinueArticleViewHolder continueArticleViewHolder = (ContinueArticleViewHolder) holder;
            continueArticleViewHolder.notifyDataChanged();
            /*int count = continueArticleViewHolder.getCount();
            Log.i("TAGTAGCONTINUE", "Continue Count  "  + count);
            if(count == 0){
                continueArticleViewHolder.hideView();
            }
            else {
                continueArticleViewHolder.showView();
                continueArticleViewHolder.notifyDataChanged();
            }*/

        }else if(holder != null && holder instanceof TrendingArticleViewHolder) {
            TrendingArticleViewHolder trendingArticleViewHolder = (TrendingArticleViewHolder) holder;
            trendingArticleViewHolder.notifyDataChanged();
           /* int count = trendingArticleViewHolder.getCount();
            if(count == 0){
                trendingArticleViewHolder.hideView();
            }
            else {
                trendingArticleViewHolder.showView();
                trendingArticleViewHolder.notifyDataChanged();
            }*/
        }
        else if(holder != null && holder instanceof SponsoredArticleViewHolder) {
            SponsoredArticleViewHolder sponsoredArticleViewHolder = (SponsoredArticleViewHolder) holder;
            sponsoredArticleViewHolder.notifyDataChanged();
            /*int count = sponsoredArticleViewHolder.getCount();
            if(count == 0){
                sponsoredArticleViewHolder.hideView();
            }
            else {
                sponsoredArticleViewHolder.showView();
                sponsoredArticleViewHolder.notifyDataChanged();
            }*/
        }
        else if(holder != null && holder instanceof TopProductViewHolder) {
            TopProductViewHolder topProductViewHolder = (TopProductViewHolder) holder;
            topProductViewHolder.notifyDataChanged();
            /*int count = topProductViewHolder.getCount();
            if(count == 0){
                topProductViewHolder.hideView();
            }
            else {
                topProductViewHolder.showView();
                topProductViewHolder.notifyDataChanged();
            }*/
        }
        else if(holder != null && holder instanceof LatestArticleViewHolder) {
            LatestArticleViewHolder latestArticleViewHolder = (LatestArticleViewHolder) holder;
            latestArticleViewHolder.notifyDataChanged();
            /*int count = latestArticleViewHolder.getCount();
            if(count == 0){
                latestArticleViewHolder.hideView();
            }
            else {
                latestArticleViewHolder.showView();
                latestArticleViewHolder.notifyDataChanged();
            }*/
        }
        else if(holder != null && holder instanceof LatestProductViewHolder) {
            LatestProductViewHolder latestProductViewHolder = (LatestProductViewHolder) holder;
            latestProductViewHolder.notifyDataChanged();
            /*int count = latestProductViewHolder.getCount();
            if(count == 0){
                latestProductViewHolder.hideView();
            }
            else {
                latestProductViewHolder.showView();
                latestProductViewHolder.notifyDataChanged();
            }*/
        }
    }

    @Override
    public int getItemCount() {
        Map<Integer, Integer> map = IMyFeedPresenter.getArticlesType();
        int count = 0;
        if(map != null){
            count = map.size();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        Map<Integer, Integer> map = IMyFeedPresenter.getArticlesType();
        Set<Integer> keys = map.keySet();
        Iterator iterator = keys.iterator();
        Log.i("TAGMAPVAL", "Position " + position);
        while (iterator.hasNext()){
            Log.i("TAGMAPVAL", " VAL " + map.get(iterator.next()));
        }
        Log.i("TAGMAPVAL", "END");


        int type = 0;
        if(map != null && position < map.size()){
            type = map.get(position);
        }
        return type;
    }

    public void deleteItem(int index) {
        notifyItemRemoved(index);
    }

    public void addItem(int index) {
        notifyItemInserted(index);
    }
}
