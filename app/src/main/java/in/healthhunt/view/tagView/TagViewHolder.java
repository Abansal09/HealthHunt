package in.healthhunt.view.tagView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.model.tags.TagItem;
import in.healthhunt.presenter.tagPresenter.ITagPresenter;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class TagViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tag_image)
    public ImageView mTagImage;

    /*  @BindView(R.id.plus)
      public ImageView mPlus;
  */
  /*  @BindView(R.id.check)
    public ImageView mCheck;
*/
    @BindView(R.id.tag)
    public TextView mTag;

    @BindView(R.id.tag_details)
    LinearLayout mTagBGView;

    /*  @BindView(R.id.tag_view)
      RelativeLayout mTagView;
  */
    private int mViewHolderPos;

    private Context mContext;
    private ITagPresenter ITagPresenter;
    private TagAdapter.OnClickListener mOnClickListener;
    public TagViewHolder(View itemView, ITagPresenter tagPresenter, TagAdapter.OnClickListener onClickListener) {
        super(itemView);
        ITagPresenter = tagPresenter;
        mContext = itemView.getContext();
        mOnClickListener = onClickListener;
        ButterKnife.bind(this, itemView);
    }

    @OnClick(R.id.tag_view)
    public void onTagClick() {
        List<TagItem> itemList = ITagPresenter.getTagList();
        TagItem tagItem = itemList.get(getAdapterPosition());
        tagItem.setPressed(!tagItem.isPressed());

        updateTag(tagItem);
        updateSelection(tagItem);
        mOnClickListener.onItemClick(getAdapterPosition());
    }


    public void updateTag(TagItem tagItem){
        boolean isPressed = tagItem.isPressed();

        if(isPressed){
            mTagBGView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tag_item_bg_green));
            mTagImage.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

            //mTag.setTextColor(Color.WHITE);
            // mPlus.setVisibility(View.GONE);
            //mCheck.setVisibility(View.VISIBLE);

            //Drawable mDrawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_check);
            //mDrawable.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));
            //Check.setBackground(mDrawable);
        }
        else {
            mTagBGView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.tag_item_bg_white));
            mTagImage.setColorFilter(ContextCompat.getColor(mContext, R.color.hh_green_light2), PorterDuff.Mode.SRC_ATOP);

            // mTag.setTextColor(ContextCompat.getColor(mContext, R.color.hh_green_light2));
            //mCheck.setVisibility(View.GONE);
            //mPlus.setVisibility(View.VISIBLE);

            //Drawable mDrawable = ContextCompat.getDrawable(mContext, R.mipmap.ic_plus);
            //mDrawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(mContext, R.color.hh_grey_dark1), PorterDuff.Mode.SRC_ATOP));
            //mPlus.setBackground(mDrawable);
        }
        //mTagView.setPressed(!isPressed);
    }

    private void updateSelection(TagItem tagItem) {


        boolean isPressed = tagItem.isPressed();
        if(isPressed){
            ITagPresenter.addTag(tagItem);
        }
        else {
            ITagPresenter.removeTag(tagItem);
        }
    }

    public void setViewPos(int pos){
        mViewHolderPos = pos;
    }
}