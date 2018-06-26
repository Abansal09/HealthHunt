package in.healthhunt.view.tagView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.model.beans.SpaceDecoration;
import in.healthhunt.model.utility.HealthHuntUtility;
import in.healthhunt.presenter.tagPresenter.ITagPresenter;

/**
 * Created by abhishekkumar on 6/2/18.
 */

public class TagFragment extends Fragment{

    @BindView(R.id.tags_recycler_list)
    public RecyclerView mRecyclerView;

    @BindView(R.id.select_all_checkbox)
    public CheckBox mSelectAll;

    private ITagPresenter ITagPresenter;
    private ITagView ITagView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ITagView  =(TagActivity) getActivity();
        ITagPresenter = ITagView.getPresenter();
        ITagPresenter.fetchTags();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag, container, false);
        ButterKnife.bind(this, view);
        setAdapter();
        return view;
    }

    private void setAdapter() {
        TagAdapter tagAdapter = new TagAdapter(getContext(), ITagPresenter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceDecoration(HealthHuntUtility.
                dpToPx(8, getContext()), SpaceDecoration.GRID));
        mRecyclerView.setAdapter(tagAdapter);
    }

    public void updateAdapter() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    /*@OnClick(R.id.select_all)
    void onSelectAll(){
        isSelectAll = !isSelectAll;
        TagAdapter tagAdapter = (TagAdapter) mRecyclerView.getAdapter();
        if(isSelectAll) {
            ITagPresenter.selectAll();
            tagAdapter.setSelectAll(true);
        }
        else {
            ITagPresenter.unSelectAll();
            tagAdapter.setSelectAll(false);
        }
    }*/

    @OnClick(R.id.select_all_checkbox)
    void onClickCheckBox(){
        boolean isSelect = mSelectAll.isChecked();
        Log.i("TAGTAGISSELCT", "ISELCXT "  + isSelect);
        TagAdapter tagAdapter = (TagAdapter) mRecyclerView.getAdapter();
        if(isSelect){
            ITagPresenter.selectAll();
            tagAdapter.setSelectAll(true);
            //mSelectAll.setChecked(true);
        }
        else {
            ITagPresenter.unSelectAll();
            tagAdapter.setSelectAll(false);
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
