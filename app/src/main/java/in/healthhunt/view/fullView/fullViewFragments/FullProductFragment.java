package in.healthhunt.view.fullView.fullViewFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import framework.permisisons.Permissions;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.commonResponse.Author;
import in.healthhunt.model.articles.commonResponse.CategoriesItem;
import in.healthhunt.model.articles.commonResponse.Content;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.commonResponse.Excerpt;
import in.healthhunt.model.articles.commonResponse.Likes;
import in.healthhunt.model.articles.commonResponse.MediaItem;
import in.healthhunt.model.articles.commonResponse.Synopsis;
import in.healthhunt.model.articles.commonResponse.TagsItem;
import in.healthhunt.model.articles.commonResponse.Title;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.beans.SpaceDecoration;
import in.healthhunt.model.comment.CommentsItem;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.model.utility.URLImageParser;
import in.healthhunt.presenter.fullPresenter.FullPresenterImp;
import in.healthhunt.presenter.fullPresenter.IFullPresenter;
import in.healthhunt.view.fullView.commentView.CommentAdapter;
import in.healthhunt.view.fullView.commentView.CommentViewHolder;
import in.healthhunt.view.homeScreenView.HomeActivity;
import in.healthhunt.view.homeScreenView.IHomeView;
import in.healthhunt.view.viewAll.ViewAllFragment;

/**
 * Created by abhishekkumar on 5/24/18.
 */

public class FullProductFragment extends Fragment implements IFullFragment, CommentAdapter.ClickListener,
        RelatedProductAdapter.ClickListener, TextToSpeech.OnInitListener {

    @BindView(R.id.full_product_name)
    TextView mProductName;

    @BindView(R.id.full_likes_count)
    TextView mLikesCount;

    @BindView(R.id.full_product_like)
    ImageView mLikeImage;

    @BindView(R.id.full_product_image)
    ImageView mProductImage;

    @BindView(R.id.full_product_description)
    TextView mContent;

    @BindView(R.id.comment_count)
    TextView mCommentCount;

    @BindView(R.id.comments_view_all_text)
    TextView mCommentViewAll;

    @BindView(R.id.comments_view_all_arrow)
    ImageView mCommentArrow;

    @BindView(R.id.comment_view)
    LinearLayout mCommentView;

    @BindView(R.id.full_product_download)
    ImageView mDownloadView;

    @BindView(R.id.full_product_bookmark)
    ImageView mBookMark;

    @BindView(R.id.comments_recycler_list)
    RecyclerView mCommentViewer;

    @BindView(R.id.full_view_scroll)
    NestedScrollView mFullViewScroll;

    @BindView(R.id.comment_content)
    EditText mCommentContent;

    @BindView(R.id.email)
    TextView mEmail;

    @BindView(R.id.phone)
    TextView mPhone;

    @BindView(R.id.buy_product_name)
    TextView mBuyProductName;

    @BindView(R.id.buy_product_type)
    TextView mBuyProductType;

    @BindView(R.id.buy_product_price)
    TextView mBuyProductPrice;

    /* @BindView(R.id.top_product_unit)
     TextView mBuyProductUnit;
 */
    @BindView(R.id.related_product_recycler_list)
    RecyclerView mRelatedProductViewer;

    @BindView(R.id.related_product_view_all)
    LinearLayout mProductViewAll;

    @BindView(R.id.related_product_view)
    LinearLayout mRelatedProductView;

    @BindView(R.id.detail_text)
    ReadMoreTextView mDetailText;

    @BindView(R.id.about_name)
    TextView mAboutName;


    private IFullPresenter IFullPresenter;
    private IHomeView IHomeView;
    private Unbinder mUnbinder;
    private int mType;
    private boolean isDownloaded;
    private String mId;
    private TextToSpeech mTextToSpeech;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IFullPresenter = new FullPresenterImp(getContext(), this);
        IHomeView  =(HomeActivity) getActivity();


        mId = getArguments().getString(ArticleParams.ID);
        mType = getArguments().getInt(ArticleParams.POST_TYPE);
        isDownloaded = getArguments().getBoolean(Constants.IS_DOWNLOADED);
        mTextToSpeech = new TextToSpeech(getContext(),this);
    }

    private void fetchProductFromDataBase(String id) {
        IHomeView.showProgress();
        FetchProductsTask productsTask = new FetchProductsTask(id);
        productsTask.execute();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_product_view, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        IHomeView.setStatusBarTranslucent(true);
        IHomeView.hideBottomFooter();
        IHomeView.hideActionBar();
        if(!isDownloaded) {
            IFullPresenter.fetchProduct(mId);
        }
        else {
            fetchProductFromDataBase(mId);
        }
        setContent();
        return view;
    }

    @Override
    public void onDestroy() {
        if(mUnbinder != null){
            mUnbinder.unbind();
        }

        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }

        super.onDestroy();
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

    /*@Override
    public void showFullViewAlert(String msg) {
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
    public void setContent() {
        ProductPostItem productPost = IFullPresenter.getProduct();

        if (productPost != null) {
            setTopImageContent(productPost);
            setProductContent(productPost);
            setEmailInfo(productPost);
            setBuyInfo(productPost);
            setAboutContent(productPost);
            setCommentContent(productPost);
            setRelatedProductAdapter();
        }
    }

    private void setRelatedProductAdapter() {
        if(IFullPresenter.getRelatedProductsCount() > 0){
            mRelatedProductView.setVisibility(View.VISIBLE);
        }
        else {
            mRelatedProductView.setVisibility(View.GONE);
        }

        RelatedProductAdapter relatedProductAdapter = new RelatedProductAdapter(getContext(), IFullPresenter);
        relatedProductAdapter.setClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRelatedProductViewer.setLayoutManager(layoutManager);
        mRelatedProductViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(8, getContext()), SpaceDecoration.VERTICAL));
        mRelatedProductViewer.setAdapter(relatedProductAdapter);
        mRelatedProductViewer.setFocusableInTouchMode(false);
    }

    @Override
    public int getPostType() {
        return mType;
    }

    @Override
    public void updateBookMarkIcon() {
        CurrentUser currentUser = null;
        ProductPostItem productPost = IFullPresenter.getProduct();
        currentUser = productPost.getCurrent_user();
        if(currentUser != null) {
            updateBookMark(currentUser.isBookmarked());
        }

        if(isDownloaded & currentUser != null){
            currentUser.save();
            productPost.save();
            IHomeView.updateDownloadData();
        }
    }

    @Override
    public CommentViewHolder createViewHolder(View view) {
        return new CommentViewHolder(view, IFullPresenter, this);
    }

    private void setCommentAdapter(){
        CommentAdapter commentAdapter = new CommentAdapter(getContext(), IFullPresenter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mCommentViewer.setLayoutManager(layoutManager);
        mCommentViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(8, getContext()), SpaceDecoration.VERTICAL));
        mCommentViewer.setAdapter(commentAdapter);
    }

    @Override
    public void updateCommentAdapter() {
        mCommentContent.setText("");
        CommentAdapter commentAdapter = (CommentAdapter) mCommentViewer.getAdapter();
        if(commentAdapter == null){
            setCommentAdapter();
        }
        else {
            commentAdapter.notifyDataSetChanged();
        }

        setCommentContent(IFullPresenter.getProduct());
    }

    @Override
    public void updateLike() {
        CurrentUser currentUser =  IFullPresenter.getProduct().getCurrent_user();
        Likes likes = IFullPresenter.getProduct().getLikes();
        if(currentUser != null) {
            int like = currentUser.getLike();
            long count = 0;
            if(likes != null){
                count = Long.parseLong(likes.getLikes());
            }

            if (like > 0) {
                updateLikeIcon(true);
                count++;
            } else {
                updateLikeIcon(false);
                if(count > 0) {
                    count--;
                }
            }

            if(likes != null){
                likes.setLikes(String.valueOf(count));
                String likeCount = HealthHuntUtility.formatNumber(count);
                mLikesCount.setText(likeCount);
            }
        }
    }

    @Override
    public void updateRelatedArticleAdapter() {

    }

    @Override
    public void updateRelatedProductAdapter() {
        if(mRelatedProductViewer != null){
            mRelatedProductViewer.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        IHomeView.getHomePresenter().loadNonFooterFragment(fragmentName, bundle);
    }

    @Override
    public void updateProductSaved(ProductPostItem postItem) {
        IHomeView.updateMyhuntsProductSaved(postItem);
        showToast(postItem.getCurrent_user());
        IHomeView.updateMyFeedProduct(postItem);
        IHomeView.updateShop(postItem);
    }

    @Override
    public void updateArticleSaved(ArticlePostItem articlePostItem) {
        IHomeView.updateMyhuntsArticleSaved(articlePostItem);
        showToast(articlePostItem.getCurrent_user());
        IHomeView.updateMyFeedArticle(articlePostItem);
    }

    @Override
    public void updateVideoSaved(ArticlePostItem articlePostItem) {

    }

    @OnClick(R.id.buy)
    void onBuy(){
        buyProduct();
    }

    public void updateLikeIcon(boolean isLike) {
        if(!isLike){
            mLikeImage.setColorFilter(null);
            mLikeImage.setImageResource(R.mipmap.ic_full_article_heart);
        }
        else {
            mLikeImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.hh_green_light2), PorterDuff.Mode.SRC_IN);
        }
    }

    @OnClick(R.id.comments_view_all)
    void onViewAll(){
        Log.i("TAGCOMMENT" , " isShown " + mCommentView.isShown());

        if(mCommentView.isShown()){
            mCommentViewAll.setText(R.string.view_all);
            mCommentArrow.setImageResource(R.mipmap.ic_chevron_down);
            mCommentView.setVisibility(View.GONE);
            mCommentViewer.setVisibility(View.GONE);
        }
        else {
            mCommentViewAll.setText(R.string.close_all);
            mCommentArrow.setImageResource(R.mipmap.ic_chevron_up);
            mCommentView.setVisibility(View.VISIBLE);
            mCommentViewer.setVisibility(View.VISIBLE);

            // CommentAdapter adapter = (CommentAdapter) mCommentViewer.getAdapter();
            //if(adapter == null) {
            ProductPostItem post = IFullPresenter.getProduct();
            IFullPresenter.fetchComments(String.valueOf(post.getProduct_id()));
            mFullViewScroll.fullScroll(View.FOCUS_DOWN);
            // }
        }
    }

    @OnClick(R.id.full_product_bookmark)
    void onBookMark(){
        CurrentUser currentUser = null;
        ProductPostItem productPost = IFullPresenter.getProduct();
        if(productPost != null){
            currentUser = productPost.getCurrent_user();
            if(currentUser != null ) {
                if(!currentUser.isBookmarked()) {
                    IFullPresenter.bookmark(String.valueOf(productPost.getProduct_id()), ArticleParams.PRODUCT);
                }
                else {
                    IFullPresenter.unBookmark(String.valueOf(productPost.getProduct_id()), ArticleParams.PRODUCT);
                }
            }
        }
    }

    @OnClick(R.id.send_comment)
    void onSend(View view){

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        String content = mCommentContent.getText().toString().trim();

        if(content.isEmpty()){
            IHomeView.showToast(getString(R.string.please_enter_comment));
            return;
        }

        try {
            byte[] data = content.getBytes("UTF-8");
            content = Base64.encodeToString(data, Base64.DEFAULT);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException){

        }

        ProductPostItem productPost = IFullPresenter.getProduct();
        if(productPost != null){
            String post_id = productPost.getProduct_id();
            if(!content.isEmpty()){
                IFullPresenter.addNewComment(String.valueOf(post_id), content);
            }
        }
    }

    @OnClick(R.id.full_product_like)
    void onLikeClick(){
        String id = IFullPresenter.getProduct().getProduct_id();
        IFullPresenter.updateLike(String.valueOf(id));
    }

    private void setCommentContent(ProductPostItem productPost) {
        String commentCount = productPost .getComments();
        if(commentCount != null){
            long count = Long.parseLong(commentCount);
            if(count > 0){
                commentCount = HealthHuntUtility.formatNumber(count);
                mCommentCount.setText(commentCount);
            }
            else {
                mCommentCount.setText("");
            }
        }
    }

    private void setAboutContent(ProductPostItem productPost) {
        Author author = productPost.getAuthor();
        if(author != null){
            String authorName = author.getName();
            Log.i("TAGNAMNE","NAMe " + authorName);
            mAboutName.setText(authorName);

            String info = author.getInfo();
            mDetailText.setText(info);
        }

    }

    private void setProductContent(ProductPostItem productPost) {

        Content content = productPost.getContent();
        Log.i("TAGCONTENT", "Content " + content.getRendered());
        if (content != null) {
            URLImageParser imageGetter = new URLImageParser(getContext(), mContent);
            Spannable html;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                html = (Spannable) Html.fromHtml(content.getRendered(), Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
            } else {
                html = (Spannable) Html.fromHtml(content.getRendered(), imageGetter, null);
            }
            mContent.setText(html);
        }
    }

    private void setBuyInfo(ProductPostItem productPost) {
        /*String productName = productPost.getPost_name();
        if(productName != null){
            mBuyProductName.setText(productName);
        }*/

        Title title = productPost.getTitle();
        if(title != null){
            String render = title.getRendered();
            mBuyProductName.setText(render);
        }

        String productType = productPost.getCompany_name();
        if(productType != null){
            mBuyProductType.setText(productType);
        }

        String price = productPost.getPost_price();
        if(price != null) {
            String postQuantity = productPost.getPost_quantity();
            String rs = productPost.getPost_currency();
            if(rs == null){
                rs = "";
            }
            Spanned spanned = Html.fromHtml(rs) ;
            rs = spanned.toString();

            price = HealthHuntUtility.addSeparator(price);
            rs = rs + " " + price + "/" + postQuantity;
            rs.trim();

            String postUnit = productPost.getPost_unit();
            if(postUnit != null) {
                rs = rs + postUnit;
            }

            mBuyProductPrice.setText(rs);
        }

        /*String postUnit = productPost.getPost_unit();
        Log.i("TAGTAGUNIT", "unit "+ postUnit);
        if(postUnit != null) {
            postUnit.trim();
            mBuyProductUnit.setText(postUnit);
        }*/
    }

    private void setEmailInfo(ProductPostItem productPost) {
        String email = productPost.getPost_email();
        if(email != null){
            mEmail.setText(email);
        }

        String phone = productPost.getPost_contactno();

        if(phone != null){
            phone = "+" + phone;
            mPhone.setText(phone);
        }
    }

    private void setTopImageContent(ProductPostItem productPost) {


        Title title = productPost.getTitle();
        if(title != null){
            String render = title.getRendered();
            mProductName.setText(render);
        }
        /*String productName = productPost.getPost_name();
        if(productName != null){
            mProductName.setText(productName);
        }*/

        CurrentUser currentUser = productPost.getCurrent_user();
        if(currentUser != null) {
            updateBookMark(currentUser.isBookmarked());
            Log.i("TAGUSR", " user " + currentUser.getLike());
            int like = currentUser.getLike();
            if(like > 0){
                updateLikeIcon(true);
            }
            else {
                updateLikeIcon(false);
            }
        }


        Likes likes = productPost.getLikes();
        if(likes != null){
            String likeCount = likes.getLikes();
            if(likeCount != null){
                likeCount = HealthHuntUtility.formatNumber(Long.valueOf(likeCount));
                mLikesCount.setText(likeCount);
            }
        }




        String url = null;
        List<MediaItem> mediaItems = productPost.getMedia();
        if (mediaItems != null && !mediaItems.isEmpty()) {
            MediaItem media = mediaItems.get(0);
            Log.i("TAGMedia", "media " + media);
            if ("image".equals(media.getMedia_type())) {
                url = media.getUrl();
                Log.i("TAGMedia", "URL " + url);

            }
        }

        if (url != null) {
            Glide.with(this).load(url).placeholder(R.mipmap.ic_no_top_product_image).into(mProductImage);
        } else {
            mProductImage.setBackgroundResource(R.mipmap.ic_no_top_product_image);
        }
    }

    @OnClick(R.id.full_product_download)
    void onDownloadClick(){
        final Dialog mBottomSheetDialog = new Dialog(getContext(), R.style.MaterialDialogSheet);
        View customView = getLayoutInflater().inflate(R.layout.download_popup_window_view,null);
        TextView textView = customView.findViewById(R.id.download_text);
        textView.setText(getString(R.string.download_this_product));
        mBottomSheetDialog.setContentView(customView); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelOffset(R.dimen.popup_window_height));
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);


        LinearLayout shareView = customView.findViewById(R.id.share_view);
        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
                // Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
                shareProduct();
            }
        });

        LinearLayout downloadView = customView.findViewById(R.id.download_article_view);
        downloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
                String msg = getString(R.string.downloaded_successfully);
                if(!isAlreadyDownloaded()) {
                    storeProduct();
                }
                else {
                    msg = getString(R.string.already_downloaded);
                }
                IHomeView.showToast(msg);
            }
        });

        mBottomSheetDialog.show();
    }

    private boolean isAlreadyDownloaded() {
        ProductPostItem productPostItem = ProductPostItem.getProduct(IFullPresenter.getProduct().getProduct_id());
        if(productPostItem != null){
            return true;
        }
        return false;
    }

    private void shareProduct(){
        ProductPostItem productPost = IFullPresenter.getProduct();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, productPost.getLink());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share)));
    }

    private void buyProduct(){
        ProductPostItem productPost = IFullPresenter.getProduct();
        String url = productPost.getPost_url();
        if(url == null || url.isEmpty()){
            IHomeView.showToast("Url is empty");
            return;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(Intent.createChooser(browserIntent, getString(R.string.buy)));
    }

    private void updateBookMark(boolean isBookMark) {
        Log.i("TAGBOOKMARK", "ISBOOK " + isBookMark);
        if(!isBookMark){
            mBookMark.setColorFilter(null);
            mBookMark.setImageResource(R.mipmap.ic_bookmark_full_view);
        }
        else {
            mBookMark.setColorFilter(ContextCompat.getColor(getContext(), R.color.hh_green_light2), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void onMore(View view, final int position) {
        Log.i("TAGPOPUP", "onMore");

        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.setGravity(Gravity.LEFT);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                CommentsItem commentsItem = IFullPresenter.getComment(position);
                int id = item.getItemId();
                switch (id){
                    case R.id.comment_edit:
                        editComment(position);
                        break;
                    case R.id.comment_delete:
                        IFullPresenter.deleteComment(String.valueOf(commentsItem.getId()));
                        break;
                }
                return true;
            }
        });
        popup.inflate(R.menu.comment_menu);
        popup.show();
    }

    @Override
    public void update(View view, int position) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        CommentViewHolder holder = (CommentViewHolder) mCommentViewer.getChildViewHolder(mCommentViewer.getChildAt(position));
        CommentsItem commentsItem = IFullPresenter.getComment(position);
        int id = commentsItem.getId();
        String content = holder.mCommentEditText.getText().toString();

        if(content != null && content.isEmpty()){
            IHomeView.showToast(getString(R.string.please_enter_comment));
            return;
        }

        try {
            byte[] data = content.getBytes("UTF-8");
            content = Base64.encodeToString(data, Base64.DEFAULT);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException){

        }
        IFullPresenter.updateComment(String.valueOf(id), content);
    }

    private void editComment(int position) {
        // CommentsItem commentsItem = IFullPresenter.getComment(position);
        CommentViewHolder holder = (CommentViewHolder) mCommentViewer.getChildViewHolder(mCommentViewer.getChildAt(position));
        holder.mCommentText.setVisibility(View.GONE);
        holder.mCommentEditText.setVisibility(View.VISIBLE);
        holder.mCommentUpdate.setVisibility(View.VISIBLE);
        // Spannable html = (Spannable) Html.fromHtml(commentsItem.getContent().getRendered());
        holder.mCommentEditText.setText(holder.mCommentText.getText().toString().trim());
        holder.mCommentEditText.requestFocus();
    }

    @Override
    public void onRelatedItemClicked(View v, int position, int type) {
        // Intent intent = new Intent(getContext(), FullViewActivity.class);
        String id = null;
        if(type == ArticleParams.PRODUCT){
            ProductPostItem postItem = IFullPresenter.getRelatedProduct(position);
            id = String.valueOf(postItem.getProduct_id());
        }
        Bundle bundle = new Bundle();
        bundle.putString(ArticleParams.ID, String.valueOf(id));
        bundle.putInt(ArticleParams.POST_TYPE, type);
        IFullPresenter.loadFragment(FullProductFragment.class.getSimpleName(), bundle);
    }

    @OnClick(R.id.related_product_view_all)
    void onRelatedProductViewAll(){
        Bundle bundle = new Bundle();
        String id = IFullPresenter.getProduct().getProduct_id();
        bundle.putInt(ArticleParams.ARTICLE_TYPE, ArticleParams.RELATED_PRODUCTS);
        bundle.putString(ArticleParams.ID, String.valueOf(id));
        bundle.putBoolean(Constants.IS_RELATED, true);
        IFullPresenter.loadFragment(ViewAllFragment.class.getSimpleName(), bundle);
    }

    private void storeProduct(){
        ProductPostItem productPost = IFullPresenter.getProduct();

        Log.i("TAGPOSTSINGLE", "product " + productPost);

        if(productPost != null) {
            Title title = productPost.getTitle();
            if (title != null) {
                title.save();
            }

            Content content = productPost.getContent();
            if (content != null) {
                content.save();
            }

            List<MediaItem> mediaItems = productPost.getMedia();
            if (mediaItems != null) {

                for (MediaItem item : mediaItems) {
                    item.setParent_id(productPost.getProduct_id());
                    item.save();
                }
            }

            List<CategoriesItem> categoriesItemList = productPost.getCategories();
            if (categoriesItemList != null) {
                for (CategoriesItem item : categoriesItemList) {
                    item.setParent_id(productPost.getProduct_id());
                    item.save();
                }
            }

            Likes likes = productPost.getLikes();
            if (likes != null) {
                likes.save();
            }

            CurrentUser currentUser = productPost.getCurrent_user();
            if (currentUser != null) {
            /*List<Collections> collections = currentUser.getCollections();
            if(collections != null){
                for(Collections collect: collections) {
                    collect.setParent_id(productPost.getProduct_id());
                    collect.save();
                }
            }*/
                currentUser.save();
            }

            Author author = productPost.getAuthor();
            if (author != null) {
                author.save();
            }

            Synopsis synopsis = productPost.getSynopsis();
            if (synopsis != null) {
                synopsis.save();
            }

            List<TagsItem> tagsItems = productPost.getTags();
            if (tagsItems != null) {
                for (TagsItem tagsItem : tagsItems) {
                    tagsItem.setParent_id(productPost.getProduct_id());
                    tagsItem.save();
                }
            }

            Excerpt excerpt = productPost.getExcerpt();
            if (excerpt != null) {
                excerpt.save();
            }

            productPost.save();
            IHomeView.updateDownloadData();
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = mTextToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //btnSpeak.setEnabled(true);
                //speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut() {

        ProductPostItem postItem  = IFullPresenter.getProduct();

        if(postItem != null) {
            Content content = postItem.getContent();
            Spanned spanned = Html.fromHtml(content.getRendered()) ;
            String text = spanned.toString();
            int maxLen = TextToSpeech.getMaxSpeechInputLength();
            int strLen = text.length();

            if(strLen > maxLen){
                int subParts = strLen/maxLen;
                Log.i("TAGLEN"," Len " + subParts);
                int start = 0;
                int end = maxLen;
                while(subParts>=0){
                    String subStr = text.substring(start, end);
                    addSpeakText(subStr);
                    Log.i("TAGLEN"," SubStr " + subStr);
                    start = end;
                    int remainLen = text.substring(end, text.length()).length();
                    if(remainLen > maxLen){
                        end = end + maxLen;
                    }
                    else {
                        end = end + remainLen;
                    }

                    subParts--;
                }
            }
            else {
                addSpeakText(text.toString());
            }


            /*HashMap<String, String> hash = new HashMap<String,String>();
            hash.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                    String.valueOf(AudioManager.STREAM_NOTIFICATION));
            Log.i("TAGTAGTAGSPEECH","TEXT" + text);
            mTextToSpeech.speak(text.toString(), TextToSpeech.QUEUE_FLUSH, hash);*/
        }
    }

    private void addSpeakText(String text){
        mTextToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    private class FetchProductsTask extends AsyncTask<Void, Void, ProductPostItem> {

        private String mProductID;
        public FetchProductsTask(String id){
            mProductID = id;
        }

        @Override
        protected ProductPostItem doInBackground(Void... voids) {

            ProductPostItem productPost = new Select().from(ProductPostItem.class).
                    where("product_id = ?" , mProductID).executeSingle();

            if(productPost == null){
                Log.i("TAGPOS", "Product Post is null");
                return null;
            }

            Log.i("TAGDATA", "productPost " + productPost);

            /*List<MediaItem> mediaItem = new Select().from(MediaItem.class).
                    where("parent_id = ?" , productPost.getProduct_id()).execute();
            productPost.setMedia(mediaItem);

            List<CategoriesItem> categoriesItems = new Select().from(CategoriesItem.class).
                    where("parent_id = ?" , productPost.getProduct_id()).execute();

            productPost.setCategories(categoriesItems);

            List<TagsItem> tagsItems = new Select().from(TagsItem.class).
                    where("parent_id = ?" , productPost.getProduct_id()).execute();
            productPost.setTags(tagsItems);

            CurrentUser currentUser = productPost.getCurrent_user();
            if(currentUser != null) {
                List<Collections> collections = new Select().from(Collections.class).
                        where("parent_id = ?", productPost.getProduct_id()).execute();
                currentUser.setCollections(collections);
            }*/
            return productPost;
        }

        @Override
        protected void onPostExecute(ProductPostItem productPosts) {
            super.onPostExecute(productPosts);
            IHomeView.hideProgress();
            IFullPresenter.updateProduct(productPosts);
        }
    }

    @OnClick(R.id.email_view)
    void onEmail(){
        ProductPostItem productPost = IFullPresenter.getProduct();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("mailto:" +productPost.getPost_email()));
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share)));
    }

    @OnClick(R.id.phone_view)
    void onCall(){
        ProductPostItem productPost = IFullPresenter.getProduct();
        String phoneNumber = productPost.getPost_contactno();
        HomeActivity homeActivity = (HomeActivity) getActivity();
        if (!TextUtils.isEmpty(phoneNumber)) {
            if (homeActivity.getPermission(Permissions.PERMISSION_CALL_PHONE, HomeActivity.HOME_REQUEST_CODE)) {
                String dial = "tel:" + phoneNumber;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            } /*else {
                Toast.makeText(getContext(), "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
            }*/
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

    // @OnClick(R.id.listen_view)
    void onListen(){

        if(!mTextToSpeech.isSpeaking()) {
            speakOut();
        }
        else {
            mTextToSpeech.stop();
        }
    }
}
