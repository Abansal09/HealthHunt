package in.healthhunt.view.searchView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.beans.SpaceDecoration;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.searchPresenter.ISearchPresenter;
import in.healthhunt.presenter.searchPresenter.SearchAdapter;
import in.healthhunt.presenter.searchPresenter.SearchArticleHolder;
import in.healthhunt.presenter.searchPresenter.SearchPresenterImp;
import in.healthhunt.view.fullView.fullViewFragments.FullArticleFragment;
import in.healthhunt.view.fullView.fullViewFragments.YoutubeFragment;
import in.healthhunt.view.homeScreenView.HomeActivity;
import in.healthhunt.view.homeScreenView.IHomeView;

/**
 * Created by abhishekkumar on 5/3/18.
 */

public class SearchFragment extends Fragment implements SearchAdapter.ClickListener, ISearch{


    @BindView(R.id.view_all_recycler_list)
    RecyclerView mArticlesViewer;

    @BindView(R.id.no_records)
    TextView mNoRecords;

    private ISearchPresenter ISearchPresenter;
    private IHomeView IHomeView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ISearchPresenter = new SearchPresenterImp(getContext(), this);
        IHomeView  =(HomeActivity) getActivity();
        Log.i("TAGSEARCHFRAGMENT", "SEARCH");

        Bundle bundle = getArguments();
        String str = null;
        if(bundle != null) {
            str = bundle.getString(Constants.SEARCH_TEXT_KEY);
        }


        ISearchPresenter.searchArticles(str);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all, container, false);
        ButterKnife.bind(this, view);
        IHomeView.setStatusBarTranslucent(false);
        IHomeView.hideBottomNavigationSelection();
        IHomeView.hideDrawerMenu();
        IHomeView.showBottomFooter();
        IHomeView.showActionBar();
        IHomeView.updateTitle(getString(R.string.search_result));
        setAdapter();
        updateVisibility();
        return view;
    }

    private void setAdapter() {
        SearchAdapter searchAdapter = new SearchAdapter(getContext(), ISearchPresenter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mArticlesViewer.setLayoutManager(layoutManager);
        mArticlesViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(8, getContext()), SpaceDecoration.VERTICAL));
        mArticlesViewer.setAdapter(searchAdapter);
    }

    public int getCount() {
        return ISearchPresenter.getCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View view) {

        return new SearchArticleHolder(view, this, ISearchPresenter);

    }

    @Override
    public void showProgress() {
        IHomeView.showProgress();
    }

    @Override
    public void hideProgress() {
        IHomeView.hideProgress();
    }

    @Override
    public void updateAdapter() {
        updateVisibility();
        mArticlesViewer.getAdapter().notifyDataSetChanged();;
    }

    @Override
    public void showAlert(String msg) {
        IHomeView.showAlert(msg);
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IHomeView.loadNonFooterFragment(fragmentName, bundle);
    }

    @Override
    public void updateArticleSaved(ArticlePostItem postItem) {
        IHomeView.updateMyhuntsArticleSaved(postItem);
        showToast(postItem.getCurrent_user());
        IHomeView.updateMyFeedArticle(postItem);
    }

    private void showToast(CurrentUser currentUser) {
        boolean isBookMark = currentUser.isBookmarked();
        String str = getString(R.string.added_to_my_hunt);//getString(R.string.saved);
        if(!isBookMark){
            str = getString(R.string.removed_from_my_hunt);//getString(R.string.removed);
        }
        IHomeView.showToast(str);
    }

    @Override
    public void ItemClicked(View v, int position) {
        ArticlePostItem postItem = ISearchPresenter.getArticle(position);
        String id = postItem.getArticle_Id();

        Bundle bundle = new Bundle();
        bundle.putString(ArticleParams.ID, id);

        String url = postItem.getVideo_thumbnail();
        if(url == null || url.isEmpty()) {
            bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.ARTICLE);
            ISearchPresenter.loadFragment(FullArticleFragment.class.getSimpleName(), bundle);
        }
        else {
            bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.VIDEO);
            ISearchPresenter.loadFragment(YoutubeFragment.class.getSimpleName(), bundle);
        }
    }

    public void updateData(String searchStr){
        ISearchPresenter.searchArticles(searchStr);
    }

    public void updateVisibility(){
        int count = ISearchPresenter.getCount();
        if(count == 0){
            mNoRecords.setVisibility(View.VISIBLE);
            mArticlesViewer.setVisibility(View.GONE);
        }
        else {
            mNoRecords.setVisibility(View.GONE);
            mArticlesViewer.setVisibility(View.VISIBLE);
        }
    }
}
