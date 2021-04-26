package com.sh.orderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sh.orderapp.R;
import com.sh.orderapp.model.FoodModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.sh.orderapp.utils.Common.getDecimalFormattedString;


public class ConfirmOrderAdapter extends BaseAdapter {
    private final Context context;
    private final int layout;
    private final List<FoodModel> lstFoods;
    private final OnItemFoodClickListener listener;

    public ConfirmOrderAdapter(Context context, int layout, List<FoodModel> lstFoods, OnItemFoodClickListener listener) {
        this.context = context;
        this.layout = layout;
        this.lstFoods = lstFoods;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return lstFoods.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolders {
        private TextView tvQty, tvTitle, tvTotalPrice;
        private ImageView imgDrink;
        private Button btnDelete;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        try {
            ConfirmOrderAdapter.ViewHolders viewHolders;
            if (view == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                view = layoutInflater.inflate(layout, null);

                viewHolders = new ConfirmOrderAdapter.ViewHolders();
                viewHolders.tvQty = view.findViewById(R.id.a4_item_orderQty);
                viewHolders.tvTitle = view.findViewById(R.id.a4_item_tvTitle);
                viewHolders.tvTotalPrice = view.findViewById(R.id.a4_item_tvTotalPrice);
                viewHolders.imgDrink = view.findViewById(R.id.a4_item_img);
                viewHolders.btnDelete = view.findViewById(R.id.btnDeleteItemCart);

                view.setTag(viewHolders);
                view.setTag(R.id.a4_item_orderQty, viewHolders.tvQty);
                view.setTag(R.id.a4_item_tvTitle, viewHolders.tvTitle);
                view.setTag(R.id.a4_item_tvTotalPrice, viewHolders.tvTotalPrice);
                view.setTag(R.id.a4_item_img, viewHolders.imgDrink);
                view.setTag(R.id.btnDeleteItemCart, viewHolders.btnDelete);
            } else {
                viewHolders = (ConfirmOrderAdapter.ViewHolders) view.getTag();
            }
            // Hiển thị thông tin
            viewHolders.tvTitle.setText(String.valueOf(lstFoods.get(position).getName()));
            viewHolders.tvQty.setText(String.valueOf(lstFoods.get(position).getNowQty()));
            Picasso.with(context).load(lstFoods.get(position).getImage())
                    .placeholder(R.drawable.c1)
                    .error(R.drawable.c1).into(viewHolders.imgDrink);
            int totalPrice = lstFoods.get(position).getPrice() * lstFoods.get(position).getNowQty();
            viewHolders.tvTotalPrice.setText(getDecimalFormattedString(String.valueOf(totalPrice)));
            viewHolders.btnDelete.setOnClickListener(v -> {
                FoodModel foodModel = lstFoods.get(position);
                listener.onClickDeleteFood(foodModel);
            });

            return view;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface OnItemFoodClickListener {
        void onClickDeleteFood(FoodModel foodModel);
    }
}
