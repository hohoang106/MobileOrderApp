package com.sh.orderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.orderapp.R;
import com.sh.orderapp.model.UserModel;
import com.sh.orderapp.utils.StringFormatUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.TripViewHolder> implements Filterable {
    private final Context mContext;
    private final List<UserModel> listRestaurants;
    private List<UserModel> lstFiltered;
    private final OnRestaurantItemClickListener listener;

    public RestaurantAdapter(Context mContext, List<UserModel> listRestaurants, OnRestaurantItemClickListener listener) {
        this.mContext = mContext;
        this.listRestaurants = listRestaurants;
        this.lstFiltered = listRestaurants;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RestaurantAdapter.TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantAdapter.TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.TripViewHolder holder, int position) {
        UserModel model = lstFiltered.get(position);
        holder.tvName.setText(model.getFullName());
        holder.tvRate.setText("Phone Number: " + model.getPhone());
        holder.tvDescription.setText("Address:" + model.getAddress());
        Picasso.with(mContext).load(model.getLinkImage())
                .placeholder(R.drawable.restaurant).error(R.drawable.restaurant).into(holder.imvImage);
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        if (lstFiltered != null) {
            return lstFiltered.size();
        } else {
            return 0;
        }
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName, tvDescription, tvRate;
        protected ImageView imvImage;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            imvImage = itemView.findViewById(R.id.imvRestaurantItem);
            tvRate = itemView.findViewById(R.id.tvRateRestaurantItem);
            tvName = itemView.findViewById(R.id.tvNameRestaurantItem);
            tvDescription = itemView.findViewById(R.id.tvDescRestaurantItem);
        }

        public void bind(final UserModel model, final RestaurantAdapter.OnRestaurantItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onClickItem(model));
        }
    }

    /**
     * Filter for search in EditText
     */
    private Filter mFilter;

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new SearchFilters();
        }
        return mFilter;
    }

    //filter class
    private class SearchFilters extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence str) {
            FilterResults results = new FilterResults();
            if (str == null || str.length() == 0) {
                lstFiltered = listRestaurants;
            } else {
                str = StringFormatUtils.convertUTF8ToString(str.toString().trim().toLowerCase());
                List<UserModel> lstRecordFilters = new ArrayList<>();
                for (UserModel obj : listRestaurants) {
                    String name = obj.getFullName() != null ? StringFormatUtils.convertUTF8ToString(obj.getFullName().trim().toLowerCase()) : "";
                    String address = StringFormatUtils.convertUTF8ToString(String.valueOf(obj.getAddress()).trim().toLowerCase());
                    assert str != null;
                    if ((name != null && name.contains(str))
                            || (address != null && address.contains(str))) {
                        lstRecordFilters.add(obj);
                    }
                }
                lstFiltered = lstRecordFilters;
            }
            results.values = lstFiltered;
            results.count = lstFiltered.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            lstFiltered = (List<UserModel>) results.values;
            notifyDataSetChanged();
        }
    }

    public interface OnRestaurantItemClickListener {
        void onClickItem(UserModel model);
    }

}
