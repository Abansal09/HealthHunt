package in.healthhunt.presenter.userPresenter;

import android.content.Context;

import framework.retrofit.RestError;
import in.healthhunt.model.login.User;
import in.healthhunt.model.user.UserRequest;
import in.healthhunt.presenter.interactor.userInteractor.IUserInteractor;
import in.healthhunt.presenter.interactor.userInteractor.UserInteractorImpl;
import in.healthhunt.view.profileView.IEditProfileView;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class UserPresenterImp implements IUserPresenter, IUserInteractor.OnUserUpdateFinishListener,
        IUserInteractor.OnUserFinishListener{

    private String TAG = UserPresenterImp.class.getSimpleName();
    private Context mContext;
    private IEditProfileView IEditProfileView;
    private IUserInteractor IUserInteractor;

    public UserPresenterImp(Context context, IEditProfileView editProfileView) {
        mContext =  context;
        IEditProfileView = editProfileView;
        IUserInteractor = new UserInteractorImpl();
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public void updateUser(UserRequest userRequest) {
        IEditProfileView.showProgress();
        IUserInteractor.updateUser(mContext, userRequest, this);
    }

    @Override
    public void fetchCurrentUser() {
        IUserInteractor.fetchCurrentUser(mContext, this);
    }

    @Override
    public void onError(RestError errorInfo) {
        IEditProfileView.hideProgress();
        if(errorInfo != null) {
            IEditProfileView.showAlert(errorInfo.getMessage());
        }
    }

    @Override
    public void onUserSuccess(User user) {
        updateUserData(user);
        IEditProfileView.updateUserInfo();
    }

    @Override
    public void onUserUpdateSuccess(User user) {
        IEditProfileView.hideProgress();
        updateUserData(user);
        IEditProfileView.updateUserInfo();
    }

    private void updateUserData(User user){
        User savedUser = User.getUser(user.getUserId());
        String tagList = savedUser.getTagList();
        String continueList = savedUser.getContinueList();
        User.removeUser(savedUser.getUserId());

        user.setTagList(tagList);
        user.setContinueList(continueList);
        user.setCurrentLogin(true);
        user.save();
    }
}
