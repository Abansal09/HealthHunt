package in.healthhunt.view.fullView.fullViewFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

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
import in.healthhunt.view.homeScreenView.IHomeView;
import in.healthhunt.view.viewAll.ViewAllFragment;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class YoutubeFragment extends Fragment implements IFullFragment , CommentAdapter.ClickListener,
        RelatedArticlesAdapter.ClickListener, RelatedProductAdapter.ClickListener, TextToSpeech.OnInitListener{

    @BindView(R.id.full_video_name)
    TextView mVideoName;

    @BindView(R.id.full_likes_count)
    TextView mLikesCount;

    @BindView(R.id.full_article_like)
    ImageView mLikeImage;

    @BindView(R.id.full_view_category_name)
    TextView mCategoryName;

    @BindView(R.id.full_article_category_icon)
    ImageView mCategoryImage;

    @BindView(R.id.full_article_content)
    TextView mTitle;

    @BindView(R.id.author_pic)
    ImageView mAuthorImage;

    @BindView(R.id.author_name)
    TextView mAuthorName;

    @BindView(R.id.author_publish_date)
    TextView mPublishDate;

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

    @BindView(R.id.overlay_video_view)
    RelativeLayout mOverlayVideoView;

    private IFullPresenter IFullPresenter;
    private IHomeView IHomeView;
    private Unbinder mUnbinder;
    int mPostType;
    //  private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer mYouTubePlayer;
    private boolean isDownloaded;
    private String mId;
    private boolean isFullScreen;
    private TextToSpeech mTextToSpeech;
    private long mStartTime;
    private boolean isBackPressed;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IHomeView = (IHomeView)getActivity();

        mId = getArguments().getString(ArticleParams.ID);
        mPostType = getArguments().getInt(ArticleParams.POST_TYPE, ArticleParams.ARTICLE);

        isDownloaded = getArguments().getBoolean(Constants.IS_DOWNLOADED, false);

        IFullPresenter = new FullPresenterImp(getContext(), this);
        mTextToSpeech = new TextToSpeech(getContext(),this);
        //addToContinueList();
        // playbackEventListener = new MyPlaybackEventListener();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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

        String articleTime = IFullPresenter.getArticle().getRead_time();
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


            if(minute<=actualReadTime) {
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


    private void fetchVideoFromDataBase(String id) {
        FetchVideoTask productsTask = new FetchVideoTask(id);
        productsTask.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mStartTime = Calendar.getInstance().getTimeInMillis();

        View rootView = inflater.inflate(R.layout.fragment_full_video_view, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

       /* Log.i("TAGORIENTA", "YoutubeFragment");
        // YouTubeフラグメントインスタンスを取得
       final YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        // レイアウトにYouTubeフラグメントを追加
        final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
        transaction.addToBackStack(YouTubePlayerSupportFragment.class.getSimpleName());

        // YouTubeフラグメントのプレーヤーを初期化する
        youTubePlayerFragment.initialize(getString(R.string.youtube_key), new OnInitializedListener() {

            // YouTubeプレーヤーの初期化成功
            @Override
            public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
                player.setFullscreenControlFlags(0);
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                   // player.loadVideo(VIDEO_ID);
                    player.cueVideo(VIDEO_ID);
                    player.play();
                }
                else {
                    player.play();
                }

                player.setShowFullscreenButton(true);
                player.setOnFullscreenListener(
                        new YouTubePlayer.OnFullscreenListener() {
                            @Override
                            public void onFullscreen(boolean b) {
                                if(b){

                                }
                            }

                        });
            }

            // YouTubeプレーヤーの初期化失敗
            @Override
            public void onInitializationFailure(Provider provider, YouTubeInitializationResult error) {
                // YouTube error
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }
        });

*/
        IHomeView.setStatusBarTranslucent(true);
        IHomeView.hideBottomFooter();
        IHomeView.hideActionBar();
        if(!isDownloaded) {
            IFullPresenter.fetchArticle(mId);
        }
        else {
            fetchVideoFromDataBase(mId);
        }
        setContent();
        return rootView;
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
        mYouTubePlayer.setFullscreen(fullScreen);
    }

    private void initializeYoutubePlayer() {
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        youTubePlayerFragment.initialize(getString(R.string.youtube_key), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {

                mYouTubePlayer = player;
                mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
                String youTubeId = IFullPresenter.getArticle().getPost_youtube_id();
                Log.i("TAGYUBE", "YubeID " + youTubeId);
                if(youTubeId == null || youTubeId.isEmpty()){
                    Log.i("TAGYUBE", "Youtube id is null");
                    //return;
                }
                //if initialization success then load the video id to youtube player
                if (!wasRestored) {
                    //set the player style here: like CHROMELESS, MINIMAL, DEFAULT
                    mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    // mYouTubePlayer.setPlaybackEventListener(playbackEventListener);

                    //load the video
                    //youTubePlayer.loadVideo(IFullPresenter.getVideo().getPost_youtube_id());

                    //OR

                    //cue the video
                    mYouTubePlayer.cueVideo(IFullPresenter.getArticle().getPost_youtube_id());

                    //if you want when activity start it should be in full screen uncomment below comment
                    //youTubePlayer.setFullscreen(true);

                    //If you want the video should play automatically then uncomment below comment
                    mYouTubePlayer.play();

                    //If you want to control the full screen event you can uncomment the below code
                    //Tell the player you want to control the fullscreen change
                    // mYouTubePlayer.setFullscreenControlFlags(0);
                    //Tell the player how to control the change
                    mYouTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean arg0) {
                            isFullScreen = arg0;
                            // do full screen stuff here, or don't.
                            Log.e("TAGFULLSCREN","Full screen mode " + arg0);
                        }
                    });

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult result) {
                if (result.isUserRecoverableError()) {
                    result.getErrorDialog(getActivity(), 1).show();
                }
            }
        });

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_fragment, youTubePlayerFragment).commit();

    }


   /* private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            Log.i("TAGViewYube", "ViewPlaying");
            mOverlayVideoView.setVisibility(View.GONE);
        }

        @Override
        public void onPaused() {
            Log.i("TAGViewYube", "ViewPause");
            mOverlayVideoView.setVisibility(View.VISIBLE);
            // Called when playback is paused, either due to user action or call to pause().
        }

        @Override
        public void onStopped() {
            mOverlayVideoView.setVisibility(View.VISIBLE);
            // Called when playback stops for a reason other than being paused.
        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }*/

    @Override
    public void onDestroy() {
        if(mUnbinder != null){
            mUnbinder.unbind();
        }

        if (mYouTubePlayer != null) {
            mYouTubePlayer.release();
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
        ArticlePostItem articlePost = IFullPresenter.getArticle();

        if (articlePost != null) {
            initializeYoutubePlayer();
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
        return mPostType;
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
                String likesCount = HealthHuntUtility.formatNumber(count);
                mLikesCount.setText(likesCount);
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
        IHomeView.updateMyhuntsVideoSaved(articlePostItem);
        showToast(articlePostItem.getCurrent_user());
        IHomeView.updateWatch(articlePostItem);
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

            //CommentAdapter adapter = (CommentAdapter) mCommentViewer.getAdapter();
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
                    IFullPresenter.bookmark(String.valueOf(articlePost.getArticle_Id()), ArticleParams.VIDEO);
                }
                else {
                    IFullPresenter.unBookmark(String.valueOf(articlePost.getArticle_Id()), ArticleParams.VIDEO);
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
        if(author != null){
            String authorName = author.getName();
            Log.i("TAGNAMNE","NAMe " + authorName);
            mAboutName.setText(authorName);

            String info = author.getInfo();
            mDetailText.setText(info);
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

        String tagsName = "";
        List<TagsItem> tagItems = articlePost.getTags();
        if (tagItems != null && !tagItems.isEmpty()) {
            for (TagsItem tagItem : tagItems) {
                String tag = tagItem.getName();
                tagsName = tagsName + "#" + tag + " ";
            }
        }
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

        Title title = articlePost.getTitle();
        if (title != null) {
            mVideoName.setText(title.getRendered());
        }


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
                likeCount = HealthHuntUtility.formatNumber(Long.parseLong(likeCount));
                mLikesCount.setText(likeCount);
            }
        }


        String categoryName = null;
        List<CategoriesItem> categories = articlePost.getCategories();
        if (categories != null && !categories.isEmpty()) {
            categoryName = categories.get(0).getName();
            mCategoryName.setText(categoryName);
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

        /*String url = articlePost.getVideo_thumbnail();

        if (url != null) {
            Glide.with(this).load(url).placeholder(R.drawable.artical).into(mArticleImage);
        } else {
            mArticleImage.setBackgroundResource(R.drawable.artical);
        }*/
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

        Log.i("TAGPOSTSINGLE", "Article " + articlePostItem);
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

        articlePostItem.setVideo(true);

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


    private class FetchVideoTask extends AsyncTask<Void, Void, ArticlePostItem> {

        private String mArticleID;
        public FetchVideoTask(String id){
            mArticleID = id;
        }

        @Override
        protected ArticlePostItem doInBackground(Void... voids) {

            ArticlePostItem articlePostItem = new Select().from(ArticlePostItem.class).
                    where("article_id = ? AND is_Video = ?" , mArticleID, true).executeSingle();

            if(articlePostItem == null){
                Log.i("TAGPOS", "Article Post is null");
                return null;
            }

            Log.i("TAGDATA", "articlePost " + articlePostItem);

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
            //mProgress.dismiss();
            Log.i("TAGPOSTARTICLE", "artc" + articlePostItem);
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
    void onListen(){

        boolean isStopOtherAudio = isStopOtherAudio();
        Log.i("TAGListe", "isStopOtherAudio " + isStopOtherAudio);

        if(!mTextToSpeech.isSpeaking() && isStopOtherAudio) {
            speakOut();
        }
        else {
            mTextToSpeech.stop();
        }
    }

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
        if(isFullScreen()){
            setFullScreen(false);
            return true;
        }

        isBackPressed  = true;
        addToContinueList();
        return false;
    }
}