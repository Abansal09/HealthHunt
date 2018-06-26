package in.healthhunt.view.profileView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.model.beans.SpaceDecoration;
import in.healthhunt.model.login.User;
import in.healthhunt.model.tags.TagItem;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.tagPresenter.ITagPresenter;
import in.healthhunt.presenter.tagPresenter.TagPresenterImp;
import in.healthhunt.presenter.userPresenter.IUserPresenter;
import in.healthhunt.presenter.userPresenter.UserPresenterImp;
import in.healthhunt.view.homeScreenView.HomeActivity;
import in.healthhunt.view.homeScreenView.IHomeView;
import in.healthhunt.view.loginView.LoginActivity;
import in.healthhunt.view.tagView.ITagView;
import in.healthhunt.view.tagView.TagAdapter;
import in.healthhunt.view.tagView.TagSearchAdapter;
import in.healthhunt.view.tagView.TagViewHolder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by abhishekkumar on 6/3/18.
 */

public class ProfileFragment extends Fragment implements ITagView, IEditProfileView, TagAdapter.OnClickListener{

    @BindView(R.id.tags_recycler_list)
    RecyclerView mTagViewer;

    @BindView(R.id.user_pic)
    ImageView mProfilePic;

    @BindView(R.id.user_name)
    TextView mUserName;

    @BindView(R.id.designation)
    TextView mDesignation;

    @BindView(R.id.tag_count)
    TextView mTagCount;

    @BindView(R.id.search_view)
    AutoCompleteTextView mSearchView;

    @BindView(R.id.select_all_checkbox)
    public CheckBox mSelectAllCheck;

    @BindView(R.id.edit_profile_tag_view_item)
    LinearLayout mTagView;

    @BindView(R.id.tags)
    TextView mTagsButton;

    @BindView(R.id.options)
    TextView mOptionsButton;

    @BindView(R.id.change_photo)
    TextView mChangePhoto;

    @BindView(R.id.cross)
    ImageView mCross;


    private IUserPresenter IUserPresenter;
    private ITagPresenter ITagPresenter;
    private IHomeView IHomeView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ITagPresenter = new TagPresenterImp(getContext(), this);
        IUserPresenter = new UserPresenterImp(getContext(), this);
        IHomeView = (HomeActivity)getActivity();
        IUserPresenter.fetchCurrentUser();
        ITagPresenter.fetchTags();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        mChangePhoto.setVisibility(View.GONE);
        IHomeView.updateTitle(getString(R.string.profile));
        IHomeView.hideBottomNavigationSelection();
        IHomeView.hideDrawerMenu();
        mCross.setVisibility(View.GONE);
        setUserInfo();
        // mSelectAll.setVisibility(View.GONE);
        setAdapter();
        setSearchAdapter();
        return view;
    }


    private void setUserInfo() {

        User user = User.getCurrentUser();
        String name = user.getFirst_name();//getName();//HealthHuntPreference.getString(getContext(), user.getUsername());
        if(name != null) {
            mUserName.setText(name);
        }

        String url = user.getUser_image();//HealthHuntPreference.getString(getContext(), user.getUserId());

        if(url != null) {
            url = url.replace("\n", "");
            Glide.with(getContext())
                    .load(url)
                    .bitmapTransform(new CropCircleTransformation(getContext())).placeholder(R.mipmap.avatar)
                    .into(mProfilePic);
        }
        else {
            mProfilePic.setImageResource(R.mipmap.avatar);
        }

        //String designation = user.get
    }

    private void setAdapter() {
        TagAdapter tagAdapter = new TagAdapter(getContext(), ITagPresenter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mTagViewer.setLayoutManager(gridLayoutManager);
        mTagViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.
                dpToPx(8, getContext()), SpaceDecoration.GRID));
        mTagViewer.setAdapter(tagAdapter);
    }


    @Override
    public void onShowProgress() {
        IHomeView.showProgress();
    }

    @Override
    public void onHideProgress() {
        IHomeView.hideProgress();
    }

    @Override
    public void updateAdapter() {
        //Set<String> stringSet = HealthHuntPreference.getSet(getContext(), Constants.SELECTED_TAGS_KEY);
        /*User user = User.getCurrentUser();
        String tags = user.getTagList();

        Log.i("TAGSTRINGSET", "tags " + tags);
        List<TagItem> tagItems = ITagPresenter.getTagList();
        if(tagItems != null) {
            for(TagItem tagItem: tagItems){
                if(tags.contains(String.valueOf(tagItem.getId()))){
                    Log.i("TAGSTRINGSET", "tagId " + tagItem.getId());
                    tagItem.setPressed(true);
                    ITagPresenter.addTag(tagItem);
                }
            }
        }*/
        updateTagCount();
        updateCheckBox();
        mTagViewer.getAdapter().notifyDataSetChanged();
    }

    private void updateCheckBox() {
        int selectedCount = ITagPresenter.getSelectedTagList().size();
        int totalCount = ITagPresenter.getTagCount();

        if(selectedCount == totalCount){
            mSelectAllCheck.setChecked(true);
        }
        else {
            mSelectAllCheck.setChecked(false);
        }
    }

    @Override
    public void showAlert(String msg) {
        IHomeView.showAlert(msg);
    }

    @Override
    public TagViewHolder createTagViewHolder(View view) {
        return new TagViewHolder(view, ITagPresenter, this);
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {

    }

    @Override
    public ITagPresenter getPresenter() {
        return ITagPresenter;
    }

    @Override
    public void updateSearchAdapter() {

    }

    @Override
    public void onItemClick(int position) {
        updateTagCount();
        updateCheckBox();
    }

    @OnClick(R.id.save)
    void onSaved(){
        List<TagItem> itemList = ITagPresenter.getSelectedTagList();
        if(itemList.size() < 5) {
            Toast.makeText(getContext(), getString(R.string.select_at_least_5_tags),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getContext(), getString(R.string.tags_saved_successfully), Toast.LENGTH_SHORT).show();
        ITagPresenter.storeSelectedTags();
        IHomeView.updateMyFeed();
    }

    @OnClick(R.id.edit_profile_text)
    void onEditProfile(){
        IHomeView.loadNonFooterFragment(EditProfileFragment.class.getSimpleName(), null);
    }

    @OnClick(R.id.logout)
    void onLogout(){
        //HealthHuntPreference.putBoolean(getContext(), Constants.IS_LOGIN_FIRST_KEY, false);
        //new Delete().from(User.class).execute();
        User user = User.getCurrentUser();
        user.setCurrentLogin(false);
        user.save();
        startLoginActivity();
    }

    private void startLoginActivity(){
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void updateTagCount(){
        List<TagItem> tagList = ITagPresenter.getSelectedTagList();
        int count = 0;
        if(tagList != null){
            count = tagList.size();
        }
        mTagCount.setText(String.valueOf(count));
    }

    @OnClick(R.id.tags)
    void onTags(){
        mTagView.setVisibility(View.VISIBLE);
        mTagsButton.setBackgroundColor(Color.WHITE);
        mOptionsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hh_background_light));
    }

    @OnClick(R.id.options)
    void onOptions(){
        mOptionsButton.setBackgroundColor(Color.WHITE);
        mTagsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hh_background_light));
        mTagView.setVisibility(View.GONE);
    }

    /*private void setSearchAdapter() {

        List<TagItem> tagItemList = ITagPresenter.getTagList();
        List<String> searchList = new ArrayList<String>();

        for(TagItem item: tagItemList){
            searchList.add(item.getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.popup_window_item, searchList);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        width = width - HealthHuntUtility.dpToPx(18, getContext());
        mSearchView.setDropDownWidth(width);
        mSearchView.setDropDownHorizontalOffset(HealthHuntUtility.dpToPx(1, getContext()));
        mSearchView.setDropDownVerticalOffset(HealthHuntUtility.dpToPx(1, getContext()));
        mSearchView.setAdapter(arrayAdapter);

        mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                String selectedText = mSearchView.getText().toString();
                for(TagItem fullTagItem: ITagPresenter.getTagList()){
                    if(fullTagItem.getName().equalsIgnoreCase(selectedText)){
                        fullTagItem.setPressed(!fullTagItem.isPressed());

                        boolean isPressed = fullTagItem.isPressed();
                        if(isPressed){
                            ITagPresenter.addTag(fullTagItem);
                        }
                        else {
                            ITagPresenter.removeTag(fullTagItem);
                        }
                        break;
                    }
                }
                updateAdapter();
            }
        });

        *//*mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    mCross.setVisibility(View.GONE);
                }
                else {
                    mCross.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*//*

    }*/

    private void setSearchAdapter() {

       /* List<TagItem> tagItemList = ITagPresenter.getTagList();
        List<String> searchList = new ArrayList<String>();

        for(TagItem item: tagItemList){
            searchList.add(item.getName());
        }*/

        //  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.popup_window_item, searchList);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        width = width - HealthHuntUtility.dpToPx(18, getContext());
        mSearchView.setDropDownWidth(width);
        mSearchView.setDropDownHorizontalOffset(HealthHuntUtility.dpToPx(1, getContext()));
        mSearchView.setDropDownVerticalOffset(HealthHuntUtility.dpToPx(1, getContext()));

        final TagSearchAdapter tagSearchAdapter = new TagSearchAdapter(getContext(), R.layout.popup_window_item, ITagPresenter);
        mSearchView.setAdapter(tagSearchAdapter);

        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                tagSearchAdapter.getFilter().filter(str);
                if(str.isEmpty()){
                    mCross.setVisibility(View.GONE);
                }
                else {
                    mCross.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                //String selectedText = mSearchView.getText().toString();
                TagItem fullTagItem = (TagItem) tagSearchAdapter.getTagItem(pos);

                boolean isPressed = fullTagItem.isPressed();
                if(!isPressed){
                    fullTagItem.setPressed(true);
                    ITagPresenter.addTag(fullTagItem);
                }
                /*else {
                    ITagPresenter.removeTag(fullTagItem);
                }*/

                /*for(TagItem fullTagItem: ITagPresenter.getTagList()){
                    if(fullTagItem.getName().equalsIgnoreCase(selectedText)){
                        fullTagItem.setPressed(!fullTagItem.isPressed());

                        boolean isPressed = fullTagItem.isPressed();
                        if(isPressed){
                            ITagPresenter.addTag(fullTagItem);
                        }
                        else {
                            ITagPresenter.removeTag(fullTagItem);
                        }
                        break;
                    }
                }*/
                updateAdapter();
                updateCheckBox();
            }
        });

    }


    @OnClick(R.id.select_all_checkbox)
    void onClickCheckBox(){
        boolean isSelect = mSelectAllCheck.isChecked();
        TagAdapter tagAdapter = (TagAdapter) mTagViewer.getAdapter();
        if(isSelect){
            ITagPresenter.selectAll();
            tagAdapter.setSelectAll(true);
            //mSelectAllCheck.setChecked(true);
        }
        else {
            ITagPresenter.unSelectAll();
            tagAdapter.setSelectAll(false);
            //mSelectAllCheck.setChecked(false);
        }
        updateTagCount();

    }

    @OnClick(R.id.cross)
    void onCross(){
        mSearchView.setText("");
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void updateUserInfo() {
        setUserInfo();
    }
}
