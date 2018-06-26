package in.healthhunt.view.notificationView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.healthhunt.R;
import in.healthhunt.model.articles.ArticleParams;
import in.healthhunt.model.beans.SpaceDecoration;
import in.healthhunt.model.notification.NotificationsItem;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.notificationPresenter.INotificationPresenter;
import in.healthhunt.presenter.notificationPresenter.NotificationPresenterImp;
import in.healthhunt.view.fullView.fullViewFragments.FullArticleFragment;
import in.healthhunt.view.fullView.fullViewFragments.FullProductFragment;
import in.healthhunt.view.fullView.fullViewFragments.YoutubeFragment;
import in.healthhunt.view.homeScreenView.HomeActivity;
import in.healthhunt.view.homeScreenView.IHomeView;

/**
 * Created by abhishekkumar on 6/2/18.
 */

public class NotificationFragment extends Fragment implements INotificationView, NotificationAdapter.ClickListener{

    @BindView(R.id.notification_recycler_list)
    RecyclerView mNotificationViewer;

    @BindView(R.id.no_records)
    TextView mNoRecords;

    private INotificationPresenter INotificationPresenter;
    private IHomeView IHomeView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IHomeView  =(HomeActivity) getActivity();
        INotificationPresenter = new NotificationPresenterImp(getContext(), this);
        INotificationPresenter.fetchNotifications();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, view);
        IHomeView.setStatusBarTranslucent(false);
        IHomeView.hideBottomNavigationSelection();
        IHomeView.showBottomFooter();
        IHomeView.hideDrawerMenu();
        IHomeView.showActionBar();
        IHomeView.updateTitle(getString(R.string.notifications));
        Log.i("TAGSEARCHFRAGMENT", "SEARCH NOTIFICATION");
        setAdapter();
        return view;
    }

    private void setAdapter() {
        NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(), INotificationPresenter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mNotificationViewer.setLayoutManager(layoutManager);
        mNotificationViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.dpToPx(2, getContext()), SpaceDecoration.VERTICAL));
        mNotificationViewer.setAdapter(notificationAdapter);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View view) {
        return new NotificationHolder(view, this, INotificationPresenter);
    }

    @Override
    public void onStart() {
        super.onStart();
        IHomeView.hideSearchView();
        IHomeView.hideNotificationView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IHomeView.showSearchView();
        IHomeView.showNotificationView();
    }

    @Override
    public int getCount() {
        int count = 0;
        if(INotificationPresenter.getNotificationList() != null){
            count = INotificationPresenter.getNotificationList().size();
        }
        return count;
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

    @Override
    public void updateBottomNavigation() {

    }

    @Override
    public void updateAdapter() {
        mNotificationViewer.getAdapter().notifyDataSetChanged();
        updateVisibility();
    }

    public void updateVisibility(){
        int count = INotificationPresenter.getCount();
        if(count == 0){
            mNoRecords.setVisibility(View.VISIBLE);
            mNotificationViewer.setVisibility(View.GONE);
        }
        else {
            mNoRecords.setVisibility(View.GONE);
            mNotificationViewer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ItemClicked(View v, int position) {
        NotificationsItem notificationsItem = INotificationPresenter.getNotification(position);

        if(notificationsItem != null){
            notificationsItem.setIsViewed(true);
            String post_id = String.valueOf(notificationsItem.getPost_id());
            String postType = notificationsItem.getPost_type();

            if(postType != null && postType.equalsIgnoreCase(ArticleParams.POST)){
                String thumbnailImage = notificationsItem.getVideo_thumbnail_image();
                if(thumbnailImage == null || thumbnailImage.isEmpty()){
                    openFullViewArticle(post_id);
                }
                else {
                    openFullViewVideo(post_id);
                }

            }
            else {
                openFullViewProduct(post_id);
            }
        }
        updateAdapter();
    }


    private void openFullViewArticle(String id){
        Bundle bundle = new Bundle();
        bundle.putString(ArticleParams.ID, id);
        bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.ARTICLE);
        IHomeView.loadNonFooterFragment(FullArticleFragment.class.getSimpleName(), bundle);
    }

    private void openFullViewVideo(String id){
        Bundle bundle = new Bundle();
        bundle.putString(ArticleParams.ID, id);
        bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.VIDEO);
        IHomeView.loadNonFooterFragment(YoutubeFragment.class.getSimpleName(), bundle);
    }

    private void openFullViewProduct(String id){
        Bundle bundle = new Bundle();
        bundle.putString(ArticleParams.ID, id);
        bundle.putInt(ArticleParams.POST_TYPE, ArticleParams.PRODUCT);
        IHomeView.loadNonFooterFragment(FullProductFragment.class.getSimpleName(), bundle);
    }
}
