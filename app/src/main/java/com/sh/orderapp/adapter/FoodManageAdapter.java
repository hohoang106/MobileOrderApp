package com.sh.orderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.orderapp.R;
import com.sh.orderapp.model.FoodModel;
import com.sh.orderapp.utils.StringFormatUtils;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FoodManageAdapter extends RecyclerView.Adapter<FoodManageAdapter.ItemFoodViewHolder> {
    private final Context mContext;
    private final List<FoodModel> listFoods;
    public final OnItemFoodClickListener listener;

    public FoodManageAdapter(Context mContext, List<FoodModel> listFoods, OnItemFoodClickListener listener) {
        this.mContext = mContext;
        this.listFoods = listFoods;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodManageAdapter.ItemFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_food_manage, parent, false);
        return new FoodManageAdapter.ItemFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodManageAdapter.ItemFoodViewHolder holder, int position) {
        FoodModel foodModel = listFoods.get(position);
        holder.tvName.setText(foodModel.getName());
        holder.tvPrice.setText(StringFormatUtils.convertToStringMoneyVND(foodModel.getPrice()));
        Picasso.with(mContext).load(foodModel.getImage()).placeholder(R.drawable.restaurant)
                .error(R.drawable.restaurant).into(holder.imvImage);
        holder.bind(foodModel, listener);
    }

    @Override
    public int getItemCount() {
        if (listFoods != null) {
            return listFoods.size();
        } else {
            return 0;
        }
    }

    public static class ItemFoodViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName, tvPrice;
        protected ImageView imvImage;
        protected TextView tvDelete, tvUpdate;

        public ItemFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.a3_item_tvTitle);
            imvImage = itemView.findViewById(R.id.a3_item_img);
            tvPrice = itemView.findViewById(R.id.a3_item_tvPrice);
            tvDelete = itemView.findViewById(R.id.btnDeleteFoodManage);
            tvUpdate = itemView.findViewById(R.id.btnUpdateFoodManage);
        }

        public void bind(final FoodModel foodModel, final FoodManageAdapter.OnItemFoodClickListener listener) {
            tvUpdate.setOnClickListener(v -> listener.onClickUpdateFood(foodModel));

            tvDelete.setOnClickListener(v -> listener.onClickDeleteFood(foodModel));
        }

    }

    public interface OnItemFoodClickListener {
        void onClickUpdateFood(FoodModel foodModel);

        void onClickDeleteFood(FoodModel foodModel);
    }
}
