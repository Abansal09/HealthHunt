package in.healthhunt.view.profileView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ScrollView;
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



    private final String P_INTEREST_URL = "https://in.pinterest.com/healthhunt0213/";
    private final String TWEETER_URL = "https://twitter.com/healthhuntIN";
    private final String FACEBOOK_URL = "https://www.facebook.com/HHunt.in";
    private final String INSTA_GRAM_URL = "https://www.instagram.com/healthhuntin/?hl=en";
    private final String YOU_TUBE_URL = "https://www.youtube.com/channel/UCM_h4FbZmWjk5qnhq_3me9w";
    private final String WHATS_APP_URL = "https://api.whatsapp.com/send?text=https://www.healthhunt.in";
    private final String LINKED_IN_URL = "https://www.linkedin.com/company/healthhunt/";

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
    CheckBox mSelectAllCheck;

    @BindView(R.id.edit_profile_tag_view_item)
    LinearLayout mTagView;

    @BindView(R.id.tags)
    TextView mTagsButton;

    @BindView(R.id.options)
    TextView mOptionsButton;

    @BindView(R.id.options_view)
    ScrollView mOptionView;

    @BindView(R.id.change_photo)
    TextView mChangePhoto;

    @BindView(R.id.cross)
    ImageView mCross;

    /* @BindView(R.id.child_frame_layout)
     FrameLayout mChildFrame;
 */
    private IUserPresenter IUserPresenter;
    private ITagPresenter ITagPresenter;
    private IHomeView IHomeView;
    //private FragmentManager mChildFragmentManager;
    private boolean IS_TAG_TAB;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ITagPresenter = new TagPresenterImp(getContext(), this);
        IUserPresenter = new UserPresenterImp(getContext(), this);
        IHomeView = (HomeActivity)getActivity();
        IS_TAG_TAB = true;
        //mChildFragmentManager = getChildFragmentManager();
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
        IHomeView.showActionBar();
        mCross.setVisibility(View.GONE);
        setUserInfo();
        updateTabVisibility();
        // mSelectAll.setVisibility(View.GONE);
        setAdapter();
        setSearchAdapter();
        return view;
    }

    private void setUserInfo() {

        User user = User.getCurrentUser();
        String name = user.getFirst_name();//getName();//HealthHuntPreference.getString(getContext(), user.getUsername());

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
                    .load(url)
                    .bitmapTransform(new CropCircleTransformation(getContext())).placeholder(R.mipmap.default_profile)
                    .into(mProfilePic);
        }
        else {
            mProfilePic.setImageResource(R.mipmap.default_profile);
        }

        //String designation = user.get
    }

    private void setAdapter() {
        TagAdapter tagAdapter = new TagAdapter(getContext(), ITagPresenter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mTagViewer.setLayoutManager(gridLayoutManager);
        mTagViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.
                dpToPx(24, getContext()), SpaceDecoration.VERTICAL));

        /*mTagViewer.addItemDecoration(new SpaceDecoration(HealthHuntUtility.
                dpToPx(8, getContext()), SpaceDecoration.GRID));*/
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

    /*private void showChildFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();//mChildFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.child_frame_layout, fragment);
        fragmentTransaction.addToBackStack(tag);
        //getChildFragmentManager().executePendingTransactions();
        fragmentTransaction.commit();
    }

    public void loadChildFragment(String fragmentName, Bundle bundle) {
        mChildFrame.setVisibility(View.VISIBLE);
        Fragment fragment = null;

        if(fragmentName.equalsIgnoreCase(WhatWeDoFragment.class.getSimpleName())){
            fragment = new WhatWeDoFragment();
        }
        else if(fragmentName.equalsIgnoreCase(CollaborateWithUSFragment.class.getSimpleName())){
            fragment = new CollaborateWithUSFragment();
        }
        else if(fragmentName.equalsIgnoreCase(LegalOtherFragment.class.getSimpleName())){
            fragment = new LegalOtherFragment();
        }

        fragment.setArguments(bundle);
        showChildFragment(fragment, fragmentName);
    }*/

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

    /*@OnClick(R.id.edit_profile_text)
    void onEditProfile(){
        IHomeView.loadNonFooterFragment(EditProfileFragment.class.getSimpleName(), null);
    }*/

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
        IS_TAG_TAB = true;
        updateTabVisibility();
    }

    @OnClick(R.id.options)
    void onOptions(){
        IS_TAG_TAB = false;
        updateTabVisibility();
    }

    private void updateTabVisibility(){
        if(IS_TAG_TAB){
            mOptionView.setVisibility(View.GONE);
            mTagView.setVisibility(View.VISIBLE);
            mOptionsButton.setBackgroundColor(Color.WHITE);
            mTagsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hh_background_light2));

        }
        else {
            mTagsButton.setBackgroundColor(Color.WHITE);
            mOptionsButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hh_background_light2));
            mTagView.setVisibility(View.GONE);
            mOptionView.setVisibility(View.VISIBLE);
        }
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

    @OnClick(R.id.edit_profile_icon)
    void onEditClick(){
        IHomeView.loadNonFooterFragment(EditProfileFragment.class.getSimpleName(), null);
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

    @OnClick(R.id.what_we_de_arrow_view)
    void onWhatWeDo(){
        //loadChildFragment(WhatWeDoFragment.class.getSimpleName(), null);
        IHomeView.loadNonFooterFragment(WhatWeDoFragment.class.getSimpleName(), null);
    }

    @OnClick(R.id.collaborate_withus_arrow_view)
    void onCollaborateWithUs(){
        //loadChildFragment(CollaborateWithUSFragment.class.getSimpleName(), null);
        IHomeView.loadNonFooterFragment(CollaborateWithUSFragment.class.getSimpleName(), null);
    }

    @OnClick(R.id.legal_other_arrow_view)
    void onLegalOthers(){
        //loadChildFragment(LegalOtherFragment.class.getSimpleName(), null);
        IHomeView.loadNonFooterFragment(LegalOtherFragment.class.getSimpleName(), null);
    }

    @OnClick(R.id.about_us_arrow_view)
    void onAboutUs(){
        //loadChildFragment(LegalOtherFragment.class.getSimpleName(), null);
        IHomeView.loadNonFooterFragment(AboutUsFragment.class.getSimpleName(), null);
    }

    @OnClick(R.id.health_council_arrow_view)
    void onHealthCouncil(){
        //loadChildFragment(LegalOtherFragment.class.getSimpleName(), null);
        IHomeView.loadNonFooterFragment(HealthCouncilFragment.class.getSimpleName(), null);
    }

    /*public boolean onBackPressed(){

        int count = getFragmentManager().getBackStackEntryCount();
        Log.i("TAGCOUNTProfile", "Profile Count " + count);

        if(count > 0){
            if(!isNeedPopBack()) {
                getFragmentManager().popBackStackImmediate();
                mChildFrame.setVisibility(View.GONE);
                IHomeView.updateTitle(getString(R.string.profile));
            }
            return true;
        }
        return false;
    }*/

    /*private boolean isNeedPopBack(){

        boolean isBack = false;
        Fragment fragment = mChildFragmentManager.findFragmentById(R.id.child_frame_layout);


        if(fragment instanceof WhatWeDoFragment){
            WhatWeDoFragment whatWeDoFragment = (WhatWeDoFragment)fragment;
            isBack = whatWeDoFragment.onBackPressed();
        }
        else if(fragment instanceof CollaborateWithUSFragment){
            CollaborateWithUSFragment collaborateWithUSFragment = (CollaborateWithUSFragment)fragment;
            isBack = collaborateWithUSFragment.onBackPressed();
        }
        else if(fragment instanceof LegalOtherFragment){
            LegalOtherFragment legalOtherFragment = (LegalOtherFragment)fragment;
            isBack = legalOtherFragment.onBackPressed();
        }

        return isBack;
    }*/

    @OnClick(R.id.pinterest_view)
    void onPinterest(){
        openInBrowser(P_INTEREST_URL);
    }

    @OnClick(R.id.tweeter_view)
    void onTweeter(){
        openInBrowser(TWEETER_URL);
    }

    @OnClick(R.id.facebook_view)
    void onFacebook(){
        openInBrowser(FACEBOOK_URL);
    }

    @OnClick(R.id.insta_view)
    void onInstagram(){
        openInBrowser(INSTA_GRAM_URL);
    }

    @OnClick(R.id.youtube_view)
    void onYoutube(){
        openInBrowser(YOU_TUBE_URL);
    }

    @OnClick(R.id.whatsapp_view)
    void onWhatsApp(){
        openInBrowser(WHATS_APP_URL);
    }

    @OnClick(R.id.linkedin_view)
    void onLinkedIn(){
        openInBrowser(LINKED_IN_URL);
    }

    private void openInBrowser(String str){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
        startActivity(Intent.createChooser(intent, getString(R.string.browser)));
    }
}
