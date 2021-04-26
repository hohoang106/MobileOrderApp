package com.sh.orderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.sh.orderapp.R;
import com.sh.orderapp.model.FoodModel;
import com.sh.orderapp.utils.StringFormatUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.sh.orderapp.utils.Common.getDecimalFormattedString;


public class FoodAdapter extends BaseAdapter implements Filterable {
    private final Context context;
    private final int layout;
    private final List<FoodModel> listFoods;
    private List<FoodModel> lstFiltered;

    public FoodAdapter(Context context, int layout, List<FoodModel> listFoods) {
        this.context = context;
        this.layout = layout;
        this.listFoods = listFoods;
        this.lstFiltered = listFoods;
    }

    @Override
    public int getCount() {
        return lstFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        return lstFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolders {
        private TextView tvTotal, tvTitle, tvPrice;
        private ImageView imgDrink, btnIncrease, btnReduction;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        try {
            FoodAdapter.ViewHolders viewHolders;
            if (view == null) {
                // Khai báo màn hình
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                view = layoutInflater.inflate(layout, null);

                // Ánh xạ đối tượng
                viewHolders = new FoodAdapter.ViewHolders();
                viewHolders.tvTotal = view.findViewById(R.id.a3_item_tvTotal);
                viewHolders.tvTitle = view.findViewById(R.id.a3_item_tvTitle);
                viewHolders.tvPrice = view.findViewById(R.id.a3_item_tvPrice);
                viewHolders.imgDrink = view.findViewById(R.id.a3_item_img);
                viewHolders.btnIncrease = view.findViewById(R.id.a3_item_imgIncrease);
                viewHolders.btnReduction = view.findViewById(R.id.a3_item_imgReduction);

                view.setTag(viewHolders);
                view.setTag(R.id.a3_item_tvTotal, viewHolders.tvTotal);
                view.setTag(R.id.a3_item_tvTitle, viewHolders.tvTitle);
                view.setTag(R.id.a3_item_tvPrice, viewHolders.tvPrice);
                view.setTag(R.id.a3_item_img, viewHolders.imgDrink);
                view.setTag(R.id.a3_item_imgIncrease, viewHolders.btnIncrease);
                view.setTag(R.id.a3_item_imgReduction, viewHolders.btnReduction);
            } else {
                viewHolders = (FoodAdapter.ViewHolders) view.getTag();
            }
            // Hiển thị thông tin
            FoodModel foodModel = lstFiltered.get(position);

            viewHolders.tvTitle.setText(String.valueOf(foodModel.getName()));
            String showPrice = getDecimalFormattedString(String.valueOf(foodModel.getPrice())) + " vnđ";
            viewHolders.tvPrice.setText(showPrice);
            viewHolders.tvTotal.setText(String.valueOf(foodModel.getNowQty()));
            if (foodModel.getNowQty() > 0)
                viewHolders.tvTotal.setTextColor(context.getResources().getColor(R.color.primaryTextColor));
            Picasso.with(context).load(foodModel.getImage())
                    .placeholder(R.drawable.c1)
                    .error(R.drawable.c1).into(viewHolders.imgDrink);

            final ViewHolders finalViewHolders = viewHolders;
            viewHolders.btnReduction.setOnClickListener(v -> {
                int orderNum = Integer.parseInt(finalViewHolders.tvTotal.getText().toString());
                if (orderNum == 10) {
                    finalViewHolders.btnReduction.setClickable(false);
                } else {
                    orderNum += 1;
                    foodModel.setNowQty(orderNum);
                    finalViewHolders.tvTotal.setText(String.valueOf(orderNum));
                    finalViewHolders.tvTotal.setTextColor(context.getResources().getColor(R.color.primaryTextColor));
                    finalViewHolders.btnIncrease.setClickable(true);
                }
            });
            viewHolders.btnIncrease.setOnClickListener(v -> {
                int orderNum = Integer.parseInt(finalViewHolders.tvTotal.getText().toString());
                if (orderNum == 0) {
                    finalViewHolders.btnIncrease.setClickable(false);
                } else {
                    orderNum -= 1;
                    if (orderNum == 0) {
                        finalViewHolders.tvTotal.setTextColor(context.getResources().getColor(R.color.black));
                    }
                    finalViewHolders.tvTotal.setText(String.valueOf(orderNum));
                    foodModel.setNowQty(orderNum);
                    finalViewHolders.btnReduction.setClickable(true);
                }
            });

            return view;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
                lstFiltered = listFoods;
            } else {
                str = StringFormatUtils.convertUTF8ToString(str.toString().trim().toLowerCase());
                List<FoodModel> lstRecordFilters = new ArrayList<>();
                for (FoodModel obj : listFoods) {
                    String name = obj.getName() != null ? StringFormatUtils.convertUTF8ToString(obj.getName().trim().toLowerCase()) : "";
                    String giaCa = StringFormatUtils.convertUTF8ToString(String.valueOf(obj.getPrice()).trim().toLowerCase());
                    assert str != null;
                    if ((name != null && name.contains(str))
                            || (giaCa != null && giaCa.contains(str))) {
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
            lstFiltered = (List<FoodModel>) results.values;
            notifyDataSetChanged();
        }
    }

}
