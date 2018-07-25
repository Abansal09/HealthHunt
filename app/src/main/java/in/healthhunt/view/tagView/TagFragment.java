package in.healthhunt.view.tagView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceDecoration(HealthHuntUtility.
                dpToPx(24, getContext()), SpaceDecoration.VERTICAL));
        /*mRecyclerView.addItemDecoration(new SpaceDecoration(HealthHuntUtility.
                dpToPx(24, getContext()), SpaceDecoration.GRID));*/
        mRecyclerView.setAdapter(tagAdapter);
    }

    public void updateAdapter() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void setSelectAll(boolean isSelectAll){
        if(mRecyclerView.getAdapter() != null) {
            TagAdapter tagAdapter = (TagAdapter) mRecyclerView.getAdapter();
            tagAdapter.setSelectAll(isSelectAll);
        }
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
}
