package in.healthhunt.view.notificationView;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import in.healthhunt.view.homeScreenView.myFeedView.IView;

/**
 * Created by abhishekkumar on 6/2/18.
 */

public interface INotificationView extends IView{
    RecyclerView.ViewHolder onCreateViewHolder(View view);
    void updateAdapter();
}
