package in.healthhunt.presenter.notificationPresenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.retrofit.RestError;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.notification.NotificationData;
import in.healthhunt.model.notification.NotificationsItem;
import in.healthhunt.presenter.interactor.notificationInteractor.INotificationInteractor;
import in.healthhunt.presenter.interactor.notificationInteractor.NotificationInteractorImpl;
import in.healthhunt.view.notificationView.INotificationView;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class NotificationPresenterImp implements INotificationPresenter, INotificationInteractor.OnFinishListener{

    private String TAG = NotificationPresenterImp.class.getSimpleName();
    private Context mContext;
    private List<NotificationsItem> mNotificationsItems;
    private INotificationInteractor INotificationInteractor;
    private INotificationView INotificationView;


    public NotificationPresenterImp(Context context, INotificationView notificationView) {
        mContext =  context;
        INotificationView = notificationView;
        INotificationInteractor = new NotificationInteractorImpl();
    }

    @Override
    public void fetchNotifications() {
        INotificationView.showProgress();
        Map<String, String> map = new HashMap<String, String>();
        map.put(ArticleParams.FETCH, ArticleParams.NOTI_ALL);
        map.put(ArticleParams.OFFSET, String.valueOf(0));
        map.put(ArticleParams.LIMIT, String.valueOf(30));
        INotificationInteractor.fetchNotifications(mContext, map, this);
    }

    @Override
    public int getCount() {
        return INotificationView.getCount();
    }

    @Override
    public List<NotificationsItem> getNotificationList() {
        return mNotificationsItems;
    }

    @Override
    public NotificationsItem getNotification(int pos) {
        NotificationsItem notificationsItem = null;
        if(mNotificationsItems != null && !mNotificationsItems.isEmpty()){
            notificationsItem = mNotificationsItems.get(pos);
        }
        return notificationsItem;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(View view) {
        return INotificationView.onCreateViewHolder(view);
    }

    @Override
    public void onNotificationSuccess(NotificationData notificationData) {
        INotificationView.hideProgress();
        mNotificationsItems = notificationData.getNotifications();
        INotificationView.updateAdapter();
    }

    @Override
    public void onError(RestError errorInfo) {
        INotificationView.hideProgress();
        if(errorInfo != null){
            INotificationView.showAlert(errorInfo.getMessage());
        }
    }
}
