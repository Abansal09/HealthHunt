package in.healthhunt.view.fullView.fullViewFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.AudioManager;
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
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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
import in.healthhunt.model.login.User;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.model.utility.URLImageParser;
import in.healthhunt.presenter.fullPresenter.FullPresenterImp;
import in.healthhunt.presenter.fullPresenter.IFullPresenter;
import in.healthhunt.view.fullView.commentView.CommentAdapter;
import in.healthhunt.view.fullView.commentView.CommentViewHolder;
import in.healthhunt.view.homeScreenView.HomeActivity;
import in.healthhunt.view.homeScreenView.IHomeView;
import in.healthhunt.view.viewAll.ViewAllFragment;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.util.Log.i;

/**
 * Created by abhishekkumar on 5/24/18.
 */

public class FullArticleFragment extends Fragment implements IFullFragment, CommentAdapter.ClickListener,
        RelatedArticlesAdapter.ClickListener, RelatedProductAdapter.ClickListener, TextToSpeech.OnInitListener {

    @BindView(R.id.full_article_read_time)
    TextView mReadTime;

    @BindView(R.id.full_likes_count)
    TextView mLikesCount;

    @BindView(R.id.full_article_like)
    ImageView mLikeImage;

    @BindView(R.id.full_view_category_name)
    TextView mCategoryName;

    @BindView(R.id.full_article_category_icon)
    ImageView mCategoryImage;

    @BindView(R.id.full_article_image)
    ImageView mArticleImage;

    @BindView(R.id.full_article_content)
    TextView mTitle;

    @BindView(R.id.author_pic)
    ImageView mAuthorImage;

    @BindView(R.id.author_name)
    TextView mAuthorName;

    @BindView(R.id.author_publish_date)
    TextView mPublishDate;

    @BindView(R.id.about_view)
    LinearLayout mAboutView;

    @BindView(R.id.detail_text)
    ReadMoreTextView mDetailText;

    @BindView(R.id.about_name)
    TextView mAboutName;

    @BindView(R.id.full_article_description)
    TextView mContent;

    @BindView(R.id.full_article_hash_tags)
    TextView mHashTags;

    @BindView(R.id.comment_count)
    TextView mCommentCount;

    @BindView(R.id.comments_view_all_text)
    TextView mCommentViewAll;

    @BindView(R.id.comments_view_all_arrow)
    ImageView mCommentArrow;

    @BindView(R.id.comment_view)
    LinearLayout mCommentView;

    @BindView(R.id.full_article_download)
    ImageView mDownloadView;

    @BindView(R.id.full_article_bookmark)
    ImageView mBookMark;

    @BindView(R.id.comments_recycler_list)
    RecyclerView mCommentViewer;

    @BindView(R.id.full_view_scroll)
    NestedScrollView mFullViewScroll;

    @BindView(R.id.send_comment)
    Button mSendComment;

    @BindView(R.id.comment_content)
    EditText mCommentContent;

    @BindView(R.id.related_recycler_list)
    RecyclerView mRelatedArticleViewer;

    @BindView(R.id.related_view_all)
    LinearLayout mArticleViewAll;

    @BindView(R.id.related_product_recycler_list)
    RecyclerView mRelatedProductViewer;

    @BindView(R.id.related_product_view_all)
    LinearLayout mProductViewAll;

    @BindView(R.id.related_product_view)
    LinearLayout mRelatedProductView;

    @BindView(R.id.related_article_root_view)
    LinearLayout mRelatedArticleView;

    @BindView(R.id.listen_view)
    LinearLayout mListenView;

    private IFullPresenter IFullPresenter;
    private IHomeView IHomeView;
    private Unbinder mUnbinder;
    private int mType;
    private boolean isDownloaded;
    private String mId;
    private boolean scrollingToBottom;
    private TextToSpeech mTextToSpeech;
    private long mStartTime;
    private boolean isBackPressed;


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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i("TAG1111", "onPause");
        super.onPause();
        Log.i("TAGPAUSE", "onPause");
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            //mTextToSpeech.shutdown();
        }


        if(!isBackPressed) {
            isBackPressed = false;
            addToContinueList();
        }
    }

    public long getMinute() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long diff = currentTime - mStartTime;
        long minute = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(diff);
        return minute;
    }

    private void addToContinueList() {

        ArticlePostItem postItem = IFullPresenter.getArticle();

        if(postItem == null){
            Log.i("TAGTAGCONTINUE", "Article is null ");
            return;
        }

        String articleTime = postItem.getRead_time();
        if(articleTime == null || articleTime.isEmpty()){
            Log.i("TAGTAGCONTINUE", "Reading time is empty ");
            return;
        }


        long minute = getMinute();
        long actualReadTime = Long.valueOf(articleTime);
        boolean isAdd = false;


        Log.i("TAGTAGCONTINUE", "Minute " + minute);


        User user = User.getCurrentUser();
        String continueList = user.getContinueList();

        Log.i("TAGTAGCONTINUE", "Before List " + continueList);

        if(continueList == null || continueList.isEmpty()){
            continueList = mId;
            isAdd = true;
        }
        else {
            String[] itemArray = continueList.split(Constants.SEPARATOR);

            List<String> list = new ArrayList<String>(Arrays.asList(itemArray));
            if(list.contains(mId)){
                list.remove(mId);
            }

            if(list.size() >= 20) {    // maximum 20 articles
                list.remove(list.size() - 1);
            }


            if(minute<actualReadTime) {
                isAdd = true;
                list.add(0, mId);
            }

            continueList = createStringFromList(list);
        }

        Log.i("TAGTAGCONTINUE", "After List " + continueList);
        user.setContinueList(continueList);
        user.save();

        IHomeView.addContinueArticle(IFullPresenter.getArticle(), isAdd);
    }

    private String createStringFromList(List list) {

        int size = list.size();
        String continueList = "";
        for(int i=0; i<size; i++){

            String id = (String) list.get(i);

            if(i < size - 1){
                continueList = continueList + id + Constants.SEPARATOR;
            }
            else {
                continueList = continueList + id;
            }
        }

        return continueList;
    }

    private void scrollRecyclerViewToBottom(RecyclerView recyclerView) {
        if(recyclerView != null) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter != null && adapter.getItemCount() > 0) {
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        }
    }

    private void fetchArticleFromDataBase(String id) {
        IHomeView.showProgress();
        FetchArticlesTask productsTask = new FetchArticlesTask(id);
        productsTask.execute();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mStartTime = Calendar.getInstance().getTimeInMillis();

        Log.i("TAGONCREAE", "onCreateView");
        final View view = inflater.inflate(R.layout.fragment_full_article_view, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        IHomeView.setStatusBarTranslucent(true);
        IHomeView.hideBottomFooter();
        IHomeView.hideActionBar();
        if(!isDownloaded) {
            IFullPresenter.fetchArticle(mId);
        }
        else {
            fetchArticleFromDataBase(mId);
        }
        setContent();
        //mCommentContent.setOnFocusChangeListener(focusListener);

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                int screenHeight = view.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    if (!scrollingToBottom) {
                        scrollingToBottom = true;
                        scrollRecyclerViewToBottom(mCommentViewer);
                    }
                }
                else {
                    // keyboard is closed
                    scrollingToBottom = false;
                }
            }
        });
        return view;
    }

    /*private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                mFullViewScroll.setScrollY(mCommentView.getScrollX());//;View.FOCUS_UP);
            } else {

            }
        }
    };*/

    @Override
    public void onDestroy() {

        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }

        if(mUnbinder != null){
            mUnbinder.unbind();
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
    }
*/
    @Override
    public void setContent() {
        ArticlePostItem articlePost = IFullPresenter.getArticle();


        if (articlePost != null) {
            IHomeView.addContinueArticle(articlePost, true);
            setTopImageContent(articlePost);
            setArticleContent(articlePost);
            setAboutContent(articlePost);
            setCommentContent(articlePost);
            setRelatedArticleAdapter();
            setRelatedProductAdapter();
        }
    }

    private void setRelatedArticleAdapter() {
        if(IFullPresenter.getRelatedArticlesCount() > 0){
            mRelatedArticleView.setVisibility(View.VISIBLE);
        }
        else {
            mRelatedArticleView.setVisibility(View.GONE);
        }

        RelatedArticlesAdapter relatedArticlesAdapter = new RelatedArticlesAdapter(getContext(), IFullPresenter);
        relatedArticlesAdapter.setClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mRelatedArticleViewer.setLayoutManager(layoutManager);
        mRelatedArticleViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(8, getContext()), SpaceDecoration.VERTICAL));
        mRelatedArticleViewer.setAdapter(relatedArticlesAdapter);
        mRelatedArticleViewer.setFocusableInTouchMode(false);
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
        ArticlePostItem articlePost = IFullPresenter.getArticle();
        currentUser = articlePost.getCurrent_user();
        if(currentUser != null) {
            updateBookMark(currentUser.isBookmarked());
        }

        if(isDownloaded & currentUser != null){
            currentUser.save();
            articlePost.save();
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

        /*if (Build.VERSION.SDK_INT >= 11) {
            recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v,
                                           int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (bottom < oldBottom) {
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(
                                        recyclerView.getAdapter().getItemCount() - 1);
                            }
                        }, 100);
                    }
                }
            });
        }*/
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

        setCommentContent(IFullPresenter.getArticle());
    }

    @Override
    public void updateLike() {
        CurrentUser currentUser =  IFullPresenter.getArticle().getCurrent_user();
        Likes likes = IFullPresenter.getArticle().getLikes();
        if(currentUser != null) {
            int like = currentUser.getLike();
            long count = 0;
            if(likes != null){
                count   = Long.parseLong(likes.getLikes());
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
        if(mRelatedArticleViewer != null){
            mRelatedArticleViewer.getAdapter().notifyDataSetChanged();
        }
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
        i("TAGCOMMENT" , " isShown " + mCommentView.isShown());

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
            ArticlePostItem post = IFullPresenter.getArticle();
            IFullPresenter.fetchComments(String.valueOf(post.getArticle_Id()));
            mFullViewScroll.fullScroll(View.FOCUS_DOWN);
            //}
        }
    }

    @OnClick(R.id.full_article_bookmark)
    void onBookMark(){
        CurrentUser currentUser = null;
        ArticlePostItem articlePost = IFullPresenter.getArticle();
        if(articlePost != null){
            currentUser = articlePost.getCurrent_user();
            if(currentUser != null ) {
                if(!currentUser.isBookmarked()) {
                    IFullPresenter.bookmark(String.valueOf(articlePost.getArticle_Id()), ArticleParams.ARTICLE);
                }
                else {
                    IFullPresenter.unBookmark(String.valueOf(articlePost.getArticle_Id()), ArticleParams.ARTICLE);
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

        ArticlePostItem articlePost = IFullPresenter.getArticle();
        if(articlePost != null){
            String post_id = articlePost.getArticle_Id();
            if(!content.isEmpty()){
                IFullPresenter.addNewComment(String.valueOf(post_id), content);
            }
        }
    }

    @OnClick(R.id.full_article_like)
    void onLikeClick(){
        String id = IFullPresenter.getArticle().getArticle_Id();
        IFullPresenter.updateLike(String.valueOf(id));
    }

    private void setCommentContent(ArticlePostItem articlePost) {
        String commentCount = articlePost.getComments();
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

    private void setAboutContent(ArticlePostItem articlePost) {

        Author author = articlePost.getAuthor();
        if(author != null && !author.getName().isEmpty()){
            mAboutView.setVisibility(View.VISIBLE);
            String authorName = author.getName();
            i("TAGNAMNE","NAMe " + authorName);
            mAboutName.setText(authorName);

            String info = author.getInfo();
            mDetailText.setText(info);
        }
        else {
            mAboutView.setVisibility(View.GONE);
        }

    }

    private void setArticleContent(ArticlePostItem articlePost) {

        Title title = articlePost.getTitle();
        String articleTitle = null;
        if (title != null) {
            articleTitle = title.getRendered();
        }
        mTitle.setText(articleTitle);

        Content content = articlePost.getContent();
        i("TAGCONTENT", "Content " + content.getRendered());
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

        String tagsName = "";
        List<TagsItem> tagItems = articlePost.getTags();
        i("TAGITEMSTAG", "TAGITEMS " + tagItems);
        if (tagItems != null && !tagItems.isEmpty()) {
            for (TagsItem tagItem : tagItems) {
                String tag = tagItem.getName();
                tagsName = tagsName + "#" + tag + " ";
            }
        }
        i("TAGITEMSTAG", "TAGNAME " + tagsName);
        mHashTags.setText(tagsName);

        String date = articlePost.getDate();
        date = HealthHuntUtility.getDateWithFormat(date);
        if (date != null) {
            date.trim();
            mPublishDate.setText(date);
        }


        Author author = articlePost.getAuthor();
        if (author != null) {
            mAuthorName.setText(author.getName());
            String authorUrl = author.getUrl();
            if (authorUrl != null) {
                authorUrl = authorUrl.replace("\n", "");
                Glide.with(this)
                        .load(authorUrl).placeholder(R.mipmap.default_profile)
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(mAuthorImage);
            } else {
                mAuthorImage.setBackgroundResource(R.mipmap.default_profile);
            }
        }
    }

    private void setTopImageContent(ArticlePostItem articlePost) {

        String readTime = articlePost.getRead_time();
        if (readTime != null) {
            readTime = getString(R.string.read_time, readTime);
        }

        mReadTime.setText(readTime);

        CurrentUser currentUser = articlePost.getCurrent_user();
        if(currentUser != null) {
            updateBookMark(currentUser.isBookmarked());

            int like = currentUser.getLike();
            if(like > 0){
                updateLikeIcon(true);
            }
            else {
                updateLikeIcon(false);
            }
        }


        Likes likes = articlePost.getLikes();
        if(likes != null){
            String likeCount = likes.getLikes();
            if(likeCount != null){
                Log.i("TAGCOUNT", "Count " + likeCount);
                likeCount = HealthHuntUtility.formatNumber(Long.valueOf(likeCount));
                mLikesCount.setText(likeCount);
            }
        }


        String categoryName = null;
        List<CategoriesItem> categories = articlePost.getCategories();
        if (categories != null && !categories.isEmpty()) {
            categoryName = categories.get(0).getName();
            mCategoryName.setText(categoryName);
            mCategoryImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.hh_blue_light), PorterDuff.Mode.SRC_IN);
            int res = HealthHuntUtility.getCategoryIcon(categoryName);
            int color = HealthHuntUtility.getCategoryColor(categoryName);
            mCategoryName.setTextColor(ContextCompat.getColor(getContext(), color));
            mCategoryImage.setColorFilter(ContextCompat.getColor(getContext(), color), PorterDuff.Mode.SRC_IN);
            if(res != 0){
                mCategoryImage.setImageResource(res);
            }
            else {
                mCategoryImage.setImageResource(R.mipmap.ic_fitness);
            }
        }

        String url = null;
        List<MediaItem> mediaItems = articlePost.getMedia();
        if (mediaItems != null && !mediaItems.isEmpty()) {
            MediaItem media = mediaItems.get(0);
            i("TAGMedia", "media " + media);
            if ("image".equals(media.getMedia_type())) {
                url = media.getUrl();
                i("TAGMedia", "URL " + url);

            }
        }

        if (url != null) {
            Glide.with(this).load(url).placeholder(R.mipmap.ic_no_article_image).into(mArticleImage);
        } else {
            mArticleImage.setBackgroundResource(R.mipmap.ic_no_article_image);
        }
    }

    @OnClick(R.id.full_article_download)
    void onDownloadClick(){
        final Dialog mBottomSheetDialog = new Dialog(getContext(), R.style.MaterialDialogSheet);
        View customView = getLayoutInflater().inflate(R.layout.download_popup_window_view,null);
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
                //Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
                shareArticle();
            }
        });

        LinearLayout downloadView = customView.findViewById(R.id.download_article_view);
        downloadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();

                String msg = getString(R.string.downloaded_successfully);
                if(!isAlreadyDownloaded()) {
                    storeArticle();
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
        ArticlePostItem articlePostItem = ArticlePostItem.getArticle(IFullPresenter.getArticle().getArticle_Id());
        i("TAGISISIS", "artic" + articlePostItem);
        if(articlePostItem != null){
            return true;
        }
        return false;
    }

    private void shareArticle(){
        ArticlePostItem articlePost = IFullPresenter.getArticle();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, articlePost.getLink());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share)));
    }

    private void updateBookMark(boolean isBookMark) {
        i("TAGBOOKMARK", "ISBOOK " + isBookMark);
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
        i("TAGPOPUP", "onMore");

        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.setGravity(Gravity.LEFT);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                CommentsItem commentsItem = IFullPresenter.getComment(position);
                int id = item.getItemId();
                switch (id){
                    case R.id.comment_edit:
                        RecyclerView.Adapter adapter = mCommentViewer.getAdapter();
                        if(adapter != null){
                            CommentAdapter commentAdapter = (CommentAdapter) adapter;
                            commentAdapter.addEditCommentPos(commentsItem.getId());
                            commentAdapter.notifyDataSetChanged();
                        }
                        //editComment(position);
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

        CommentAdapter adapter = (CommentAdapter) mCommentViewer.getAdapter();
        adapter.addEditCommentPos(Integer.MIN_VALUE);  // To remove the edit options

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
        String id = null;
        String tag = FullArticleFragment.class.getSimpleName();
        if(type == ArticleParams.ARTICLE){
            ArticlePostItem postItem = IFullPresenter.getRelatedArticle(position);
            id = String.valueOf(postItem.getArticle_Id());
            tag = FullArticleFragment.class.getSimpleName();

        }
        else if(type == ArticleParams.PRODUCT){
            ProductPostItem postItem = IFullPresenter.getRelatedProduct(position);
            id = String.valueOf(postItem.getProduct_id());
            tag = FullProductFragment.class.getSimpleName();
        }
        Bundle bundle = new Bundle();
        bundle.putString(ArticleParams.ID, String.valueOf(id));
        bundle.putInt(ArticleParams.POST_TYPE, type);
        IFullPresenter.loadFragment(tag, bundle);
    }

    private void openViewAllFragment() {
    }

    @OnClick(R.id.related_view_all)
    void onRelatedViewAll(){
        Bundle bundle = new Bundle();
        String id = IFullPresenter.getArticle().getArticle_Id();
        bundle.putInt(ArticleParams.ARTICLE_TYPE, ArticleParams.RELATED_ARTICLES);
        bundle.putString(ArticleParams.ID, String.valueOf(id));
        bundle.putBoolean(Constants.IS_RELATED, true);
        IFullPresenter.loadFragment(ViewAllFragment.class.getSimpleName(), bundle);
    }

    @OnClick(R.id.related_product_view_all)
    void onRelatedProductViewAll(){
        Bundle bundle = new Bundle();
        String id = IFullPresenter.getArticle().getArticle_Id();
        bundle.putInt(ArticleParams.ARTICLE_TYPE, ArticleParams.RELATED_PRODUCTS);
        bundle.putString(ArticleParams.ID, String.valueOf(id));
        bundle.putBoolean(Constants.IS_RELATED, true);
        IFullPresenter.loadFragment(ViewAllFragment.class.getSimpleName(), bundle);
    }

    private void storeArticle(){
        ArticlePostItem articlePostItem = IFullPresenter.getArticle();

        i("TAGPOSTSINGLE", "Article " + articlePostItem);
        Title title = articlePostItem.getTitle();
        if(title != null){
            title.save();
        }

        Content content = articlePostItem.getContent();
        if(content != null){
            content.save();
        }

        List<MediaItem> mediaItems = articlePostItem.getMedia();
        if(mediaItems != null){

            for(MediaItem item: mediaItems){
                item.setParent_id(articlePostItem.getArticle_Id());
                item.save();
            }
        }

        List<CategoriesItem> categoriesItemList = articlePostItem.getCategories();
        if(categoriesItemList != null){
            for (CategoriesItem item : categoriesItemList){
                item.setParent_id(articlePostItem.getArticle_Id());
                item.save();
            }
        }

        Likes likes = articlePostItem.getLikes();
        if(likes != null){
            likes.save();
        }

        CurrentUser currentUser = articlePostItem.getCurrent_user();
        if(currentUser != null){
            /*List<Collections> collections = currentUser.getCollections();
            if(collections != null){
                for(Collections collect: collections) {
                    collect.setParent_id(productPost.getProduct_id());
                    collect.save();
                }
            }*/
            currentUser.save();
        }

        Author author = articlePostItem.getAuthor();
        if(author != null){
            author.save();
        }

        Synopsis synopsis = articlePostItem.getSynopsis();
        if(synopsis != null){
            synopsis.save();
        }

        List<TagsItem> tagsItems = articlePostItem.getTags();
        if(tagsItems != null){
            for(TagsItem tagsItem: tagsItems){
                tagsItem.setParent_id(articlePostItem.getArticle_Id());
                tagsItem.save();
            }
        }

        Excerpt excerpt = articlePostItem.getExcerpt();
        if(excerpt != null){
            excerpt.save();
        }

        articlePostItem.save();
        IHomeView.updateDownloadData();
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

        ArticlePostItem postItem  = IFullPresenter.getArticle();

        if(postItem != null) {
            Content content = postItem.getContent();
            Spanned spanned = Html.fromHtml(content.getRendered()) ;
            String text = spanned.toString();
            int maxLen = TextToSpeech.getMaxSpeechInputLength();
            int strLen = text.length();

            if(strLen >= maxLen){
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

    private class FetchArticlesTask extends AsyncTask<Void, Void, ArticlePostItem> {

        private String mArticleID;
        public FetchArticlesTask(String id){
            mArticleID = id;
        }

        @Override
        protected ArticlePostItem doInBackground(Void... voids) {

            ArticlePostItem articlePostItem = new Select().from(ArticlePostItem.class).
                    where("article_id = ?" , mArticleID).executeSingle();

            if(articlePostItem == null){
                i("TAGPOS", "Article Post is null");
                return null;
            }

            i("TAGDATA", "articlePost " + articlePostItem);

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
            return articlePostItem;
        }

        @Override
        protected void onPostExecute(ArticlePostItem articlePostItem) {
            super.onPostExecute(articlePostItem);
            IHomeView.hideProgress();
            IFullPresenter.updateArticle(articlePostItem);
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

    @OnClick(R.id.listen_view)
    void onListen() {

        boolean isStopOtherAudio = isStopOtherAudio();
        Log.i("TAGListe", "isStopOtherAudio " + isStopOtherAudio);

        if (!mTextToSpeech.isSpeaking() && isStopOtherAudio) {
            speakOut();
        } else {
            mTextToSpeech.stop();
        }
    }

    /*void listenText(){

        ArticlePostItem postItem  = IFullPresenter.getArticle();
        Content content = postItem.getContent();
        Spanned spanned = Html.fromHtml(content.getRendered()) ;
        String text = spanned.toString();


        File soundFile = new File("ABBB");
        if (soundFile.exists())
            soundFile.delete();

        TextToSpeech ttf =  new TextToSpeech(getContext(),this);
        if(ttf.synthesizeToFile("TESTING TERING", null, soundFile, "0")== TextToSpeech.SUCCESS) {
            Toast.makeText(getContext(),"Sound file created",Toast.LENGTH_SHORT).show();
        }

        try {
            MediaPlayer player = new MediaPlayer();
            player.setDataSource("ABBB");
            player.prepare();
            player.start();
        }
        catch(Exception e) {
            Toast.makeText(getContext(),"Hmmmmm. Can't play file",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/



        /*Thread x =new Thread(){
            public void run(){
                try{
                    String url1="http://www.translate.google.com/translate_tts?ie=UTF-8&q=" + "TEsting TEsting v TEsting TEsting" + "%0A&tl=" + "EN"  + "&prev=input";



                    mediaPlayer=new MediaPlayer();
                    mediaPlayer.reset();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(url1);
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                    mediaPlayer.start();
                } catch (IllegalArgumentException e) {
                    mediaPlayer.reset();
                } catch (IllegalStateException e) {
                    mediaPlayer.reset();
                } catch (IOException e) {
                    mediaPlayer.reset();
                }


                finally{
                    //  x.suspend();
                }
            }

        };
        x.start();
}*/


    public boolean isStopOtherAudio(){
        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        int result = am.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                                              @Override
                                              public void onAudioFocusChange(int i) {

                                              }
                                          },
// Use the music stream.
                AudioManager.STREAM_MUSIC,
// Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);


        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return true;
        }
        return false;
    }

    public boolean onBackPressed(){
        isBackPressed  = true;
        addToContinueList();
        return false;
    }
}
