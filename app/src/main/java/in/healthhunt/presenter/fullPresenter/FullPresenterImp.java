package in.healthhunt.presenter.fullPresenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.retrofit.RestError;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.articles.articleResponse.ArticlePostItem;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkData;
import in.healthhunt.model.articles.bookmarkResponse.BookMarkInfo;
import in.healthhunt.model.articles.commonResponse.CurrentUser;
import in.healthhunt.model.articles.productResponse.ProductPostItem;
import in.healthhunt.model.comment.CommentData;
import in.healthhunt.model.comment.CommentRequest;
import in.healthhunt.model.comment.CommentsItem;
import in.healthhunt.model.likes.LikesInfo;
import in.healthhunt.model.likes.LikesRequest;
import in.healthhunt.presenter.interactor.articleInteractor.ArticleInteractorImpl;
import in.healthhunt.presenter.interactor.articleInteractor.IArticleInteractor;
import in.healthhunt.presenter.interactor.bookMarkInteractor.BookMarkInteractorImpl;
import in.healthhunt.presenter.interactor.bookMarkInteractor.IBookMarkInteractor;
import in.healthhunt.presenter.interactor.commentInteractor.CommentInteractorImpl;
import in.healthhunt.presenter.interactor.commentInteractor.ICommentInteractor;
import in.healthhunt.presenter.interactor.likesInteractor.ILikesInteractor;
import in.healthhunt.presenter.interactor.likesInteractor.LikesInteractorImpl;
import in.healthhunt.presenter.interactor.productInteractor.IProductInteractor;
import in.healthhunt.presenter.interactor.productInteractor.ProductInteractorImpl;
import in.healthhunt.view.fullView.commentView.CommentViewHolder;
import in.healthhunt.view.fullView.fullViewFragments.IFullFragment;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class FullPresenterImp implements IFullPresenter, IArticleInteractor.OnFullViewFinishListener,
        IProductInteractor.OnFullViewFinishListener,  IBookMarkInteractor.OnFinishListener ,
        ILikesInteractor.OnFinishListener, ICommentInteractor.OnFinishListener, IArticleInteractor.OnRelatedFinishListener,
        IProductInteractor.OnRelatedProductFinishListener{

    private String TAG = FullPresenterImp.class.getSimpleName();
    private IFullFragment IFullFragment;
    private Context mContext;
    private ArticlePostItem mArticlePost;
    private ProductPostItem mProductPost;
    // private IFullInteractor IFullInteractor;
    private IBookMarkInteractor IBookMarkInteractor;
    private ILikesInteractor ILikesInteractor;
    private ICommentInteractor ICommentInteractor;
    private IArticleInteractor IArticleInteractor;
    private IProductInteractor IProductInteractor;
    private int mOffset = 0;
    private int mLimit = 10;
    private List<CommentsItem> mCommentsItems;
    private List<ArticlePostItem> mRelatedArticleItems;
    private List<ProductPostItem> mRelatedProductItems;
    private int mArticleCount;
    private int mProductCount;

    public FullPresenterImp(Context context, IFullFragment fullView) {
        mContext =  context;
        IFullFragment = fullView;
        //IFullInteractor = new FullInteractorImpl();
        IBookMarkInteractor = new BookMarkInteractorImpl();
        ILikesInteractor = new LikesInteractorImpl();
        ICommentInteractor = new CommentInteractorImpl();
        IArticleInteractor = new ArticleInteractorImpl();
        IProductInteractor = new ProductInteractorImpl();
    }

    @Override
    public void loadFragment(String tag, Bundle bundle) {
        IFullFragment.loadFragment(tag, bundle);
    }

    @Override
    public void fetchArticle(String id) {
        mArticleCount = 3;
        IFullFragment.showProgress();
        IArticleInteractor.fetchFullArticle(mContext, id, this);
        fetchRelatedArticles(id);
        fetchRelatedProducts(id, ArticleParams.ARTICLE);
    }

    private void fetchRelatedArticles(String id){
        Map<String, String> map = new HashMap<String, String>();
        String filter = ArticleParams.FILTER + "[" + ArticleParams.FORMAT + "]";
        map.put(filter, ArticleParams.POST_FORMAT_IMAGE);
        map.put(ArticleParams.RELATED, id);
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.OFFSET, String.valueOf(0));
        map.put(ArticleParams.LIMIT, String.valueOf(2));
        IArticleInteractor.fetchRelatedArticle(mContext, map, this);
    }

    private void fetchRelatedProducts(String id, int type){
        Map<String, String> map = new HashMap<String, String>();
        map.put(ArticleParams.RELATED, id);
        map.put(ArticleParams.APP, String.valueOf(1));
        map.put(ArticleParams.TYPE, ArticleParams.MARKET);
        map.put(ArticleParams.OFFSET, String.valueOf(0));
        map.put(ArticleParams.LIMIT, String.valueOf(2));
        IProductInteractor.fetchRelatedProduct(mContext, type, map, this);
    }

    @Override
    public void fetchProduct(String id) {
        mProductCount = 2;
        IFullFragment.showProgress();
        IProductInteractor.fetchFullProduct(mContext, id, this);
        fetchRelatedProducts(id, ArticleParams.PRODUCT);
    }

    @Override
    public ArticlePostItem getArticle() {
        return mArticlePost;
    }

    @Override
    public ProductPostItem getProduct() {
        return mProductPost;
    }

    @Override
    public void bookmark(String id, int type) {
        IFullFragment.showProgress();
        IBookMarkInteractor.bookmark(mContext, id, type, this);
    }

    @Override
    public void unBookmark(String id, int type) {
        IFullFragment.showProgress();
        IBookMarkInteractor.unBookmark(mContext, id, type, this);
    }

    @Override
    public CommentViewHolder createViewHolder(View view) {
        return IFullFragment.createViewHolder(view);
    }

    @Override
    public int getCommentCount() {
        int size  = 0;
        if(mCommentsItems != null){
            size = mCommentsItems.size();
        }
        return size;
    }

    @Override
    public void fetchComments(String id) {
        IFullFragment.showProgress();
        mOffset = 0;
        mLimit = 10;
        Map<String, String> map = new HashMap<String, String>();
        map.put(ArticleParams.POST_ID, id);
        map.put(ArticleParams.ORDER_BY, ArticleParams.DATE);
        map.put(ArticleParams.ORDER, ArticleParams.DESC);
        map.put(ArticleParams.OFFSET, String.valueOf(mOffset));
        map.put(ArticleParams.LIMIT, String.valueOf(mLimit));
        ICommentInteractor.fetchComments(mContext, map, this);
    }

    @Override
    public List<CommentsItem> getComments() {
        return mCommentsItems;
    }

    @Override
    public CommentsItem getComment(int pos) {
        CommentsItem commentsItem = mCommentsItems.get(pos);
        return commentsItem;
    }

    @Override
    public void deleteComment(String id) {
        IFullFragment.showProgress();
        ICommentInteractor.deleteComment(mContext, id, this);
    }

    @Override
    public void addNewComment(String post_id, String content) {
        IFullFragment.showProgress();
        CommentRequest request = new CommentRequest();
        request.setPost_id(post_id);
        request.setContent(content);
        Log.i("TAGPOSTID", " ID " + post_id + " content " + content);
        ICommentInteractor.addNewComment(mContext, request, this);
    }

    @Override
    public void updateComment(String id, String content) {
        IFullFragment.showProgress();
        CommentRequest request = new CommentRequest();
        request.setContent(content);
        ICommentInteractor.updateComment(mContext, id, request,this);
    }

    @Override
    public void updateLike(String id) {
        IFullFragment.showProgress();
        LikesRequest likesRequest = new LikesRequest();
        likesRequest.setLike_type(String.valueOf(2));
        ILikesInteractor.updateLikes(mContext, id, likesRequest, this);
    }

    @Override
    public ArticlePostItem getRelatedArticle(int pos) {
        if(mRelatedArticleItems != null){
            return mRelatedArticleItems.get(pos);
        }
        return null;
    }

    @Override
    public int getRelatedArticlesCount() {
        int size = 0;
        if(mRelatedArticleItems != null){
            size = mRelatedArticleItems.size();
        }
        return size;
    }

    @Override
    public ProductPostItem getRelatedProduct(int pos) {
        if(mRelatedProductItems != null){
            return mRelatedProductItems.get(pos);
        }
        return null;
    }

    @Override
    public int getRelatedProductsCount() {
        int size = 0;
        if(mRelatedProductItems != null){
            size = mRelatedProductItems.size();
        }
        return size;
    }

    @Override
    public void updateArticle(ArticlePostItem articlePost) {
        mArticlePost = articlePost;
        updateArticleInfo();
    }

    @Override
    public void updateProduct(ProductPostItem productPost) {
        mProductPost = productPost;
        updateProductInfo();
    }

    @Override
    public void onArticleSuccess(ArticlePostItem item) {
        Log.i("TAGITEM", "ITEM " + item);
        mArticleCount--;
        mArticlePost = item;
        updateArticleInfo();
    }

    @Override
    public void onProductSuccess(ProductPostItem item) {
        mProductCount--;
        mProductPost = item;
        updateProductInfo();
    }

    @Override
    public void onBookMarkSuccess(BookMarkData markResponse) {
        IFullFragment.hideProgress();
        BookMarkInfo bookMarkInfo = markResponse.getBookMarkInfo();

        if(bookMarkInfo == null){
            Log.i("TAG", " Book Mark info is null");
            return;
        }

        int type = bookMarkInfo.getType();
        CurrentUser currentUser = null;
        switch (type){
            case ArticleParams.ARTICLE:
            case ArticleParams.VIDEO:
                if(bookMarkInfo.getPost_id().equals(String.valueOf(mArticlePost.getArticle_Id()))) {
                    currentUser = mArticlePost.getCurrent_user();
                    if (currentUser != null) {
                        currentUser.setBookmarked(bookMarkInfo.isBookMark());

                        if(type == ArticleParams.ARTICLE) {
                            IFullFragment.updateArticleSaved(mArticlePost);
                        }
                        else {
                            IFullFragment.updateVideoSaved(mArticlePost);
                        }
                    }
                }
                break;
            case ArticleParams.RELATED_ARTICLES:
                for (int i = 0; i < getRelatedArticlesCount(); i++) {
                    ArticlePostItem postItem = getRelatedArticle(i);
                    BookMarkInfo bookMark = markResponse.getBookMarkInfo();
                    if (bookMark.getPost_id().equals(String.valueOf(postItem.getArticle_Id()))) {
                        CurrentUser current = postItem.getCurrent_user();
                        if (current != null) {
                            current.setBookmarked(bookMarkInfo.isBookMark());
                            IFullFragment.updateArticleSaved(postItem);
                        }
                        break;
                    }
                }
                IFullFragment.updateRelatedArticleAdapter();
                break;

            case ArticleParams.RELATED_PRODUCTS:
                for(int i = 0; i< getRelatedProductsCount(); i++){
                    ProductPostItem postItem = getRelatedProduct(i);
                    BookMarkInfo info = markResponse.getBookMarkInfo();
                    if(info != null) {
                        if (postItem.getProduct_id().equals(info.getPost_id())) {
                            postItem.getCurrent_user().setBookmarked(info.isBookMark());
                            IFullFragment.updateProductSaved(postItem);
                            break;
                        }
                    }
                }
                IFullFragment.updateRelatedProductAdapter();
                break;

            case ArticleParams.PRODUCT:
                if(bookMarkInfo.getPost_id().equals(String.valueOf(mProductPost.getProduct_id()))) {
                    currentUser = mProductPost.getCurrent_user();
                    if (currentUser != null) {
                        currentUser.setBookmarked(bookMarkInfo.isBookMark());
                        IFullFragment.updateProductSaved(mProductPost);
                    }
                }
                break;
        }

        IFullFragment.updateBookMarkIcon();
    }

    @Override
    public void onBookMarkError(RestError errorInfo) {
        IFullFragment.hideProgress();
        if(errorInfo != null) {
            IFullFragment.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onRelatedSuccess(List<ArticlePostItem> items) {
        mArticleCount--;
        mRelatedArticleItems = items;
        /*for(ArticlePostItem postItem: mRelatedArticleItems){
            Log.i("TAGRELATED", "id " + postItem.getArticle_Id());
        }*/
        //Log.i("TAGRTICLERELATED", "RELATED " + mRelatedArticleItems.size());
        updateArticleInfo();
    }

    @Override
    public void onRelatedProductSuccess(List<ProductPostItem> items, int type) {
        mRelatedProductItems = items;
        if(type == ArticleParams.ARTICLE){
            mArticleCount--;
            Log.i("TAGRTICLERELATED", "RELATED Product Article " + mRelatedProductItems);
            updateArticleInfo();
        }
        else {
            mProductCount--;
            updateProductInfo();
        }
    }

    @Override
    public void onError(RestError errorInfo) {
        IFullFragment.hideProgress();
        if(errorInfo != null) {
            IFullFragment.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onLikesError(RestError errorInfo) {
        IFullFragment.hideProgress();
        if(errorInfo != null) {
            IFullFragment.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onLikesSuccess(LikesInfo likesInfo) {


        CurrentUser currentUser = null;

        Log.i("PostType " , "TYPE " + IFullFragment.getPostType());
        switch (IFullFragment.getPostType()){
            case ArticleParams.ARTICLE:
            case ArticleParams.VIDEO:
                currentUser = mArticlePost.getCurrent_user();
                break;

            case ArticleParams.PRODUCT:
                currentUser = mProductPost.getCurrent_user();
                break;
        }

        Log.i("TAGUSERcurrentUser" , "currentUser " + currentUser);

        if(currentUser != null) {
            int likes = currentUser.getLike();
            Log.i("TAGUSER" , "Likes " + likes);
            if(likes == 0){
                likes = 1;
            }
            else {
                likes = 0;
            }
            currentUser.setLike(likes);
        }

        IFullFragment.hideProgress();
        IFullFragment.updateLike();
    }

    @Override
    public void onCommentError(RestError errorInfo) {
        IFullFragment.hideProgress();
        if(errorInfo != null) {
            IFullFragment.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onCommentSuccess(List<CommentsItem> item) {
        IFullFragment.hideProgress();
        mCommentsItems = item;
        mOffset = mLimit;
        mLimit+=10;
        IFullFragment.updateCommentAdapter();
    }

    @Override
    public void onCommentDeleteSuccess(String id) {
        for(CommentsItem item: mCommentsItems){
            if(id.equals(String.valueOf(item.getId()))){
                mCommentsItems.remove(item);

                String comments = null;
                switch (IFullFragment.getPostType()){
                    case ArticleParams.ARTICLE:
                    case ArticleParams.VIDEO:
                        comments = mArticlePost.getComments();;
                        int com = Integer.parseInt(comments);
                        com--;
                        mArticlePost.setComments(String.valueOf(com));

                        break;

                    case ArticleParams.PRODUCT:
                        comments = mProductPost.getComments();
                        int proCom = Integer.parseInt(comments);
                        proCom--;
                        mProductPost.setComments(String.valueOf(proCom));

                        break;
                }
                break;
            }
        }

        IFullFragment.hideProgress();
        IFullFragment.updateCommentAdapter();
    }

    @Override
    public void onNewCommentSuccess(CommentData newComment) {
        int post_comment = newComment.getPost_comment_count();

        switch (IFullFragment.getPostType()){
            case ArticleParams.ARTICLE:
            case ArticleParams.VIDEO:
                mArticlePost.setComments(String.valueOf(post_comment));
                break;

            case ArticleParams.PRODUCT:
                mProductPost.setComments(String.valueOf(post_comment));
                break;

        }

        mCommentsItems.add(0, newComment.getComment()); // always add new comment top of the list
        IFullFragment.hideProgress();
        IFullFragment.updateCommentAdapter();
    }

    @Override
    public void onUpdateSuccess(CommentsItem commentsItem) {
        // int pos = 0;
        for(int i=0; i<mCommentsItems.size(); i++){
            CommentsItem item = mCommentsItems.get(i);
            if(item.getId() == commentsItem.getId()){
                //pos = i;
                mCommentsItems.set(i, commentsItem);
                break;
            }
        }

        /*Log.i("TAGDATA", " Content " + commentsItem.getContent().getRendered());
        CommentsItem item = mCommentsItems.get(pos);
        item.getContent().setRendered(commentsItem.getContent().getRendered());
        mCommentsItems.set(pos, item);
*/
        //  Log.i("TAGPOS " , "POS " + pos);
        IFullFragment.hideProgress();
        IFullFragment.updateCommentAdapter();
    }

    private void updateArticleInfo(){
        if(mArticleCount == 0){
            IFullFragment.hideProgress();
            IFullFragment.setContent();
        }
    }

    private void updateProductInfo(){
        if(mProductCount == 0){
            IFullFragment.hideProgress();
            IFullFragment.setContent();
        }
    }
}
