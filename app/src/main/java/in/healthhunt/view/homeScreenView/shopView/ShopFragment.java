package in.healthhunt.view.homeScreenView.shopView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.preference.HealthHuntPreference;
import in.healthhunt.presenter.homeScreenPresenter.shopPresenter.IShopPresenter;
import in.healthhunt.presenter.homeScreenPresenter.shopPresenter.ShopPresenterImp;
import in.healthhunt.view.fullView.fullViewFragments.FullProductFragment;
import in.healthhunt.view.homeScreenView.IHomeView;
import in.healthhunt.view.homeScreenView.filterView.FilterFragment;

/**
 * Created by abhishekkumar on 5/19/18.
 */

public class ShopFragment extends Fragment implements IShopView, ShopAdapter.ClickListener{

    private IShopPresenter IShopPresenter;
    private Unbinder mUnbinder;

    @BindView(R.id.recycler_view)
    RecyclerView mShopViewer;

    @BindView(R.id.suggestion_header)
    TextView mSuggestionHeader;

    @BindView(R.id.suggestion_cross)
    ImageView mSuggestionCross;

    @BindView(R.id.suggestion_content)
    TextView mSuggestionContent;

    /*@BindView(R.id.filter_button)
    Button mFilterButton;
*/
    /*@BindView(R.id.filter_view)
    LinearLayout mFilterView;
*/
    @BindView(R.id.shop_view)
    LinearLayout mSuggestionView;

    @BindView(R.id.no_records)
    TextView mNoRecords;

    /* @BindView(R.id.shop_view)
     LinearLayout mShopView;
 */
    private IHomeView IHomeView;
    private Map<Integer, List<String>> mFilterData;

    private final int FRAGMENT_FILTER_REQUEST = 1000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IShopPresenter = new ShopPresenterImp(getContext(), this);
        IHomeView = (IHomeView) getActivity();
        setHasOptionsMenu(true);
        IShopPresenter.fetchMarkets();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mSuggestionHeader.setText(getString(R.string.healthhunt_shop));
        mSuggestionContent.setText(R.string.healthhunt_shop_content);
        Log.i("TAGSHOWPR", "IHomeView ONCREATE " + IHomeView);
        IHomeView.updateTitle(getString(R.string.shop));
        setAdapter();
        return view;
    }

    @Override
    public int getCount() {
        return IShopPresenter.getCount();
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
    public ShopViewHolder createViewHolder(View view) {
        return new ShopViewHolder(view, IShopPresenter, this);
    }

    @Override
    public void updateAdapter() {
        if(mShopViewer != null && mShopViewer.getAdapter() != null) {
            mShopViewer.getAdapter().notifyDataSetChanged();
            updateVisibility();
        }
    }

    public void updateVisibility(){
        int count = IShopPresenter.getCount();
        if(count == 0){
            mNoRecords.setVisibility(View.VISIBLE);
            mShopViewer.setVisibility(View.GONE);
        }
        else {
            mNoRecords.setVisibility(View.GONE);
            mShopViewer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void handleFilterData(Map<Integer, List<String>> map) {
        IHomeView.updateTitle(getString(R.string.shop));
        mFilterData = map;
        if(mFilterData == null || mFilterData.isEmpty()){
            IShopPresenter.fetchMarkets();
        }
        else if(mFilterData != null && !mFilterData.isEmpty()){
            IShopPresenter.fetchMarketFilters(mFilterData);
        }

        Log.i("TAGSHOP", "MAP "+ map);
    }

    @Override
    public Map<Integer, List<String>> getFilterData() {
        return mFilterData;
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IHomeView.loadNonFooterFragment(fragmentName, bundle);
    }

    @Override
    public void updateProductSaved(ProductPostItem productPostItem) {
        IHomeView.updateMyhuntsProductSaved(productPostItem);
        showToast(productPostItem.getCurrent_user());
        IHomeView.updateMyFeedProduct(productPostItem);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick(R.id.suggestion_cross)
    void onCrossClick(){
        mSuggestionView.setVisibility(View.GONE);
        HealthHuntPreference.putBoolean(getContext(), Constants.SHOP_FRAGMENT_SUGG_KEY, true);
        /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFilterButton.getLayoutParams();
        int margin = HealthHuntUtility.dpToPx(4,getContext());
        params.setMargins(params.getMarginStart(),margin, params.getMarginEnd(), params.bottomMargin - margin);
        mFilterButton.setLayoutParams(params);
        mFilterView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hh_green_light2));*/
    }

    //@OnClick(R.id.filter_button)
    void onFilterClick(){
        IHomeView.updateTitle(getString(R.string.filter));
        IHomeView.hideBottomNavigationSelection();
        IHomeView.getHomePresenter().loadNonFooterFragment(FilterFragment.class.getSimpleName(), null);
    }

    private void setAdapter() {
        ShopAdapter shopAdapter = new ShopAdapter(getContext(), IShopPresenter);
        shopAdapter.setClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mShopViewer.setLayoutManager(layoutManager);
        //mShopViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(2, getContext()), SpaceDecoration.GRID));
        mShopViewer.setAdapter(shopAdapter);
    }

    @Override
    public void ItemClicked(View v, int position) {
        ProductPostItem postsItem = IShopPresenter.getProduct(position);
        if(postsItem != null) {
            /*Intent intent = new Intent(getContext(), FullViewActivity.class);
            intent.putExtra(ArticleParams.ID, String.valueOf(postsItem.getMedia_id()));
            intent.putExtra(ArticleParams.POST_TYPE, ArticleParams.PRODUCT);
            getContext().startActivity(intent);*/

            Bundle bundle = new Bundle();
            bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.PRODUCT);
            bundle.putString(ArticleParams.ID, String.valueOf(postsItem.getProduct_id()));
            IShopPresenter.loadFragment(FullProductFragment.class.getSimpleName(), bundle);
        }
    }

    public void updateDataOfProducts(ProductPostItem productPostItem){
        List<ProductPostItem> list = IShopPresenter.getAllProducts();
        for(ProductPostItem postItem: list){
            if(postItem.getProduct_id().equalsIgnoreCase(productPostItem.getProduct_id())){
                CurrentUser currentUser = postItem.getCurrent_user();
                if(currentUser != null){
                    currentUser.setBookmarked(productPostItem.getCurrent_user().isBookmarked());
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if(menu != null){
            /*MenuItem item =
                    menu.add(Menu.NONE, R.id.filter_icon, 10, R.string.filter);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            item.setIcon(R.drawable.ic_fitness);*/
            inflater.inflate(R.menu.filter_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.filter_icon:
                onFilterClick();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
