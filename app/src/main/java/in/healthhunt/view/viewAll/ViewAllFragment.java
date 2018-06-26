package in.healthhunt.view.viewAll;

import android.app.ProgressDialog;
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
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.beans.SpaceDecoration;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.viewAllPresenter.IViewAllPresenter;
import in.healthhunt.presenter.viewAllPresenter.ViewAllPresenterImp;
import in.healthhunt.view.fullView.fullViewFragments.FullArticleFragment;
import in.healthhunt.view.fullView.fullViewFragments.FullProductFragment;
import in.healthhunt.view.homeScreenView.HomeActivity;
import in.healthhunt.view.homeScreenView.IHomeView;

/**
 * Created by abhishekkumar on 5/3/18.
 */

public class ViewAllFragment extends Fragment implements IViewAll, ViewAllAdapter.ClickListener{


    @BindView(R.id.view_all_recycler_list)
    RecyclerView mViewAllViewer;

    @BindView(R.id.no_records)
    TextView mNoRecords;

    private IViewAllPresenter IViewAllPresenter;
    private ProgressDialog mProgress;
    private int mType;
    private IHomeView IHomeView;
    private boolean isRelated;
    private String mFullViewItemSelectedId;
    private String mRelatedId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgress = new ProgressDialog(getContext());
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        mProgress.setMessage(getResources().getString(R.string.please_wait));
        IViewAllPresenter = new ViewAllPresenterImp(getContext(), this);
        IHomeView  =(HomeActivity) getActivity();

        Bundle bundle = getArguments();
        String id = null;
        if(bundle != null) {
            mType = bundle.getInt(ArticleParams.ARTICLE_TYPE);
            mRelatedId = bundle.getString(ArticleParams.ID);
            isRelated = bundle.getBoolean(Constants.IS_RELATED);
        }

        Log.i("TAGTYPE " , "TYPE " + mType);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all, container, false);
        ButterKnife.bind(this, view);
        IHomeView.setStatusBarTranslucent(false);
        IHomeView.hideBottomNavigationSelection();
        IHomeView.showBottomFooter();
        IHomeView.hideDrawerMenu();
        IHomeView.showActionBar();
        IHomeView.updateTitle(getArticleName());
        IViewAllPresenter.fetchAll(mType, mRelatedId);
//        if(mFullViewItemSelectedId != null && !mFullViewItemSelectedId.isEmpty()){
//            updateBookOfSelectedItem();
//        }
        setAdapter();
        return view;
    }

  /*  private void updateBookOfSelectedItem() {

        switch (mType){
            case ArticleParams.BASED_ON_TAGS:
            case ArticleParams.LATEST_ARTICLES:
            case ArticleParams.RELATED_ARTICLES:
                updateBookOfSelectedArticle();
                break;
            case ArticleParams.LATEST_PRODUCTS:
            case ArticleParams.RELATED_PRODUCTS:
                updateBookOfSelectedAProduct();
                break;
        }


    }
*/
   /* private void updateBookOfSelectedAProduct() {
        List<ProductPostItem> productPostItems = IViewAllPresenter.getAllProduct();
        if(productPostItems != null && !productPostItems.isEmpty()){
            for (ProductPostItem postItem: productPostItems){
                if(postItem.getProduct_id().equalsIgnoreCase(mFullViewItemSelectedId)){
                    CurrentUser currentUser = postItem.getCurrent_user();
                    if(currentUser != null) {
                        if(!currentUser.isBookmarked()){
                            IViewAllPresenter.bookmark(mFullViewItemSelectedId);
                        }
                        else {
                            IViewAllPresenter.unBookmark(mFullViewItemSelectedId);
                        }
                    }
                    break;
                }
            }
        }
    }

    private void updateBookOfSelectedArticle() {
        List<ArticlePostItem> articlePostItems = IViewAllPresenter.getAllArticles();
        if(articlePostItems != null && !articlePostItems.isEmpty()){
            for (ArticlePostItem postItem: articlePostItems){
                if(postItem.getArticle_Id().equalsIgnoreCase(mFullViewItemSelectedId)){
                    CurrentUser currentUser = postItem.getCurrent_user();
                    if(currentUser != null) {
                        if(!currentUser.isBookmarked()){
                            IViewAllPresenter.bookmark(mFullViewItemSelectedId);
                        }
                        else {
                            IViewAllPresenter.unBookmark(mFullViewItemSelectedId);
                        }
                    }
                    break;
                }
            }
        }
    }
*/
    private void setAdapter() {
        ViewAllAdapter viewAllAdapter = new ViewAllAdapter(getContext(), IViewAllPresenter, mType);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mViewAllViewer.setLayoutManager(layoutManager);
        mViewAllViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(8, getContext()), SpaceDecoration.VERTICAL));
        mViewAllViewer.setAdapter(viewAllAdapter);
    }

    public int getCount() {
        return IViewAllPresenter.getCount(mType);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View view) {

        RecyclerView.ViewHolder  viewHolder = null;
        switch (mType){
            case ArticleParams.BASED_ON_TAGS:
            case ArticleParams.LATEST_ARTICLES:
            case ArticleParams.RELATED_ARTICLES:
                viewHolder = new ViewAllArticleHolder(view, this, IViewAllPresenter);
                break;
            case ArticleParams.LATEST_PRODUCTS:
            case ArticleParams.RELATED_PRODUCTS:
                viewHolder = new ViewAllProductHolder(view, this, IViewAllPresenter);
                break;
        }
        return viewHolder;
    }

    @Override
    public void showProgress() {
        mProgress.show();;
    }

    @Override
    public void hideProgress() {
        mProgress.dismiss();
    }

    @Override
    public void showAlert(String msg) {
        IHomeView.showAlert(msg);
    }

    /*@Override
    public void showViewAllAlert(String msg) {
        final Dialog dialog = new Dialog(getContext());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialog_view);
        dialog.setCancelable(false);
        //dialog.

        TextView message = dialog.findViewById(R.id.alert_message);
        message.setText(msg);

        String str = getResources().getString(R.string.alert);
        TextView title = dialog.findViewById(R.id.alert_title);
        title.setText(str);

        Button okButton = dialog.findViewById(R.id.action_button);
        okButton.setText(android.R.string.ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                IHomeView.popTopBackStack();
            }
        });
        dialog.show();
    }*/

    @Override
    public void updateAdapter() {
        mViewAllViewer.getAdapter().notifyDataSetChanged();
        updateVisibility();
    }

    public void updateVisibility(){
        int count = IViewAllPresenter.getCount(mType);
        if(count == 0){
            mNoRecords.setVisibility(View.VISIBLE);
            mViewAllViewer.setVisibility(View.GONE);
        }
        else {
            mNoRecords.setVisibility(View.GONE);
            mViewAllViewer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getViewLayout() {
        int layout = 0;
        switch (mType) {
            case ArticleParams.BASED_ON_TAGS:
            case ArticleParams.LATEST_ARTICLES:
            case ArticleParams.RELATED_ARTICLES:
                layout = R.layout.view_all_article_item_view;
                break;


            case ArticleParams.LATEST_PRODUCTS:
            case ArticleParams.RELATED_PRODUCTS:
                layout = R.layout.view_all_product_item_view;
                break;
        }

        return layout;
    }

    @Override
    public int getType() {
        return mType;
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

    @Override
    public void updateProductSaved(ProductPostItem postItem) {
        IHomeView.updateMyhuntsProductSaved(postItem);
        showToast(postItem.getCurrent_user());
        IHomeView.updateMyFeedProduct(postItem);
        IHomeView.updateShop(postItem);
    }

    @Override
    public boolean isRelated() {
        return isRelated;
    }

    @Override
    public List<String> getCategories() {
        return IHomeView.getCategories();
    }


    private String getArticleName() {
        String name = "";
        switch (mType) {
            case ArticleParams.BASED_ON_TAGS:
                name = ArticleParams.TEXT_BASED_ON_TAGS;
                break;
            case ArticleParams.LATEST_ARTICLES:
                name = ArticleParams.TEXT_LATEST_ARTICLES;
                break;
            case ArticleParams.LATEST_PRODUCTS:
                name = ArticleParams.TEXT_LATEST_PRODUCTS_ARTICLES;
                break;
            case ArticleParams.RELATED_ARTICLES:
                name = ArticleParams.TEXT_RELATED_ARTICLES;
                break;
            case ArticleParams.RELATED_PRODUCTS:
                name = ArticleParams.TEXT_RELATED_PRODUCTS;
                break;

        }

        return name;
    }

    @Override
    public void ItemClicked(View v, int position) {
        String fragmentTag = FullArticleFragment.class.getSimpleName();
        int postType = ArticleParams.ARTICLE;
        switch (mType){
            case ArticleParams.BASED_ON_TAGS:
            case ArticleParams.LATEST_ARTICLES:
            case ArticleParams.RELATED_ARTICLES:
                mFullViewItemSelectedId = String.valueOf(IViewAllPresenter.getArticle(position).getArticle_Id());
                postType = ArticleParams.ARTICLE;
                fragmentTag = FullArticleFragment.class.getSimpleName();
                break;

            case ArticleParams.LATEST_PRODUCTS:
            case ArticleParams.RELATED_PRODUCTS:
                mFullViewItemSelectedId = String.valueOf(IViewAllPresenter.getProduct(position).getProduct_id());
                postType = ArticleParams.PRODUCT;
                fragmentTag = FullProductFragment.class.getSimpleName();
                break;
        }

        /*Intent intent = new Intent(getContext(), FullViewActivity.class);
        intent.putExtra(ArticleParams.ID, id);
        intent.putExtra(ArticleParams.POST_TYPE, postType);
        startActivity(intent);*/

        Bundle bundle = new Bundle();
        bundle.putInt(ArticleParams.POST_TYPE, postType);
        bundle.putString(ArticleParams.ID, mFullViewItemSelectedId);
        IViewAllPresenter.loadFragment(fragmentTag, bundle);
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
