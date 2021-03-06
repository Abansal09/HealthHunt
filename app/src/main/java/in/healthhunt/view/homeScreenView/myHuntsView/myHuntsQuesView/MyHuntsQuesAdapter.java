package in.healthhunt.view.homeScreenView.myHuntsView.myHuntsQuesView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.healthhunt.R;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.presenter.homeScreenPresenter.myHuntPresenter.myHuntArticlesPresenter.IMyHuntsArticlesPresenter;

/**
 * Created by abhishekkumar on 5/21/18.
 */

public class MyHuntsQuesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private IMyHuntsArticlesPresenter IMyHuntsArticlePresenter;
    private Context mContext;
    private ClickListener mClickListener;
    private List<ArticlePostItem> mArticleList;

    public MyHuntsQuesAdapter(Context context, IMyHuntsArticlesPresenter myHuntsArticlePresenter) {
        mContext = context;
        IMyHuntsArticlePresenter = myHuntsArticlePresenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_hunts_article_item_view, parent, false);
        return IMyHuntsArticlePresenter.createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return IMyHuntsArticlePresenter.getCount();
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View v, int position);
    }
}
