package in.healthhunt.view.notificationView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.presenter.notificationPresenter.INotificationPresenter;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class NotificationHolder extends RecyclerView.ViewHolder{

    private Context mContext;

    @BindView(R.id.notification_item_view)
    LinearLayout mNotificationView;

    @BindView(R.id.pic)
    ImageView mPic;

    @BindView(R.id.notification_item_image)
    ImageView mNotificationItemImage;

    @BindView(R.id.name)
    TextView mName;

   /* @BindView(R.id.action_type)
    TextView mAction;
*/
    @BindView(R.id.hour)
    TextView mHour;

    private INotificationPresenter INotificationPresenter;
    private NotificationAdapter.ClickListener mClickListener;

    public NotificationHolder(View articleView, NotificationAdapter.ClickListener clickListener, INotificationPresenter notificationPresenter) {
        super(articleView);
        ButterKnife.bind(this, articleView);
        mContext = articleView.getContext();
        mClickListener = clickListener;
        INotificationPresenter = notificationPresenter;
    }

    @OnClick(R.id.notification_item_view)
    void onClick(View view) {
        if(mClickListener != null) {
            mClickListener.ItemClicked(view, getAdapterPosition());
        }
    }
}