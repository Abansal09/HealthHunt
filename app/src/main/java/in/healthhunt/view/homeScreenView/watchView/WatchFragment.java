package in.healthhunt.view.homeScreenView.watchView;

import android.content.Intent;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.beans.SpaceDecoration;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.watchPresenter.IWatchPresenter;
import in.healthhunt.presenter.homeScreenPresenter.watchPresenter.WatchPresenterImp;
import in.healthhunt.view.fullView.fullViewFragments.YoutubeFragment;
import in.healthhunt.view.homeScreenView.IHomeView;

/**
 * Created by abhishekkumar on 5/19/18.
 */

public class WatchFragment extends Fragment implements IWatchView, WatchAdapter.ClickListener{

    private IWatchPresenter IWatchPresenter;
    private Unbinder mUnbinder;

    @BindView(R.id.watch_recycler_list)
    RecyclerView mWatchViewer;

    @BindView(R.id.no_records)
    TextView mNoRecords;


    private IHomeView IHomeView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IWatchPresenter = new WatchPresenterImp(getContext(), this);
        IHomeView = (IHomeView) getActivity();
        IWatchPresenter.fetchVideoArticles();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        IHomeView.showDrawerMenu();
        IHomeView.updateTitle(getString(R.string.watch));
        setAdapter();
        return view;
    }

    @Override
    public int getCount() {
        return IWatchPresenter.getCount();
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
    public void showAlert(String msg) {
        IHomeView.showAlert(msg);
    }

    @Override
    public void updateBottomNavigation() {
        IHomeView.hideBottomNavigationSelection();
    }

    @Override
    public WatchViewHolder createViewHolder(View view) {
        return new WatchViewHolder(view, IWatchPresenter, this);
    }

    @Override
    public void updateAdapter() {
        if(mWatchViewer != null) {
            mWatchViewer.getAdapter().notifyDataSetChanged();
        }
        updateVisibility();
        IHomeView.updateCategoryVisibility();
    }

    public void updateVisibility(){
        int count = IWatchPresenter.getCount();
        if(count == 0){
            mNoRecords.setVisibility(View.VISIBLE);
            mWatchViewer.setVisibility(View.GONE);
        }
        else {
            mNoRecords.setVisibility(View.GONE);
            mWatchViewer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IHomeView.loadNonFooterFragment(fragmentName, bundle);
    }

    @Override
    public void updateVideoSaved(ArticlePostItem postItem) {
        IHomeView.updateMyhuntsVideoSaved(postItem);
        showToast(postItem.getCurrent_user());
    }

    @Override
    public List<String> getCategories() {
        return IHomeView.getCategories();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    private void setAdapter() {
        WatchAdapter watchAdapter = new WatchAdapter(getContext(), IWatchPresenter);
        watchAdapter.setClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mWatchViewer.setLayoutManager(layoutManager);
        mWatchViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(8, getContext()), SpaceDecoration.VERTICAL));
        mWatchViewer.setAdapter(watchAdapter);
    }



    @Override
    public void ItemClicked(View v, int position) {
        ArticlePostItem postsItem = IWatchPresenter.getArticle(position);
        if(postsItem != null) {
            /*Intent intent = new Intent(getContext(), FullVideoActivity.class);
            intent.putExtra(ArticleParams.ID, String.valueOf(postsItem.getArticle_Id()));
            intent.putExtra(ArticleParams.POST_TYPE, ArticleParams.VIDEO);
            startActivityForResult(intent, Constants.FULL_VIDEO_REQUEST_CODE);*/

            Bundle bundle = new Bundle();
            bundle.putString(ArticleParams.ID, String.valueOf(postsItem.getArticle_Id()));
            bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.VIDEO);
            IWatchPresenter.loadFragment(YoutubeFragment.class.getSimpleName(), bundle);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("TAGRESULT ", " requestCode " + requestCode);
        if(requestCode == Constants.FULL_VIDEO_REQUEST_CODE){
            IHomeView.updateDownloadData();
        }
    }

    public void updateDataOfArticles(ArticlePostItem postItem){
        int count = IWatchPresenter.getCount();
        for(int i=0; i<count; i++){
            ArticlePostItem articlePostItem = IWatchPresenter.getArticle(i);
            if(articlePostItem.getArticle_Id().equalsIgnoreCase(postItem.getArticle_Id())){
                CurrentUser currentUser = articlePostItem.getCurrent_user();
                if(currentUser != null){
                    currentUser.setBookmarked(postItem.getCurrent_user().isBookmarked());
                }
                updateAdapter();
                break;
            }
        }
    }

    private void showToast(CurrentUser currentUser) {
        boolean isBookMark = currentUser.isBookmarked();
        String str = getString(R.string.added_to_my_hunt);//getString(R.string.saved);
        if(!isBookMark){
            str = getString(R.string.removed_from_my_hunt);//getString(R.string.removed);
        }
        IHomeView.showToast(str);
    }

    public void updateData(){
        IWatchPresenter.fetchVideoArticles();
    }
}
