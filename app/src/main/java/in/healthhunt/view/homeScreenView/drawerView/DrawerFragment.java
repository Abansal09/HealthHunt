package in.healthhunt.view.homeScreenView.drawerView;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.healthhunt.R;
import in.healthhunt.model.login.User;
import in.healthhunt.model.tags.TagItem;
import in.healthhunt.presenter.homeScreenPresenter.IHomePresenter;
import in.healthhunt.presenter.userPresenter.IUserPresenter;
import in.healthhunt.presenter.userPresenter.UserPresenterImp;
import in.healthhunt.view.homeScreenView.HomeActivity;
import in.healthhunt.view.homeScreenView.IHomeView;
import in.healthhunt.view.profileView.IEditProfileView;
import in.healthhunt.view.profileView.ProfileFragment;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by abhishekkumar on 5/8/18.
 */

public class DrawerFragment extends Fragment implements IEditProfileView, CategoryAdapter.ClickListener {


    @BindView(R.id.drawer_list_viewer)
    RecyclerView mDrawerList;

    @BindView(R.id.profile_pic)
    ImageView mProfilePic;

    @BindView(R.id.profile_name)
    TextView mUserName;

    private IHomePresenter IHomePresenter;
    private IUserPresenter IUserPresenter;
    private Unbinder mUnbinder;
    private CategoryAdapter mCategoryAdapter;
    private IHomeView IHomeView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IHomePresenter = ((HomeActivity)getActivity()).getHomePresenter();
        IUserPresenter = new UserPresenterImp(getContext(), this);
        IHomeView = (HomeActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        setUserInfo();
        setAdapter();
        return view;
    }

    private void setUserInfo() {

        User user = User.getCurrentUser();
        String name = user.getFirst_name();//getName();//HealthHuntPreference.getString(getContext(), user.getUsername());
        Log.i("TAGUSERNAME", "First Name " + name);

        if(name == null || name.isEmpty()){
            name = user.getName();
        }
        if(name != null) {
            mUserName.setText(name);
        }

        String url = user.getUser_image();//HealthHuntPreference.getString(getContext(), user.getUserId());

        if(url != null) {
            url = url.replace("\n", "");
            Glide.with(getContext())
                    .load(url).dontAnimate()
                    .bitmapTransform(new CropCircleTransformation(getContext())).placeholder(R.mipmap.default_profile)
                    .into(mProfilePic);
        }
        else {
            mProfilePic.setImageResource(R.mipmap.default_profile);
        }
    }

    private void setAdapter() {
        mCategoryAdapter = new CategoryAdapter(getContext(), IHomePresenter);
        mCategoryAdapter.setClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mDrawerList.setLayoutManager(layoutManager);
        mDrawerList.setAdapter(mCategoryAdapter);

        Log.i("TAGSelectIed", "SelectIed");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void ItemClicked(View v, int pos) {
        TagItem tagItem = IHomePresenter.getCategory(pos);

        if(tagItem.getId() != 1) {
            if (IHomePresenter.isCategoryContain(tagItem)) {
                IHomePresenter.removeCategory(tagItem);
            } else {
                IHomePresenter.addCategory(tagItem);
            }

            TagItem item = IHomePresenter.getCategory(0);
            if(IHomePresenter.isCategoryContain(item)){
                IHomePresenter.removeCategory(item);
            }
        }
        else {
            IHomePresenter.getSelectedCategoryList().clear();
            IHomePresenter.addCategory(tagItem);
        }

        updateAdapter();
    }

    public void updateAdapter(){
        if(mCategoryAdapter != null){
            mCategoryAdapter.notifyDataSetChanged();
        }
    }

    /*@OnClick(R.id.edit_profile)
    void onEdit(){
        IHomeView.closeDrawer();

        *//*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {*//*
                IHomePresenter.loadNonFooterFragment(ProfileFragment.class.getSimpleName(), null);
         //   }
       // }, 100);
    }*/


    @OnClick(R.id.drawer_profile_view)
    void onDrawerProfile(){
        IHomeView.closeDrawer();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                IHomePresenter.loadNonFooterFragment(ProfileFragment.class.getSimpleName(), null);
            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void updateUserInfo(){
        setUserInfo();
    }

    @Override
    public void showAlert(String msg) {
    }

    public void updateUserData(){
        IUserPresenter.fetchCurrentUser();
    }

    /*public void updateCategory(){
        List<String> categoryList = IHomeView.getCategories();

        List<TagItem> prevCategory = IHomePresenter.getSelectedCategoryList();
        if(prevCategory != null) {
            prevCategory.clear();
        }

        Log.i("TAGTAGLIST", "category " + categoryList);
        int count = IHomePresenter.getCategoryCount();
        for(int i=0; i<count; i++) {
            TagItem category = IHomePresenter.getCategory(i);
            if(categoryList.contains(String.valueOf(category.getId()))){
                IHomePresenter.addCategory(category);
            }
        }

        updateAdapter();
    }*/
}
