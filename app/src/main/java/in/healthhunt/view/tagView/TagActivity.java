package in.healthhunt.view.tagView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.model.beans.Constants;
import in.healthhunt.model.tags.TagItem;
import in.healthhunt.presenter.tagPresenter.ITagPresenter;
import in.healthhunt.presenter.tagPresenter.TagPresenterImp;
import in.healthhunt.view.BaseActivity;
import in.healthhunt.view.homeScreenView.HomeActivity;
import io.fabric.sdk.android.Fabric;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class TagActivity extends BaseActivity implements ITagView, TagAdapter.OnClickListener{

    @BindView(R.id.spinner)
    public Spinner mSpinner;

    /* @BindView(R.id.search_view)
     public AutoCompleteTextView mSearchView;
 */
    /*@BindView(R.id.cross)
    public ImageView mCross;
*/
    @BindView(R.id.done)
    public TextView mDone;

    @BindView(R.id.select_all_checkbox)
    public CheckBox mSelectAll;

    @BindView(R.id.image_view_flipper)
    ViewFlipper mViewFlipper;

    @BindView(R.id.search_view)
    public AutoCompleteTextView mSearchView;

    @BindView(R.id.cross)
    public ImageView mCross;


    private String[] spinnerItems = {"English", "Hindi"};

    private ITagPresenter ITagPresenter;

    private TagFragment mTagFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        Fabric.with(this, new Crashlytics());
        ButterKnife.bind(this);

        addSpinnerAdapter();
        ITagPresenter = new TagPresenterImp(getApplicationContext(), this);
        ITagPresenter.loadFragment(TagFragment.class.getSimpleName(), null);
        //addTagAdapter();
        //mTagPresenterImp.fetchTags();

        addSliderShow();
        setSearchAdapter();
    }

    private void addSliderShow() {

        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));

        if(!mViewFlipper.isFlipping()){
            mViewFlipper.setAutoStart(true);
            mViewFlipper.setFlipInterval(Constants.FLIP_DURATION);
            mViewFlipper.startFlipping();
        }

        mViewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }


    private void addSpinnerAdapter() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_text_item, spinnerItems);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);
    }


    @OnClick(R.id.done)
    void onDoneClick(){
        List<TagItem> itemList = ITagPresenter.getSelectedTagList();
        if(itemList.size() < 5) {
            Toast.makeText(getApplicationContext(), getString(R.string.select_at_least_5_tags),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ITagPresenter.storeSelectedTags();
        /*List<TagItem> list = ITagPresenter.getSelectedTagList();
        String tags = "";
        if(list != null && !list.isEmpty()){
            for(int i=0; i<list.size(); i++){
                TagItem tag = list.get(i);
                if(i<list.size() - 1) {
                    tags = tags + tag.getId() + Constants.SEPARATOR;
                }
            }
        }
        Log.i("TAGUSER", "TAGS " + tags);

        User currentUser = User.getCurrentUser();
        if(currentUser != null){
            currentUser.setTagList(tags);
            currentUser.save();
        }*/

        //Intent intent = new Intent(getApplicationContext(), OnBoardingActivity.class);
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onShowProgress() {
        mProgress.show();
    }

    @Override
    public void onHideProgress() {
        mProgress.dismiss();
    }

    @Override
    public void updateAdapter() {
        mTagFragment.updateAdapter();
    }

    private void setSearchAdapter() {

       /* List<TagItem> tagItemList = ITagPresenter.getTagList();
        List<String> searchList = new ArrayList<String>();

        for(TagItem item: tagItemList){
            searchList.add(item.getName());
        }*/

        //  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.popup_window_item, searchList);

       // DisplayMetrics displayMetrics = new DisplayMetrics();
       // getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int width = displayMetrics.widthPixels;
        //width = width - HealthHuntUtility.dpToPx(18, getApplicationContext());
        //mSearchView.setDropDownWidth(width);
        //mSearchView.setDropDownHorizontalOffset(HealthHuntUtility.dpToPx(1, getApplicationContext()));
        //mSearchView.setDropDownVerticalOffset(HealthHuntUtility.dpToPx(1, getApplicationContext()));

        final TagSearchAdapter tagSearchAdapter = new TagSearchAdapter(getApplicationContext(), R.layout.popup_window_item, ITagPresenter);
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

                TagItem fullTagItem = (TagItem) tagSearchAdapter.getTagItem(pos);

                boolean isPressed = fullTagItem.isPressed();
                if(!isPressed){
                    fullTagItem.setPressed(true);
                    ITagPresenter.addTag(fullTagItem);
                }
                updateAdapter();
                updateSelectAll();

            }
        });

    }

    private void updateSelectAll(){
        int totalCount = ITagPresenter.getTagCount();
        int prevCount = 0;
        List<TagItem> list = ITagPresenter.getSelectedTagList();
        if(list != null) {
            prevCount = list.size();
        }

        if(totalCount == prevCount){
            /*mTagFragment.*/updateCheckBox(true);
        }
        else {
            /*mTagFragment.*/updateCheckBox(false);
        }
    }

    /*@Override
    public void setTags(List<TagItem> list) {

        Log.i("TAG123", "List = " + list);
        if(list != null && !list.isEmpty()){
            mTags.addAll(list);
        }

        if(mTagAdapter == null){
            addTagAdapter();
        }
        mTagAdapter.updateTagList(list);
        mTagAdapter.notifyDataSetChanged();
    }*/

    @Override
    public void showAlert(String msg) {
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alertdialog_view);
        //dialog.

        TextView message = dialog.findViewById(R.id.alert_message);
        message.setText(msg);

        String str = getResources().getString(R.string.alert);
        TextView title = dialog.findViewById(R.id.alert_title);
        title.setText(str);

        Button okButton = dialog.findViewById(R.id.action_button);
        okButton.setText(android.R.string.ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    @Override
    public TagViewHolder createTagViewHolder(View view) {
        return new TagViewHolder(view, ITagPresenter, this);
    }

    @Override
    public void loadFragment(String fragmentName, Bundle bundle) {
        mTagFragment = new TagFragment();
        showFragment(mTagFragment);
    }

    @Override
    public ITagPresenter getPresenter() {
        return ITagPresenter;
    }

    @Override
    public void updateSearchAdapter() {
        TagSearchAdapter tagSearchAdapter = (TagSearchAdapter) mSearchView.getAdapter();
        tagSearchAdapter.notifyDataSetChanged();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tag_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onItemClick(int position) {
        List<TagItem> itemList = ITagPresenter.getSelectedTagList();
        if(itemList != null && !itemList.isEmpty()){
            mDone.setTextColor(ContextCompat.getColor(this, R.color.hh_edittext_text_color));
        }
        else {
            mDone.setTextColor(ContextCompat.getColor(this, R.color.hh_grey_dark));
        }
        updateSelectAll();
    }

    @OnClick(R.id.cross)
    void onCross(){
        mSearchView.setText("");
    }

    @OnClick(R.id.select_all_checkbox)
    void onClickCheckBox(){
        boolean isSelect = mSelectAll.isChecked();
        Log.i("TAGTAGISSELCT", "ISELCXT "  + isSelect);
        //TagAdapter tagAdapter = (TagAdapter) mTagFragment.getAdapter();
        if(isSelect){
            ITagPresenter.selectAll();
            mTagFragment.setSelectAll(true);
            //tagAdapter.setSelectAll(true);
            //mSelectAll.setChecked(true);
        }
        else {
            ITagPresenter.unSelectAll();
            mTagFragment.setSelectAll(false);
            //tagAdapter.setSelectAll(false);
            //mSelectAll.setChecked(false);
        }
    }

    void updateCheckBox(boolean isCheck){
        if(isCheck){
            mSelectAll.setChecked(true);
        }
        else {
            mSelectAll.setChecked(false);
        }
    }
}
