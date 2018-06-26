package in.healthhunt.view.homeScreenView.myFeedView.articleView;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.commonResponse.MediaItem;
import in.healthhunt.model.articles.commonResponse.TagsItem;
import in.healthhunt.model.articles.commonResponse.Title;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.homeScreenPresenter.myFeedPresenter.articlePresenter.IArticlePresenter;
import in.healthhunt.view.fullView.fullViewFragments.FullArticleFragment;
import in.healthhunt.view.viewAll.ViewAllFragment;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public class ArticleFragment extends Fragment {

    private Unbinder mUnBinder;


    @BindView(R.id.tags_article_view_item)
    RelativeLayout mTagItemView;

    @BindView(R.id.last_page_view_all)
    TextView mViewAll;

    @BindView(R.id.article_image)
    ImageView mArticleImage;

    @BindView(R.id.article_bookmark)
    ImageView mArticleBookMark;

    @BindView(R.id.category_image)
    ImageView mCategoryImage;

    @BindView(R.id.category_name)
    TextView mCategoryName;

    @BindView(R.id.author_pic)
    ImageView mAuthorImage;

    @BindView(R.id.author_name)
    TextView mAuthorName;

    @BindView(R.id.article_content)
    TextView mArticleTitle;

    @BindView(R.id.hash_tags)
    TextView mHashTags;

    @BindView(R.id.reading_time)
    TextView mReadingTime;

    @BindView(R.id.article_date)
    TextView mArticleDate;

    private int mType;
    private int mPos;
    private IArticlePresenter IArticlePresenter;
    private ArticlePostItem mArticlePostItem;

    public ArticleFragment(){
    }

    @SuppressLint("ValidFragment")
    public ArticleFragment(IArticlePresenter articlePresenter) {
        IArticlePresenter = articlePresenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_item_view, container, false);
        if(IArticlePresenter == null){
            return view;
        }

        mUnBinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        if(bundle != null) {
            boolean isLast = bundle.getBoolean(ArticleParams.IS_LAST_PAGE);
            mType = bundle.getInt(ArticleParams.ARTICLE_TYPE);
            mPos = bundle.getInt(ArticleParams.POSITION);
            if(IArticlePresenter != null) {
                mArticlePostItem = IArticlePresenter.getArticle(mPos);
            }

            //if(!isLast) {
            mTagItemView.setVisibility(View.VISIBLE);
            // mViewAll.setVisibility(View.GONE);
            setContent();
           /* }
            else {
                mTagItemView.setVisibility(View.GONE);
                mViewAll.setVisibility(View.VISIBLE);
            }*/
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mUnBinder != null) {
            mUnBinder.unbind();
        }
    }


    @OnClick(R.id.tags_article_view_item)
    void onArticleClick(){

        if(HealthHuntUtility.checkInternetConnection(getContext())) {
            openFullView();
        }
        else {
            IArticlePresenter.showAlert(getString(R.string.please_check_internet_connectivity_status));
        }
    }

    @OnClick(R.id.last_page_view_all)
    void onViewAll(){
        Bundle bundle = new Bundle();
        bundle.putInt(ArticleParams.ARTICLE_TYPE, mType);
        IArticlePresenter.loadFragment(ViewAllFragment.class.getSimpleName(), bundle);
    }

    private void openFullView() {
       /* Intent intent = new Intent(getContext(), FullViewActivity.class);
        intent.putExtra(ArticleParams.ID, String.valueOf(mArticlePostItem.getMedia_id()));
        intent.putExtra(ArticleParams.POST_TYPE, ArticleParams.ARTICLE);
        startActivity(intent);
*/
        Bundle bundle = new Bundle();
        bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.ARTICLE);
        bundle.putString(ArticleParams.ID, String.valueOf(mArticlePostItem.getArticle_Id()));
        IArticlePresenter.loadFragment(FullArticleFragment.class.getSimpleName(), bundle);
    }

    @OnClick(R.id.article_bookmark)
    void onBookMark(){

        if (HealthHuntUtility.checkInternetConnection(getContext())) {
            String id = String.valueOf(mArticlePostItem.getArticle_Id());
            CurrentUser currentUser = mArticlePostItem.getCurrent_user();
            if (currentUser != null) {
                if (!currentUser.isBookmarked()) {
                    IArticlePresenter.bookmark(id, mType);
                } else {
                    IArticlePresenter.unBookmark(id, mType);
                }
            }
        }
        else {
            IArticlePresenter.showAlert(getString(R.string.please_check_internet_connectivity_status));
        }
    }

    private void setContent() {
        String categoryName = null;
        List<CategoriesItem> categories = mArticlePostItem.getCategories();
        if(categories != null && !categories.isEmpty()){
            categoryName = categories.get(0).getName();
        }
        if(categoryName != null) {
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


        Author author = mArticlePostItem.getAuthor();
        if(author != null){
            String authorName = author.getName();
            if(authorName != null) {
                mAuthorName.setText(authorName);
            }

            String authorUrl = author.getUrl();
            if(authorUrl != null) {
                authorUrl = authorUrl.replace("\n", "");
                Glide.with(this)
                        .load(authorUrl).placeholder(R.mipmap.default_profile)
                        .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(mAuthorImage);
            }
            else {
                mAuthorImage.setImageResource(R.mipmap.default_profile);
            }
        }



        Title title = mArticlePostItem.getTitle();
        String articleTitle = null;
        if(title != null) {
            articleTitle = title.getRendered();
        }
        if(articleTitle != null) {
            mArticleTitle.setText(articleTitle);
        }


        String tagsName = "";
        List<TagsItem> tagItems = mArticlePostItem.getTags();
        if(tagItems != null && !tagItems.isEmpty()) {
            for(TagsItem  tagItem: tagItems) {
                tagsName = tagsName + "#" + tagItem.getName() + " ";
            }
        }
        mHashTags.setText(tagsName);

        String readingTime = mArticlePostItem.getRead_time();
        if(readingTime != null) {
            mReadingTime.setText(readingTime + " min read");
        }

        String date = mArticlePostItem.getDate();
        date = HealthHuntUtility.getDateWithFormat(date);
        if(date != null) {
            mArticleDate.setText(date);
        }

        CurrentUser currentUser = mArticlePostItem.getCurrent_user();
        if(currentUser != null) {
            Log.i("TAGUSER", " ISBOOK" + currentUser.isBookmarked());
            updateBookMark(currentUser.isBookmarked());
        }

        String articleUrl = null;
        List<MediaItem> mediaItems = mArticlePostItem.getMedia();
        if(mediaItems != null && !mediaItems.isEmpty()) {
            MediaItem media = mediaItems.get(0);
            if("image".equalsIgnoreCase(media.getMedia_type())) {
                articleUrl = media.getUrl();
            }
        }
        if(articleUrl != null) {
            Glide.with(this).load(articleUrl).placeholder(R.drawable.artical).into(mArticleImage);
        }
        else {
            mArticleImage.setImageResource(R.drawable.artical);
        }
    }

    private void updateBookMark(boolean isBookMark) {
        Log.i("TAGBOOKMARK", "ISBOOK " + isBookMark);
        if(!isBookMark){
            mArticleBookMark.setColorFilter(null);
            mArticleBookMark.setImageResource(R.mipmap.ic_bookmark);
        }
        else {
            mArticleBookMark.setColorFilter(ContextCompat.getColor(getContext(), R.color.hh_green_light2), PorterDuff.Mode.SRC_IN);
        }
    }
}
