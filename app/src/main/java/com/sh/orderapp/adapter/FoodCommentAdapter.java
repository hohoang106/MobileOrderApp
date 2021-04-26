package com.sh.orderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.orderapp.R;
import com.sh.orderapp.model.FoodCommentModel;

import java.util.List;

public class FoodCommentAdapter extends RecyclerView.Adapter<FoodCommentAdapter.ItemFoodCommentViewHolder> {
    private final Context mContext;
    private final List<FoodCommentModel> commentModels;

    public FoodCommentAdapter(Context mContext, List<FoodCommentModel> commentModels) {
        this.mContext = mContext;
        this.commentModels = commentModels;
    }

    @NonNull
    @Override
    public FoodCommentAdapter.ItemFoodCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        return new FoodCommentAdapter.ItemFoodCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodCommentAdapter.ItemFoodCommentViewHolder holder, int position) {
        FoodCommentModel model = commentModels.get(position);
        holder.tvName.setText(model.getUserCommentModel().getFullName());
        holder.tvCreatedDate.setText(model.getCreatedDate());
        holder.tvContent.setText(model.getContent());
    }

    @Override
    public int getItemCount() {
        if (commentModels != null) {
            return commentModels.size();
        } else {
            return 0;
        }
    }

    public static class ItemFoodCommentViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName, tvContent, tvCreatedDate;

        public ItemFoodCommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameCustomerCommentItem);
            tvContent = itemView.findViewById(R.id.tvContentCommentItem);
            tvCreatedDate = itemView.findViewById(R.id.tvTimeCommentItem);
        }
    }
}