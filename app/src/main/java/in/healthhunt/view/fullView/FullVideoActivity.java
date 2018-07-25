package in.healthhunt.view.fullView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

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
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.model.utility.URLImageParser;
import in.healthhunt.presenter.fullPresenter.FullPresenterImp;
import in.healthhunt.presenter.fullPresenter.IFullPresenter;
import in.healthhunt.view.fullView.commentView.CommentViewHolder;
import in.healthhunt.view.fullView.fullViewFragments.IFullFragment;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by abhishekkumar on 5/24/18.
 */

public class FullVideoActivity extends YouTubeBaseActivity implements IFullFragment {

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

  /*  @BindView(R.id.full_video_image)
    ImageView mArticleImage;

    @BindView(R.id.play_icon)
    ImageView mPlayIcon;*/

    @BindView(R.id.full_article_content)
    TextView mTitle;

    @BindView(R.id.author_pic)
    ImageView mAuthorImage;

    @BindView(R.id.author_name)
    TextView mAuthorName;

    @BindView(R.id.author_publish_date)
    TextView mPublishDate;

    @BindView(R.id.full_article_description)
    TextView mContent;

    @BindView(R.id.full_article_hash_tags)
    TextView mHashTags;

    @BindView(R.id.full_article_download)
    ImageView mDownloadView;

    @BindView(R.id.full_article_bookmark)
    ImageView mBookMark;

   // @BindView(R.id.youtube_player_view)
    YouTubePlayerView mYouTubePlayerView;

    @BindView(R.id.overlay_video_view)
    RelativeLayout mOverlayVideoView;

    private IFullPresenter IFullPresenter;
    private Unbinder mUnbinder;
    private ProgressDialog mProgress;
    int mPostType;
    private MyPlaybackEventListener playbackEventListener;
    private YouTubePlayer mYouTubePlayer;
    private boolean isDownloaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_full_video_view);
        mUnbinder = ButterKnife.bind(this);
        String mId = getIntent().getStringExtra(ArticleParams.ID);
        mPostType = getIntent().getIntExtra(ArticleParams.POST_TYPE, ArticleParams.ARTICLE);

        isDownloaded = getIntent().getBooleanExtra(Constants.IS_DOWNLOADED, false);
        mProgress = new ProgressDialog(this);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        mProgress.setMessage(getResources().getString(R.string.please_wait));
        IFullPresenter = new FullPresenterImp(this, this);
        if(!isDownloaded) {
            IFullPresenter.fetchArticle(mId);
        }
        else {
            fetchVideoFromDataBase(mId);
        }

        playbackEventListener = new MyPlaybackEventListener();
        setStatusBarTranslucent(true);
    }

    private void fetchVideoFromDataBase(String id) {
        mProgress.show();
        FetchVideoTask productsTask = new FetchVideoTask(id);
        productsTask.execute();
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    private void initializeYoutubePlayer() {
        mYouTubePlayerView.initialize(getString(R.string.youtube_key), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {

                mYouTubePlayer = player;
                String youTubeId = IFullPresenter.getArticle().getPost_youtube_id();
                Log.i("TAGYUBE", "YubeID " + youTubeId);
                if(youTubeId == null || youTubeId.isEmpty()){
                    Log.i("TAGYUBE", "Youtube id is null");
                    return;
                }
                //if initialization success then load the video id to youtube player
                if (!wasRestored) {
                    //set the player style here: like CHROMELESS, MINIMAL, DEFAULT
                    mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    mYouTubePlayer.setPlaybackEventListener(playbackEventListener);

                    //load the video
                    //youTubePlayer.loadVideo(IFullPresenter.getVideo().getPost_youtube_id());

                    //OR

                    //cue the video
                    mYouTubePlayer.cueVideo("3gFAduagfaE"/*IFullPresenter.getArticle().getPost_youtube_id()*/);

                    //if you want when activity start it should be in full screen uncomment below comment
                    //youTubePlayer.setFullscreen(true);

                    //If you want the video should play automatically then uncomment below comment
                    mYouTubePlayer.play();

                    //If you want to control the full screen event you can uncomment the below code
                    //Tell the player you want to control the fullscreen change
                   /*player.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                    //Tell the player how to control the change
                    player.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean arg0) {
                            // do full screen stuff here, or don't.
                            Log.e(TAG,"Full screen mode");
                        }
                    });*/

                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult result) {
                if (result.isUserRecoverableError()) {
                    result.getErrorDialog(FullVideoActivity.this, 1).show();
                }
            }
        });
    }

    private final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            mOverlayVideoView.setVisibility(View.GONE);
        }

        @Override
        public void onPaused() {
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
    }

    @Override
    public void onDestroy() {
        if(mUnbinder != null){
            mUnbinder.unbind();
        }

        if (mYouTubePlayer != null) {
            mYouTubePlayer.release();
        }

        super.onDestroy();
    }

    @Override
    public void showProgress() {
        mProgress.show();
    }

    @Override
    public void hideProgress() {
        mProgress.dismiss();
    }

    @Override
    public void showAlert(String msg) {

    }

    @Override
    public void setContent() {
        ArticlePostItem articlePost = IFullPresenter.getArticle();

        if (articlePost != null) {
            setTopImageContent(articlePost);
            setArticleContent(articlePost);
            initializeYoutubePlayer();
        }
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
        }
    }

    @Override
    public CommentViewHolder createViewHolder(View view) {
        return null;
    }

    @Override
    public void updateCommentAdapter() {
    }

    @Override
    public void updateLike() {
        CurrentUser currentUser =  IFullPresenter.getArticle().getCurrent_user();
        if(currentUser != null) {
            int like = currentUser.getLike();
            int count = Integer.parseInt(mLikesCount.getText().toString());
            if (like > 0) {
                updateLikeIcon(true);
                count++;
            } else {
                updateLikeIcon(false);
                if(count > 0) {
                    count--;
                }
            }

            mLikesCount.setText(String.valueOf(count));
        }
    }

    @Override
    public void updateRelatedArticleAdapter() {

    }

    @Override
    public void updateRelatedProductAdapter() {

    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {

    }

    @Override
    public void updateProductSaved(ProductPostItem postItem) {

    }

    @Override
    public void updateArticleSaved(ArticlePostItem articlePostItem) {

    }

    @Override
    public void updateVideoSaved(ArticlePostItem articlePostItem) {

    }

    @Override
    public int getTotalCommentCount() {
        ArticlePostItem postItem = IFullPresenter.getArticle();
        int count = 0;
        if(postItem != null){
            String countStr = postItem.getComments();
            if(countStr != null){
                count = Integer.parseInt(countStr);
            }
        }
        return count;
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

    @OnClick(R.id.full_article_like)
    void onLikeClick(){
        String id = IFullPresenter.getArticle().getArticle_Id();
        IFullPresenter.updateLike(String.valueOf(id));
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
            URLImageParser imageGetter = new URLImageParser(this, mContent);
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
                tagsName = "#" + tag + " ";
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
                        .bitmapTransform(new CropCircleTransformation(this))
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
                mLikesCount.setText(likeCount);
            }
        }


        String categoryName = null;
        List<CategoriesItem> categories = articlePost.getCategories();
        if (categories != null && !categories.isEmpty()) {
            categoryName = categories.get(0).getName();
            mCategoryName.setText(categoryName);
            mCategoryImage.setColorFilter(ContextCompat.getColor(this, R.color.hh_blue_light), PorterDuff.Mode.SRC_IN);
            int res = HealthHuntUtility.getCategoryIcon(categoryName);
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
        final Dialog mBottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);
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
                storeArticle();
                Toast.makeText(getApplicationContext(), getString(R.string.downloaded_successfully), Toast.LENGTH_SHORT).show();
            }
        });

        mBottomSheetDialog.show();
    }

    private void shareArticle(){
        ArticlePostItem articlePost = IFullPresenter.getArticle();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, articlePost.getLink());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.share)));
    }

    public void updateLikeIcon(boolean isLike) {
        if(!isLike){
            mLikeImage.setColorFilter(null);
            mLikeImage.setImageResource(R.mipmap.ic_full_article_heart);
        }
        else {
            mLikeImage.setColorFilter(ContextCompat.getColor(this, R.color.hh_green_light2), PorterDuff.Mode.SRC_IN);
        }
    }

    private void updateBookMark(boolean isBookMark) {
        Log.i("TAGBOOKMARK", "ISBOOK " + isBookMark);
        if(!isBookMark){
            mBookMark.setColorFilter(null);
            mBookMark.setImageResource(R.mipmap.ic_bookmark_full_view);
        }
        else {
            mBookMark.setColorFilter(ContextCompat.getColor(this, R.color.hh_green_light2), PorterDuff.Mode.SRC_IN);
        }
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
            mProgress.dismiss();
            Log.i("TAGPOSTARTICLE", "artc" + articlePostItem);
            IFullPresenter.updateArticle(articlePostItem);
        }
    }
}
