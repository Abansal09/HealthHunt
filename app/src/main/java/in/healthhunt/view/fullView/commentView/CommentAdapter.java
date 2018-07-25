package in.healthhunt.view.fullView.commentView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;

import in.healthhunt.R;
import in.healthhunt.model.articles.commonResponse.Author;
import in.healthhunt.model.articles.commonResponse.Content;
import in.healthhunt.model.comment.CommentsItem;
import in.healthhunt.model.login.User;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.model.utility.URLImageParser;
import in.healthhunt.presenter.fullPresenter.IFullPresenter;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    private IFullPresenter IFullPresenter;
    private Context mContext;
    private ClickListener mClickListener;
    private int mEditCommentId = Integer.MIN_VALUE;

    public CommentAdapter(Context context, IFullPresenter fullPresenter) {
        mContext = context;
        IFullPresenter = fullPresenter;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item_view, parent, false);
        return IFullPresenter.createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        setContent(holder, position);
    }

    private void setContent(CommentViewHolder holder, int position) {
        CommentsItem commentsItem = IFullPresenter.getComment(position);

        if(commentsItem != null) {
            Author author = commentsItem.getAuthor();
            if(author != null){
                String name = author.getName();
                holder.mUserName.setText(name);

                User user = User.getCurrentUser();
                String user_id = user.getUserId();//HealthHuntPreference.getString(mContext, Constants.USER_ID);
                Log.i("TAGUSERU", "User_ID " + user_id + " Author ID " + author.getAuthor_id() );
                if(user_id.equals(String.valueOf(author.getAuthor_id()))){
                    holder.mCommentEdit.setVisibility(View.VISIBLE);
                }
                else{
                    holder.mCommentEdit.setVisibility(View.GONE);
                }

                String url = author.getUrl();
                Glide.with(mContext)
                        .load(url)
                        .bitmapTransform(new CropCircleTransformation(mContext)).placeholder(R.mipmap.default_profile)
                        .into(holder.mUserPic);

            }

            String date = commentsItem.getDate();
            //Log.i("TAGDATEDATE", "Date " + date);
            date = HealthHuntUtility.getLocalTime(date);
            //Log.i("TAGDATEDATE", "Date Local " + date);
            if(date != null) {

                long milli = HealthHuntUtility.getDateInMilliSeconds(date);
                date = HealthHuntUtility.getTimeAgo(milli, mContext);
                if(date == null || date.isEmpty()){
                    date = commentsItem.getDate();
                    date = HealthHuntUtility.getDateWithFormat(date);
                }
            }
            holder.mCommentDate.setText(date);

            Content content = commentsItem.getContent();
            if(content != null) {


                Log.i("TAGTAGTAG", "Content " + content.getRendered());
                //byte[] data = Base64.decode(str, Base64.DEFAULT);

                URLImageParser imageGetter = new URLImageParser(mContext, holder.mCommentText);
                Spannable html;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    html = (Spannable) Html.fromHtml(content.getRendered(), Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
                } else {
                    html = (Spannable) Html.fromHtml(content.getRendered(), imageGetter, null);
                }

                //String str = content.getRendered();
                /*try {
                    *//*byte[] encodeValue = Base64.encode(str.getBytes("UTF-8"), Base64.DEFAULT);
                    byte[] decodeValue = Base64.decode(encodeValue, Base64.DEFAULT);

                    //byte[] data = Base64.decode(str, Base64.DEFAULT);
                    str = new String(decodeValue, "UTF-8");
                }*//*
                catch (UnsupportedEncodingException unsupportedEncodingException){

                }*/
                String str = null;
                try {
                    str = java.net.URLDecoder.decode( html.toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.i("TAGCOMMENT", "comment ID " + commentsItem.getId());
                if(str != null){
                    str = str.trim();
                }
                holder.mCommentText.setText(str);
                if(mEditCommentId > 0 && mEditCommentId == commentsItem.getId()) {
                    /*holder.mCommentText.setVisibility(View.VISIBLE);
                    holder.mCommentEditText.setVisibility(View.GONE);
                    holder.mCommentUpdate.setVisibility(View.GONE);*/
                    holder.mCommentText.setVisibility(View.GONE);
                    holder.mCommentEditText.setVisibility(View.VISIBLE);
                    holder.mCommentUpdate.setVisibility(View.VISIBLE);
                    holder.mCommentEditText.setText("");
                    holder.mCommentEditText.append(str.trim());
                    //holder.mCommentEditText.setText(holder.mCommentText.getText().toString().trim());
                    //holder.mCommentEditText.setSelection(holder.mCommentText.getText().toString().length() - 1);
                    holder.mCommentEditText.requestFocus();
                    //holder.mCommentEditText.setShowSoftInputOnFocus(true);
                }
                else {
                    holder.mCommentText.setVisibility(View.VISIBLE);
                    holder.mCommentEditText.setVisibility(View.GONE);
                    holder.mCommentUpdate.setVisibility(View.GONE);
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return IFullPresenter.getCommentCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener {
        void onMore(View v, int position);
        void update(View v, int position);
    }

    public void addEditCommentPos(int editCommentId){
        mEditCommentId = editCommentId;
    }
}