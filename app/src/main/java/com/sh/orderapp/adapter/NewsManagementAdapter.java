package com.sh.orderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sh.orderapp.R;
import com.sh.orderapp.model.NewsModel;

import java.util.List;

public class NewsManagementAdapter extends RecyclerView.Adapter<NewsManagementAdapter.ItemFoodViewHolder> {
    private final Context mContext;
    private final List<NewsModel> listNews;
    public final OnItemNewsClickListener listener;

    public NewsManagementAdapter(Context mContext,
                                 List<NewsModel> listNews,
                                 NewsManagementAdapter.OnItemNewsClickListener listener) {
        this.mContext = mContext;
        this.listNews = listNews;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsManagementAdapter.ItemFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_news_management, parent, false);
        return new NewsManagementAdapter.ItemFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsManagementAdapter.ItemFoodViewHolder holder, int position) {
        NewsModel newsModel = listNews.get(position);
        holder.tvName.setText(newsModel.getName());
        holder.tvContent.setText(newsModel.getDescription());
        holder.bind(newsModel, listener);
    }

    @Override
    public int getItemCount() {
        if (listNews != null) {
            return listNews.size();
        } else {
            return 0;
        }
    }

    public static class ItemFoodViewHolder extends RecyclerView.ViewHolder {
        protected TextView tvName, tvContent;
        protected TextView tvDelete, tvUpdate;

        public ItemFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameNewsManageItem);
            tvContent = itemView.findViewById(R.id.tvDescriptionNewsManageItem);
            tvDelete = itemView.findViewById(R.id.btnDeleteNewsManage);
            tvUpdate = itemView.findViewById(R.id.btnUpdateNewsManage);
        }

        public void bind(final NewsModel newsModel, final NewsManagementAdapter.OnItemNewsClickListener listener) {
            tvUpdate.setOnClickListener(v -> listener.onClickUpdateNews(newsModel));

            tvDelete.setOnClickListener(v -> listener.onClickDeleteNews(newsModel));
        }

    }

    public interface OnItemNewsClickListener {
        void onClickUpdateNews(NewsModel newsModel);

        void onClickDeleteNews(NewsModel newsModel);
    }
}