package in.healthhunt.view.homeScreenView.filterView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.presenter.filterPresenter.FilterPresenterImp;
import in.healthhunt.presenter.filterPresenter.IFilterPresenter;
import in.healthhunt.view.homeScreenView.IHomeView;
import in.healthhunt.view.homeScreenView.filterView.brandView.BrandAdapter;
import in.healthhunt.view.homeScreenView.filterView.cityView.LocationAdapter;
import in.healthhunt.view.homeScreenView.filterView.productView.ProductAdapter;

/**
 * Created by abhishekkumar on 5/23/18.
 */

public class FilterFragment extends Fragment implements IFilterView, ProductAdapter.ClickListener,
        BrandAdapter.ClickListener, LocationAdapter.ClickListener {

    private Unbinder mUnbinder;

    @BindView(R.id.product_filter)
    RelativeLayout mProductFilter;

    @BindView(R.id.brand_filter)
    RelativeLayout mBrandFilter;

    @BindView(R.id.city_filter)
    RelativeLayout mCityFilter;

    @BindView(R.id.product_view)
    LinearLayout mProductView;

    @BindView(R.id.brand_view)
    LinearLayout mBrandView;

    @BindView(R.id.city_view)
    LinearLayout mCityView;

    @BindView(R.id.filter_product_text)
    TextView mProductText;

    @BindView(R.id.filter_brand_text)
    TextView mBrandText;

    @BindView(R.id.filter_city_text)
    TextView mCityText;

    @BindView(R.id.product_recycler_list)
    RecyclerView mProductViewer;

    @BindView(R.id.brand_recycler_list)
    RecyclerView mBrandViewer;

    @BindView(R.id.city_recycler_list)
    RecyclerView mCityViewer;

    @BindView(R.id.product_arrow_icon)
    ImageView mProductArrow;

    @BindView(R.id.brand_arrow_icon)
    ImageView mBrandArrow;

    @BindView(R.id.city_arrow_icon)
    ImageView mCityArrow;

    @BindView(R.id.product_search_view)
    EditText mProductSearch;

    @BindView(R.id.brand_search_view)
    EditText mBrandSearch;

    @BindView(R.id.city_search_view)
    EditText mCitySearch;

    @BindView(R.id.cross_product)
    ImageView mCrossProduct;

    @BindView(R.id.cross_brand)
    ImageView mCrossBrand;

    @BindView(R.id.cross_city)
    ImageView mCrossCity;

    @BindView(R.id.remove)
    TextView mRemove;


    private IFilterPresenter IFilterPresenter;
    private IHomeView IHomeView;
    private ProductAdapter mProductAdapter;
    private BrandAdapter mBrandAdapter;
    private LocationAdapter mLocationAdapter;
    //private CityAdapter mCityAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IHomeView = (IHomeView) getActivity();
        IFilterPresenter = new FilterPresenterImp(getContext(), this);
        IFilterPresenter.fetchFilters(ArticleParams.PRODUCT_TYPE);
        IFilterPresenter.fetchFilters(ArticleParams.BRANDS);

        Map<Integer, List<String>> map = IHomeView.getFilterData();
        if(map != null && !map.isEmpty()){
            List<String> ids = map.get(Constants.PRODUCT_FILTER);
            if(ids != null && !ids.isEmpty()) {
                for (String item : ids) {
                    IFilterPresenter.addProduct(item);
                }
            }

            ids = map.get(Constants.BRAND_FILTER);
            if(ids != null && !ids.isEmpty()){
                for(String item: ids){
                    IFilterPresenter.addBrand(item);
                }
            }

            ids = map.get(Constants.CITY_FILTER);
            if(ids != null && !ids.isEmpty()){
                IFilterPresenter.setCity(ids.get(0));
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        IHomeView.hideKeyboardIfOpen();
        IHomeView.showActionBar();
        IHomeView.hideDrawerMenu();
        IHomeView.hideSearchView();
        IHomeView.updateTitle(getString(R.string.filter));
        mProductSearch.setHint(R.string.search_by_product_type);
        mBrandSearch.setHint(R.string.search_by_brand_name);

        if(IHomeView.getFilterData() != null && !IHomeView.getFilterData().isEmpty()){
            updateRemoveColor(R.color.hh_green_light2);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mUnbinder != null){
            mUnbinder.unbind();
        }
    }

    @OnClick(R.id.product_filter)
    void onProduct(){
        if(IFilterPresenter.getFilterProductCount() <= 0){
            Toast.makeText(getContext(), "No product available", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mProductView.getVisibility() == View.VISIBLE){
            updateProductViewVisibility(View.GONE);
        }
        else {
            updateProductViewVisibility(View.VISIBLE);
        }

        updateBrandViewVisibility(View.GONE);
        updateCityViewVisibility(View.GONE);
        mProductViewer.setLayoutManager(new LinearLayoutManager(getContext()));
        if(mProductAdapter == null) {
            mProductAdapter = new ProductAdapter(getContext(), IFilterPresenter, ArticleParams.PRODUCT_TYPE);
            mProductAdapter.setClickListener(this);
            mProductViewer.setAdapter(mProductAdapter);
            addProductSearchFilter();
        }
    }

    @OnClick(R.id.brand_filter)
    void onBrand(){
        if(IFilterPresenter.getFilterBrandCount() <= 0){
            Toast.makeText(getContext(), "No brand available", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mBrandView.getVisibility() == View.VISIBLE){
            updateBrandViewVisibility(View.GONE);
        }
        else {
            updateBrandViewVisibility(View.VISIBLE);
        }

        updateProductViewVisibility(View.GONE);
        updateCityViewVisibility(View.GONE);
        mBrandViewer.setLayoutManager(new LinearLayoutManager(getContext()));
        if(mBrandAdapter == null) {
            mBrandAdapter = new BrandAdapter(getContext(), IFilterPresenter);
            mBrandAdapter.setClickListener(this);
            mBrandViewer.setAdapter(mBrandAdapter);
            addBrandSearchFilter();
        }
    }

    @OnClick(R.id.city_filter)
    void onCity(){
        if(mCityView.getVisibility() == View.VISIBLE){
            updateCityViewVisibility(View.GONE);
        }
        else {
            updateCityViewVisibility(View.VISIBLE);
        }
        updateProductViewVisibility(View.GONE);
        updateBrandViewVisibility(View.GONE);
        String city = IFilterPresenter.getCity();
        if(city != null){
            mCitySearch.setText(city);
        }
        mCityViewer.setLayoutManager(new LinearLayoutManager(getContext()));
        if(mLocationAdapter == null) {
            mLocationAdapter = new LocationAdapter(getContext(), IFilterPresenter);
            mLocationAdapter.setClickListener(this);
            mCityViewer.setAdapter(mLocationAdapter);
            addCitySearchFilter();
        }



        /*if(mCityAdapter == null) {
            mCityAdapter = new CityAdapter(getContext(), R.layout.filter_item, IFilterPresenter);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            width = width - HealthHuntUtility.dpToPx(20, getContext());
            mCitySearch.setDropDownWidth(width);
            mCitySearch.setDropDownHorizontalOffset(HealthHuntUtility.dpToPx(2, getContext()));
            mCitySearch.setDropDownVerticalOffset(HealthHuntUtility.dpToPx(1, getContext()));
            mCitySearch.setAdapter(mCityAdapter);

            mCitySearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String description = (String) parent.getItemAtPosition(position);
                    IFilterPresenter.setCity(description);
                    updateRemoveColor(R.color.hh_green_light2);
                }
            });
        }*/
    }

    @OnClick(R.id.remove)
    void onRemove(){
        IHomeView.popTopBackStack();
        IHomeView.sendFilterData(null);
    }

    @OnClick(R.id.apply)
    void onApply(){
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        if(IFilterPresenter.getProductList() != null){
            map.put(Constants.PRODUCT_FILTER, IFilterPresenter.getProductList());
        }

        if(IFilterPresenter.getBrandList() != null){
            map.put(Constants.BRAND_FILTER, IFilterPresenter.getBrandList());
        }

        if(IFilterPresenter.getCity() != null){
            List<String> city = new ArrayList<String>();
            city.add(IFilterPresenter.getCity());
            map.put(Constants.CITY_FILTER, city);
        }
        IHomeView.popTopBackStack();
        IHomeView.sendFilterData(map);
    }

    private void addProductSearchFilter() {
        mProductSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text ["+s+"]");

                String str = s.toString();
                mProductAdapter.getFilter().filter(str);
                if(str.isEmpty()){
                    mCrossProduct.setVisibility(View.GONE);
                }
                else {
                    mCrossProduct.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void addBrandSearchFilter() {
        mBrandSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text ["+s+"]");

                String str = s.toString();
                mBrandAdapter.getFilter().filter(str);

                if(str.isEmpty()){
                    mCrossBrand.setVisibility(View.GONE);
                }
                else {
                    mCrossBrand.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void addCitySearchFilter() {
        mCitySearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Text ["+s+"]");
                String str = s.toString();
                mLocationAdapter.getFilter().filter(str);

                if(str.isEmpty()){
                    mCrossCity.setVisibility(View.GONE);
                }
                else {
                    mCrossCity.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void ItemClicked() {

        if((IFilterPresenter.getProductList() != null && !IFilterPresenter.getProductList().isEmpty())
                || IFilterPresenter.getCity() != null ||
                (IFilterPresenter.getBrandList() != null && !IFilterPresenter.getBrandList().isEmpty())) {
            updateRemoveColor(R.color.hh_green_light2);
        }
        else {
            updateRemoveColor(R.color.hh_text_color2);
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

    private void updateRemoveColor(int color){
        mRemove.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    private void updateProductViewVisibility(int isVisible){
        if(isVisible == View.VISIBLE) {
            mProductView.setVisibility(View.VISIBLE);
            mProductText.setTextColor(ContextCompat.getColor(getContext(), R.color.hh_edittext_text_color));
            mProductArrow.setImageResource(R.mipmap.ic_chevron_up);
            mProductViewer.setVisibility(View.VISIBLE);
        }
        else {
            mProductView.setVisibility(View.GONE);
            mProductText.setTextColor(ContextCompat.getColor(getContext(), R.color.hh_text_color));
            mProductArrow.setImageResource(R.mipmap.ic_chevron_down);
            mProductViewer.setVisibility(View.GONE);
        }
    }

    private void updateBrandViewVisibility(int isVisible){
        if(isVisible == View.VISIBLE) {
            mBrandView.setVisibility(View.VISIBLE);
            mBrandText.setTextColor(ContextCompat.getColor(getContext(), R.color.hh_edittext_text_color));
            mBrandArrow.setImageResource(R.mipmap.ic_chevron_up);
            mBrandViewer.setVisibility(View.VISIBLE);
        }
        else {
            mBrandView.setVisibility(View.GONE);
            mBrandText.setTextColor(ContextCompat.getColor(getContext(), R.color.hh_text_color));
            mBrandArrow.setImageResource(R.mipmap.ic_chevron_down);
            mBrandViewer.setVisibility(View.GONE);
        }
    }

    private void updateCityViewVisibility(int isVisible){
        if(isVisible == View.VISIBLE) {
            mCityView.setVisibility(View.VISIBLE);
            mCityText.setTextColor(ContextCompat.getColor(getContext(), R.color.hh_edittext_text_color));
            mCityArrow.setImageResource(R.mipmap.ic_chevron_up);
            mCityViewer.setVisibility(View.VISIBLE);
        }
        else {
            mCityView.setVisibility(View.GONE);
            mCityText.setTextColor(ContextCompat.getColor(getContext(), R.color.hh_text_color));
            mCityArrow.setImageResource(R.mipmap.ic_chevron_down);
            mCityViewer.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.cross_product)
    void onCrossProduct(){
        mProductSearch.setText("");
    }

    @OnClick(R.id.cross_brand)
    void onCrossBrand(){
        mBrandSearch.setText("");
    }

    @OnClick(R.id.cross_city)
    void onCrossCity(){
        mCitySearch.setText("");
    }
}
