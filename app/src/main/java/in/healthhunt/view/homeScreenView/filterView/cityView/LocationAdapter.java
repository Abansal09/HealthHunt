package in.healthhunt.view.homeScreenView.filterView.cityView;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.healthhunt.R;
import in.healthhunt.presenter.filterPresenter.IFilterPresenter;

/**
 * Created by abhishekkumar on 4/23/18.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.CityViewHolder> implements Filterable {

    private IFilterPresenter IFilterPresenter;
    private Context mContext;
    private ClickListener mClickListener;
    PlaceAPI mPlaceAPI = new PlaceAPI();
    private List<String> mFilterList;

    public LocationAdapter(Context context, IFilterPresenter filterPresenter) {
        mContext = context;
        IFilterPresenter = filterPresenter;

    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.filter_item, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        setContent(holder, position);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(mFilterList != null){
            size = mFilterList.size();
        }
        return size;
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    public interface ClickListener {
        void ItemClicked();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.filter_item_view)
        LinearLayout mCityItemView;

        @BindView(R.id.filter_item_name)
        TextView mCityName;

        public CityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void notifyDataChanged() {
            notifyDataSetChanged();
        }

        @OnClick(R.id.filter_item_view)
        void onClick(View view) {
            if(mClickListener != null) {

                if(mFilterList.isEmpty()){
                    Log.i("TAGFLITE", "Filter city is empty");
                    return;
                }

                String name = mFilterList.get(getAdapterPosition());

                if(IFilterPresenter.getCity() != null &&
                        IFilterPresenter.getCity().equalsIgnoreCase(name)){
                    IFilterPresenter.setCity(null);
                }
                else {
                    IFilterPresenter.setCity(name);
                }
                mClickListener.ItemClicked();
                notifyDataChanged();
            }
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                Log.i("TAGFilterResults", "Content " + constraint);
                if (constraint != null) {
                    mFilterList = mPlaceAPI.autocomplete(constraint.toString());
                    Log.i("TAGFilterResults", "result " + mFilterList);

                    filterResults.values = mFilterList;
                    filterResults.count = mFilterList.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                //notifyDataSetChanged();
            }
        };

        return filter;
    }

    private void setContent(CityViewHolder holder, int pos) {
        if(mFilterList.isEmpty()){
            return;
        }

        String name = mFilterList.get(pos);
        if(name != null) {
            holder.mCityName.setText(name);

            if(IFilterPresenter.getCity() != null &&
                    IFilterPresenter.getCity().equalsIgnoreCase(name)){
                holder.mCityItemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.hh_background_item));
            }
            else {
                holder.mCityItemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.hh_background_light));
            }
        }
    }
}