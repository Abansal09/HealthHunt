package in.healthhunt.view.homeScreenView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.healthhunt.R;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.tags.TagItem;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.HomePresenterImp;
import in.healthhunt.presenter.homeScreenPresenter.IHomePresenter;
import in.healthhunt.view.BaseActivity;
import in.healthhunt.view.fullView.fullViewFragments.FullArticleFragment;
import in.healthhunt.view.fullView.fullViewFragments.FullProductFragment;
import in.healthhunt.view.fullView.fullViewFragments.YoutubeFragment;
import in.healthhunt.view.homeScreenView.drawerView.DrawerFragment;
import in.healthhunt.view.homeScreenView.filterView.FilterFragment;
import in.healthhunt.view.homeScreenView.myFeedView.MyFeedFragment;
import in.healthhunt.view.homeScreenView.myHuntsView.MyHuntFragment;
import in.healthhunt.view.homeScreenView.shopView.IShopView;
import in.healthhunt.view.homeScreenView.shopView.ShopFragment;
import in.healthhunt.view.homeScreenView.watchView.WatchFragment;
import in.healthhunt.view.notificationView.NotificationFragment;
import in.healthhunt.view.profileView.EditProfileFragment;
import in.healthhunt.view.profileView.ProfileFragment;
import in.healthhunt.view.searchView.SearchFragment;
import in.healthhunt.view.viewAll.ViewAllFragment;
import io.fabric.sdk.android.Fabric;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public class HomeActivity extends BaseActivity implements IHomeView{


    private IHomePresenter IHomePresenter;
    private Map<String,Fragment> fragmentMap = new HashMap<String, Fragment>();

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mNavigation;

    @BindView(R.id.nvView)
    NavigationView mNavigationView;

    @BindView(R.id.category_viewer)
    LinearLayout mCategoryViewer;

    @BindView(R.id.horizontalScrollView)
    HorizontalScrollView mHorizontalScrollView;

    //@BindView(R.id.my_feed_frame)
    //FrameLayout mFeedLayout;

    //@BindView(R.id.shop_frame)
    //FrameLayout mShopLayout;


    private FragmentManager mFragmentManager;
    private Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;
    private Dialog mAlertDialog;
    private DrawerFragment mDrawerFragment;

    private Fragment[] mFragment = new Fragment[4];

    private int mCurrentItem;

    private MenuItem mSearchAction;
    private MenuItem mNotificationAction;
    public final static int HOME_REQUEST_CODE = 101;

    private List<String> mCategoryList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());

        mCategoryList = new ArrayList<String>();
        IHomePresenter = new HomePresenterImp(getApplicationContext(), this);
        IHomePresenter.fetchCategories();

        mAlertDialog = new Dialog(this);
        mAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlertDialog.setContentView(R.layout.alertdialog_view);

        mFragmentManager = getSupportFragmentManager();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        addDrawer();
        removeShiftMode(mNavigation);

        //Select select = new Select();


        /*List<Title> titles = select.from(Title.class).execute();

        for(Title title: titles) {
            Log.i("TAGPOSTSINGLE", "DATA title " + title);
        }*/

        // List<MediaItem> items = select.from(MediaItem.class).execute();
        //ProductPost post = ProductPost.load(ProductPost.class, produc.getProduct_id());
        /*for(MediaItem mediaItem: items) {
            Log.i("TAGPOSTSINGLE", "Media  " + mediaItem);
        }*/
        //List<ProductPostItem> productPost = select.from(ProductPostItem.class).execute();

        /*for(ProductPost produc: productPost) {
                List<MediaItem> mediaItem = new Select().from(MediaItem.class).where("parent_id = ?" , produc.getProduct_id()).execute();
            //Log.i("TAGPOSTSINGLE", "Id " + produc.getProduct_id());

            Log.i("TAGPOSTSINGLE", "mediaItem  " + mediaItem );

           Log.i("TAGPOSTSINGLE", "Product Post  " + produc );
        }*/

        IHomePresenter.loadFooterFragment(MyFeedFragment.class.getSimpleName(), null);
        updateTitle(getString(R.string.my_feed));

        mNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setCheckable(true);
                popAllBackStack();
                showActionBar();
                hideDrawerMenu();

                if(item.getItemId() == R.id.navigation_my_feed) {
                    showDrawerMenu();
                    mCurrentItem = Constants.FRAGMENT_MY_FEED;
                    //updateTitle(getString(R.string.my_feed));
                    Fragment fragment = mFragment[mCurrentItem];
                    if(fragment == null){
                        IHomePresenter.loadFooterFragment(MyFeedFragment.class.getSimpleName(), null);
                    }
                    updateCategoryIfNeeded();
                    updateMyFeedOfContinue();
                    //mHorizontalScrollView.setVisibility(View.VISIBLE);
                    //mCategoryViewer.setVisibility(View.VISIBLE);
                }
                else if(item.getItemId() == R.id.navigation_my_hunts) {
                    mCurrentItem = Constants.FRAGMENT_MY_HUNTS;
                    //updateTitle(getString(R.string.my_hunts));
                    Fragment fragment = mFragment[mCurrentItem];
                    if(fragment == null){
                        IHomePresenter.loadFooterFragment(MyHuntFragment.class.getSimpleName(), null);
                    }
                    mHorizontalScrollView.setVisibility(View.GONE);
                    mCategoryViewer.setVisibility(View.GONE);
                }
                else if(item.getItemId() == R.id.navigation_watch) {
                    showDrawerMenu();
                    mCurrentItem = Constants.FRAGMENT_WATCH;
                    //updateTitle(getString(R.string.watch));
                    Fragment fragment = mFragment[mCurrentItem];
                    if(fragment == null){
                        IHomePresenter.loadFooterFragment(WatchFragment.class.getSimpleName(), null);
                    }
                    updateCategoryIfNeeded();
                }
                else if(item.getItemId() == R.id.navigation_shop) {
                    mCurrentItem = Constants.FRAGMENT_SHOP;
                    //updateTitle(getString(R.string.shop));
                    Fragment fragment = mFragment[mCurrentItem];
                    if(fragment == null){
                        IHomePresenter.loadFooterFragment(ShopFragment.class.getSimpleName(), null);
                    }
                    mHorizontalScrollView.setVisibility(View.GONE);
                    mCategoryViewer.setVisibility(View.GONE);
                }
                updateTitleWithFooter(mCurrentItem);
                updateFragVisible(mCurrentItem);
                return true;
            }
        });
    }

    private void popAllBackStack() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        for(int i=0;i <count; i++){
            popTopBackStack();
        }
    }

    private void addDrawer() {
        mDrawerToggle = new android.support.v7.app.
                ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(mDrawerFragment != null){
                    mDrawerFragment.updateUserData();
                    //mDrawerFragment.updateCategory();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Fragment fragment = mFragmentManager.findFragmentById(R.id.main_frame);
                Log.i("TAGFRAG", "fragment " + fragment);
                if(fragment == null && !isUpdateCategory()) {
                    updateDataBasedOnCategory();
                }
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mDrawerFragment = new DrawerFragment();
        fragmentTransaction.replace(R.id.drawer_frame, mDrawerFragment);
        fragmentTransaction.commit();

    }

    private void updateDataBasedOnCategory(){
        updateCategoryList();
        updateMyFeed();
        updateWatch();
        updateCategoryView();
    }

    private void updateFragVisible(int pos){
        for(int i=0; i<mFragment.length; i++){
            Fragment fragment = mFragment[i];
            if(fragment != null){
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                if(i == pos){
                    fragmentTransaction.show(fragment);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                }
                else {
                    fragmentTransaction.hide(fragment);
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                }

                fragmentTransaction.commit();
            }
        }
    }

    private void hideAllFooterFragment(){
        for(int i=0; i<mFragment.length; i++){
            Fragment fragment = mFragment[i];
            if(fragment != null){
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.hide(fragment);
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void updateDownloadData() {
        Fragment fragment = mFragment[Constants.FRAGMENT_MY_HUNTS];
        if(fragment != null && fragment instanceof MyHuntFragment){
            ((MyHuntFragment)fragment).updateDownloadData();
        }
    }

    @Override
    public void updateMyhuntsArticleSaved(ArticlePostItem articlePostItem) {
        Fragment fragment = mFragment[Constants.FRAGMENT_MY_HUNTS];
        if(fragment != null && fragment instanceof MyHuntFragment){
            ((MyHuntFragment)fragment).updateSavedArticleData(articlePostItem);
        }
    }

    @Override
    public void updateMyhuntsVideoSaved(ArticlePostItem articlePostItem) {
        Fragment fragment = mFragment[Constants.FRAGMENT_MY_HUNTS];
        if(fragment != null && fragment instanceof MyHuntFragment){
            ((MyHuntFragment)fragment).updateSavedVideoData(articlePostItem);
        }
    }

    @Override
    public void updateMyhuntsProductSaved(ProductPostItem productPostItem) {
        Fragment fragment = mFragment[Constants.FRAGMENT_MY_HUNTS];
        if(fragment != null && fragment instanceof MyHuntFragment){
            ((MyHuntFragment)fragment).updateSavedProductData(productPostItem);
        }
    }

    @Override
    public void showDrawerMenu() {
        /*Menu menu = mNavigationView.getMenu();
        menu.findItem(android.R.id.home).setVisible(true);
        *//*ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.*/
        //actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    @Override
    public void hideDrawerMenu() {
        //mNavigationView.setVisibility(View.GONE);
        mDrawerToggle.setDrawerIndicatorEnabled(false);

    }

    @Override
    public void updateMyFeedArticle(ArticlePostItem articlePostItem) {
        Fragment fragment = mFragment[Constants.FRAGMENT_MY_FEED];
        if(fragment != null && fragment instanceof MyFeedFragment){
            ((MyFeedFragment) fragment).updateDataOfArticles(articlePostItem);
        }
    }

    @Override
    public void updateMyFeedProduct(ProductPostItem productPostItem) {
        Fragment fragment = mFragment[Constants.FRAGMENT_MY_FEED];
        if(fragment != null && fragment instanceof MyFeedFragment){
            ((MyFeedFragment) fragment).updateDataOfProducts(productPostItem);
        }
    }

    @Override
    public void updateWatch(ArticlePostItem articlePostItem) {
        Fragment fragment = mFragment[Constants.FRAGMENT_WATCH];
        if(fragment != null && fragment instanceof WatchFragment){
            ((WatchFragment) fragment).updateDataOfArticles(articlePostItem);
        }
    }

    @Override
    public void updateShop(ProductPostItem productPostItem) {
        Fragment fragment = mFragment[Constants.FRAGMENT_SHOP];
        if(fragment != null && fragment instanceof ShopFragment){
            ((ShopFragment) fragment).updateDataOfProducts(productPostItem);
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public List<String> getCategories() {
        return mCategoryList;
    }

    @Override
    public void loadNonFooterFragment(String tag, Bundle bundle) {
        if(tag != null) {
            int layout = R.id.main_frame;

            Fragment fragment = null;
            if (tag.equals(ViewAllFragment.class.getSimpleName())) {
                fragment = new ViewAllFragment();
            } else if (tag.equals(FilterFragment.class.getSimpleName())) {
                fragment = new FilterFragment();
            }
            else if (tag.equals(FullArticleFragment.class.getSimpleName())) {
                fragment = new FullArticleFragment();
            }
            else if (tag.equals(FullProductFragment.class.getSimpleName())) {
                fragment = new FullProductFragment();
            }
            else if (tag.equals(NotificationFragment.class.getSimpleName())) {
                fragment = new NotificationFragment();
            }
            else if (tag.equals(ProfileFragment.class.getSimpleName())) {
                fragment = new ProfileFragment();
            }
            else if (tag.equals(EditProfileFragment.class.getSimpleName())) {
                fragment = new EditProfileFragment();
            }
            else if (tag.equals(SearchFragment.class.getSimpleName())) {
                fragment = new SearchFragment();
            }
            else if (tag.equals(YoutubeFragment.class.getSimpleName())) {
                fragment = new YoutubeFragment();
            }

            if (bundle != null && fragment != null) {
                fragment.setArguments(bundle);
            }

            if(fragment != null) {
                showNonFooterFragment(fragment, tag, layout);
            }

            hideAllFooterFragment(); // to hide all the previous fragments
            //hideDrawerMenu();
        }
    }

    @Override
    public void popTopBackStack() {
        mFragmentManager.popBackStack();
    }

    @Override
    public void sendFilterData(Map<Integer, List<String>> map) {
        updateFragVisible(mCurrentItem);
        setBottomNavigation();
        showSearchView();
        Fragment fragment = mFragment[mCurrentItem];
        if(mFragment != null && fragment instanceof ShopFragment){
            ((IShopView)fragment).handleFilterData(map);
        }
    }

    @Override
    public Map<Integer, List<String>> getFilterData() {
        Fragment fragment = mFragment[mCurrentItem];
        if(mFragment != null && fragment instanceof ShopFragment){
            return ((IShopView)fragment).getFilterData();
        }
        return null;
    }

    @Override
    public void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void updateMyFeed() {
        Fragment fragment = mFragment[Constants.FRAGMENT_MY_FEED];
        if(fragment != null && fragment instanceof MyFeedFragment){
            ((MyFeedFragment)fragment).updateData();
        }
    }

    @Override
    public void updateWatch() {
        Fragment fragment = mFragment[Constants.FRAGMENT_WATCH];
        if(fragment != null && fragment instanceof WatchFragment){
            ((WatchFragment)fragment).updateData();
        }
    }

    @Override
    public void updateCategoryVisibility() {
        if(mCategoryList != null && !mCategoryList.isEmpty() && !mCategoryList.contains("1")){
            mHorizontalScrollView.setVisibility(View.VISIBLE);
            mCategoryViewer.setVisibility(View.VISIBLE);
        }
        else {
            mHorizontalScrollView.setVisibility(View.GONE);
            mCategoryViewer.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateDrawerFragment() {
        if(mDrawerFragment != null){
            mDrawerFragment.updateAdapter();
            updateCategoryList();
        }
    }

    @Override
    public void updateCategoryList() {
        List<TagItem> tagItemList = IHomePresenter.getSelectedCategoryList();

        if(tagItemList != null) {
            mCategoryList.clear();

            for (TagItem tagItem : tagItemList) {
                mCategoryList.add(String.valueOf(tagItem.getId()));
            }
        }
    }

    @Override
    public void hideSearchView() {
        if(mSearchAction != null) {
            mSearchAction.setVisible(false);
        }
    }

    @Override
    public void showSearchView() {
        if(mSearchAction != null) {
            mSearchAction.setVisible(true);
        }
    }

    @Override
    public void hideNotificationView() {
        if(mNotificationAction != null){
            mNotificationAction.setVisible(false);
        }
    }

    @Override
    public void showNotificationView() {
        if(mNotificationAction != null){
            mNotificationAction.setVisible(true);
        }
    }

    @Override
    public void addContinueArticle(ArticlePostItem postItem, boolean needToAdd) {
        Fragment fragment = mFragment[Constants.FRAGMENT_MY_FEED];
        if(fragment instanceof MyFeedFragment){
            ((MyFeedFragment)fragment).addContinueArticle(postItem, needToAdd);
        }
    }

    @Override
    public void hideActionBar() {
        mToolbar.setVisibility(View.GONE);
    }

    @Override
    public void showActionBar() {
        mToolbar.setVisibility(View.VISIBLE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
        actionBar.setDisplayShowTitleEnabled(true); //show the title in the action bar
        showSearchView();

        if(mNotificationAction != null) {
            mNotificationAction.setVisible(true);
        }
    }

    private void showNonFooterFragment(Fragment fragment, String tag, int layout) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(layout, fragment);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
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

    @Override
    public IHomePresenter getHomePresenter() {
        return IHomePresenter;
    }

    @Override
    public void hideBottomFooter() {
        mNavigation.setVisibility(View.GONE);
    }

    @Override
    public void showBottomFooter() {
        mNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadFooterFragment(String tag, Bundle bundle) {
        int layout = R.id.my_feed_frame;
        Fragment fragment = null;
        if (tag.equals(MyFeedFragment.class.getSimpleName())) {
            layout = R.id.my_feed_frame;
            if (fragment == null) {
                fragment = new MyFeedFragment();
            }
        }  else if (tag.equals(MyHuntFragment.class.getSimpleName())) {
            layout = R.id.my_hunt_frame;
            if (fragment == null) {
                fragment = new MyHuntFragment();
            }
        }
        else if (tag.equals(WatchFragment.class.getSimpleName())) {
            layout = R.id.watch_frame;
            if (fragment == null) {
                fragment = new WatchFragment();
            }
        }
        else if (tag.equals(ShopFragment.class.getSimpleName())) {
            layout = R.id.shop_frame;
            if (fragment == null) {
                fragment = new ShopFragment();
            }
        }

        mFragment[mCurrentItem] = fragment;
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        showFooterFragment(fragment, layout);
    }

    private void showFooterFragment(Fragment fragment, int layout) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(layout, fragment);
        fragmentTransaction.commit();
    }




    /*private void setAdapter() {
        HomeScreenAdapter homeScreenAdapter = new HomeScreenAdapter(mHomePresenterImp, getSupportFragmentManager());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mHomeScreenViewer.setLayoutManager(layoutManager);
        mHomeScreenViewer.setAdapter(homeScreenAdapter);
    }*/

    /*@Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }*/

    @Override
    public void showHomeAlert(String msg) {

        if (!((Activity) this).isFinishing()) {

            mAlertDialog.setCancelable(false);
            TextView message = mAlertDialog.findViewById(R.id.alert_message);
            message.setText(msg);

            String str = getResources().getString(R.string.alert);
            TextView title = mAlertDialog.findViewById(R.id.alert_title);
            title.setText(str);

            Button okButton = mAlertDialog.findViewById(R.id.action_button);
            okButton.setText(android.R.string.ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAlertDialog.dismiss();
                    finishActivity();
                }
            });
            mAlertDialog.show();
        }
    }

    @Override
    public void showAlert(String msg) {
        if (!((Activity) this).isFinishing()) {

            mAlertDialog.setCancelable(false);
            TextView message = mAlertDialog.findViewById(R.id.alert_message);
            message.setText(msg);

            String str = getResources().getString(R.string.alert);
            TextView title = mAlertDialog.findViewById(R.id.alert_title);
            title.setText(str);

            Button okButton = mAlertDialog.findViewById(R.id.action_button);
            okButton.setText(android.R.string.ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAlertDialog.dismiss();
                }
            });
            mAlertDialog.show();
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void onBackPressed() {

        Log.i("TAGCOUNTfragment", "onBackPressed ");
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            Fragment fragment = mFragmentManager.findFragmentById(R.id.main_frame);    // When we press back button of full video screen.
            if(fragment instanceof YoutubeFragment){
                YoutubeFragment youtubeFragment = (YoutubeFragment)fragment;
                boolean isBack = youtubeFragment.onBackPressed();
                if(isBack){
                    return;
                }
            }
            else if(fragment instanceof FullArticleFragment){
                FullArticleFragment fullArticleFragment = (FullArticleFragment) fragment;
                boolean isBack = fullArticleFragment.onBackPressed();
                if(isBack){
                    return;
                }
            }


            Log.i("TAGCOUNTfragment", "fragment " + fragment);


            int count = getSupportFragmentManager().getBackStackEntryCount();
            Log.i("TAGCOUNT", "count " + count);
            if(count == 1){ //when comes back to home screen
                updateHomeScreen();
                updateCategoryIfNeeded();
            }
            super.onBackPressed();
        }

        /*int value = getResources().getConfiguration().orientation;

        if (value == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/
    }

    private void updateCategoryIfNeeded() {
        if(mCurrentItem == Constants.FRAGMENT_MY_FEED || mCurrentItem == Constants.FRAGMENT_WATCH){
            updateCategoryVisibility();
            if(!isUpdateCategory()) {
                updateDataBasedOnCategory();
            }
        }
    }

    @Override
    public void hideBottomNavigationSelection() {
        int count = mNavigation.getMenu().size();
        Log.i("TAGCOUNT ", "count" + count);
        for(int i=0; i<count; i++) {
            mNavigation.getMenu().getItem(i).setCheckable(false);
        }
    }

    @Override
    public void setBottomNavigation() {
        mNavigation.getMenu().getItem(mCurrentItem).setCheckable(true);
    }

    @Override
    public void updateTitle(String msg) {
        //SpannableString s = new SpannableString(msg);
        //.setSpan(new ForegroundColorSpan(Color.WHITE), 0, msg.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(msg);
        //mToolbar.setT
    }

    @Override
    public void showProgress(){
        if(mProgress != null) {
            mProgress.show();
            ;
        }
    }

    @Override
    public void hideProgress(){
        if(mProgress != null) {
            mProgress.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgress.dismiss();
        mAlertDialog.dismiss();
    }

    private void updateHomeScreen(){
        setStatusBarTranslucent(false);
        showActionBar();
        updateFragVisible(mCurrentItem);
        setBottomNavigation();
        updateTitleWithFooter(mCurrentItem);
        showBottomFooter();

        if(mCurrentItem == 0 || mCurrentItem == 2){ // for my feed and watch screen
            showDrawerMenu();
        }
        else {
            hideDrawerMenu();
        }

        updateMyFeedOfContinue();
    }

    private void updateMyFeedOfContinue() {
        Fragment fragment = mFragment[Constants.FRAGMENT_MY_FEED];
        if(fragment != null && fragment instanceof MyFeedFragment){
            ((MyFeedFragment)fragment).updateContinueArticles();
        }
    }

    private void updateTitleWithFooter(int pos){
        String str = "";
        switch (pos){
            case 0:
                str = getString(R.string.my_feed);
                break;

            case 1:
                str = getString(R.string.my_hunts);
                break;

            case 2:
                str = getString(R.string.watch);
                break;

            case 3:
                str = getString(R.string.shop);
                break;
        }
        updateTitle(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.notification_id:
                if(HealthHuntUtility.checkInternetConnection(getApplicationContext())) {
                    hideKeyboardIfOpen();
                    IHomePresenter.loadNonFooterFragment(NotificationFragment.class.getSimpleName(), null);
                }
                else {
                    showAlert(getString(R.string.please_check_internet_connectivity_status));
                }
                return true;
            case R.id.article_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch() {
        hideSearchView();
        ActionBar action = getSupportActionBar(); //get the actionbar

        action.setDisplayShowCustomEnabled(true); //enable it to display a
        // custom view in the action bar.
        action.setCustomView(R.layout.search_layout);//add the custom view
        action.setDisplayShowTitleEnabled(false); //hide the title

        final EditText edtSeach = (EditText) action.getCustomView().findViewById(R.id.edtSearch); //the text editor
        final ImageView cross = (ImageView) action.getCustomView().findViewById(R.id.cross); //the text editor


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtSeach.setText("");
            }
        });

        //this is a listener to do a search when the user clicks on search button
        edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if(HealthHuntUtility.checkInternetConnection(getApplicationContext())) {
                        String searchStr = v.getText().toString();

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

                        if(searchStr.isEmpty()){
                            showToast(getString(R.string.search_validation));
                            return false;
                        }

                        Fragment fragment = mFragmentManager.findFragmentById(R.id.main_frame);
                        if (fragment instanceof SearchFragment) {
                            ((SearchFragment) fragment).updateData(searchStr);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.SEARCH_TEXT_KEY, searchStr);
                            IHomePresenter.loadNonFooterFragment(SearchFragment.class.getSimpleName(), bundle);
                        }
                    }
                    else {
                        showAlert(getString(R.string.please_check_internet_connectivity_status));
                    }

                    return true;
                }
                return false;
            }
        });

        edtSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if(str.isEmpty()){
                    cross.setVisibility(View.GONE);
                }
                else {
                    cross.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        edtSeach.requestFocus();

        //open the keyboard focused in the edtSearch
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*MenuItem searchViewMenuItem = menu.findItem(R.id.article_search);
        SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchViewMenuItem);
        int searchImgId = android.support.v7.appcompat.R.id.search_button; // I used the explicit layout ID of searchview's ImageView
        ImageView v = (ImageView) mSearchView.findViewById(searchImgId);
        v.setImageResource(R.mipmap.ic_search_white_icon);
        */
        mSearchAction = menu.findItem(R.id.article_search);
        mNotificationAction = menu.findItem(R.id.notification_id);
        return super.onPrepareOptionsMenu(menu);
    }

    public void hideKeyboardIfOpen() {
        View view = findViewById(android.R.id.content);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(view != null){
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateCategoryView() {
        List<String> categoryList = getCategories();

        Log.i("TAGCategoryList", "List " + categoryList);
        if(categoryList == null || categoryList.isEmpty() || categoryList.contains("1")){ // 1 for All Category
            removeAllCategory();
            mHorizontalScrollView.setVisibility(View.GONE);
            return;
        }


        removeAllCategory();
        //mHorizontalScrollView.setVisibility(View.VISIBLE);
        for(String id: categoryList) {
            addCategoryView(id);
        }
    }

    private void addCategoryView(String id) {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.category_my_feed_item_view, null);
        ImageView image = view.findViewById(R.id.category_image);
        TextView textView = view.findViewById(R.id.category_name);


        TagItem tagItem = IHomePresenter.getSelectedCategory(id);
        String name = tagItem.getName();
        textView.setText(name);

        if(name.equalsIgnoreCase(Constants.LOVE)){
            LinearLayout imageBack = view.findViewById(R.id.category_image_bg);
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_view);
            drawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hh_red_light), PorterDuff.Mode.SRC_ATOP);
            imageBack.setBackground(drawable);
        }
        else if(name.equalsIgnoreCase(Constants.FITNESS)){
            LinearLayout imageBack = view.findViewById(R.id.category_image_bg);
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_view);
            drawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hh_blue_light), PorterDuff.Mode.SRC_ATOP);
            imageBack.setBackground(drawable);
        }
        else if(name.equalsIgnoreCase(Constants.NUTRITION)){
            LinearLayout imageBack = view.findViewById(R.id.category_image_bg);
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_view);
            drawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hh_orange_light), PorterDuff.Mode.SRC_ATOP);
            imageBack.setBackground(drawable);
        }
        else if(name.equalsIgnoreCase(Constants.MENTAL_WELLBEING)){
            LinearLayout imageBack = view.findViewById(R.id.category_image_bg);
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_view);
            drawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hh_yello_light), PorterDuff.Mode.SRC_ATOP);
            imageBack.setBackground(drawable);
        }
        else if(name.equalsIgnoreCase(Constants.ORGANIC_BEAUTY)){
            LinearLayout imageBack = view.findViewById(R.id.category_image_bg);
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.circle_view);
            drawable.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.hh_green_light2), PorterDuff.Mode.SRC_ATOP);
            imageBack.setBackground(drawable);
        }

        int src = HealthHuntUtility.getCategoryIcon(name);
        image.setImageResource(src);

        view.setTag(tagItem);
        mCategoryViewer.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IHomePresenter.removeCategory((TagItem) view.getTag());
                updateCategoryList();
                mCategoryViewer.removeView(view);
                // will update the articles list
                /*if(mCategoryViewer.getChildCount() == 0){
                    //mCategoryViewer.setVisibility(View.GONE);
                    mCategoryViewer.removeAllViews();
                    mHorizontalScrollView.setVisibility(View.GONE);
                }*/
                updateMyFeed();
                updateWatch();
                updateDrawerFragment();
                //updateCategoryVisibility();
            }
        });
    }

    private void removeAllCategory() {
        mCategoryViewer.removeAllViews();
    }

    private boolean isUpdateCategory(){

        List<TagItem> catergory = IHomePresenter.getSelectedCategoryList();
        List<String> prevCategory = mCategoryList;

        if(catergory == null || prevCategory == null){
            Log.i("TAGTAG", "category is null");
            return false;
        }

        int count = catergory.size();
        int prevCount = prevCategory.size();

        if(count == prevCount){
            List<String> selectedCategory = new ArrayList<String>();
            for(TagItem tagItem: catergory){
                selectedCategory.add(String.valueOf(tagItem.getId()));
            }

            if(prevCategory.containsAll(selectedCategory)){
                return true;
            }
        }
        return false;
    }
}
