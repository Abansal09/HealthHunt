package in.healthhunt.view.notificationView;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.notification.NotificationsItem;
import in.healthhunt.presenter.notificationPresenter.INotificationPresenter;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by abhishekkumar on 4/27/18.
 */

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private INotificationPresenter INotificationPresenter;
    private Context mContext;
    private ClickListener mClickListener;

    public NotificationAdapter(Context context, INotificationPresenter notificationPresenter) {
        mContext = context;
        INotificationPresenter = notificationPresenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return INotificationPresenter.createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setContent(((NotificationHolder)holder), position);
    }

    private void setContent(NotificationHolder holder, int position) {
        NotificationsItem notificationsItem = INotificationPresenter.getNotification(position);

        if(notificationsItem != null) {

            String authorUrl = notificationsItem.getUser_url();
            if(authorUrl != null) {
                authorUrl = authorUrl.replace("\n", "");
                Glide.with(mContext)
                        .load(authorUrl)
                        .bitmapTransform(new CropCircleTransformation(mContext)).placeholder(R.mipmap.avatar)
                        .into(holder.mPic);
            }

            String thumbnailImage = notificationsItem.getVideo_thumbnail_image();
            if(thumbnailImage == null || thumbnailImage.isEmpty()){
                thumbnailImage = notificationsItem.getPost_image_url();
            }

            if(thumbnailImage != null) {

                String postType = notificationsItem.getPost_type();
                int placeHolder = R.drawable.artical;
                if(postType != null && postType.equalsIgnoreCase(ArticleParams.MARKET)){
                    placeHolder = R.drawable.top_products;
                }

                Glide.with(mContext)
                        .load(thumbnailImage)
                        .placeholder(placeHolder)
                        .dontAnimate()
                        .into(holder.mNotificationItemImage);
            }

            String name = notificationsItem.getUser_name();
            if(name != null) {
                String action = notificationsItem.getEvent_type();
                if(action != null){
                    action = getActionType(action);
                }

                Spannable spannable = getSpannable(name, action);
                holder.mName.setText(spannable, TextView.BufferType.SPANNABLE);
            }
            /*else {
                holder.mName.setText("");
            }*/



            String date = notificationsItem.getEvent_time();
            if(date != null) {
                holder.mHour.setText(date);
            }
            else {
                holder.mHour.setText("");
            }

            boolean isViewed = notificationsItem.IsViewed();
            int color = Color.WHITE;
            if(!isViewed){
                color = ContextCompat.getColor(mContext, R.color.hh_green_light4);
            }
            holder.mNotificationView.setBackgroundColor(color);

        }
    }

    @Override
    public int getItemCount() {
        return INotificationPresenter.getCount();
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked(View v, int position);
    }

    private String getActionType(String str){

        String action = mContext.getString(R.string.card_like);
        if(str != null){
            if(str.contains("comment")){
                action = mContext.getString(R.string.card_comment);
            }
            else if(str.contains("share")){
                action = mContext.getString(R.string.card_share);
            }
        }

        return action;
    }

    private Spannable getSpannable(String name , String action) {

        String str = name + " "  +  action;
        Spannable spannable = new SpannableString(str);

        spannable.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(mContext, R.color.hh_edittext_hint_color)),
                name.length() + 1 , str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }
}
