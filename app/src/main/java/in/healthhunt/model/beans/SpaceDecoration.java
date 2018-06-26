package in.healthhunt.model.beans;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpaceDecoration extends RecyclerView.ItemDecoration {
  private final int spacing;
  private int displayMode;

  public static final int HORIZONTAL = 0;
  public static final int VERTICAL = 1;
  public static final int GRID = 2;

  public SpaceDecoration(int spacing) {
    this(spacing, -1);
  }

  public SpaceDecoration(int spacing, int displayMode) {
    this.spacing = spacing;
    this.displayMode = displayMode;
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                             RecyclerView.State state) {

    switch (displayMode) {

      case VERTICAL:
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)
          outRect.bottom = spacing;
          break;

      case HORIZONTAL:
        //if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)
          outRect.right = spacing;
        break;

          case GRID:
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            int itemCount = state.getItemCount();
            int position = parent.getChildViewHolder(view).getAdapterPosition();

            if (layoutManager instanceof GridLayoutManager) {
              GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
              int cols = gridLayoutManager.getSpanCount();
              int rows = itemCount / cols;

              outRect.left = spacing;
              outRect.right = position % cols == cols - 1 ? spacing : 0;

              if (position > 1) {
                outRect.top = spacing;
              } else {
                outRect.top = 0;
              }

             /* if (position < itemCount - 2) {
                outRect.bottom = position / cols == rows - 1 ? spacing : 0;
              } else*/ {
                outRect.bottom = 0;
              }
              break;
            }
    }
  }

  /*@Override
  public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
    int left = parent.getPaddingLeft();
    int right = parent.getWidth() - parent.getPaddingRight();

    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = parent.getChildAt(i);

      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

      int top = child.getBottom() + params.bottomMargin;
      int bottom = top + mDivider.getIntrinsicHeight();

      mDivider.setBounds(left, top, right, bottom);
      mDivider.draw(c);
    }
  }*/

  /*@Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    int position = parent.getChildViewHolder(view).getAdapterPosition();
    int itemCount = state.getItemCount();
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    setSpacingForDirection(outRect, layoutManager, position, itemCount);
  }

  private void setSpacingForDirection(Rect outRect,
                                      RecyclerView.LayoutManager layoutManager,
                                      int position,
                                      int itemCount) {
    
    // Resolve display mode automatically
    if (displayMode == -1) {
      displayMode = resolveDisplayMode(layoutManager);
    }

    switch (displayMode) {
      case HORIZONTAL:
        outRect.left = spacing;
        outRect.right = position == itemCount - 1 ? spacing : 0;
        outRect.top = spacing;
        outRect.bottom = spacing;
        break;
      case VERTICAL:
       // outRect.left = spacing;
        //outRect.right = spacing;
        //outRect.top = spacing;
        //outRect.bottom = position == itemCount - 1 ? spacing : 0;
        break;
      case GRID:
        if (layoutManager instanceof GridLayoutManager) {
          GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
          int cols = gridLayoutManager.getSpanCount();
          int rows = itemCount / cols;

          outRect.left = spacing;
          outRect.right = position % cols == cols - 1 ? spacing : 0;
          outRect.top = spacing;
          outRect.bottom = position / cols == rows - 1 ? spacing : 0;
        }
        break;
    }
  }

  private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
    if (layoutManager instanceof GridLayoutManager) return GRID;
    if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
    return VERTICAL;
  }

  @Override
  public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
    int dividerLeft = parent.getPaddingLeft();
    int dividerRight = parent.getWidth() - parent.getPaddingRight();

    int childCount = parent.getChildCount();
    for (int i = 0; i < childCount - 1; i++) {
      View child = parent.getChildAt(i);

      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

      int dividerTop = child.getBottom() + params.bottomMargin;
      int dividerBottom = dividerTop + spacing;

      mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
      mDivider.draw(canvas);
    }
  }*/
}