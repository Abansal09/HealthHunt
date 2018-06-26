package in.healthhunt.view.homeScreenView.filterView.productView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.model.filter.DataItem;
import in.healthhunt.presenter.filterPresenter.IFilterPresenter;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    private IFilterPresenter IFilterPresenter;
    private Context mContext;
    private ClickListener mClickListener;
    private ItemFilter mFilter = new ItemFilter();
    private List<DataItem> mFilterList;

    public ProductAdapter(Context context, IFilterPresenter filterPresenter, String type) {
        mContext = context;
        IFilterPresenter = filterPresenter;

        mFilterList = new ArrayList<DataItem>();
        for(int i=0; i<IFilterPresenter.getFilterProductCount(); i++){
            mFilterList.add(IFilterPresenter.getProductItem(i));
        }
    }

    @Override
    public  ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.filter_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        setContent(holder, position);
    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.filter_item_view)
        LinearLayout mProductItemView;

        @BindView(R.id.filter_item_name)
        TextView mProductName;

        public ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void notifyDataChanged() {
            notifyDataSetChanged();
        }

        @OnClick(R.id.filter_item_view)
        void onClick(View view) {
            if(mClickListener != null) {

                DataItem id = mFilterList.get(getAdapterPosition());
                String strID = id.getTerm_id();

                List<String> itemList = IFilterPresenter.getProductList();
                if(itemList != null){
                    itemList.clear();
                }
                IFilterPresenter.addProduct(strID);

                /*if(!IFilterPresenter.isProductContain(strID)){
                    IFilterPresenter.addProduct(strID);
                }
                else {
                    IFilterPresenter.removeProduct(strID);
                }*/
                mClickListener.ItemClicked();
                notifyDataChanged();
            }
        }
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            int count = IFilterPresenter.getFilterProductCount();
            final ArrayList<DataItem> nlist = new ArrayList<DataItem>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                DataItem id = IFilterPresenter.getProductItem(i);
                filterableString = id.getTerm_name();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(id);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilterList = (ArrayList<DataItem>) results.values;
            notifyDataSetChanged();
        }
    }

    private void setContent(ProductViewHolder holder, int pos) {
        DataItem dataItem = mFilterList.get(pos);
        if(dataItem != null) {
            holder.mProductName.setText(dataItem.getTerm_name());

            if(IFilterPresenter.isProductContain(dataItem.getTerm_id())){
                holder.mProductItemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.hh_background_item));
            }
            else {
                holder.mProductItemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.hh_background_light));
            }
        }
    }
}