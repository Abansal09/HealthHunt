package in.healthhunt.view.homeScreenView.myHuntsView.myHuntsShopView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import in.healthhunt.model.login.User;
import in.healthhunt.presenter.homeScreenPresenter.myHuntPresenter.myHuntsShopPresenter.IMyHuntsProductsPresenter;
import in.healthhunt.presenter.homeScreenPresenter.myHuntPresenter.myHuntsShopPresenter.MyHuntsShopPresenterImp;
import in.healthhunt.view.fullView.fullViewFragments.FullProductFragment;
import in.healthhunt.view.homeScreenView.IHomeView;
import in.healthhunt.view.homeScreenView.myHuntsView.IMyHuntsView;

public class MyHuntsShopFragment extends Fragment implements IMyHuntsView, MyHuntsShopAdapter.ClickListener{


    private IMyHuntsProductsPresenter IMyHuntsProductsPresenter;
    private Unbinder mUnbinder;

    @BindView(R.id.recycler_view)
    RecyclerView mProductViewer;

    @BindView(R.id.top_navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.no_records)
    TextView mNoRecords;

    private int mNavigationType;
    private IHomeView IHomeView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IHomeView = (IHomeView) getActivity();
        IMyHuntsProductsPresenter = new MyHuntsShopPresenterImp(getContext(), this);
        mNavigationType = ArticleParams.SAVED;
        String userId = User.getCurrentUser().getUserId();//HealthHuntPreference.getString(getContext(), Constants.USER_ID);
        Log.i("TAGUSRRID", "USER ID " + userId);
        IMyHuntsProductsPresenter.fetchProducts(userId);
        fetchDownloadProducts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_hunts_shop_view, container, false);
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
        return new MyHuntsShopHolder(view, IMyHuntsProductsPresenter, this);
    }

    @Override
    public void updateAdapter() {
        mProductViewer.getAdapter().notifyDataSetChanged();
        updateVisibility();
    }

    public void updateVisibility(){
        int count = IMyHuntsProductsPresenter.getCount();
        if(count == 0){
            mNoRecords.setVisibility(View.VISIBLE);
            mProductViewer.setVisibility(View.GONE);
        }
        else {
            mNoRecords.setVisibility(View.GONE);
            mProductViewer.setVisibility(View.VISIBLE);
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
        IHomeView.updateMyFeedArticle(articlePostItem);
        showToast(articlePostItem.getCurrent_user());
    }

    @Override
    public void updateSavedProduct(ProductPostItem productPostItem) {
        IHomeView.updateMyFeedProduct(productPostItem);
        IHomeView.updateShop(productPostItem);
        IHomeView.updateMyhuntsProductSaved(productPostItem);
        showToast(productPostItem.getCurrent_user());
    }

    @Override
    public void deletePost(String id) {
        List<ProductPostItem> postItems = IMyHuntsProductsPresenter.getProductList();
        if(postItems !=null && !postItems.isEmpty()){
            for(ProductPostItem postItem: postItems){
                if(postItem.getProduct_id().equalsIgnoreCase(id)){
                    postItems.remove(postItem);
                    updateAdapter();
                    break;
                }
            }
        }
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
        ProductPostItem postsItem = IMyHuntsProductsPresenter.getProduct(position);
        if(postsItem != null) {
            /*Intent intent = new Intent(mContext, FullViewActivity.class);
            intent.putExtra(ArticleParams.ID, String.valueOf(postsItem.getMedia_id()));
            intent.putExtra(ArticleParams.POST_TYPE, ArticleParams.PRODUCT);
            mContext.startActivity(intent);*/

            Bundle bundle = new Bundle();
            bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.PRODUCT);
            bundle.putString(ArticleParams.ID, String.valueOf(postsItem.getProduct_id()));
            if(mNavigationType == ArticleParams.DOWNLOADED) {
                bundle.putBoolean(Constants.IS_DOWNLOADED, true);
            }
            IMyHuntsProductsPresenter.loadFragment(FullProductFragment.class.getSimpleName(), bundle);
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
                        List<ProductPostItem> productPostItems = IMyHuntsProductsPresenter.getProductList();
                        if(productPostItems != null && !productPostItems.isEmpty()){
                            ProductPostItem productPostItem = productPostItems.get(position);
                            if(mNavigationType == ArticleParams.DOWNLOADED) {
                                ProductPostItem.removeProduct(productPostItem.getProduct_id());
                                productPostItems.remove(position);
                                updateAdapter();
                            }
                            else if(mNavigationType == ArticleParams.APPROVED){
                                IMyHuntsProductsPresenter.deleteProduct(productPostItem.getProduct_id());
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
        MyHuntsShopAdapter adapter = new MyHuntsShopAdapter(getContext(), IMyHuntsProductsPresenter);
        adapter.setClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mProductViewer.setLayoutManager(layoutManager);
       // mProductViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(8, getContext()), SpaceDecoration.GRID));
        mProductViewer.setAdapter(adapter);
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

    private void fetchDownloadProducts() {
        FetchProductsTask productsTask = new FetchProductsTask();
        //IHomeView.showProgress();
        productsTask.execute();
    }

    private class FetchProductsTask extends AsyncTask<Void, Void, List<ProductPostItem>> {

        @Override
        protected List<ProductPostItem> doInBackground(Void... voids) {

            List<ProductPostItem> productPost = new Select().from(ProductPostItem.class).execute();

            for(ProductPostItem product: productPost) {
                Log.i("TAGDATA", "Myhunt shop " + product);

                List<MediaItem> mediaItem = new Select().from(MediaItem.class).
                        where("parent_id = ?" , product.getProduct_id()).execute();
                product.setMedia(mediaItem);

                List<CategoriesItem> categoriesItems = new Select().from(CategoriesItem.class).
                        where("parent_id = ?" , product.getProduct_id()).execute();

                product.setCategories(categoriesItems);

                List<TagsItem> tagsItems = new Select().from(TagsItem.class).
                        where("parent_id = ?" , product.getProduct_id()).execute();
                product.setTags(tagsItems);

                CurrentUser currentUser = product.getCurrent_user();
                if(currentUser != null){
                    List<Collections> collections = new Select().from(Collections.class).
                            where("parent_id = ?" , product.getProduct_id()).execute();
                    currentUser.setCollections(collections);
                }
            }

            return productPost;
        }

        @Override
        protected void onPostExecute(List<ProductPostItem> productPosts) {
            super.onPostExecute(productPosts);
            // IHomeView.hideProgress();
            IMyHuntsProductsPresenter.updateDownloadList(productPosts);
        }
    }

    public void updateDownloadData(){
        fetchDownloadProducts();
    }

    public void updateSavedData(ProductPostItem postItem){
        IMyHuntsProductsPresenter.updateProductSaved(postItem);
        updateAdapter();
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