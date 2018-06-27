package in.healthhunt.view.profileView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.model.login.User;
import in.healthhunt.model.user.UserRequest;
import in.healthhunt.presenter.userPresenter.IUserPresenter;
import in.healthhunt.presenter.userPresenter.UserPresenterImp;
import in.healthhunt.view.homeScreenView.HomeActivity;
import in.healthhunt.view.homeScreenView.IHomeView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_OK;

/**
 * Created by abhishekkumar on 6/3/18.
 */

public class EditProfileFragment extends Fragment implements IEditProfileView{

    @BindView(R.id.user_pic)
    ImageView mProfilePic;

    @BindView(R.id.change_photo)
    TextView mChangePhoto;

    @BindView(R.id.profile_details)
    LinearLayout mUserInfo;

    @BindView(R.id.username)
    EditText mUserName;

    @BindView(R.id.email)
    EditText mEmail;

    @BindView(R.id.bio)
    EditText mBio;

    @BindView(R.id.edit_profile_text)
    TextView mCancel;

    @BindView(R.id.logout)
    TextView mDone;

    private IUserPresenter IUserPresenter;
    private IHomeView IHomeView;
    private final int PICK_IMAGE_REQUEST = 1001;
    private Uri mProfileUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IHomeView = (HomeActivity)getActivity();
        IUserPresenter = new UserPresenterImp(getContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_edit_profile, container, false);
        ButterKnife.bind(this, view);
        mChangePhoto.setVisibility(View.VISIBLE);
        mUserInfo.setVisibility(View.GONE);
        mCancel.setText(R.string.cancel);
        mDone.setText(R.string.done);
        IHomeView.hideDrawerMenu();
        IHomeView.updateTitle(getString(R.string.edit_profile));
        IHomeView.hideBottomNavigationSelection();
        setUserInfo();
        return view;
    }

    public void setUserInfo() {

        User user = User.getCurrentUser();
        String name = user.getFirst_name();//user.getName();//HealthHuntPrefere

        // nce.getString(getContext(), user.getUsername());

        if(name == null || name.isEmpty()){
            name = user.getName();
        }

        if(name != null) {
            mUserName.setText(name);
        }

        String email = user.getEmail();//HealthHuntPrefere

        // nce.getString(getContext(), user.getUsername());
        if(email != null) {
            mEmail.setText(email);
        }

        String bio = user.getDescription();//getBio();
        if(bio != null){
            mBio.setText(bio);
        }

        String url = user.getUser_image();//HealthHuntPreference.getString(getContext(), user.getUserId());
        // mProfileUrl = url;
        if(url != null) {
            url = url.replace("\n", "");
            Glide.with(getContext())
                    .load(url)
                    .bitmapTransform(new CropCircleTransformation(getContext())).placeholder(R.mipmap.default_profile)
                    .into(mProfilePic);
        }
        else {
            mProfilePic.setImageResource(R.mipmap.default_profile);
        }

        //String designation = user.get
    }

    @OnClick(R.id.edit_profile_text) //Cancel Button
    void onCancel(){
        finishFragment();
    }

    private void finishFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @OnClick(R.id.logout) //Done Button
    void onDone(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mUserName.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(mBio.getWindowToken(), 0);

        UserRequest userRequest = createUserRequest();
        IUserPresenter.updateUser(userRequest);
    }

    private UserRequest createUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirst_name(mUserName.getText().toString());

        if(mProfileUrl != null && !mProfileUrl.toString().isEmpty()) {
            String decodeUrl = convertToBase64();
            decodeUrl = "data:image/jpeg;base64," + decodeUrl;
            Log.i("PATHTAG", " decodeUrl " + decodeUrl);

            userRequest.setRfile1(decodeUrl);
            userRequest.setRfile2(decodeUrl);
        }
        userRequest.setDescription(mBio.getText().toString());

        return userRequest;
    }

    @OnClick(R.id.user_pic)
    void onChange(){
        changeImage();
    }

    @OnClick(R.id.change_photo)
    void onPhotoChange(){
        changeImage();
    }

    private void changeImage(){
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            if(uri != null && !uri.toString().isEmpty()) {
                Glide.with(this).load(uri).bitmapTransform(new CropCircleTransformation(getContext())).
                        placeholder(R.mipmap.default_profile).into(mProfilePic);
                mProfileUrl = uri;
            }

            Log.i("TAGTAGURI", "mProfileUrl " + mProfileUrl);

            /*try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
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
    public void updateUserInfo() {
        Toast.makeText(getContext(), getString(R.string.profile_update_msg/*saved*/), Toast.LENGTH_SHORT).show();
        //updateUserInfo(user);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void showAlert(String msg) {
        IHomeView.showAlert(msg);
    }

    /*private void updateUserInfo(User user) {
        User savedUser = User.getCurrentUser();
        String tagList = savedUser.getTagList();
        user.setCurrentLogin(true);
        user.setTagList(tagList);
        if(mProfileUrl != null && !mProfileUrl.isEmpty()){
            user.setUser_image(mProfileUrl);
        }
        user.save();
    }*/

    private String convertToBase64() {

        String encodedImage = null;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mProfileUrl);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

            byte[] byteArrayImage = baos.toByteArray();

            encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Bitmap bm = BitmapFactory.decodeFile(imagePath);



        return encodedImage;

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        CursorLoader cursorLoader = new CursorLoader(
                getContext(),
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }
}
