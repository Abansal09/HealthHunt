package in.healthhunt.view.homeScreenView.myHuntsView.myHuntsVideoView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.CategoriesItem;
import in.healthhunt.model.articles.commonResponse.Collections;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.commonResponse.MediaItem;
import in.healthhunt.model.articles.commonResponse.TagsItem;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.beans.SpaceDecoration;
import in.healthhunt.model.login.User;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.myHuntPresenter.myHuntsVideoPresenter.IMyHuntsVideoPresenter;
import in.healthhunt.presenter.homeScreenPresenter.myHuntPresenter.myHuntsVideoPresenter.MyHuntsVideoPresenterImp;
import in.healthhunt.view.fullView.fullViewFragments.YoutubeFragment;
import in.healthhunt.view.homeScreenView.IHomeView;
import in.healthhunt.view.homeScreenView.myHuntsView.IMyHuntsView;

public class MyHuntsVideoFragment extends Fragment implements IMyHuntsView, MyHuntsVideoAdapter.ClickListener{


    private IMyHuntsVideoPresenter IMyHuntsVideoPresenter;
    private Unbinder mUnbinder;

    @BindView(R.id.recycler_view)
    RecyclerView mArticleViewer;

    @BindView(R.id.top_navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.no_records)
    TextView mNoRecords;

    private int mNavigationType;
    private IHomeView IHomeView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigationType = ArticleParams.SAVED;
        IHomeView = (IHomeView) getActivity();
        IMyHuntsVideoPresenter = new MyHuntsVideoPresenterImp(getContext(), this);
        String userId = User.getCurrentUser().getUserId();//HealthHuntPreference.getString(getContext(), Constants.USER_ID);
        Log.i("TAGUSRRID", "USER ID " + userId);
        IMyHuntsVideoPresenter.fetchVideos(userId);
        fetchDownloadVideos();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_hunts_video_view, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        removeShiftMode(mNavigation);

        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                switch (id){
                    case R.id.navigation_saved:
                        mNavigationType = ArticleParams.SAVED;
                        break;

                    case R.id.navigation_approved:
                        mNavigationType = ArticleParams.APPROVED;
                        break;

                    case R.id.navigation_received:
                        mNavigationType = ArticleParams.RECEIVED;
                        break;

                    case R.id.navigation_downloaded:
                        mNavigationType = ArticleParams.DOWNLOADED;
                        break;
                }

                updateAdapter();
                return true;
            }
        });

        setAdapter();
        return view;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return new MyHuntsVideoHolder(view, IMyHuntsVideoPresenter, this);
    }

    @Override
    public void updateAdapter() {
        mArticleViewer.getAdapter().notifyDataSetChanged();
        updateVisibility();
    }

    public void updateVisibility(){
        int count = IMyHuntsVideoPresenter.getCount();
        if(count == 0){
            mNoRecords.setVisibility(View.VISIBLE);
            mArticleViewer.setVisibility(View.GONE);
        }
        else {
            mNoRecords.setVisibility(View.GONE);
            mArticleViewer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getType() {
        return mNavigationType;
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IHomeView.loadNonFooterFragment(fragmentName, bundle);
    }

    @Override
    public void updateSavedArticle(ArticlePostItem articlePostItem) {
        IHomeView.updateWatch(articlePostItem);
        IHomeView.updateMyhuntsVideoSaved(articlePostItem);
        showToast(articlePostItem.getCurrent_user());
    }

    @Override
    public void updateSavedProduct(ProductPostItem productPostItem) {
        showToast(productPostItem.getCurrent_user());
    }

    @Override
    public void deletePost(String id) {
        List<ArticlePostItem> postItems = IMyHuntsVideoPresenter.getVideoList();
        if(postItems !=null && !postItems.isEmpty()){
            for(ArticlePostItem postItem: postItems){
                if(postItem.getArticle_Id().equalsIgnoreCase(id)){
                    postItems.remove(postItem);
                    updateAdapter();
                    break;
                }
            }
        }
    }

    private void fetchDownloadVideos() {
        FetchVideoTask articlesTask = new FetchVideoTask();
        //IHomeView.showProgress();
        articlesTask.execute();
    }

    @Override
    public int getCount() {
        return 0;
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

    }

    @Override
    public void ItemClicked(View v, int position) {
        ArticlePostItem postsItem = IMyHuntsVideoPresenter.getVideo(position);
        if(postsItem != null) {
            /*Intent intent = new Intent(getContext(), FullVideoActivity.class);
            intent.putExtra(ArticleParams.ID, String.valueOf(postsItem.getArticle_Id()));
            intent.putExtra(ArticleParams.POST_TYPE, ArticleParams.VIDEO);
            */

            Bundle bundle = new Bundle();
            bundle.putString(ArticleParams.ID, String.valueOf(postsItem.getArticle_Id()));
            bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.VIDEO);

            if(mNavigationType == ArticleParams.DOWNLOADED) {
                bundle.putBoolean(Constants.IS_DOWNLOADED, true);
            }

            IMyHuntsVideoPresenter.loadFragment(YoutubeFragment.class.getSimpleName(), bundle);
            //startActivityForResult(intent, Constants.FULL_VIDEO_REQUEST_CODE);
        }
    }

    @Override
    public void onLongClicked(int position) {
        if(mNavigationType == ArticleParams.DOWNLOADED || mNavigationType == ArticleParams.APPROVED) {
            showDialog(position);
        }
    }

    private void showDialog(final int position) {

        AlertDialog alertDialog =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.delete_message))

                .setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dialog.dismiss();
                    }

                })



                .setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        List<ArticlePostItem> articlePostItems = IMyHuntsVideoPresenter.getVideoList();
                        if(articlePostItems != null && !articlePostItems.isEmpty()){
                            ArticlePostItem postItem = articlePostItems.get(position);
                            if(mNavigationType == ArticleParams.DOWNLOADED) {
                                articlePostItems.remove(position);
                                updateAdapter();
                            }
                            else if(mNavigationType == ArticleParams.APPROVED){
                                IMyHuntsVideoPresenter.deleteArticle(postItem.getArticle_Id());
                            }
                        }

                    }
                })
                .create();

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    private void setAdapter() {
        MyHuntsVideoAdapter adapter = new MyHuntsVideoAdapter(getContext(), IMyHuntsVideoPresenter);
        adapter.setClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mArticleViewer.setLayoutManager(layoutManager);
        mArticleViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(8, getContext()), SpaceDecoration.GRID));
        mArticleViewer.setAdapter(adapter);
    }

    @SuppressLint("RestrictedApi")
    public void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BottomNav", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BottomNav", "Unable to change value of shift mode", e);
        }
    }

    private class FetchVideoTask extends AsyncTask<Void, Void, List<ArticlePostItem>> {

        @Override
        protected List<ArticlePostItem> doInBackground(Void... voids) {
            List<ArticlePostItem> articlePostItems = new Select().from(ArticlePostItem.class).
                    where("is_Video = ?" , true).execute();

            for(ArticlePostItem article: articlePostItems) {
                Log.i("TAGDATA", "Myhunt Article " + article);

                List<MediaItem> mediaItem = new Select().from(MediaItem.class).
                        where("parent_id = ?" , article.getArticle_Id()).execute();
                article.setMedia(mediaItem);

                List<CategoriesItem> categoriesItems = new Select().from(CategoriesItem.class).
                        where("parent_id = ?" , article.getArticle_Id()).execute();

                article.setCategories(categoriesItems);

                List<TagsItem> tagsItems = new Select().from(TagsItem.class).
                        where("parent_id = ?" , article.getArticle_Id()).execute();
                article.setTags(tagsItems);

                CurrentUser currentUser = article.getCurrent_user();
                if(currentUser != null){
                    List<Collections> collections = new Select().from(Collections.class).
                            where("parent_id = ?" , article.getArticle_Id()).execute();
                    currentUser.setCollections(collections);
                }
            };

            return articlePostItems;
        }

        @Override
        protected void onPostExecute(List<ArticlePostItem> articlePosts) {
            super.onPostExecute(articlePosts);
            IMyHuntsVideoPresenter.updateDownloadList(articlePosts);
        }
    }

    public void updateDownloadData(){
        fetchDownloadVideos();
    }

    public void updateSavedData(ArticlePostItem postItem){
        IMyHuntsVideoPresenter.updateVideoSaved(postItem);
        updateAdapter();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("TAGRESULT ", " requestCode " + requestCode);
        if(requestCode == Constants.FULL_VIDEO_REQUEST_CODE){
            updateDownloadData();
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
}